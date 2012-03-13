package br.com.javac.nfe;

import java.net.URL;
import java.security.Security;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.inf.portalfiscal.www.nfe.wsdl.nferetrecepcao2.NfeRetRecepcao2Stub;

/**
 *
 * @author JavaC - Java Community
 */
public class NFeRetRecepcao {

    public static void main(String[] args) {
        try {
            /**
             * 1) codigoDoEstado = Código do Estado conforme tabela IBGE.
             *
             * 2) url = Endereço do WebService para cada Estado.
             *       Ver relação dos endereços em:
             *       Para Homologação: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx
             *       Para Produção: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx
             *
             * 3) caminhoDoCertificadoDoCliente = Caminho do Certificado do Cliente (A1).
             *
             * 4) senhaDoCertificadoDoCliente = Senha do Certificado A1 do Cliente.
             *
             * 5) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para
             * acessar o WebService. Pode ser gerado com a Classe NFeBuildCacerts.
             */
            String codigoDoEstado = "35";

            /**
             * Enderecos de Homoloção do Sefaz Virtual RS
             * para cada WebService existe um endereco diferente.
             */
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nferecepcao/NfeRecepcao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nferetrecepcao/NfeRetRecepcao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfecancelamento/NfeCancelamento2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeinutilizacao/NfeInutilizacao2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeconsulta/NfeConsulta2.asmx");
            //URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfestatusservico/NfeStatusServico2.asmx");

            URL url = new URL("https://nfe.fazenda.sp.gov.br/nfeweb/services/NfeRetRecepcao2.asmx");
            
            
            /**
             * Número do Protocolo recebido do Sefaz no Envio do Lote.
             * NfeRecepcao2
             */
            String numeroDoRecibo = "135110174838790";
            
            String caminhoDoCertificadoDoCliente = "C:/JavaC/NF-e/certificadoDoCliente.pfx";
            String senhaDoCertificadoDoCliente = "1234";
            String arquivoCacertsGeradoParaCadaEstado = "NFeCacerts";

            System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
            /**
             * Informações do Certificado Digital.
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
             * Xml de Consulta.
             */
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            	.append("<consReciNFe versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")
            	.append("<tpAmb>1</tpAmb>")
       			.append("<nRec>")
				.append(numeroDoRecibo)
				.append("</nRec>")
            	.append("</consReciNFe>");
            
            OMElement ome = AXIOMUtil.stringToOM(xml.toString());
            NfeRetRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRetRecepcao2Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NfeRetRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRetRecepcao2Stub.NfeCabecMsg();
            /**
             * Código do Estado.
             */
            nfeCabecMsg.setCUF(codigoDoEstado);

            /**
             * Versão do XML
             */
            nfeCabecMsg.setVersaoDados("2.00");

            NfeRetRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRetRecepcao2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            NfeRetRecepcao2Stub stub = new NfeRetRecepcao2Stub(url.toString());
            NfeRetRecepcao2Stub.NfeRetRecepcao2Result result = stub.nfeRetRecepcao2(dadosMsg, nfeCabecMsgE);

            System.out.println(result.getExtraElement().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
