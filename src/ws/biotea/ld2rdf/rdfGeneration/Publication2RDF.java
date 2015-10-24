package ws.biotea.ld2rdf.rdfGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBException;

import ws.biotea.ld2rdf.util.Prefix;

public interface Publication2RDF {
	public final static String PUB_CONTENT_CLASS = Prefix.CNT.getURL() + "Content";
	public final static String PUB_CONTENT_AS_TEXT_CLASS = Prefix.CNT.getURL() + "ContentAsText";
	public final static String PUB_CONTENT_CHARS_PROP = Prefix.CNT.getURL() + "chars";
	public final static String PUB_CONTENT_CHARS_ENCODING_PROP = Prefix.CNT.getURL() + "characterEncoding";
	public final static String ISO_ENCODING = "ISO-8859-1";
	public final static String UTF_ENCODING = "UTF-8";
	/**
	 * Converts a file into RDF
	 * @param outputDir
	 * @param paper
	 * @param sections
	 * @throws JAXBException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public File paper2rdf(String outputDir, File paper, boolean sections) throws JAXBException, FileNotFoundException, UnsupportedEncodingException;	
}
