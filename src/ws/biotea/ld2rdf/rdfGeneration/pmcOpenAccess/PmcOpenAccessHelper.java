package ws.biotea.ld2rdf.rdfGeneration.pmcOpenAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.rdfGeneration.RDFBasicHandler;
import ws.biotea.ld2rdf.rdfGeneration.Exception.ArticleTypeException;
import ws.biotea.ld2rdf.rdfGeneration.Exception.DTDException;
import ws.biotea.ld2rdf.rdfGeneration.Exception.PMCIdException;
import ws.biotea.ld2rdf.util.ClassesAndProperties;

public class PmcOpenAccessHelper {
	private RDFBasicHandler handler;
	private String pmcPrefix = "PMC";
	private String pmcSuffix = "_sections";
	Logger logger = Logger.getLogger(PmcOpenAccessHelper.class);
	public static final String TEXT_PROPERTY = ClassesAndProperties.CNT_PROP_CONTENT.getURLValue();
	
	public PmcOpenAccessHelper() {
		this.handler = new RDFBasicHandler(GlobalPmc.BIOTEA_PMC_DATASET, GlobalPmc.BASE_URL, GlobalPmc.BASE_URL_AO);
	}
	/**
	 * RDFizes a file
	 * @param subdir File to rdfize
	 * @param outputDir Output dir for generated RDF
	 * @param sections Should sections be processed?
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws PMCIdException 
	 * @throws ArticleTypeException 
	 * @throws DTDException 
	 */
	public File rdfizeFile(File subdir, String outputDir, boolean sections) throws JAXBException, FileNotFoundException, UnsupportedEncodingException, SAXException, ParserConfigurationException, DTDException, ArticleTypeException, PMCIdException {
		logger.info("===RDFize " + subdir.getName() + "===");
		File outRDF = null; 
		//1. Create RDF used as a mechanism for improving information retrieval over tagged resources as well as to facilitate the discovery of shared conceptualizations[2,3].
		this.handler.setStrPmcId(new StringBuilder());
		this.handler.setPaper2rdf(new PmcOpenAccess2RDF(subdir, this.handler.getStrPmcId()));
		String pmc = this.handler.getStrPmcId().toString();
		this.handler.setPaperURLId(GlobalPmc.getPmcRdfUri(pmc));
		this.handler.setDocumentPaperId(pmc);
		outRDF = new File (outputDir + "/PMC" + pmc + ".rdf");
		if (!outRDF.exists()) {
			outRDF = this.handler.createRDFFromXML(subdir, outputDir, sections);
		}	
		return outRDF;
	}
	
	/**
	 * Annotates a file with whatizit
	 * @param outRDF File to be annotated
	 * @param outputDir Output dor for the annotated RDF (output located at /AO_annotations subfolder)
	 * @param pmc Article (file) id
	 * @param pipeline Whatizit pipeline
	 * @throws Exception
	 */
	public void annotateFileWithWhatizit(File outRDF, String outputDir, String pipeline) throws Exception {
		logger.info("===Whatizit " + outRDF.getName() + "===");
		//3. Annotate with whatizit				
		String rdfFileName = outRDF.getAbsolutePath();
		String pmc;
		if (outRDF.getName().startsWith(this.pmcPrefix)) {
			pmc = outRDF.getName().substring(this.pmcPrefix.length(), outRDF.getName().length()-4);
		} else {
			pmc = outRDF.getName().substring(0, outRDF.getName().length()-4);
		}
		if (pmc.endsWith(this.pmcSuffix)) {
			pmc = pmc.substring(0, pmc.length()-this.pmcSuffix.length());
		}
		String outRDFFileName = outputDir + "/AO_annotations/PMC" + pmc + "_" + pipeline + ".rdf";
		List<Annotation> lsAnnotations = new ArrayList<Annotation>();
		this.handler.setPaperURLId(GlobalPmc.getPmcRdfUri(pmc));
		this.handler.setDocumentPaperId(pmc);
		File outRDF_AO = new File (outRDFFileName);
		if (!outRDF_AO.exists()) {
			outRDF_AO = this.handler.annotateWithWhatizit(pipeline, rdfFileName, outRDFFileName, lsAnnotations, TEXT_PROPERTY, GlobalPmc.RDF4PMC_AGENT);
		}
	}
	
	/**
	 * Annotates a file with NCBOAnnotator.
	 * @param outRDF File to be annotated
	 * @param outputDir Output dir for the annotated RDF (output located at /AO_annotations subfolder)
	 * @param pmc Article (file) id
	 * @throws Exception
	 */
	public void annotateFileWithNCBOAnnotator(File outRDF, String outputDir) throws Exception {
		logger.info("===NCBOAnnotator " + outRDF.getName() + "===");
		//5. annotate with ncboAnnotator
		String rdfFileName = outRDF.getAbsolutePath();
		String pmc;
		if (outRDF.getName().startsWith(pmcPrefix)) {
			pmc = outRDF.getName().substring(pmcPrefix.length(), outRDF.getName().length()-4);
		} else {
			pmc = outRDF.getName().substring(0, outRDF.getName().length()-4);
		}
		if (pmc.endsWith(this.pmcSuffix)) {
			pmc = pmc.substring(0, pmc.length()-this.pmcSuffix.length());
		}
		String outRDFFileName = outputDir + "/AO_annotations/PMC" + pmc + "_ncboAnnotator" + ".rdf";
		List<Annotation> lsAnnotations = new ArrayList<Annotation>();
		this.handler.setPaperURLId(GlobalPmc.getPmcRdfUri(pmc));
		this.handler.setDocumentPaperId(pmc);
		File outRDF_AO = new File (outRDFFileName);
		if (!outRDF_AO.exists()) {
			outRDF_AO = this.handler.annotateWithNCBO(rdfFileName, outRDFFileName, lsAnnotations, TEXT_PROPERTY, GlobalPmc.RDF4PMC_AGENT);
		}
	}
	
	/**
	 * Process an XML file, converts file to RDF, annotates it with both NCBOAnnotator and Whatizit, and generates the bio2rdf consolidated view.
	 * @param subdir File to process
	 * @param outputDir Output dir
	 * @param sections Process sections?
	 * @throws PMCIdException 
	 * @throws ArticleTypeException 
	 * @throws DTDException 
	 */
	public void processFileAll(String pipeline, File subdir, String outputDir, boolean sections) throws DTDException, ArticleTypeException, PMCIdException {
		File outRDF;
		try {
			outRDF = rdfizeFile(subdir, outputDir, sections);
			try {
				annotateFileWithWhatizit(outRDF, outputDir, pipeline);
			} catch (Exception e) {
				logger.error("***Whatizit " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
				e.printStackTrace();
			}
			try {
				annotateFileWithNCBOAnnotator(outRDF, outputDir);
			} catch (Exception e) {
				logger.error("***NCBOAnnotator " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (JAXBException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (SAXException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		}							
	}
	/**
	 * Process an XML file, converts file to RDF, and annotates it with both NCBOAnnotator and Whatizit.
	 * @param subdir File to process
	 * @param outputDir Output dir
	 * @param sections Process sections?
	 * @throws PMCIdException 
	 * @throws ArticleTypeException 
	 * @throws DTDException 
	 * @throws Exception 
	 */
	public void processFileRDFAndAnnotate(String pipeline, File subdir, String outputDir, boolean sections) throws DTDException, ArticleTypeException, PMCIdException {
		File outRDF, outRDFSections;
		try {
			outRDF = rdfizeFile(subdir, outputDir, sections);
			if (!outRDF.getName().endsWith("_sections.rdf")){
				String fileOutRDF = outRDF.getAbsolutePath();
				fileOutRDF = fileOutRDF.replace(".rdf", "_sections.rdf");
				outRDFSections = new File(fileOutRDF);
			} else {
				outRDFSections = outRDF;
			}
			try {				
				annotateFileWithWhatizit(outRDFSections, outputDir, pipeline);
			} catch (Exception e) {
				logger.error("***Whatizit " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
				e.printStackTrace();
			}
			try {
				annotateFileWithNCBOAnnotator(outRDFSections, outputDir);
			} catch (Exception e) {
				logger.error("***NCBOAnnotator " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (JAXBException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (SAXException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			logger.error("***RDFize " + subdir.getName() + " has thrown an error: " + e.getMessage() + "***");
			e.printStackTrace();
		} 							
	}
}
