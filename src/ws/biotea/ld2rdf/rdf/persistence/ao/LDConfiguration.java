/**
 * 
 */
package ws.biotea.ld2rdf.rdf.persistence.ao;

import java.util.ResourceBundle;

/**
 * @author leylajael
 *
 */
public class LDConfiguration {
	private static ResourceBundle res = ResourceBundle.getBundle("ws.biotea.ld2rdf.rdf.persistence.ao.config");
	public static final String LD_URI = res.getString("baseURI");
	public static final String LD_BASE = LD_URI + "#";
	public static final String LD_OWL_FILE = res.getString("owlFile");
	public static final String LD_NS = "biotea";
	public static final String LD_PAPERS_URI = res.getString("baseURIPaper");
	
	public static final int TOP_RECORDS_DOC = 10;
	public static final int TOP_RECORDS_ANNOT = 20;
	public static final int BODY = 1;
	public static final int CREATOR = 2;
}
