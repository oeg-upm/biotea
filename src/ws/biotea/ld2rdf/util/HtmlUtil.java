package ws.biotea.ld2rdf.util;  

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * HTMLUtil: This utility class manages selected and checked 
 * properties over html elements.
 * @author leylajael@gmail.com
 */
public class HtmlUtil {
	/**
	 * Returns "selected" if str1 == str2, "" otherwise.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public String selected(String str1, String str2) {
		return (str1.equalsIgnoreCase(str2) ? "selected" : "");
	}
	/**
	 * Returns "checked" if str1 == str2, "" otherwise.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public String checked(String str1, String str2) {
		return (str1.equalsIgnoreCase(str2) ? "checked" : "");
	}
	/**
	 * Returns "checked" if str1 == "value", "" otherwise.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public String checked(String str1, int value) {
		return (str1.equalsIgnoreCase("" + value) ? "checked" : "");
	}
	
	/**
	 * 
	 * @param html
	 * @return
	 */
	public static String htmlToXML(String html) {
		String str = "";
		int posInit = html.indexOf('<');
        while (posInit != -1) {
              //Agregar lo que hay antes del tag
              str += html.substring(0, posInit);
              //Encontrar el tag de apertura y dejar el html con lo que sigue
              int posFin = html.indexOf('>');
              String tag = html.substring(posInit, posFin + 1).toUpperCase();
              html = html.substring(posFin + 1); //html sin el tag de apertura
              if (tag.startsWith("<SPAN")) { //SPAN
                    //TODO: ce:sans-serif, ce:small-caps
                    if (tag.indexOf("ITALIC") != -1) { //ITALIC
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag);
                         html = html.substring(finTag + 7); //quita texto y cierre del span
                         str += "<ce:italic>" + text + "</ce:italic>";
                    } else if (tag.indexOf("BOLD") != -1) { //BOLD
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag);
                         html = html.substring(finTag + 7); //quita texto y cierre del span
                         str += "<ce:bold>" + text + "</ce:bold>";
                    } else if (tag.indexOf("text-decoration:underline") != -1) { //UNDERLINE
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag);
                         html = html.substring(finTag + 7); //quita texto y cierre del span
                         str += "<ce:underline>" + text + "</ce:underline>";
                    } else if (tag.indexOf("VERTICAL-ALIGN:SUB") != -1) { //INF
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag);
                         html = html.substring(finTag + 7); //quita texto y cierre del span
                         str += "<ce:inf>" + text + "</ce:inf>";
                    } else if (tag.indexOf("VERTICAL-ALIGN:SUPER") != -1) { //SUP
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag);
                         html = html.substring(finTag + 7); //quita texto y cierre del span
                         str += "<ce:sup>" + text + "</ce:sup>";
                    } else if (tag.indexOf("LD_TAGGEDTEXT") != -1) { //tag automático o manual
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag);
                         html = html.substring(finTag + 7); //quita texto y cierre del span
                         String id = tag.substring(tag.indexOf("id=") + 3, tag.indexOf(' ')).replace("\"", "");
                         if (id.equalsIgnoreCase("UNIPROT")) {
                        	 
                         } else if (id.equalsIgnoreCase("SPECIES")) {

                         } else if (id.equalsIgnoreCase("AMIGO")) {                               

                         } else {
                               str += "<ld:tag id=\"" + id + "\">" + text + "</ld:tag>";
                         }
                    }
              } /*else if (tag.startsWith("<TABLE id=intra")) {//Referencias múltiples
                    int finTable = html.indexOf("</TABLE>");//Table interno
                    html = html.substring(finTable + 8); //quitar
                    finTable = html.indexOf("</TABLE>");//Table externo
                    html = html.substring(finTable + 8); //quitar
                    posInit = html.indexOf("<SPAN"); //span de referencia
                    posFin = html.indexOf('>'); //fin span de referencia
                    tag = html.substring(posInit, posFin + 1).toUpperCase(); //tag de referencia
                    if (tag.indexOf("show('intra") != -1) { //tag de referencia
                         html = html.substring(posFin + 1); //quita apertura del span
                         int finTag = html.indexOf("</SPAN>"); //fin del span
                         String text = html.substring(0, finTag).replace('?', '-'); //texto entre
                         html = html.substring(finTag + 6); //quita texto y cierr del span
                         int num1 = 0;
                         int num2 = 0;
                         try {
                               int posGuion = text.indexOf('-');
                               num1 = Integer.parseInt(text.substring(1, posGuion));
                               num2 = Integer.parseInt(text.substring(posGuion, text.length()-1));
                         } catch (Exception e) {
                         }
                         str += "<ce:cross-refs refid=\""; //agrega tag XML
                         for (int i = num1; i < num2; i++) {
                               str += "BIB" + i + " ";
                         }
                         str += "BIB" + num2 + "\">" + text + "</ce:cross-refs>";
                    }
                    //TODO: <ce:cross-refs refid="BIB9 BIB10 BIB16">[9,10,16]</ce:cross-refs>
                    //TODO: <ce:cross-refs refid="FIG4 FIG5">Figs. 4 and 5</ce:cross-refs> <ce:float-anchor refid="FIG4" /><ce:float-anchor refid="FIG5" />
              } else if (tag.startsWith("<A ")) {
                    if (tag.indexOf("#BIB") != -1) { //referencia sencilla
                         int finTag = html.indexOf("</A>"); //fin del a href
                         String text = html.substring(0, finTag); //texto entre
                         html = html.substring(finTag + 4); //quita texto y cierre del a
                         String bib = text.substring(text.indexOf('[') + 1, text.indexOf(']'));
                         str += "<ce:cross-ref refid=\"BIB" + bib + "\">" + text + "</ce:cross-ref>";
                    } 
                    //TODO: <ce:cross-ref refid="TBL1">Table 1</ce:cross-ref><ce:float-anchor refid="TBL1" />
                    //TODO: <ce:cross-ref refid="FIG1">Fig. 1</ce:cross-ref><ce:float-anchor refid="FIG1" />
              }*/
              //TODO:article-footnote, ce:footnote, ce:table-footnote, ce:cross-out, create-ref-ends, ce:hsp, ce:inter-refs, ce:monospace
              posInit = html.indexOf('<');
        }
        //Agregar lo que queda
        return (str + html);
	}
	
	public static String getDateAndTime() {
		Calendar now = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    String nowStr = sdf.format(now.getTime());
	    return (nowStr);
	} 
	public static void main(String[] args) {
		//System.out.println(htmlToXML("13-<SPAN style=\"FONT-STYLE: italic\">cis</SPAN> state"));
		try {
			JSONArray array = new JSONArray("[a n,'b','c']");
			System.out.println(array.length() + array.getString(0));
		} catch (JSONException e) {
			// TODO Auto-jaxb.generated catch block
			e.printStackTrace();
		}
		
	}
}
