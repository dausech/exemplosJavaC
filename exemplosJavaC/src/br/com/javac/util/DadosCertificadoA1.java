package br.com.javac.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.X509Certificate;
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

public class DadosCertificadoA1 {

	@SuppressWarnings({ "static-access", "rawtypes" })
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
			info("KS DefType : " + ks.getDefaultType());
	
			String alias = null;
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
					
					//byte[] extVal = cert.getExtensionValue("2.5.29.17");
					
					//byte[] extnValue = DEROctetString.getInstance(ASN1Object.fromByteArray(extVal)).getOctets();
					//Enumeration it = DERSequence.getInstance(ASN1Object.fromByteArray(extnValue)).getObjects();
					//while (it.hasMoreElements()) {					
						//GeneralName genName = GeneralName.getInstance(it.nextElement());
						
						
						//if (genName.getTagNo() == GeneralName.rfc822Name) {
							//System.out.println("e-mail...: " + genName.getName().toString().toLowerCase());
						//}
						//else if (genName.getTagNo() == GeneralName.otherName) {
							//DERObject derObject = genName.getDERObject();
							
							
							
							//System.out.println(derObject.getEncoded("2.16.76.1.3.2").toString());
						//}
					//}
					
					
					Collection<?> col = X509ExtensionUtil.getSubjectAlternativeNames(cert);
					for (Object obj : col) {
						if (obj instanceof ArrayList) {
							ArrayList lst = (ArrayList) obj;
							Object value = lst.get(1);
							if (value instanceof DERSequence) {
								/**
								 * DER Sequence
								 *      ObjectIdentifier
								 *      Tagged
								 *          DER Octet String
								 */
								DERSequence seq = (DERSequence) value;
								DERObjectIdentifier oid = (DERObjectIdentifier) seq.getObjectAt(0);
								DERTaggedObject tagged = (DERTaggedObject) seq.getObjectAt(1);
								String info = null;
								DERObject derObj = tagged.getObject();
								
								if (derObj instanceof DEROctetString) {
									DEROctetString octet = (DEROctetString) derObj;
									info = new String(octet.getOctets());
								} else if (derObj instanceof DERPrintableString) {
									DERPrintableString octet = (DERPrintableString) derObj;
									info = new String(octet.getOctets());
								} else if (derObj instanceof DERUTF8String) {
									DERUTF8String str = (DERUTF8String) derObj;
									info = str.getString();
								}
								
								if (info != null && !info.isEmpty()) {
									System.out.println(oid + " - " + info);
								}
							}
						}
					}
					
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
 