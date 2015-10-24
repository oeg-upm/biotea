package ws.biotea.ld2rdf.util;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class Config {
	private static ResourceBundle res = ResourceBundle.getBundle("config");
	private static Logger logger = Logger.getLogger(Config.class);
    public static final String SCI_VERSE = "sciverse";
    public static final String bio2rdf = "Bio2RDF";
    public static boolean useBio2RDF = true;
    /**
     * Used only when files are directly uploaded to Virtuoso (not recommended)
     */
    public static final String PUBMED_OA = "pubmedOpenAccess";
    /**
     * Biotea base URI
     */
    public final static String BIOTEA_URL = "http://" + Config.getBioteaBase() + "/";
    /**
     * URI for other datasets linked to
     */
    public static final String IDENTIFIERS_ORG_PUBMED = "http://identifiers.org/pubmed/";
    public static final String BIO2RDF_PUBMED = "http://bio2rdf.org/pubmed:";
    /**
     * RDF type property
     */    
    public static final String OP_RDF_TYPE = Prefix.RDF.getURL() + "type";

    public static String getProperty(String prop) {
        try {
            return res.getString(prop);
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return ("");
        }
    }
    
    //GO    
    public static boolean getWiiGO() {
    	try {
    		if (res.getString("whatizit.GO").equals("true")) {
    			return true;
    		}
    		return false;
    	} catch (Exception e) {
    		return false;
    	}
    }
    //CHEBI
    public static boolean getWiiCHEBI() {
    	try {
    		if (res.getString("whatizit.CHEBI").equals("true")) {
    			return true;
    		}
    		return false;
    	} catch (Exception e) {
    		return false;
    	}
    }
    //Virtual ids
    public static String[] getNCBOAnnotatorIncludeOnly() {    	
    	try {
    		return res.getString("ncbo.annotator.include.only").split(",");    		
    	} catch (Exception e) {
    		return null;
    	}
    }
    public static String[] getNCBOAnnotatorExclude() {    	
    	try {
    		return res.getString("ncbo.annotator.exclude").split(",");    		
    	} catch (Exception e) {
    		return null;
    	}
    }
    //BASE
    public static String getBioteaBase() {
        try {
            return res.getString("biotea.base");
        } catch (Exception e) {
            return ("biotea.ws");
        }
    }

    //RDF
    public static String getRDFDir(String opt) {
        return (res.getString("ld.rdf.dir." + opt));
    }
    public static String getRDFURL(String opt) {
        return (res.getString("ld.rdf.url." + opt));
    }
    public static String getRDFGraph(String opt) {
        return (res.getString("ld.rdf.graph." + opt));
    }
    //RDF_AO
    public static String getRDFAODir(String opt) {
        return (res.getString("ld.rdf_ao.dir." + opt));
    }
    public static String getRDFAOURL(String opt) {
        return (res.getString("ld.rdf_ao.url." + opt));
    }
    public static String getRDFAOGraph(String opt) {
        return (res.getString("ld.rdf_ao.graph." + opt));
    }
    //Bio2RDF
    public static String getBio2RDFDir(String opt) {
        return (res.getString("ld.bio2rdf.dir." + opt));
    }
    public static String getBio2RDFURL(String opt) {
        return (res.getString("ld.bio2rdf.url." + opt));
    }
    public static String getBio2RDFGraph(String opt) {
        return (res.getString("ld.bio2rdf.graph." + opt));
    }
    //Virtuoso
    public static String getVirtuosoSPARQL() {
        return (res.getString("virtuoso.sparql"));
    }
    public static String getVirtuosoURL() {
        return (res.getString("virtuoso.url"));
    }
    public static String getVirtuosoUser() {
        return (res.getString("virtuoso.user"));
    }
    public static String getVirtuosoPasswd() {
        return (res.getString("virtuoso.passwd"));
    }
    //WEB-INF
    public static String getWebInfDir() {
        return (res.getString("ld.dir.webinf"));
    }
    public static String getWSDLLocation() {
        return (res.getString("whatizit.wsdl.dir"));
    }
    //NCBO Annotator
    public static String getNCBOServiceURL(){
    	return (Config.getProperty("ncbo.service.url"));
    }
    
    public static String getNCBOAnnotatorURL(){
    	return (Config.getProperty("ncbo.annotator.url"));
    }
    
    public static String getNCBOAPIKey(){
    	return (Config.getProperty("ncbo.apikey"));
    }
    
    public static String getNCBOStopwords(){
    	return (Config.getProperty("ncbo.stopwords"));
    }
    //Whatizit annotator
    public static String getWhatizitAnnotatorURL(){
    	return (Config.getProperty("whatizit.annotator.url"));
    }
    
    public static String getWhatizitStopwords(){
    	return (Config.getProperty("whatizit.stopwords"));
    }
    //Other URLS
    public static String getPubMedURL(){
    	return (Config.getProperty("pubmed.url"));
    }
    
    public static String getDOIURL(){
    	return (Config.getProperty("doi.url"));
    }
    
    //Dataset
    public static String getDataset(){
    	return (Config.getProperty("biotea.dataset"));
    }
    //Bio or not
    public static boolean withBio() {
    	String str = Config.getProperty("biotea.withBio");
    	if (str.length() == 0) {
    		return false;
    	} else {
    		try {
    			return Boolean.getBoolean(str);
    		} catch (Exception e) {
    			return false;
    		}
    	}
    }
    public static boolean keepStartEnd() {
    	String str = Config.getProperty("keep.start.end");
    	if (str.length() == 0) {
    		return false;
    	} else {
    		try {
    			return Boolean.getBoolean(str);
    		} catch (Exception e) {
    			return false;
    		}
    	}
    }
}