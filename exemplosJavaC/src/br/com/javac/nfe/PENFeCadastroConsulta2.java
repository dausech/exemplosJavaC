package br.com.javac.nfe;

import java.io.StringReader;
import java.net.URL;
import java.security.Security;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import br.inf.portalfiscal.www.nfe.wsdl.cadconsultacadastro2.CadConsultaCadastro2Stub;
    
public class PENFeCadastroConsulta2 {    
  
    private static final String KEYSTORE_TYPE = "PKCS12";  
    private static final String KEYSTORE_FILE = "C:/JavaC/NF-e/certificadoDoCliente.pfx";  
    private static final String KEYSTORE_PASSWORD = "1234";  
    private static final String TRUSTSTORE_TYPE = "JKS";  
    private static final String TRUSTSTORE_FILE = "NFeCacerts";  
      
    private static final String URI = "https://nfe.sefaz.pe.gov.br/nfe-service/services/CadConsultaCadastro2";  
  
    private static final String CNPJ = "05371395000152";  
    private static final String ESTADO_CONSULTA = "26";  
    private static final String ESTADO_XML = "PE";  
      
    public static void main(String[] args) {  
        try {    
              
            URL url = new URL(URI);  
              
            System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");  
              
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");    
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());    
    
            System.setProperty("javax.net.ssl.keyStoreType", KEYSTORE_TYPE);    
    
            System.clearProperty("javax.net.ssl.keyStore");    
            System.clearProperty("javax.net.ssl.keyStorePassword");    
            System.clearProperty("javax.net.ssl.trustStore");    
    
            System.setProperty("javax.net.ssl.keyStore", KEYSTORE_FILE);    
            System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);    
    
            System.setProperty("javax.net.ssl.trustStoreType", TRUSTSTORE_TYPE);    
            System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_FILE);    
    
            StringBuilder xml = new StringBuilder();    
            xml.append("<nfeDadosMsg>")    
               .append("<ConsCad versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")  
               .append("<infCons>")    
               .append("<xServ>CONS-CAD</xServ>")    
               .append("<UF>")    
               .append(ESTADO_XML)    
               .append("</UF>")    
               .append("<CNPJ>")  
               .append(CNPJ)  
               .append("</CNPJ>")     
               .append("</infCons>")    
               .append("</ConsCad>")    
               .append("</nfeDadosMsg>");    
                
            XMLStreamReader dadosXML = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml.toString()));     
            CadConsultaCadastro2Stub.NfeDadosMsg dadosMsg = CadConsultaCadastro2Stub.NfeDadosMsg.Factory.parse(dadosXML);    
    
            CadConsultaCadastro2Stub.NfeCabecMsg nfeCabecMsg = new CadConsultaCadastro2Stub.NfeCabecMsg();  
              
            nfeCabecMsg.setCUF(ESTADO_CONSULTA);    
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