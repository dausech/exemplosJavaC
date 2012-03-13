package br.com.javac.cce;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub;

/**
 * Envio do Lote da CCe (Carta de Correção Eletrônica)
 * @author Maciel Gonçalves - http://www.javac.com.br
 *
 */
public class CCeRecepcaoEvento {

	public static void main(String[] args) {
		
		try {
            String codigoDoEstado = "42";

            URL url = new URL("https://hom.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx");
            
            String caminhoDoCertificadoDoCliente = "C:/JavaC/CC-e/certificadoDoCliente.pfx";
            String senhaDoCertificadoDoCliente = "123456";
            String arquivoCacertsGeradoParaCadaEstado = "C:/JavaC/CC-e/NFeCacerts";

            System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
            
            /**
             * Informações do Certificado Digital.
             */
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

            System.clearProperty("javax.net.ssl.keyStore");
            System.clearProperty("javax.net.ssl.keyStorePassword");
            System.clearProperty("javax.net.ssl.trustStore");

            System.setProperty("javax.net.ssl.keyStore", caminhoDoCertificadoDoCliente);
            System.setProperty("javax.net.ssl.keyStorePassword", senhaDoCertificadoDoCliente);

            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            System.setProperty("javax.net.ssl.trustStore", arquivoCacertsGeradoParaCadaEstado);

            /**
             * IMPORTANTE: O XML já deve ser assinado antes do envio.
             * Lendo o Xml de um arquivo Gerado.
             */
            StringBuilder xml = new StringBuilder();
            String linha = null;
            String caminhoArquivo = "C:/JavaC/CC-e/LoteCCe.xml";
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(caminhoArquivo), "ISO-8859-1"));
            while ((linha = in.readLine()) != null) {
                xml.append(linha);
            }
            in.close();

            String xmlEnvNFe = xml.toString();
            OMElement ome = AXIOMUtil.stringToOM(xmlEnvNFe);

            RecepcaoEventoStub.NfeDadosMsg dadosMsg = new RecepcaoEventoStub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);
            RecepcaoEventoStub.NfeCabecMsg nfeCabecMsg = new RecepcaoEventoStub.NfeCabecMsg();

            /**
             * Código do Estado.
             */
            nfeCabecMsg.setCUF(codigoDoEstado);

            /**
             * Versao do XML
             */
            nfeCabecMsg.setVersaoDados("1.00");

            RecepcaoEventoStub.NfeCabecMsgE nfeCabecMsgE = new RecepcaoEventoStub.NfeCabecMsgE();
            nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

            RecepcaoEventoStub stub = new RecepcaoEventoStub(url.toString());
            RecepcaoEventoStub.NfeRecepcaoEventoResult result = stub.nfeRecepcaoEvento(dadosMsg, nfeCabecMsgE);

            info(result.getExtraElement().toString());
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
