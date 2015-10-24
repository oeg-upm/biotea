package ws.biotea.ld2rdf.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.zip.DataFormatException;

/**
 * Conversion: Utility class to converts null into strings, 
 * strings into calendars, etc.
 * @author leylajael@gmail.com
 */
public class Conversion {
	/**
	 * Converts  null into "0" string.
	 * @param str
	 * @return
	 */
	public static String nullToNumber (String str) {
		return ((str == null || str.length() == 0) ? "0" : str);
	}
	/**
	 * Converts a string into a long.
	 * @param str
	 * @return
	 */
	public static long strToLong (String str) {
		try {
			return (Long.parseLong(str));
		} catch (NullPointerException npe) {
			return (0);
		} catch (NumberFormatException nfe) {
			return (0);
		}
	}
	/**
	 * converts a string into an integer.
	 * @param str
	 * @return
	 */
	public static int strToInt (String str) {
		try {
			return (Integer.parseInt(str));
		} catch (NullPointerException npe) {
			return (0);
		} catch (NumberFormatException nfe) {
			return (0);
		}
	}
	/**
	 * Converts a string into a double.
	 * @param str
	 * @return
	 */
	public static double strToDouble (String str) {
		try {
			return (Double.parseDouble(str));
		} catch (NullPointerException npe) {
			return (0);
		} catch (NumberFormatException nfe) {
			return (0);
		}
	}
	/**
	 * Converts a null into a " " string.
	 * @param str
	 * @return
	 */
	public static String nullToString (String str) {
		return ((str == null || str.length() == 0 || str.equalsIgnoreCase("null")) ? Global.NO_STRING : str);
	}
	/**
	 * Converts null into string Yes - No.
	 * @param str
	 * @return
	 */
	public static String nullToYesNo (String str) {
		if (str == null) {
			return ("No");
		} else if (str.equalsIgnoreCase("Y") || str.equals("1") || str.equalsIgnoreCase("Yes")) {
			return ("Yes");
		} else {
			return ("No");
		}
	}
	/**
	 * Converts a date into a String dd/mm/yyyy.
	 * @param cal
	 * @return
	 */
	public static String calendarToString(Calendar cal) {
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH);// + 1;
		int year = cal.get(Calendar.YEAR);
		String strDay = day < 10 ? "0" + day : "" + day;
		String strMonth = month < 10 ? "0" + month : "" + month;
		return ("" + strDay + "/" + strMonth + "/" + year);
	}
	/**
	 * Converts a string xsd:dateTime (2010-10-24T12:48:42.361Z) into a calendar date.
	 * @param str
	 * @return
	 * @throws ParseException 
	 * @throws DataFormatException 
	 */
	public static Calendar xsdDateTimeToCalendar(String str) throws Exception {
		//2010-10-24T12:48:42.361Z
		Calendar cal = Calendar.getInstance();
		int year = Integer.parseInt(str.substring(0,4));
		int month = Integer.parseInt(str.substring(5,7));
		int day = Integer.parseInt(str.substring(8,10));
		int hourOfDay = Integer.parseInt(str.substring(11,13));
		int minute = Integer.parseInt(str.substring(14,16));
		int second = Integer.parseInt(str.substring(17,19));
		cal.set(year, month, day, hourOfDay, minute, second);
		return (cal);
	}
	/**
	 * Converts a string dd/mm/yyyy into a calendar date.
	 * @param str
	 * @return
	 * @throws ParseException 
	 * @throws DataFormatException 
	 */
	public static Calendar stringToCalendar(String str) throws Exception {
		//2010-10-24T12:48:42.361Z
		Calendar cal = Calendar.getInstance();
		String[] date = str.split("/");
		int year = Integer.parseInt(date[2]);
		int month = Integer.parseInt(date[1]) - 1;
		int day = Integer.parseInt(date[0]);
		cal.set(year, month, day);
		return (cal);
	}
	/**
	 * Deletes XML tags form a document abstract.
	 * @param docAbstract
	 * @return
	 */
	public static String abstractXMLToString(String docAbstract) {
		docAbstract = docAbstract.replace("<ce:simple-para xmlns:ce=\"http://www.elsevier.com/xml/common/dtd\">", " ");
		docAbstract = docAbstract.replace("</ce:simple-para>", " ");
		return (docAbstract);
	}
	/**
	 * Deletes XML tags from a document keywords. 
	 * @param keyWords
	 * @return
	 */
	public static String keyWordsXMLToString(String keyWords) {
		keyWords = keyWords.replace("<ce:keyword xmlns:ce=\"http://www.elsevier.com/xml/common/dtd\">", " ");
		keyWords = keyWords.replace("<ce:text>", " ");
		keyWords = keyWords.replace("</ce:text>", " ");
		keyWords = keyWords.replace("</ce:keyword>", " ");
		return (keyWords);
	}
	/**
	 * Deletes all XML tags from an XML string.
	 * @param xml
	 * @param namespace
	 * @return
	 */
	public static String XMLToString(String xml) {
		int posInit = xml.indexOf("<");
		for (; posInit != -1; ) {
			int posFin = xml.indexOf(">");
			
			xml = xml.substring(0, posInit) + " " + xml.substring(posFin + 1);
			posInit = xml.indexOf("<");
		}
		return (xml);
	}
	
	/**
	 * Returns a calendar representing the date in string format yyyy-mm-dd.
	 * @param str
	 * @return
	 */
	public static Calendar coverDateToCalendar(String str) {
		//2010-07-31
		Calendar cal = Calendar.getInstance();
		String[] date = str.split("-");
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		cal.set(year, month, day);
		return (cal);
	}
	
	/**
	 * Replaces {0}, {1}, etc for the elements in [pos0, pos1, etc].
	 * @param str
	 * @param params
	 * @return
	 */
	public static String replaceParameter(String str, String[] params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i]);
		}
		return str;
	}
}
