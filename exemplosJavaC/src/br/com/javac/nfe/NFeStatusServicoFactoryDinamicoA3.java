package br.com.javac.nfe;

import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.httpclient.protocol.Protocol;

import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;

public class NFeStatusServicoFactoryDinamicoA3 {
	private static final int SSL_PORT = 443;

	public static void main(String[] args) {
        try {
            String codigoDoEstado = "42";
            URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");

            /**
             * Informações do Certificado Digital A3.
             */
            String configName = "SmartCard.cfg";
            String senhaDoCertificado = "1234";
            String arquivoCacertsGeradoTodosOsEstados = "NFeCacerts";

            Provider p = new sun.security.pkcs11.SunPKCS11(configName);
    		Security.addProvider(p);
    		char[] pin = senhaDoCertificado.toCharArray();
    		KeyStore ks = KeyStore.getInstance("pkcs11", p);
    		ks.load(null, pin);

    		/**
    		 * Resolve o problema do 403.7 Forbidden para Certificados A3 e A1 
    		 * e elimina o uso das cofigurações:
    		 * - System.setProperty("javax.net.ssl.keyStore", "NONE");
    		 * - System.setProperty("javax.net.ssl.keyStoreType", "PKCS11");
    		 * - System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-SmartCard");
    		 * - System.setProperty("javax.net.ssl.trustStoreType", "JKS");
    		 * - System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoTodosOsEstados);
    		 */
			String alias = ks.aliases().nextElement();  
    		X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
    		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, senhaDoCertificado.toCharArray());
    		SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(certificate, privateKey);
    		socketFactoryDinamico.setFileCacerts(arquivoCacertsGeradoTodosOsEstados);

            Protocol protocol = new Protocol("https", socketFactoryDinamico, SSL_PORT);  
            Protocol.registerProtocol("https", protocol);    		
    		
    		/**
             * Xml de Consulta.
             */
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<consStatServ versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")
                .append("<tpAmb>2</tpAmb>")
                .append("<cUF>")
                .append(codigoDoEstado)
                .append("</cUF>")
                .append("<xServ>STATUS</xServ>")
                .append("</consStatServ>");

            OMElement ome = AXIOMUtil.stringToOM(xml.toString());
            NfeStatusServico2Stub.NfeDadosMsg dadosMsg = new NfeStatusServico2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeStatusServico2Stub.NfeCabecMsg nfeCabecMsg = new NfeStatusServico2Stub.NfeCabecMsg();
            /**
             * Código do Estado.
             */
            nfeCabecMsg.setCUF(codigoDoEstado);

            /**
             * Versao do XML
             */
            nfeCabecMsg.setVersaoDados("2.00");
            NfeStatusServico2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeStatusServico2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeStatusServico2Stub stub = new NfeStatusServico2Stub(url.toString());
            NfeStatusServico2Stub.NfeStatusServicoNF2Result result = stub.nfeStatusServicoNF2(dadosMsg, nfeCabecMsgE);

            info(result.getExtraElement().toString());
        } catch (Exception e) {
            error(e.toString());
        }
	}

	/**
	 * Log Info.
	 * @param log
	 */
	private static void info(String log) {
		System.out.println("INFO: " + log);
	}

	/**
	 * Log Error.
	 * @param log
	 */
	private static void error(String log) {
		System.out.println("ERROR: " + log);
	}
	
}
