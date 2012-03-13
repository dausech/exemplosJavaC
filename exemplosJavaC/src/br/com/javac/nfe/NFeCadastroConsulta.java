package br.com.javac.nfe;

import java.io.StringReader;
import java.net.URL;
import java.security.Security;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import br.inf.portalfiscal.www.nfe.wsdl.cadconsultacadastro2.CadConsultaCadastro2Stub;

/**
 *
 * @author JavaC - Java Community
 */
public class NFeCadastroConsulta {

    public static void main(String[] args) {
        try {
            String estadoConsulta = "43";
            String estadoXML = "RS";

            /**
             * CNPJ Sem formatacao. Caso esta formatado
             * retornara Falha de Schema XML.
             */
            String cnpjConsultado = "03940848000199";
            //URL url = new URL("https://nfe.fazenda.sp.gov.br/nfeweb/services/cadconsultacadastro2.asmx");
            //URL url = new URL("https://nfe.sefaz.mt.gov.br/nfews/v2/services/CadConsultaCadastro2");
            URL url = new URL("https://sef.sefaz.rs.gov.br/ws/cadconsultacadastro/cadconsultacadastro2.asmx");

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
             * Xml de Consulta.
             */
            StringBuilder xml = new StringBuilder();
            xml.append("<nfeDadosMsg>")
            	.append("<ConsCad versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">") 
            	.append("<infCons>")
            	.append("<xServ>CONS-CAD</xServ>")
            	.append("<UF>")
            	.append(estadoXML)
            	.append("</UF>")
            	.append("<CNPJ>")
            	.append(cnpjConsultado)
            	.append("</CNPJ>") 
            	.append("</infCons>")
            	.append("</ConsCad>")
            	.append("</nfeDadosMsg>");
            
            XMLStreamReader dadosXML = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml.toString())); 
            CadConsultaCadastro2Stub.NfeDadosMsg dadosMsg = CadConsultaCadastro2Stub.NfeDadosMsg.Factory.parse(dadosXML);

            CadConsultaCadastro2Stub.NfeCabecMsg nfeCabecMsg = new CadConsultaCadastro2Stub.NfeCabecMsg();
            /**
             * Codigo do Estado.
             */
            nfeCabecMsg.setCUF(estadoConsulta);

            /**
             * Versao do XML
             */
            nfeCabecMsg.setVersaoDados("2.00");
            CadConsultaCadastro2Stub.NfeCabecMsgE nfeCabecMsgE = new CadConsultaCadastro2Stub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            CadConsultaCadastro2Stub stub = new CadConsultaCadastro2Stub(url.toString());
            CadConsultaCadastro2Stub.ConsultaCadastro2Result result = stub.consultaCadastro2(dadosMsg, nfeCabecMsgE);

            System.out.println(result.getExtraElement().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
