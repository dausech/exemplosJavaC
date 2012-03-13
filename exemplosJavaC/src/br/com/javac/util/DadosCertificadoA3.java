package br.com.javac.util;

import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class DadosCertificadoA3 {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		try {
			String configName = "SmartCard.cfg";
			String senhaDoCertificado = "32770292";
			
			Provider provider = new sun.security.pkcs11.SunPKCS11(configName);
			Security.addProvider(provider);
			
			String alias = null;
			KeyStore ks = KeyStore.getInstance("pkcs11", provider);
			ks.load(null, senhaDoCertificado.toCharArray());
			
			Provider pp = ks.getProvider();
			info("--------------------------------------------------------");
			info("Provider   : " + pp.getName());
			info("Prov.Vers. : " + pp.getVersion());
			info("KS Type    : " + ks.getType());
			info("KS DefType : " + ks.getDefaultType());
	
			Enumeration <String> al = ks.aliases();
			while (al.hasMoreElements()) {
				alias = al.nextElement();
				info("--------------------------------------------------------");
				if (ks.containsAlias(alias)) {
					info("Alias exists : '" + alias + "'");
					
					X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
					info("Certificate  : '" + cert.toString() + "'");
					info("Version      : '" + cert.getVersion() + "'");
					info("SerialNumber : '" + cert.getSerialNumber() + "'");
					info("SigAlgName   : '" + cert.getSigAlgName() + "'");
					info("NotBefore    : '" + cert.getNotBefore().toString() + "'");
					info("NotAfter     : '" + cert.getNotAfter().toString() + "'");
					info("TBS          : '" + cert.getTBSCertificate().toString() + "'");
				} else {
					info("Alias doesn't exists : '" + alias + "'");
				}
			}
		} catch (Exception e) {
			error(e.toString());
		}
	}

	/**
	 * Log ERROR.
	 * 
	 * @param error
	 */
	private static void error(String error) {
		System.out.println("| ERROR: " + error);
	}

	/**
	 * Log INFO.
	 * 
	 * @param info
	 */
	private static void info(String info) {
		System.out.println("| INFO: " + info);
	}
	
}
