package br.com.javac.nfe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.inf.portalfiscal.www.nfe.wsdl.nfecancelamento2.NfeCancelamento2Stub;

/**
 *
 * @author JavaC - Java Community
 */
public class NFeCancelamento {

    public static void main(String[] args) {
        try {
            /**
             * 1) codigoDoEstado = Codigo do Estado conforme tabela IBGE.
             *
             * 2) url = Endereço do WebService para cada Estado.
             *       Ver relacao dos enderecos em:
             *       Para Homologação: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx
             *       Para Producao: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx
             *
             * 3) caminhoDoCertificadoDoCliente = Caminho do Certificado do Cliente (A1).
             *
             * 4) senhaDoCertificadoDoCliente = Senha do Certificado A1 do Cliente.
             *
             * 5) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para
             * acessar o WebService. Pode ser gerado com a Classe NFeBuildCacerts.
             */
            String codigoDoEstado = "42";
            URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeCancelamento/NfeCancelamento2.asmx");
            String caminhoDoCertificadoDoCliente = "C:/JavaC/NF-e/certificadoDoCliente.pfx";
            String senhaDoCertificadoDoCliente = "123456";
            String arquivoCacertsGeradoParaCadaEstado = "NFeCacerts";

            System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
            /**
             * Informacoes do Certificado Digital.
             */
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

            System.clearProperty("javax.net.ssl.keyStore");
            System.clearProperty("javax.net.ssl.keyStorePassword");
            System.clearProperty("javax.net.ssl.trustStore");

            System.setProperty("javax.net.ssl.keyStore", caminhoDoCertificadoDoCliente);
            System.setProperty("javax.net.ssl.keyStorePassword", senhaDoCertificadoDoCliente);

            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoParaCadaEstado);

            /**
             * IMPORTANTE: O XML já deve estar assinado antes do envio.
             * Lendo o Xml de um arquivo Gerado.
             */
            StringBuilder xml = new StringBuilder();
            String linha = null;
            String caminhoArquivo = "C:/JavaC/NF-e/xml-cancelamento-teste.xml";
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(caminhoArquivo), "ISO-8859-1"));
            while ((linha = in.readLine()) != null) {
                xml.append(linha);
            }
            in.close();

            OMElement ome = AXIOMUtil.stringToOM(xml.toString());
            NfeCancelamento2Stub.NfeDadosMsg dadosMsg = new NfeCancelamento2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeCancelamento2Stub.NfeCabecMsg nfeCabecMsg = new NfeCancelamento2Stub.NfeCabecMsg();
            /**
             * Codigo do Estado.
             */
            nfeCabecMsg.setCUF(codigoDoEstado);

            /**
             * Versao do XML
             */
            nfeCabecMsg.setVersaoDados("2.00");
            NfeCancelamento2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeCancelamento2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeCancelamento2Stub stub = new NfeCancelamento2Stub(url.toString());
            NfeCancelamento2Stub.NfeCancelamentoNF2Result result = stub.nfeCancelamentoNF2(dadosMsg, nfeCabecMsgE);

            System.out.println(result.getExtraElement().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
