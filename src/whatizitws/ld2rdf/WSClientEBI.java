/**
 * 
 */
package whatizitws.ld2rdf;


import whatizitws.client.Whatizit;
import whatizitws.client.Whatizit_Service;

/**
 * @author leylajael
 *
 */
public class WSClientEBI {
	public static String whatizit(String inStr, String pipe) throws Exception {
		try {
			// Get the WHATIZIT service end point (always like this) 
		    Whatizit_Service service = new Whatizit_Service();    
		    //System.out.println("service created");
		    Whatizit pipeline = service.getPipeline();
		    //System.out.println("pipeline created");
		    boolean convertToHtml = false; // vs. leave it as plain XML
		    String out = pipeline.contact(pipe, inStr, convertToHtml);
		    //System.out.println("out retrieved");
		    return out;
		    //URLManager.createFile(out, outStr);
		} catch (Exception e) {
			throw e;
		}
	}
	private static String removeWhatizitXML(String str) {
		str = str.replaceAll("<document xmlns:xlink='http://www.w3.org/1999/xlink'  xmlns:z='http://www.ebi.ac.uk/z'  source='Whatizit'>", "");
		str = str.replaceAll("<document xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:z=\"http://www.ebi.ac.uk/z\" source=\"Whatizit\">", "");
		str = str.replaceAll("</document>", "");
		str = str.replaceAll("<plain>", "");
		str = str.replaceAll("</plain>", "");
		str = str.replaceAll("<text>", "");
		str = str.replaceAll("</text>", "");
		str = str.replaceAll("<SENT>", "");
		str = str.replaceAll("</SENT>", "");
		str = str.replaceAll("&lt; ", "<"); //&lt;ce:para id="1"&gt;
		str = str.replaceAll(" &gt;", ">");
		str = str.replaceAll("&lt;", "<"); //&lt;ce:para id="1"&gt;
		str = str.replaceAll("&gt;", ">");
		//<SENT sid="1" pm="."> <SENT sid="1" pm="?">
		int pos;
		pos = str.indexOf("<SENT");
		String aux = "";
		while (pos != -1) {
			aux = aux + str.substring(0, pos);
			str = str.substring(pos);
			pos = str.indexOf('>');
			str = str.substring(pos + 1);
			pos = str.indexOf("<SENT");
		}
		aux = aux + str;
		//<ce:cross-ref refid="<z:uniprot fb="0" ids="P38224,Q96RQ9">FIG1</z:uniprot>">Fig. 1</ce:cross-ref> 
		//id="<z:
		String out = "";
		pos = aux.indexOf("id=\"<z:");
		while (pos != -1) {
			out = out + aux.substring(0, pos); //<ce:cross-ref ref
			aux = aux.substring(pos); //id="<z:uniprot fb="0" ids="P38224,Q96RQ9">FIG1</z:uniprot>">Fig. 1</ce:cross-ref>
			pos = aux.indexOf('>');
			aux = "id=\"" + aux.substring(pos + 1); //id="FIG1</z:uniprot>">Fig. 1</ce:cross-ref>
			pos = aux.indexOf('<');
			out = out + aux.substring(0, pos); //id="FIG1
			aux = aux.substring(pos + 1); //</z:uniprot>">Fig. 1</ce:cross-ref>
			pos = aux.indexOf('>');
			aux = aux.substring(pos + 1); //">Fig. 1</ce:cross-ref>
			pos = aux.indexOf("id=\"<z:");
		}
		out = out + aux;
		return (out);
	}
	/**
	 * @param args
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) {//http://sideeffects.embl.de/se/  http://www.healthcentral.com/ency/408/		
		String in = "Future studies should among other things compare modern conventional ED management supplemented by risk prediction "+"algorithms and/or selected investigations such as exercise stress testing [[B27]] and myocardial perfusion imaging [[B28]], with the CPU concept strategy. "
			+"It may well be that improved conventional care will decrease or even eliminate the benefits of "
			+"establishing dedicated chest pain units." 
//			+ "Patients with symptoms of acute myocardial infarction (AMI) or unstable angina pectoris (UA),"
//			+ " i.e. "
//			+ "an acute coronary syndrome "
//			+ "(ACS), "
//			+ "are very common in emergency departments EDs. Because of (diagnostic) " 
//			+ "difficulties, a certain \"overadmission\" to in-hospital care for suspected ACS is usually accepted. " 
//			+ "The size of this overadmission is generally unknown, despite its negative influence on hospital " 
//			+ "efficiency and resource utilization. In order to reduce unnecessary admission, to optimize " 
//			+ "disposition of admitted patients and to decrease cost, chest pain units (CPUs) with risk-based " 
//			+ "diagnostic protocols have been established in many hospitals, primarily in the United States " 
//			+ "[[B1],[B2]]. These CPUs have been shown to increase the efficiency of patient evaluation with a " 
//			+ "quality similar to that of traditional EDs [[B3],[B4]]."
			;		
		in = in.replaceAll("[(]", "( ");
		String pipeline = "whatizitUkPmcAll";			
		try {
			System.out.println("INIT");
			String out = WSClientEBI.whatizit(in, pipeline);
			System.out.println("OUT:" + out);
			//URLManager.transform(Config.getElsevierXSLT(), xmlNameOut, "E:/workspace/LDProject/temp/" + base + "_" + pipeline + "_out.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
