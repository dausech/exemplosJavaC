package br.com.javac.nfe;

import java.net.URL;
import java.security.Security;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;

/**
 *
 * @author JavaC - Java Community
 */
public class NFeConsultaStatusServicoAll {

    public static void main(String[] args) {
        try {
            /**
             * 1) codigoDoEstado = Código do Estado conforme tabela IBGE.
             *
             * 2) url = Endereço do WebService para cada Estado.
             *       Ver relação dos endereços em:
             *       Para Homologação: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx
             *       Para Produção: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx
             *
             * 3) caminhoDoCertificadoDoCliente = Caminho do Certificado do Cliente (A1).
             *
             * 4) senhaDoCertificadoDoCliente = Senha do Certificado A1 do Cliente.
             *
             * 5) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para
             * acessar o WebService. Pode ser gerado com a Classe NFeBuildAllCacerts.
             */
            String caminhoDoCertificadoDoCliente = "C:/JavaC/NF-e/certificadoDoCliente.pfx";
            String senhaDoCertificadoDoCliente = "1234";
            String arquivoCacertsGeradoParaCadaEstado = "NFeCacerts";

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
             * Consulta Todos os Estados (Homologação);
             * Região Norte.
			*/            
            consultaStatus("11", // 11-Rondônia
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("12", // 12-Acre 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("13", // 13-Amazonas 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("14", // 14-Roraima 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("15", // 15-Pará 
            		new URL("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx"));
            consultaStatus("16", // 16-Amapá 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("17", // 17-Tocantins 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
        	
            /**
             * Regiao Nordeste.
             */
            consultaStatus("21", // 21-Maranhão 
            		new URL("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx"));
            consultaStatus("22", // 22-Piauí 
            		new URL("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx"));
            consultaStatus("23", // 23-Ceará 
            		new URL("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx"));
            consultaStatus("24", // 24-Rio Grande do Norte 
            		new URL("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx"));
            consultaStatus("25", // 25-Paraíba 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("26", // 26-Pernambuco 
            		new URL("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2"));
            consultaStatus("27", // 27-Alagoas 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("28", // 28-Sergipe 
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("29", // 29-Bahia
            		new URL("https://hnfe.sefaz.ba.gov.br/webservices/nfenw/NfeStatusServico2.asmx"));

            /**
             * Região Sudeste.
             */
            consultaStatus("31", // 31-Minas Gerais
            		new URL("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeStatus2"));
            consultaStatus("32", // 32-Espírito Santo
            		new URL("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx"));
            consultaStatus("33", // 33-Rio de Janeiro
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("35", // 35-São Paulo
            		new URL("https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/NfeStatusServico2.asmx"));

            /**
             * Região Sul.
             */
            consultaStatus("41", // 41-Paraná
            		new URL("https://homologacao.nfe2.fazenda.pr.gov.br/nfe/NFeStatusServico2?wsdl"));
            consultaStatus("42", // 42-Santa Catarina
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            consultaStatus("43", // 43-Rio Grande do Sul
            		new URL("https://homologacao.nfe.sefaz.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));
            
            /**
             * Regiao Centro Oeste.
             * 
             * 
             * 53-Distrito Federal
             */
            consultaStatus("50", // 50-Mato Grosso do Sul
            		new URL("https://homologacao.nfe.ms.gov.br/homologacao/services2/NfeStatusServico2"));
            consultaStatus("51", // 51-Mato Grosso
            		new URL("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2"));
            consultaStatus("52", // 52-Goiás
            		new URL("https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2"));
            consultaStatus("53", // 53-Distrito Federal
            		new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx"));

        } catch (Exception e) {
        	error(e.toString());
        }
    }

	private static void consultaStatus(String codigoDoEstado, URL url) {
		try {
			/**
			 * Xml de Consulta.
			 */
			StringBuilder xml = new StringBuilder();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			    .append("<consStatServ versao=\"2.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\">")
			    .append("<tpAmb>2</tpAmb>")
			    .append("<cUF>")
			    .append(codigoDoEstado)
			    .append("</cUF>")
			    .append("<xServ>STATUS</xServ>")
			    .append("</consStatServ>");
	
			OMElement ome = AXIOMUtil.stringToOM(xml.toString());
			NfeStatusServico2Stub.NfeDadosMsg dadosMsg = new NfeStatusServico2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);
	
			NfeStatusServico2Stub.NfeCabecMsg nfeCabecMsg = new NfeStatusServico2Stub.NfeCabecMsg();
			/**
			 * Código do Estado.
			 */
			nfeCabecMsg.setCUF(codigoDoEstado);
	
			/**
			 * Versão do XML
			 */
			nfeCabecMsg.setVersaoDados("2.00");
			NfeStatusServico2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeStatusServico2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);
	
			NfeStatusServico2Stub stub = new NfeStatusServico2Stub(url.toString());
			NfeStatusServico2Stub.NfeStatusServicoNF2Result result = stub.nfeStatusServicoNF2(dadosMsg, nfeCabecMsgE);
	
			info("Estado " + codigoDoEstado + ": XML = " + result.getExtraElement().toString());
		} catch (Exception e) {
			error(codigoDoEstado + ": " + e.toString());
		}
	}

	private static void error(String log) {
		System.out.println("ERROR: " + log);
	}

	private static void info(String log) {
		System.out.println("INFO: " + log);
	}

}
