package br.com.javac.nfe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author JavaC - Java Community
 */
public class NFeBuildCacerts {
    private static final String JSSECACERTS = "nfe-cacerts";
    private static final int TIMEOUT_WS = 60;

    public static void main(String[] args) {
        try {
            /**
             * EndereÃ§o do WebService.
             * O Endereço abaixo é do Sefaz Virtual RS, altere para o endereço que
             * for necessário.
             */
            //String enderecoWebService = "homologacao.nfe.sefazvirtual.rs.gov.br";
        	String enderecoWebService = "homologacao.nfe2.fazenda.pr.gov.br";
        	
            File cacertsFile = gerarCacerts(enderecoWebService, 443);
            if (cacertsFile != null) {
                info("| Cacerts gerado em: " + cacertsFile.getAbsolutePath());
            }
        } catch (Exception e) {
            error(e.toString());
        }
    }

    public static File gerarCacerts(String host, int port) throws Exception {
            char[] passphrase = "changeit".toCharArray();

        File file = new File(JSSECACERTS);
        if (file.isFile() == false) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            file = new File(dir, JSSECACERTS);
            if (file.isFile() == false) {
                    file = new File(dir, "cacerts");
            }
        }

        info("| Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory factory = context.getSocketFactory();

        info("| Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(TIMEOUT_WS * 1000);
        try {
            info("| Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            info("| No errors, certificate is already trusted");
        } catch (SSLHandshakeException e) {
            /**
             * PKIX path building failed:
             * sun.security.provider.certpath.SunCertPathBuilderException:
             * unable to find valid certification path to requested target
             * NÃ£o tratado, pois sempre ocorre essa exceÃ§Ã£o quando o cacerts
             * nao esta gerado.
             */
        } catch (SSLException e) {
            error("| " + e.toString());
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            info("| Could not obtain server certificate chain");
            return null;
        }

        info("| Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            sha1.update(cert.getEncoded());
            md5.update(cert.getEncoded());

            String alias = host + "-" + (i);
            ks.setCertificateEntry(alias, cert);
            info("| Added certificate to keystore '" + JSSECACERTS + "' using alias '" + alias + "'");
        }

        File cafile = new File(JSSECACERTS);
        OutputStream out = new FileOutputStream(cafile);
        ks.store(out, passphrase);
        out.close();

        return cafile;
    }

    private static class SavingTrustManager implements X509TrustManager {
        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

    private static void info(String info) {
        System.out.println("INFO: " + info);
    }

    private static void error(String error) {
        System.out.println("ERROR: " + error);
    }

}
