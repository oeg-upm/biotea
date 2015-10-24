/**
 * 
 */
package ws.biotea.ld2rdf.rdfGeneration;

import org.apache.log4j.Logger;
import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.util.Conversion;

import java.io.File;
import java.util.List;

/**
 * @author Leyla Garcia
 *
 */
public abstract class RDFAbstractHandler implements RDFHandler {	
	protected Logger logger = Logger.getLogger(RDFAbstractHandler.class);
	protected String BASE_WII_AO_URL;
	protected String DATASET_URL;
	protected String BASE_PAPER_URL;
	protected String paperURLId;	
	protected String documentPaperId;
	
	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
	/**
	 * @return the paperURLId
	 */
	public String getPaperURLId() {
		return paperURLId;
	}
	/**
	 * @param paperURLId the paperURLId to set
	 */
	public void setPaperURLId(String paperURLId) {
		this.paperURLId = paperURLId;
	}

	/*Annotation methods*/
	/* (non-Javadoc)
	 * @see ws.biotea.ld2rdf.rdfGeneration.RDFHandler#annotateWithWhatizit(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public File annotateWithWhatizit(String pipeline, String rdfFileName, String outRDFFileName, List<Annotation> lsAnnotations, String textProperty, String creatorAgent)
			throws Exception {
		String[] params = {"Whatizit"};
		String baseURL = getAnnotationURI(params); //+ "PMC" + this.documentPaperId + "_" + pipeline + ".rdf/";
		RDFAOAnnotator annotator = new WhatizitAnnotator(DATASET_URL, baseURL, pipeline, this.paperURLId, this.documentPaperId, creatorAgent);
		File file = annotator.annotate(rdfFileName, outRDFFileName, lsAnnotations, textProperty);
		return file;
	}
	
	/* (non-Javadoc)
	 * @see ws.biotea.ld2rdf.rdfGeneration.RDFHandler#annotateWithWhatizit(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public File annotateWithNCBO(String rdfFileName, String outRDFFileName, List<Annotation> lsAnnotations, String textProperty, String creatorAgent) throws Exception {		
		String[] params = {"NCBO"};
		String baseURL = getAnnotationURI(params); // + "PMC" + this.documentPaperId + "_ncboAnnotator.rdf/";
		RDFAOAnnotator annotator = new NCBOAnnotatorClient(DATASET_URL, baseURL, this.paperURLId, this.documentPaperId, creatorAgent);
		File file = annotator.annotate(rdfFileName, outRDFFileName, lsAnnotations, textProperty);
		return file;
	}

	/**
	 * @return the documentPaperId
	 */
	public String getDocumentPaperId() {
		return documentPaperId;
	}
	/**
	 * @param documentPaperId the documentPaperId to set
	 */
	public void setDocumentPaperId(String documentPaperId) {
		this.documentPaperId = documentPaperId;
	}
	
	/**
	 * REturns the URI for annotations given an annotator.
	 * @param params
	 * @return
	 */
	private String getAnnotationURI(String[] params){
		return Conversion.replaceParameter(BASE_WII_AO_URL, params) + this.getDocumentPaperId() + "/";
	}

}
