package br.com.javac.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Security;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

import br.com.javac.v200.consstatserv.ObjectFactory;
import br.com.javac.v200.consstatserv.TConsStatServ;
import br.com.javac.v200.retconsstatserv.TRetConsStatServ;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub.NfeStatusServicoNF2Result;

public class ConsultaStatusServicoJAXB {

	public static void main(String[] args) {
		try {
            /**
             * 1) codigoDoEstado = Codigo do Estado conforme tabela IBGE.
             *
             * 2) url = Endereço do WebService para cada Estado.
             *       Ver relacao dos enderecos em:
             *       Para Homologação: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx
             *       Para Producao: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx
             *
             * 3) caminhoDoCertificadoDoCliente = Caminho do Certificado do Cliente (A1).
             *
             * 4) senhaDoCertificadoDoCliente = Senha do Certificado A1 do Cliente.
             *
             * 5) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para
             * acessar o WebService. Pode ser gerado com a Classe NFeBuildCacerts.
             */
            String codigoDoEstado = "42";
            URL url = new URL("https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
            String caminhoDoCertificadoDoCliente = "C:/JavaC/NF-e/certificadoDoCliente.pfx";
            String senhaDoCertificadoDoCliente = "1234";
            String arquivoCacertsGeradoParaCadaEstado = "NFeCacerts";

            /**
             * Certificados.
             */
            loadCertificates(caminhoDoCertificadoDoCliente,
					senhaDoCertificadoDoCliente,
					arquivoCacertsGeradoParaCadaEstado);

            /**
             * Xml de Consulta.
             */
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            	.append(createXML(codigoDoEstado));
			info("XML Envio......: " + xml.toString());

			/**
			 * Consulta Status do Serviço.
			 */
			TRetConsStatServ retConsStatServ = consultaStatusServico(
					codigoDoEstado, url, xml);

			/**
			 * Retorno da Consulta.
			 */
			info("| CStat........: " + retConsStatServ.getCStat());
			info("| CUF..........: " + retConsStatServ.getCUF());
			info("| TMed.........: " + retConsStatServ.getTMed());
			info("| TpAmb........: " + retConsStatServ.getTpAmb());
			info("| VerAplic.....: " + retConsStatServ.getVerAplic());
			info("| Versao.......: " + retConsStatServ.getVersao());
			info("| XMotivo......: " + retConsStatServ.getXMotivo());
			info("| XObs.........: " + retConsStatServ.getXObs());
			info("| DhRecbto.....: " + retConsStatServ.getDhRecbto());
			info("| DhRetorno....: " + retConsStatServ.getDhRetorno());
		} catch (Exception e) {
			error(e.toString());
		}
	}

	/**
	 * Realiza a Consulta de Status do Serviço.
	 * @param codigoDoEstado
	 * @param url
	 * @param xml
	 * @return
	 * @throws XMLStreamException
	 * @throws AxisFault
	 * @throws RemoteException
	 * @throws JAXBException
	 */
	private static TRetConsStatServ consultaStatusServico(
			String codigoDoEstado, URL url, StringBuilder xml)
			throws XMLStreamException, AxisFault, RemoteException, JAXBException {
		OMElement ome = AXIOMUtil.stringToOM(xml.toString());
		NfeStatusServico2Stub.NfeDadosMsg dadosMsg = new NfeStatusServico2Stub.NfeDadosMsg();
		dadosMsg.setExtraElement(ome);

		NfeStatusServico2Stub.NfeCabecMsg nfeCabecMsg = new NfeStatusServico2Stub.NfeCabecMsg();
		/**
		 * Codigo do Estado.
		 */
		nfeCabecMsg.setCUF(codigoDoEstado);

		/**
		 * Versao do XML
		 */
		nfeCabecMsg.setVersaoDados("2.00");
		NfeStatusServico2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeStatusServico2Stub.NfeCabecMsgE();
		nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

		NfeStatusServico2Stub stub = new NfeStatusServico2Stub(url.toString());
		NfeStatusServico2Stub.NfeStatusServicoNF2Result nfeStatusServicoNF2Result = 
			stub.nfeStatusServicoNF2(dadosMsg, nfeCabecMsgE);

		return parseXML(nfeStatusServicoNF2Result);
	}

	/**
	 * Prepara o ambiente com os Certificados necessários.
	 * @param caminhoDoCertificadoDoCliente
	 * @param senhaDoCertificadoDoCliente
	 * @param arquivoCacertsGeradoParaCadaEstado
	 */
	private static void loadCertificates(String caminhoDoCertificadoDoCliente,
			String senhaDoCertificadoDoCliente,
			String arquivoCacertsGeradoParaCadaEstado) {
		/**
		 * Informacoes do Certificado Digital.
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
	}

    /**
     * Método que popula o Objeto JAXB.
     * @param codigoDoEstado
     * @return
     * @throws JAXBException
     */
	private static String createXML(String codigoDoEstado) throws JAXBException {
		TConsStatServ consStatServ = new TConsStatServ();
		consStatServ.setCUF(codigoDoEstado);
		consStatServ.setTpAmb("2");
		consStatServ.setVersao("2.00");
		consStatServ.setXServ("STATUS");
		return strValueOf(consStatServ);
	}

	/**
	 * Método que Converte String em Objeto.
	 * @param nfeStatusServicoNF2Result
	 * @return
	 * @throws JAXBException
	 */
    @SuppressWarnings("unchecked")
	private static TRetConsStatServ parseXML(
			NfeStatusServicoNF2Result nfeStatusServicoNF2Result) throws JAXBException {
    	String xml = nfeStatusServicoNF2Result.getExtraElement().toString();
    	info("XML Retorno....: " + xml);
    	
    	JAXBContext context = JAXBContext.newInstance("br.com.javac.retconsstatservv2");
    	Unmarshaller unmarshaller = context.createUnmarshaller();
    	JAXBElement<TRetConsStatServ> element = (JAXBElement<TRetConsStatServ>) 
    		unmarshaller.unmarshal(new StringReader(xml));
		return element.getValue();
	}

	/**
	 * Método que Converte o Objeto em String.
	 * @param consStatServ
	 * @return
	 * @throws JAXBException
	 */
	private static String strValueOf(TConsStatServ consStatServ) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("br.com.javac.consstatservv2");
		Marshaller marshaller = context.createMarshaller();
		JAXBElement<TConsStatServ> element = new ObjectFactory().createConsStatServ(consStatServ);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		StringWriter sw = new StringWriter();
		marshaller.marshal(element, sw);
		return sw.toString();
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
