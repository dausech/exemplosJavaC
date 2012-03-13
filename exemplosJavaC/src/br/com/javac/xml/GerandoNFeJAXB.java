package br.com.javac.xml;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.com.javac.v200.nfe.ObjectFactory;
import br.com.javac.v200.nfe.TEnderEmi;
import br.com.javac.v200.nfe.TEndereco;
import br.com.javac.v200.nfe.TNFe;
import br.com.javac.v200.nfe.TNFe.InfNFe;
import br.com.javac.v200.nfe.TNFe.InfNFe.Dest;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import br.com.javac.v200.nfe.TNFe.InfNFe.Det.Prod;
import br.com.javac.v200.nfe.TNFe.InfNFe.Emit;
import br.com.javac.v200.nfe.TNFe.InfNFe.Ide;
import br.com.javac.v200.nfe.TNFe.InfNFe.InfAdic;
import br.com.javac.v200.nfe.TNFe.InfNFe.Total;
import br.com.javac.v200.nfe.TNFe.InfNFe.Total.ICMSTot;
import br.com.javac.v200.nfe.TNFe.InfNFe.Transp;
import br.com.javac.v200.nfe.TNFe.InfNFe.Transp.Transporta;
import br.com.javac.v200.nfe.TNFe.InfNFe.Transp.Vol;
import br.com.javac.v200.nfe.TUf;
import br.com.javac.v200.nfe.TUfEmi;

public class GerandoNFeJAXB {

	public static void main(String[] args) {
		try {
			TNFe nFe = new TNFe();
			InfNFe infNFe = new InfNFe();
	
			infNFe.setId("NFe42110403452234000145550010000000281765232800");
			infNFe.setVersao("2.00");
	
			infNFe.setIde(dadosDeIdentificacao());
			infNFe.setEmit(dadosDoEmitente());
			infNFe.setDest(dadosDoDestinatario());
			
			infNFe.getDet().add(dadosDoProduto());
			
			infNFe.setTotal(totaisDaNFe());
			infNFe.setTransp(dadosDoTransporte());
			infNFe.setInfAdic(informacoesAdicionais());
			
			nFe.setInfNFe(infNFe);
	
			info("XML NF-e: " + strValueOf(nFe));
		} catch (Exception e) {
			error(e.toString());
		}
	}

	/**
	 * B - Identificação da Nota Fiscal eletrônica
	 * @return
	 */
	private static Ide dadosDeIdentificacao() {
		Ide ide = new Ide();
		ide.setCUF("42");
		ide.setCNF("76523280");
		ide.setNatOp("5102");
		ide.setIndPag("0");
		ide.setMod("55");
		ide.setSerie("1");
		ide.setNNF("101");
		ide.setDEmi("2011-04-16");
		ide.setDSaiEnt("2011-04-16");
		ide.setHSaiEnt("15:03:56");
		ide.setTpNF("1");
		ide.setCMunFG("4202800");
		ide.setTpImp("2");
		ide.setTpEmis("1");
		ide.setCDV("0");
		ide.setTpAmb("2");
		ide.setFinNFe("1");
		ide.setProcEmi("0");
		ide.setVerProc("3.0");
		return ide;
	}

	/**
	 * C - Identificação do Emitente da Nota Fiscal eletrônica
	 * @return
	 */
	private static Emit dadosDoEmitente() {
		Emit emit = new Emit();
		emit.setCNPJ("03452234000145");
		emit.setXNome("JavaC - Java Communuty");
		emit.setXFant("JavaC");

		TEnderEmi enderEmit = new TEnderEmi(); 
		enderEmit.setXLgr("AV. www.javac.com.br");
		enderEmit.setNro("677");
		enderEmit.setXBairro("WEB");
		enderEmit.setCMun("4202800");
		enderEmit.setXMun("Java");
		enderEmit.setUF(TUfEmi.valueOf("SC"));
		enderEmit.setCEP("88750000");
		enderEmit.setCPais("1058");
		enderEmit.setXPais("Brasil");
		enderEmit.setFone("4812121212");
		emit.setEnderEmit(enderEmit);

		emit.setIE("111111111");
		emit.setCRT("1");		
		return emit;
	}

	/**
	 * E - Identificação do Destinatário da Nota Fiscal eletrônica
	 * @return
	 */
	private static Dest dadosDoDestinatario() {
		Dest dest = new Dest();
		dest.setCNPJ("12345123000133");
		dest.setXNome("Comunidade JavaC");
		
		TEndereco enderDest = new TEndereco(); 
		enderDest.setXLgr("Rua: Membros JavaC");
		enderDest.setNro("10");
		enderDest.setXBairro("WEB");
		enderDest.setCMun("4202800");
		enderDest.setXMun("Java");
		enderDest.setUF(TUf.valueOf("SC"));
		enderDest.setCEP("88750000");
		enderDest.setCPais("1058");
		enderDest.setXPais("Brasil");
		enderDest.setFone("4845454545");
		dest.setEnderDest(enderDest);

		dest.setIE("464646464");
		dest.setEmail("forum@javac.com.br");
		return dest;
	}

	/**
	 * H - Detalhamento de Produtos e Serviços da NF-e
	 * I - Produtos e Serviços da NF-e
	 * M - Tributos incidentes no Produto ou Serviço
	 * V - Informações adicionais
	 * @return
	 */
	private static Det dadosDoProduto() {
		Det det = new Det();
		det.setNItem("1");

		/**
		 * Dados do Produro
		 */
		Prod prod = new Prod();
		prod.setCProd("2");
		prod.setXProd("Exemplo geracao XML JAXB");
		prod.setNCM("99");
		prod.setCFOP("5102");
		prod.setUCom("UND");
		prod.setQCom("2.0000");
		prod.setVUnCom("90.0000");
		prod.setVProd("180.00");
		prod.setUTrib("UND");
		prod.setQTrib("2.0000");
		prod.setVUnTrib("90.0000");
		prod.setIndTot("1");
		det.setProd(prod);
		
		/**
		 * Impostos da NF-e
		 */
		Imposto imposto = new Imposto();
		
		ICMS icms = new ICMS();
		ICMSSN500 icmssn500 = new ICMSSN500();
		icmssn500.setOrig("0");
		icmssn500.setCSOSN("500");
		icmssn500.setVBCSTRet("0.00");
		icmssn500.setVICMSSTRet("0.00");
		icms.setICMSSN500(icmssn500);
		
		PIS pis = new PIS();
		PISNT pisnt = new PISNT();
		pisnt.setCST("07");
		pis.setPISNT(pisnt);

		COFINS cofins = new COFINS();
		COFINSNT cofinsnt = new COFINSNT();
		cofinsnt.setCST("07");
		cofins.setCOFINSNT(cofinsnt);
		
		imposto.setICMS(icms);
		imposto.setPIS(pis);
		imposto.setCOFINS(cofins);
		
		det.setImposto(imposto);
		
		/**
		 * Informações adicionais do Produto.
		 */
		det.setInfAdProd("Nota Fiscal Eletronica de Exemplo");

		return det;
	}

	/**
	 * W - Valores Totais da NF-e
	 * @return
	 */
	private static Total totaisDaNFe() {
		Total total = new Total();

		ICMSTot icmstot = new ICMSTot();
		icmstot.setVBC("0.00");
		icmstot.setVICMS("0.00");
		icmstot.setVBCST("0.00");
		icmstot.setVST("0.00");
		icmstot.setVProd("180.00");
		icmstot.setVFrete("0.00");
		icmstot.setVSeg("0.00");
		icmstot.setVDesc("0.00");
		icmstot.setVII("0.00");
		icmstot.setVIPI("0.00");
		icmstot.setVPIS("0.00");
		icmstot.setVCOFINS("0.00");
		icmstot.setVOutro("0.00");
		icmstot.setVNF("180.00");
		total.setICMSTot(icmstot);

		return total;
	}

	/**
	 * X - Informações do Transporte da NF-e
	 * @return
	 */
	private static Transp dadosDoTransporte() {
		Transp transp = new Transp();
		transp.setModFrete("0");
		
		/**
		 * Dados da Transportadora.
		 */
		Transporta transporta = new Transporta();
		transporta.setCNPJ("34523233000123");
		transporta.setXNome("JavaC - Java Communuty");
		transporta.setIE("121212121");
		transporta.setXEnder("AV. www.javac.com.br");
		transporta.setXMun("Java");
		transporta.setUF(TUf.valueOf("SC"));		
		transp.setTransporta(transporta);
		
		/**
		 * Dados de Volumes.
		 */
		Vol vol = new Vol();
		vol.setQVol("0");
		vol.setNVol("0");
		vol.setPesoL("0.000");
		vol.setPesoB("0.000");
		transp.getVol().add(vol);

		return transp;
	}

	/**
	 * Z - Informações Adicionais da NF-e
	 * @return
	 */
	private static InfAdic informacoesAdicionais() {
		InfAdic infAdic = new InfAdic();
		infAdic.setInfCpl("Informacoes Adicionais da NF-e.");
		return infAdic;
	}

	/**
	 * Método que Converte o Objeto em String.
	 * @param consStatServ
	 * @return
	 * @throws JAXBException
	 */
	private static String strValueOf(TNFe nfe) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(TNFe.class);
		Marshaller marshaller = context.createMarshaller();
		JAXBElement<TNFe> element = new ObjectFactory().createNFe(nfe);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		StringWriter sw = new StringWriter();
		marshaller.marshal(element, sw);

		String xml = sw.toString();
		xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");
		xml = xml.replaceAll("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
		
		return xml;
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
