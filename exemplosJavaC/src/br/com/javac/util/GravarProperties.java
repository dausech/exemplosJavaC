package br.com.javac.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class GravarProperties {

	public static void main(String[] args) {
		FileOutputStream fileOutputStreamWrite = null;
		FileInputStream fileInputStreamRead = null;
		
		try {
			String arquivoConfiguracao = "confignfe.properties";
			String conteudoDoCampoTxtDirArquivoTxt = "C:/arquivosTXTs";
			String conteudoDoCampoTxtDirArquivoXml = "C:/arquivosXMLs";
			
			/**
			 * Gravando o Arquivo de configuracao.
			 */
			info("Granvando Arquivo de Configuracao...");
			Properties properties = new Properties();
			properties.setProperty("dirTXT", conteudoDoCampoTxtDirArquivoTxt);
			properties.setProperty("dirXML", conteudoDoCampoTxtDirArquivoXml);
			
			try {
				File fileConfigWrite = new File(arquivoConfiguracao);
				fileOutputStreamWrite = new FileOutputStream(fileConfigWrite);
				properties.store(fileOutputStreamWrite, "");
				fileOutputStreamWrite.flush();
				fileOutputStreamWrite.close();
			} finally {
				fileOutputStreamWrite.close();	
			}
			info("Arquivo de configuracao gravado com sucesso...");
			
			/**
			 * Recuperando os dados do Arquivo de configuracao.
			 */
			info("");
			info("Recuperando os dados do arquivo...");
			try {
				File fileConfigRead = new File(arquivoConfiguracao);
				fileInputStreamRead = new FileInputStream(fileConfigRead);
				Properties propertiesRead = new Properties();
				propertiesRead.load(fileInputStreamRead);
			} finally {
				fileInputStreamRead.close();
			}
			
			String dirTXT = properties.getProperty("dirTXT");
			String dirXML = properties.getProperty("dirXML");
			
			info("Dir TXT: " + dirTXT);
			info("Dir XML: " + dirXML);
		} catch (Exception e) {
			error(e.toString());
		}
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
