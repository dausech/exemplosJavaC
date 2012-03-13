package br.com.javac.nfe;

import java.net.URL;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;

/**
 *
 * @author JavaC - Java Community
 */
public class NFeConsultaStatusServicoA3 {

    public static void main(String[] args) {
        try {
            /**
             * 1) codigoDoEstado = Codigo do Estado conforme tabela IBGE.
             *
             * 2) url = Endereco do WebService para cada Estado.
             *       Ver relacao dos enderecos em:
             *       Para Homologacao: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx
             *       Para Producao: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx
             *
             * 3) senhaDoCertificadoDoCliente = Senha do Certificado A3 do Cliente.
             *
             * 4) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para
             * acessar o WebService. Pode ser gerado com a Classe NFeBuildCacerts.
             */
            String codigoDoEstado = "42";
            URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
            String senhaDoCertificadoDoCliente = "32770292";
            String arquivoCacertsGeradoParaCadaEstado = "NFeCacerts";

            /**
             * Tipo de Certificados.
             * A3CARTAO = A3 Cartão;
             * A3TOKEN = A3 Token;
             */
            String tipoCertificado = "A3CARTAO";
            //String tipoCertificado = "A3TOKEN";
            
            /**
             * Informacoes do Certificado Digital.
             */
            String fileCfg = "";
            if ("A3CARTAO".equals(tipoCertificado)) {
            	fileCfg = "SmartCard.cfg";
            }
            else if ("A3TOKEN".equals(tipoCertificado)) {
            	fileCfg = "Token.cfg";
            }
            else {
            	throw new Exception("Tipo de certificado inválido");
            }
            
    		Provider p = new sun.security.pkcs11.SunPKCS11(fileCfg);
    		Security.addProvider(p);
    		char[] pin = senhaDoCertificadoDoCliente.toCharArray();
    		KeyStore ks = KeyStore.getInstance("pkcs11", p);
    		ks.load(null, pin);

    		System.setProperty("javax.net.ssl.keyStore", "NONE");
    		System.setProperty("javax.net.ssl.keyStoreType", "PKCS11");

    		if ("A3CARTAO".equals(tipoCertificado)) {
    			System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-SmartCard");
    		}
    		else if ("A3TOKEN".equals(tipoCertificado)) {
    			System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-eToken");
    		}

            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoParaCadaEstado);

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
             * Codigo do Estado.
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

            System.out.println(result.getExtraElement().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
