package br.com.javac.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.javac.v200.retenvinfe.TRetEnviNFe;

public class RetEnviNFe_JAXB {

	public static void main(String[] args) {

		try {
			String xml = lerXML("C:/JavaC/XML/XML_retEnviNFe.xml");
			TRetEnviNFe retEnviNFe = retEnviNFe(xml);
			if (retEnviNFe != null) {
				info("| TpAmb........: " + retEnviNFe.getTpAmb());
				info("| VerAplic.....: " + retEnviNFe.getVerAplic());
				info("| CStat........: " + retEnviNFe.getCStat());
				info("| XMotivo......: " + retEnviNFe.getXMotivo());
				info("| CUF..........: " + retEnviNFe.getCUF());
				info("| DhRecbto.....: " + retEnviNFe.getDhRecbto());

				if (retEnviNFe.getInfRec() != null) {
					info("| NRec.........: " + retEnviNFe.getInfRec().getNRec());
					info("| TMed.........: " + retEnviNFe.getInfRec().getTMed());
				}
			}
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

	@SuppressWarnings("unchecked")
	public static TRetEnviNFe retEnviNFe(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance("br.com.javac.retenvinfev2");  
        Unmarshaller unmarshaller = context.createUnmarshaller();  
        JAXBElement<TRetEnviNFe> element = (JAXBElement<TRetEnviNFe>)   
            unmarshaller.unmarshal(new StringReader(xml));
        return element.getValue(); 		
	}

	private static void info(String log) {
		System.out.println("INFO: " + log);
	}

	private static void error(String log) {
		System.out.println("ERROR: " + log);
	}
	
}
