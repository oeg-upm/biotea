package ws.biotea.ld2rdf.util;  

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public enum Prefix {
	//OWL
	OWL("http://www.w3.org/2002/07/owl#", "owl"),
	//PROV
	PROV("http://www.w3.org/ns/prov#", "prov"),
	//FOAF
	FOAF("http://xmlns.com/foaf/0.1/", "foaf"), 
	XSP("http://www.owl-ontologies.com/2005/08/07/xsp.owl#", "xsp"),	
	//Biotea
	BIOTEA("http://www.biotea.ws/ontology/ao_biotea.owl#", "biotea"), 
	//Tags
	TAG("http://www.holygoat.co.uk/owl/redwood/0.1/tags/", "tags"), 
	//AO
	AO_CORE("http://purl.org/ao/core/", "ao"), 
	AO_TYPES("http://purl.org/ao/types/", "aot"), 
	AO_SELECTORS("http://purl.org/ao/selectors/", "aos"), 
	AO_ANNOTEA("http://purl.org/ao/annotea/", "aoa"), 
	AO_FOAF("http://purl.org/ao/foaf/", "aof"), 
	PAV("http://purl.org/pav/", "pav"), //http://purl.org/swan/pav/provenance/
	//RDF
	RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf"), 
	RDFS("http://www.w3.org/2000/01/rdf-schema#", "rdfs"),	
	//VOID
	VOID("http://rdfs.org/ns/void#", "void"),
	//DOCO
	DOCO("http://purl.org/spar/doco/", "doco"),
	//BIBO
	BIBO("http://purl.org/ontology/bibo/", "bibo"),
	//DC
	DCTERMS("http://purl.org/dc/terms/", "dcterms"),
	DC("http://purl.org/dc/elements/1.1/", "dc"),
	//Content in RDF
	CNT("http://www.w3.org/2011/content#", "cnt"),
	//scienceCommons
	//SCIENCE_COMMONS("http://purl.org/science/owl/sciencecommons/", "sciencecommons"),
	//ontos
	//CHEBI("http://purl.obolibrary.org/obo/CHEBI_", "chebi"), //bio2rdf http://identifiers.org/obo.chebi/CHEBI:16526
	//genes, proteins
	//GO("http://purl.obolibrary.org/obo/GO_", "go"), //bio2rdf http://identifiers.org/obo.go/GO:#  --was before http://purl.org/obo/owl/GO#GO_
	//PW("http://purl.obolibrary.org/obo/PW_", "pw"), //was http://purl.org/obo/owl/PW#PW_
	//MGED("http://mged.sourceforge.net/ontologies/MGEDOntology.owl#", "mged"),
	//UNIPROT("http://purl.uniprot.org/uniprot/", "uniprot"), //bio2rdf http://identifiers.org/uniprot/accession
	//UNIPROT_TAXONOMY("http://purl.uniprot.org/taxonomy/", "taxonomy"),
	//drugs
	//MDDB("http://purl.bioontology.org/ontology/MDDB/", "mddb"),
	//NDDF("http://purl.bioontology.org/ontology/NDDF/", "nddf"),
	//NDFRT("http://purl.bioontology.org/ontology/NDFRT/", "ndfrt"),
	//medicine
	//medline("http://purl.bioontology.org/ontology/MEDLINEPLUS/", "medline"),
	//SNOMED("http://purl.bioontology.org/ontology/SNOMEDCT/", "snomedct"),
	//symptom("http://purl.obolibrary.org/obo/SYMP_", "symptom"), //was http://purl.org/obo/owl/SYMP#SYMP_
	////csso("http://purl.jp/bio/11/csso/CSSO_", "csso"),
	//MedDRA("http://purl.bioontology.org/ontology/MDR/", "meddra"),
	//mesh("http://purl.bioontology.org/ontology/MSH/", "mesh"), //bio2rdf
	//OMIM("http://purl.bioontology.org/ontology/OMIM/", "omim"), //bio2rdf http://identifiers.org/omim/602080
	//FMA("http://sig.uw.edu/fma#", "fma"), //http://identifiers.org/obo.fma/FMA:67112 --was before http://purl.org/obo/owl/FMA#FMA_; in identifiers.org is http://purl.obolibrary.org/obo/FMA_
	//ICD9("http://purl.bioontology.org/ontology/ICD9CM/", "icd9"), //http://identifiers.org/icd/C34, it was http://purl.bioontology.org/ontology/ICD9-9/
	//ICD10("http://purl.bioontology.org/ontology/ICD10/", "icd10"), //http://purl.bioontology.org/ontology/ICD10/ 1516 virtual id
	
	//OBI("http://purl.obolibrary.org/obo/BFO_", "obi"), // http://www.ifomis.org/bfo/1.1/span#// http://identifiers.org/obo.obi/OBI_0000070
	//UMLS_TYPE("http://berkeleybop.org/obo/UMLS_TYPE:", "oboumls"),
	//UMLS_TYPE("http://linkedlifedata.com/resource/semanticnetwork/id/", "umlstype"),
	//UMLS_ID("http://linkedlifedata.com/resource/umls/id/", "umlscui"),
	//UMLS("http://bioportal.bioontology.org/ontologies/umls/", "umls"),
	//plants
	//PO("http://purl.obolibrary.org/obo/PO_", "po"),//plant ontology=plant anatomy + Plant Growth and Development Stage
	//thesaurus	
	//NCIt("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "ncithesaurus"),//bio2rdf http://identifiers.org/ncit/C80519
	//organisms
	//NCBITaxon("http://purl.obolibrary.org/obo/NCBITaxon_", "ncbitaxon"), //http://identifiers.org/taxonomy/9606 was http://purl.org/obo/owl/NCBITaxon#NCBITaxon_
	;
	
	private String url;
	private String ns;
	
	private Prefix(String url, String ns) {
		this.url = url;
		this.ns = ns;
	}
	public String getURL() {
		return (this.url);
	}
	public String getNS() {
		return (this.ns);
	}
	public String getPrefix() {
		return "PREFIX " + this.ns + ":<" + this.url + "> ";
	}
	public static Prefix getByNS(String ns) throws NoSuchElementException {
		for (Prefix prefix: Prefix.values()) {
			if (prefix.getNS().equals(ns)) {
				return prefix;
			}
		}
		throw new NoSuchElementException("The prefix with NS " + ns + " cannot be resolved.");
	}
	public static String convertToNSAndTerm(String uri) throws NoSuchElementException {
		for (Prefix prefix: Prefix.values()) {
			if (uri.startsWith(prefix.getURL())) {
				String term = uri.substring(prefix.getURL().length());
				return prefix.getNS() + ":" + term;
			}
		}
		throw new NoSuchElementException("The prefix for a URI " + uri + " cannot be resolved.");
	}

	public static String toIdentifiersOrg(String uri) {
		try {
			String[] values = uri.split(":");
			if (values[0].toLowerCase().equals(ConfigPrefix.getNS("CHEBI"))) {
				return "http://identifiers.org/obo.chebi/CHEBI:" + values[1];
			} else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("GO"))) {
				return "http://identifiers.org/obo.go/GO:" + values[1];
			} else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("UNIPROT"))) {
				return "http://identifiers.org/uniprot/" + values[1];
			} else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("OMIM"))) {
				if (values[1].startsWith("MTHU")) {
					return null;
				}
				return "http://identifiers.org/omim/" + values[1];
			} /*else if (values[0].equals(Prefix.FMA.getNS())) {
				return "http://identifiers.org/obo.fma/FMA:" + values[1];
			} *//*else if (values[0].equals(Prefix.ICD9.getNS())) {
				return "http://identifiers.org/icd/" + values[1];
			} */else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("ICD10"))) {
				return "http://identifiers.org/icd/" + values[1];
			} else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("OBI"))) {
				return "http://identifiers.org/obo.obi/OBI_" + values[1];
			} /*else if (values[0].equals(Prefix.NCIt.getNS())) {
				return "http://identifiers.org/ncit/" + values[1];
			} */else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("NCBITaxon"))) {
				return "http://identifiers.org/taxonomy/" + values[1];
			} else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("UNIPROT_TAXONOMY"))) {
                return "http://identifiers.org/taxonomy:" + values[1];
            }		
			return null;
		} catch (Exception e) {
			return null;
		}
	}

    public static String toBio2RDFOrg(String uri) {
        try {
            String[] values = uri.split(":");
            if (values[0].toLowerCase().equals(ConfigPrefix.getNS("CHEBI"))) {
                return "http://bio2rdf.org/chebi:" + values[1];
            } else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("GO"))) {
                return "http://bio2rdf.org/go:" + values[1];
            } else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("UNIPROT"))) {
                return "http://bio2rdf.org/uniprot:" + values[1];
            } else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("OMIM"))) {
                if (values[1].startsWith("MTHU")) {
                    return null;
                }
                return "http://bio2rdf.org/omim:" + values[1];
            } else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("mesh"))) {
                return "http://bio2rdf.org/mesh:" + values[1];
            } else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("NCIt"))) {
				return "http://bio2rdf.org/ncithesaurus:" + values[1];
			} else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("NCBITaxon"))) {
                return "http://bio2rdf.org/taxonomy:" + values[1];
            } else if (values[0].toLowerCase().equals(ConfigPrefix.getNS("UNIPROT_TAXONOMY"))) {
                return "http://bio2rdf.org/taxonomy:" + values[1];
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String getCUI(String uri) {
        try {
            String[] values = uri.split(":");
            if (values[0].toLowerCase().equals(ConfigPrefix.getNS("UMLS_ID"))) {
                return values[1];//Config.getUmlsCuiURL() + values[1];
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

	public static String prefixes_AO_DOCO_BIBO() {		
		return Prefix.BIOTEA.getPrefix() + Prefix.FOAF.getPrefix() +
			Prefix.AO_CORE.getPrefix() + Prefix.AO_TYPES.getPrefix() +
			Prefix.AO_SELECTORS.getPrefix() + Prefix.AO_ANNOTEA.getPrefix() +
			Prefix.AO_FOAF.getPrefix() + Prefix.PAV.getPrefix() +
			Prefix.RDF.getPrefix() + Prefix.RDFS.getPrefix() + 
			Prefix.DOCO.getPrefix() +
			Prefix.BIBO.getPrefix() + Prefix.DCTERMS.getPrefix() + 
			Prefix.XSP.getPrefix() + Prefix.CNT.getPrefix() + 
			Prefix.PROV.getPrefix() + Prefix.OWL.getPrefix() +
			Prefix.VOID.getPrefix() + ConfigPrefix.getPrefix("UMLS")
			//+ Prefix.SCIENCE_COMMONS.getPrefix()
			;
	}
	public static Map<String, String> prefixesMap_AO_DOCO_BIBO() {	
		Map<String, String> map = new HashMap<String, String>();
		map.put(Prefix.BIOTEA.getNS(), Prefix.BIOTEA.getURL());
		map.put(Prefix.FOAF.getNS(), Prefix.FOAF.getURL());
		map.put(Prefix.AO_CORE.getNS(), Prefix.AO_CORE.getURL());
		map.put(Prefix.AO_TYPES.getNS(), Prefix.AO_TYPES.getURL());
		map.put(Prefix.AO_SELECTORS.getNS(), Prefix.AO_SELECTORS.getURL());
		map.put(Prefix.AO_ANNOTEA.getNS(), Prefix.AO_ANNOTEA.getURL());
		map.put(Prefix.AO_FOAF.getNS(), Prefix.AO_FOAF.getURL());
		map.put(Prefix.PAV.getNS(), Prefix.PAV.getURL());
		map.put(Prefix.RDF.getNS(), Prefix.RDF.getURL());
		map.put(Prefix.RDFS.getNS(), Prefix.RDFS.getURL());
		map.put(Prefix.DOCO.getNS(), Prefix.DOCO.getURL());
		map.put(Prefix.BIBO.getNS(), Prefix.BIBO.getURL());
		map.put(Prefix.DCTERMS.getNS(), Prefix.DCTERMS.getURL());
		map.put(Prefix.XSP.getNS(), Prefix.XSP.getURL());
		map.put(Prefix.CNT.getNS(), Prefix.CNT.getURL());
		map.put(Prefix.PROV.getNS(), Prefix.PROV.getURL());
		map.put(Prefix.OWL.getNS(), Prefix.OWL.getURL());
		map.put(Prefix.VOID.getNS(), Prefix.VOID.getURL());
		if (ConfigPrefix.getPrefix("UMLS").length() != 0) {
			map.put(ConfigPrefix.getNS("UMLS"), ConfigPrefix.getURL("UMLS"));
		}
		//map.put(Prefix.SCIENCE_COMMONS.getNS(), Prefix.SCIENCE_COMMONS.getURL());
		return map;
	}
	public static String prefixesAll() {
		return Prefix.prefixes_AO_DOCO_BIBO();// + //TODO
	}
	public static Map<String, String> prefixesMapAll() {	
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(Prefix.prefixesMap_AO_DOCO_BIBO());
		//TODO map.putAA()
		return map;
	}
	public static String prefixesBio2RDF() {
		return Prefix.AO_CORE.getPrefix() + 
			Prefix.prefixes_AO_DOCO_BIBO() + 
			ConfigPrefix.getPrefix("CHEBI") + ConfigPrefix.getPrefix("GO") +
			//Prefix.UNIPROT.getPrefix() + 
			ConfigPrefix.getPrefix("mesh") + 
			ConfigPrefix.getPrefix("OMIM") + ConfigPrefix.getPrefix("NCIt");
	}
}
