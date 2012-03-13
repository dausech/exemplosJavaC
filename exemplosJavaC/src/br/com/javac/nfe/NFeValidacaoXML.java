package br.com.javac.nfe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class NFeValidacaoXML implements ErrorHandler {
	private List<String> listaComErrosDeValidacao;
	
	public NFeValidacaoXML() {
		this.listaComErrosDeValidacao = new ArrayList<String>();
	}
	
	public static void main(String[] args) {
		try {
			/**
			 * XSD usado para Validar o XML.
			 * Use o XSD adequado para cada XML a ser validado:
			 * enviNFe_v2.00.xsd (Envio do Lote)
			 * inutNFe_v2.00.xsd (Inutilização de Número da NF-e)
			 * cancNFe_v2.00.xsd (Cancelamento da NF-e)
			 * ...
			 */
			String xsd = "C:/JavaC/NF-e/xsd/enviNFe_v2.00.xsd";
			
			/**
			 * Caminho do Arquivo XML a ser validado.
			 */
			String caminhoArquivo = "C:/JavaC/teste/xml.xml";
			
            StringBuilder xml = new StringBuilder();
            String linha = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(caminhoArquivo), "ISO-8859-1"));
            while ((linha = in.readLine()) != null) {
                xml.append(linha);
            }
            in.close();

			NFeValidacaoXML validacaoXML = new NFeValidacaoXML();
			List<String> errosValidacao = validacaoXML.validateXml(normalizeXML(xml.toString()), xsd);
			if ((errosValidacao != null) && (errosValidacao.size() > 0)) {
				for (String msgError : errosValidacao) {
					info("| Erro XML: " + msgError);
				}
			}
			else {
				info("| OK: XML Validado com Sucesso!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String normalizeXML(String xml) {
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}
		return xml;
	}

    public List<String> validateXml(String xml, String xsd) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(this);
        } catch (ParserConfigurationException ex) {
    		error("| UtilsBase.validateXml():");
    		throw new Exception(ex.toString());
        }

        org.w3c.dom.Document document;
        try {
            document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            org.w3c.dom.Node rootNode  = document.getFirstChild();
            info("| Validando Node: " + rootNode.getNodeName());
            return this.getListaComErrosDeValidacao();
        } catch (Exception ex) {
    		error("| UtilsBase.validateXml():");
    		throw new Exception(ex.toString());
        }
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		if (isError(exception)) {
			listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
		}
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
	}

	private String tratamentoRetorno(String message) {
		message = message.replaceAll("cvc-type.3.1.3:", "");
		message = message.replaceAll("cvc-complex-type.2.4.a:", "");
		message = message.replaceAll("cvc-complex-type.2.4.b:", "");
		message = message.replaceAll("The value", "O valor");
		message = message.replaceAll("of element", "do campo");
		message = message.replaceAll("is not valid", "não é valido");
		message = message.replaceAll("Invalid content was found starting with element", "Encontrado o campo");
		message = message.replaceAll("One of", "Campo(s)");
		message = message.replaceAll("is expected", "é obrigatorio");
		message = message.replaceAll("\\{", "");
		message = message.replaceAll("\\}", "");
		message = message.replaceAll("\"", "");
		message = message.replaceAll("http://www.portalfiscal.inf.br/nfe:", "");
		return message.trim();
	}

	public List<String> getListaComErrosDeValidacao() {
		return listaComErrosDeValidacao;
	}

	private boolean isError(SAXParseException exception) {
		if (exception.getMessage().startsWith("cvc-pattern-valid") ||
			exception.getMessage().startsWith("cvc-maxLength-valid") ||
			exception.getMessage().startsWith("cvc-datatype")) {
			return false;
		}
		return true;
	}
	
	private static void error(String error) {
		System.out.println("ERROR: " + error);
	}

	private static void info(String info) {
		System.out.println("INFO: " + info);
	}

}
