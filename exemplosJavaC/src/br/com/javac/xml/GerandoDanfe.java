package br.com.javac.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GerandoDanfe {

	public static void main(String[] args) {
		try {
			String fileXML = "C:/JavaC/XML/proc-nfe.xml";
			String xml = lerXML(fileXML);
			String logo = "";

			byte[] pdf = getDanfe(xml, logo);
			
			String nomeArquivo = "xml.pdf";
			FileOutputStream fos = new FileOutputStream(nomeArquivo);
			fos.write(pdf);
			fos.close();
		} catch (Exception e) {
			error(e.toString());
		}
	}

	private static String lerXML(String fileXML) throws IOException {
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
	
	public static Document getXml(String xml) {
		try {
			/**
			 * Gera um objeto DOM do xml
			 */
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			return docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			error("Erro ao formatar a string em xml.");
			return null;
		}
	}
	
	public static Map<String, Object> getFaturas(Document doc) {
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < doc.getElementsByTagName("dup").getLength(); i++) {
			/**
			 * Seta uma duplicata
			 */
			Node dup = doc.getElementsByTagName("dup").item(i);

			/**
			 * Seta o numero
			 */
			Node nDoc = dup.getChildNodes().item(0);
			String numero = nDoc.getFirstChild().getNodeValue();
			map.put("FAT_NUMERO" + (i + 1), numero);
	
			/**
			 * Seta a data
			 */
			Node nData = dup.getChildNodes().item(1);
			String data = nData.getFirstChild().getNodeValue();
	
			try {
				map.put("FAT_VENCIMENTO" + (i + 1), new SimpleDateFormat("yyyy-MM-dd").parse(data));
			} catch (Exception e) {
				map.put("FAT_VENCIMENTO" + (i + 1), null);
	
			}
	
			/**
			 * Seta o valor
			 */
			Node nValor = dup.getChildNodes().item(2);
			String valor = nValor.getFirstChild().getNodeValue();
			map.put("FAT_VALOR" + (i + 1), Double.parseDouble(valor));
		}
		return map;
	}
	
	public static byte[] getDanfe(String xml, String logo) {
		byte[] pdf = null;
		try {
			/**
			 * Documento XML.
			 */
			Document doc = getXml(xml);

			String tipoImp = doc.getElementsByTagName("tpImp").item(0).getFirstChild().getNodeValue();
			/**
			 * Local do relatorio jrxml
			 */
			String urlDanfe = tipoImp.equals("1") ? "danfeR.jrxml" : "danfeP.jrxml";
			JasperDesign jasperDesign = JRXmlLoader.load(urlDanfe);  
			JasperReport jasper = JasperCompileManager.compileReport(jasperDesign);

			/**
			 * Fonte de Dados.
			 */
			JRXmlDataSource ds = new JRXmlDataSource(doc, "/nfeProc/NFe/infNFe/det");
	
			/**
			 * Parametros
			 */
			Map<String, Object> param = getFaturas(doc);
			param.put("Logo", logo);

			/**
			 * Gerando o relatorio
			 */
			JasperPrint print = JasperFillManager.fillReport(jasper, param, ds);

			/**
			 * Exportando em pdf
			 */
			pdf = JasperExportManager.exportReportToPdf(print);
		} catch (Exception e) {
			error(e.toString());
			pdf = null;
		}
		return pdf;
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
	//private static void info(String info) {
		//System.out.println("| INFO: " + info);
	//}
	
}
