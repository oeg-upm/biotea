package ws.biotea.ld2rdf.util.ncbo.annotator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.stanford.smi.protege.ui.ConfigureProtegePropertiesPanel;
import ws.biotea.ld2rdf.util.Config;
import ws.biotea.ld2rdf.util.ConfigPrefix;
import ws.biotea.ld2rdf.util.Prefix;

public class Ontology {
	private static final Ontology instance = new Ontology();
	//private Logger logger = Logger.getLogger(Ontology.class);
	private Map<String,NCBOOntology> map= new HashMap<String, NCBOOntology>();
	private Ontology() {
		InputStream is = null;
        try {
        	Properties propFile = new Properties();
            is = this.getClass().getResourceAsStream("/ontologies.properties");
            propFile.load(is);
            for (Object key: propFile.keySet()) {
            	String prop = key.toString();
            	if (ConfigPrefix.isNCBO(prop)) {
            		NCBOOntology onto = new NCBOOntology(ConfigPrefix.getVirtualId(prop), ConfigPrefix.getDescription(prop), ConfigPrefix.getNS(prop), ConfigPrefix.getURL(prop), ConfigPrefix.getAcronym(prop));
            		this.map.put(prop, onto);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
	}
	public static Ontology getInstance() {
		return instance;
	}
	public NCBOOntology getOntologyByURL(String url) {
		for (NCBOOntology o: map.values()) {			
			if (url.startsWith(o.getURL())) {
				return (o);
			}
		}
		return null;
	}
	
	public String getAllVirtualId(){
		return getAllVirtualOrAcronym(true);
	}
	
	public String getAllAcronym(){
		return getAllVirtualOrAcronym(false);
	}
	
	private String getAllVirtualOrAcronym(boolean virtual) {
		String str = "";
		String[] includeOnly = Config.getNCBOAnnotatorIncludeOnly();
		String[] exclude = Config.getNCBOAnnotatorExclude();
		if (includeOnly == null) {
			for (String key: map.keySet()) {
				if (exclude != null) {
					boolean excludeOnto = false;
					for (String no:exclude) {
						if (key.equals(no)) {
							excludeOnto = true;
							break;
						}
					}
					if (!excludeOnto) {
						if (virtual) {
							str += map.get(key).getVirtualId() + ",";
						} else {
							str += map.get(key).getAcronym() + ",";
						}
					}
				} else {
					if (virtual) {
						str += map.get(key).getVirtualId() + ",";
					} else {
						str += map.get(key).getAcronym() + ",";
					}
				}			
			}
		} else {
			for (String ns:includeOnly) {
				NCBOOntology onto = map.get(ns);
				if (onto != null) { //ontology indeed exists
					if (exclude != null) {
						boolean excludeOnto = false;
						for (String no:exclude) {
							if (ns.equals(no)) {
								excludeOnto = true;
								break;
							}
						}
						if (!excludeOnto) {
							if (virtual) {
								str += onto.getVirtualId() + ",";
							} else {
								str += onto.getAcronym() + ",";
							}
						}
					} else {
						if (virtual) {
							str += onto.getVirtualId() + ",";
						} else {
							str += onto.getAcronym() + ",";
						}
					}	
				}
			}
		}
		str = str.substring(0, str.length()-1);
		return str;
	}
	
	public static void main(String[] args) {//1224,1053,1516,1352 no 1070
		Ontology o = new Ontology();
		System.out.println(o.getAllAcronym());
	}
	
}
