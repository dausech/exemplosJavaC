package br.com.javac.nfe;

import java.net.URL;
import java.security.Security;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;

public class NFeTestStatusServico {

	public static void main(String[] args) {
		//DaoEmpresa daoE=new DaoEmpresaImp();  
		//ConexaoNFe conn = new ConexaoNFe();  
		String resultadoStatus = "";  
		URL url = null;
		
		String estado = "MG";
		int ambiente = 2;

		try {  
			//Empresa emp = new Empresa();  
			//emp.setCodigo(1);  
			//emp.setUf(new Estado("MG","MG",99));  
			//emp=daoE.retornaEmpresa(emp);  
	  
			//if (conn.configuraPorvider() == false) {
			if (true) {
				/* 
				 * Seta as configurações para a conexao 
				 **/  
				System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");  
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
	  
				System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");  
				
				System.clearProperty("javax.net.ssl.keyStore");  
				System.clearProperty("javax.net.ssl.keyStorePassword");  
				System.clearProperty("javax.net.ssl.trustStore");  
	  
				System.setProperty("javax.net.ssl.keyStore", "C:/JavaC/NF-e/certificadoDoCliente.pfx");  
				System.setProperty("javax.net.ssl.keyStorePassword", "1234");  
	  
				System.setProperty("javax.net.ssl.trustStoreType", "JKS");  
				System.setProperty("javax.net.ssl.trustStore", "nfe-cacerts");
				//System.setProperty("javax.net.ssl.trustStore", "C:/Java/controleti/nfeJavaC/NFeCacerts");
	  
			}  
	  
			/** 
			 * Xml de Consulta. 
			 */  
			StringBuilder xml = new StringBuilder();  
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<consStatServ versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">").append("<tpAmb>2</tpAmb>").append("<cUF>31</cUF>").append("<xServ>STATUS</xServ>").append("</consStatServ>");  
	  
			OMElement ome = AXIOMUtil.stringToOM(xml.toString());  
			NfeStatusServico2Stub.NfeDadosMsg dadosMsg = new NfeStatusServico2Stub.NfeDadosMsg();  
			dadosMsg.setExtraElement(ome);  
	  
			NfeStatusServico2Stub.NfeCabecMsg nfeCabecMsg = new NfeStatusServico2Stub.NfeCabecMsg();  
	  
			//minas geraris  
			//if (emp.getUf().getUf().equals("MG")) {
			if (estado.equals("MG")) {
				/** 
				 * Código do Estado. 
				 */  
				nfeCabecMsg.setCUF("31");  
				/** 
				 * Altere para o endereço que desejar. 
				 */  
				//if (emp.getTmbNfe() == 1) {//ambiente producao
				if (ambiente == 1) {//ambiente producao
					url = new URL("https://nfe.fazenda.mg.gov.br/nfe2/services/NfeStatus2");  
				} else {  
					//ambiente homologacao  
					url = new URL("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeStatus2");  
				}  
			} else {//sao Paulo  
				/** 
				 * Altere para o endereço que desejar. 
				 */  
				//if (emp.getTmbNfe() == 1) { //ambiente producao
				if (ambiente == 1) {//ambiente producao
					url = new URL("https://nfe.fazenda.sp.gov.br/nfeweb/services/nfestatusservico2.asmx ");  
				} else {  
					//ambiente homologacao  
					url = new URL("https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/NfeStatusServico2.asmx ");  
				}  
			}  
	  
	  
			/** 
			 * Versao do XML 
			 */  
			nfeCabecMsg.setVersaoDados("2.00");  
			NfeStatusServico2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeStatusServico2Stub.NfeCabecMsgE();  
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);  
	  
			NfeStatusServico2Stub stub = new NfeStatusServico2Stub(url.toString());  
			NfeStatusServico2Stub.NfeStatusServicoNF2Result result = stub.nfeStatusServicoNF2(dadosMsg, nfeCabecMsgE);  
	  
			System.out.println(result.getExtraElement().toString());  
			resultadoStatus = result.getExtraElement().toString();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  

		System.out.println(resultadoStatus);  
	}
}
