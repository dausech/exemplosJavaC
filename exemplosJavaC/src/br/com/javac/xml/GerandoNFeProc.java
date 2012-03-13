package br.com.javac.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GerandoNFeProc {

	public static void main(String[] args) {
		try {
			String enviNFe = "C:/JavaC/NF-e/XML_enviNFe.XML";
			String retConsReciNFe = "C:/JavaC/NF-e/XML_retConsReciNFe.xml";

			String xmlEnviNFe = lerXML(enviNFe);
			String xmlRetConsReciNFe = lerXML(retConsReciNFe);

			Document document = documentFactory(xmlEnviNFe);
			NodeList nodeListNfe = document.getDocumentElement().getElementsByTagName("NFe");
			NodeList nodeListInfNfe = document.getElementsByTagName("infNFe");

			for (int i = 0; i < nodeListNfe.getLength(); i++) {
				Element el = (Element) nodeListInfNfe.item(i);
				String chaveNFe = el.getAttribute("Id");				

				String xmlNFe = outputXML(nodeListNfe.item(i));
				String xmlProtNFe = getProtNFe(xmlRetConsReciNFe, chaveNFe);

				info("");
				info("ChaveNFe.....: " + chaveNFe);				
				info("NFe..........: " + xmlNFe);
				info("ProtNFe......: " + xmlProtNFe);
				info("NFeProc......: " + buildNFeProc(xmlNFe, xmlProtNFe));
			}
		} catch (Exception e) {
			error(e.toString());
		}
	}

	private static String buildNFeProc(String xmlNFe, String xmlProtNFe) {
		StringBuilder nfeProc = new StringBuilder();
		nfeProc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			.append("<nfeProc versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")
			.append(xmlNFe)
			.append(xmlProtNFe)
			.append("</nfeProc>");
		return nfeProc.toString();
	}
	
	private static String getProtNFe(String xml, String chaveNFe) throws SAXException, 
			IOException, ParserConfigurationException, TransformerException {
		Document document = documentFactory(xml);
		NodeList nodeListProtNFe = document.getDocumentElement().getElementsByTagName("protNFe");
		NodeList nodeListChNFe = document.getElementsByTagName("chNFe");
		for (int i = 0; i < nodeListProtNFe.getLength(); i++) {
			Element el = (Element) nodeListChNFe.item(i);
			String chaveProtNFe = el.getTextContent();
			if (chaveNFe.contains(chaveProtNFe)) {
				return outputXML(nodeListProtNFe.item(i));
			}
		}		
		return "";
	}

	private static String outputXML(Node node) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(node), new StreamResult(os));
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
		}
		return xml;
	}
	
	private static Document documentFactory(String xml) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(
				new ByteArrayInputStream(xml.getBytes()));
		return document;
	}

	public static String lerXML(String fileXML) throws IOException {  
        String linha = "";  
        StringBuilder xml = new StringBuilder();  
  
        BufferedReader in = new BufferedReader(new InputStreamReader(  
                new FileInputStream(fileXML)));  
        while ((linha = in.readLine()) != null) {  
            xml.append(linha);  
        }  
        in.close();  
  
        return xml.toString();  
    }

	/**
	 * Log ERROR.
	 * @param error
	 */
	private static void error(String error) {
		System.out.println("| ERROR: " + error);
	}

	/**
	 * Log INFO.
	 * @param info
	 */
	private static void info(String info) {
		System.out.println("| INFO: " + info);
	}
	
}
