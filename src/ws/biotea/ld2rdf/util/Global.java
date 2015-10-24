package ws.biotea.ld2rdf.util;

public class Global {
	public final static String NO_STRING = "";
	public final static char SEPARATOR = '_';
	public final static int TOP_RECORDS = 20;
	public final static int TOP_RECORDS_DOC = 10;
	public final static int MAX_RECORDS = 100;
	public static final int DOCS_BY_PAGE = 10;
	
	public static final String XML_OUT = "_out.xml";
	public static final String HTML_OUT = "_out.html";
	public static final String XML_EXTENSION = "xml";
	
	public static final String ERROR_USER = "We are sorry but you are not allowed to do this operation on the selected element.<br/>"
			+ "You can not edit or delete someone else's annotations and you can not insert a new annotation on an element you have already annotated.";
	public static final String ERROR_OPTION = "We are sorry. Some parameters seem to be missing, please try again.";

	public static final String ERROR_AUTHORIZED = "User is not authorized, please confirm your credentials and try again";
	public static final String ERROR_LOGIN = "Session is expired or you have not "
			+ "logon yet. Remember you need yo be authorized in order to "
			+ "view, insert, delete and update data. Please <a href=\"index.html\">logon now</a>";
	
	public static final String ERROR_SaR = "Search and retrieval service is not available at this moment, please try later.";
	public static final String ERROR_NOT_DOCUMENT = "It seems you have not set an active document yet. Please go to <a href=\"../web/ld_sr_searchAndRetrieval.html\">Search and Retrieval</a> page";
	public static final String ERROR_TRY_AGAIN = "There has been an error, please try again";
	
	public static final String ADD = "Add";
	public static final String VIEW = "View";
	public static final String SAVE = "Save";
	public static final String DELETE = "Delete";
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final int GENERAL = 1;
	public static final int NOT_GENERAL = 0;
}
