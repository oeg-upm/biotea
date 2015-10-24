package ws.biotea.ld2rdf.util;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ConfigPrefix {
	private static Logger logger = Logger.getLogger(ConfigPrefix.class);
	private static ResourceBundle res = ResourceBundle.getBundle("ontologies");
	/**
	 * True if the property is NCBO (ncbo,virtual-id,description,ns,url,ncbo-name)
	 * @param prop
	 * @return
	 */
	public static boolean isNCBO(String prop) {
		try {
            if ( res.getString(prop).split(",")[0].equals("ncbo") ) {
            	return true;
            } else {
            	return false;
            }            
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return false;
        }
	}
	/**
	 * Returns the virtual id (ncbo,virtual-id,description,ns,url,ncbo-name)
	 * @param prop
	 * @return
	 */
	public static String getVirtualId(String prop) {
		try {
            return res.getString(prop).split(",")[1];
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return ("");
        }
	}
	/**
	 * Returns the description (ncbo,virtual-id,description,ns,url,ncbo-name)
	 * @param prop
	 * @return
	 */
	public static String getDescription(String prop) {
		try {
            return res.getString(prop).split(",")[2];
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return ("");
        }
	}
	/**
	 * Returns the namespace (ncbo,virtual-id,description,ns,url,ncbo-name)
	 * @param prop
	 * @return
	 */
	public static String getNS(String prop) {
		try {
            return res.getString(prop).split(",")[3];
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return ("");
        }
	}
	/**
	 * Returns the URL (ncbo,virtual-id,description,ns,url,ncbo-name)
	 * @param prop
	 * @return
	 */
	public static String getURL(String prop) {
		try {
            return res.getString(prop).split(",")[4];
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return ("");
        }
	}
	/**
	 * Returns the acronym (ncbo,virtual-id,description,ns,url,ncbo-name)
	 * @param prop
	 * @return
	 */
	public static String getAcronym(String prop) {
		try {
            return res.getString(prop).split(",")[5];
        } catch (Exception e) {
        	logger.warn("---WARNING configuration---: " + e.getMessage());
            return ("");
        }
	}
	public static String getPrefix(String prop) {
		String ns = ConfigPrefix.getNS(prop);
		String url = ConfigPrefix.getURL(prop);
		if ((ns.length() != 0) && (url.length() != 0)) {
			return "PREFIX " + ns + ":<" + url + "> ";
		} else {
			return "";
		}		
	}
}
