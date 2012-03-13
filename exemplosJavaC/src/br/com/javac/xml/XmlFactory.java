package br.com.javac.xml;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.javac.v200.procnfe.TNfeProc;

public class XmlFactory {

	@SuppressWarnings("unchecked")
	public static TNfeProc nfeProc(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance("br.com.javac.procnfev2");  
        Unmarshaller unmarshaller = context.createUnmarshaller();  
        JAXBElement<TNfeProc> element = (JAXBElement<TNfeProc>)   
            unmarshaller.unmarshal(new StringReader(xml));
        return element.getValue(); 		
	}
	
}
