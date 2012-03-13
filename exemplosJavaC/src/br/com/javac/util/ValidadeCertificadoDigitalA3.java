package br.com.javac.util;

import java.security.KeyStore;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

public class ValidadeCertificadoDigitalA3 {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static void main(String[] args) {
		
		try {
            String senhaDoCertificadoDoCliente = "1234";
            
            String fileCfg = "SmartCard.cfg";            
            Provider p = new sun.security.pkcs11.SunPKCS11(fileCfg);
    		Security.addProvider(p);
    		char[] pin = senhaDoCertificadoDoCliente.toCharArray();
    		KeyStore keystore = KeyStore.getInstance("pkcs11", p);
    		keystore.load(null, pin);
            
		    Enumeration<String> eAliases = keystore.aliases();  
		      
		    while (eAliases.hasMoreElements()) {  
		        String alias = (String) eAliases.nextElement();  
		        Certificate certificado = (Certificate) keystore.getCertificate(alias);  
		  
		        info("Aliais: " + alias);
		        X509Certificate cert = (X509Certificate) certificado;  
		        
		        info(cert.getSubjectDN().getName());
		        info("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));
		        info("Válido até..........: " + dateFormat.format(cert.getNotAfter()));  
		    }
		    
		} catch (ProviderException e) {
			error(e.getMessage());
		} catch (Exception e) {
			error(e.toString());
		}
	}

	/**
	 * Error.
	 * @param log
	 */
	private static void error(String log) {
		System.out.println("ERROR: " + log);
	}

	/**
	 * Info
	 * @param log
	 */
	private static void info(String log) {
		System.out.println("INFO: " + log);
	}
	
}
