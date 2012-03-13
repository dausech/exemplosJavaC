package br.com.javac.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class InfoCertificado {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	
	
	public static final DERObjectIdentifier RESPONSAVEL = new DERObjectIdentifier("2.16.76.1.3.2"); 
	public static final DERObjectIdentifier CNPJ = new DERObjectIdentifier("2.16.76.1.3.3");	
	
	public static void main(String[] args) {
		
		try {
			String caminhoDoCertificadoDoCliente = "C:/JavaC/NF-e/certificadoDoCliente.pfx";
			String senhaDoCertificadoDoCliente = "123456";			
			
			InputStream entrada = new FileInputStream(caminhoDoCertificadoDoCliente);
			KeyStore ks = KeyStore.getInstance("pkcs12");
			try {
				ks.load(entrada, senhaDoCertificadoDoCliente.toCharArray());
			} catch (IOException e) {
				throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
			}
			
			Provider pp = ks.getProvider();
			info("--------------------------------------------------------");
			info("Provider   : " + pp.getName());
			info("Prov.Vers. : " + pp.getVersion());
			info("KS Type    : " + ks.getType());
			info("KS DefType : " + KeyStore.getDefaultType());
	
			String alias = null;
			Enumeration <String> al = ks.aliases();
			while (al.hasMoreElements()) {
				alias = al.nextElement();
				info("--------------------------------------------------------");
				if (ks.containsAlias(alias)) {
					info("Alias exists : '" + alias + "'");
					
					X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
					info("Version      : '" + cert.getVersion() + "'");
					info("SerialNumber : '" + cert.getSerialNumber() + "'");
					info("SigAlgName   : '" + cert.getSigAlgName() + "'");
					info("Válido de    : '" + dateFormat.format(cert.getNotBefore()) + "'");
					info("Válido até   : '" + dateFormat.format(cert.getNotAfter()) + "'");

					getInfoAdicionais(cert);
				} else {
					info("Alias doesn't exists : '" + alias + "'");
				}
				info("--------------------------------------------------------");
			}
		} catch (Exception e) {
			error(e.toString());
			e.printStackTrace();
		}
	}

	private static void getInfoAdicionais(X509Certificate certificate)
			throws CertificateParsingException {

		info("--------------------------------------------------------");
		Collection<?> alternativeNames = X509ExtensionUtil.getSubjectAlternativeNames(certificate);
		for (Object alternativeName : alternativeNames) {
			if (alternativeName instanceof ArrayList) {
				ArrayList<?> listOfValues = (ArrayList<?>) alternativeName;
				Object value = listOfValues.get(1);
				if (value instanceof DERSequence) {
					DERSequence derSequence = (DERSequence) value;
					DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier) derSequence.getObjectAt(0);
					DERTaggedObject derTaggedObject = (DERTaggedObject) derSequence.getObjectAt(1);
					DERObject derObject = derTaggedObject.getObject();

					String valueOfTag = "";
					if (derObject instanceof DEROctetString) {
						DEROctetString octet = (DEROctetString) derObject;
						valueOfTag = new String(octet.getOctets());
					} 
					else if (derObject instanceof DERPrintableString) {
						DERPrintableString octet = (DERPrintableString) derObject;
						valueOfTag = new String(octet.getOctets());
					} 
					else if (derObject instanceof DERUTF8String) {
						DERUTF8String str = (DERUTF8String) derObject;
						valueOfTag = str.getString();
					}
					
					if ((valueOfTag != null) && (!"".equals(valueOfTag))) {
						if (derObjectIdentifier.equals(RESPONSAVEL)) {
							info("Nome do Responsavel: " + derObjectIdentifier + " - " + valueOfTag);
						}
						else if (derObjectIdentifier.equals(CNPJ)) {
							info("CNPJ...............: " + derObjectIdentifier + " - " + valueOfTag); 
						}
						else {
							info("OID................: " + derObjectIdentifier + " - " + valueOfTag);
						}
					}
				}
			}
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
