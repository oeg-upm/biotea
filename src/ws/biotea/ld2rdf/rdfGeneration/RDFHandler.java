package ws.biotea.ld2rdf.rdfGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.jdom.JDOMException;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.util.Config;

public interface RDFHandler {
	public static final String PUBMED_URL = Config.getPubMedURL();//"http://www.ncbi.nlm.nih.gov/pubmed/";
	public static final String DOI_URL = Config.getDOIURL();//"http://dx.doi.org/";			
	public static final String CHAR_NOT_ALLOWED = "[^A-Za-z0-9]";
	
	/**
	 * Creates an RDF file from an XML file.
	 * @param xml XML file
	 * @param outRDFFileName Out file absolute name.
	 * @return RDF file name
	 * @throws JAXBException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public File createRDFFromXML(File xml, String outRDFFileName, boolean sections) throws FileNotFoundException, UnsupportedEncodingException, JAXBException;
	/**
	 * Creates an RDF file from an XML given as string.
	 * @param xml XML string
	 * @param outRDFFileName Out file absolute name.
	 * @return RDF file name
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public File createRDFFromXMLString(String xml, String outRDFFileName, boolean sections) throws JAXBException, JDOMException, IOException;
	/**
	 * Creates an RDF file from an HTML file.
	 * @param html HTML file
	 * @param outRDFFileName Out file absolute name.
	 * @return RDF file name
	 */
	public File createRDFFromHTML(File html, String outRDFFileName, boolean sections);
	/**
	 * Creates an RDF file from an HTML given as string.
	 * @param html HTML string
	 * @param outRDFFileName Out file absolute name.
	 * @return RDF file name
	 */
	public File createRDFFromHTMLString(String html, String outRDFFileName);
	
	/**
	 * Annotates and RDF file with a Whatizit pipeline, 
	 * creates an RDF with annotations using the Annotation Ontology.
	 * @param pipeline Whatizit pipeline name
	 * @param rdf RDF file
	 * @param outRDFFileName Out file absolute name.
	 * @param textProperty RDF property whose object contains the text to be annotated. 
	 * @return Created RDF file name
	 */
	public File annotateWithWhatizit(String pipeline, String rdfFileName, String outRDFFileName, List<Annotation> lsAnnotations, String textProperty, String creatorAgent) throws Exception;
	
	/**
	 * Annotates and RDF file with a the NCBO annotator, 
	 * creates an RDF with annotations using the Annotation Ontology.
	 * @param rdf RDF file
	 * @param outRDFFileName Out file absolute name.
	 * @param textProperty RDF property whose object contains the text to be annotated.
	 * @return Created RDF file name
	 */
	public File annotateWithNCBO(String rdfFileName, String outRDFFileName, List<Annotation> lsAnnotations, String textProperty, String creatorAgent) throws Exception;
}