package ws.biotea.ld2rdf.rdfGeneration;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import whatizitws.ld2rdf.WSClientEBI;
import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.rdf.model.ao.ElementSelector;
import ws.biotea.ld2rdf.rdf.model.ao.ExactQualifier;
import ws.biotea.ld2rdf.rdf.model.ao.FoafAgent;
import ws.biotea.ld2rdf.rdf.model.ao.FoafDocument;
import ws.biotea.ld2rdf.rdf.model.ao.Topic;
import ws.biotea.ld2rdf.rdf.persistence.ao.AnnotationOWLDAO;
import ws.biotea.ld2rdf.rdfGeneration.Exception.DTDException;
import ws.biotea.ld2rdf.util.ConfigPrefix;
import ws.biotea.ld2rdf.util.Prefix;
import ws.biotea.ld2rdf.util.annotation.WhatizitType;
import ws.biotea.ld2rdf.util.Config;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class WhatizitAnnotator extends RDFAOAnnotator {
	private static Logger logger = Logger.getLogger(WhatizitAnnotator.class);
	public static final String BASE_FOAF_WHATIZIT = Config.getWhatizitAnnotatorURL();//"http://www.ebi.ac.uk/webservices/whatizit#";
	private String BASE_URL;
	private String datasetURL;
	//http://www.healthcentral.com/ency/408/
	private int totalUniProt = 0, totalSpecies = 0, totalGO = 0, totalChebi = 0, totalDisease = 0, 
		totalE = 0, totalDrug = 0, total = 0, error = 0, totalTerms = 0;
	private FoafAgent authorAgent;
	private FoafAgent creatorAgent;
	private String doiURL;
	private String documentId;
	private List<Annotation> annotationsTable;
	private String pipeline;
	private static final String stopWords = Config.getWhatizitStopwords();
	
	public WhatizitAnnotator(String datasetURL, String baseURL, String pipeline, String doiURL, String documentId, String creatorAgent) throws URISyntaxException {
		this.datasetURL = datasetURL;
		this.BASE_URL = baseURL;
		this.authorAgent = new FoafAgent();
		this.authorAgent.setId(new URI(WhatizitAnnotator.BASE_FOAF_WHATIZIT + pipeline));
		this.creatorAgent = new FoafAgent();
		this.creatorAgent.setId(new URI(creatorAgent));
		this.doiURL = doiURL;
		this.documentId = documentId;
		this.annotationsTable = new ArrayList<Annotation>();
		this.pipeline = pipeline;
		//System.out.println(this.BASE_URL + "--" + this.documentId + "--" + this.documentId);
	}

	/**
	 * Annotates an rdf file with whatizit and write annotations to an rdf with AO.
	 * @param rdfInName
	 * @param rdfOutName
	 * @return
	 * @throws Exception
	 */
	public File annotate(String rdfInName, String rdfOutName, List<Annotation> lstAnnotations, String textProperty) throws Exception {		
		logger.info("===annotateWithWhatizit=== " + rdfInName);
		// create an empty model
		Model model = ModelFactory.createDefaultModel();
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(rdfInName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + rdfInName + " not found");
		}
		// read the RDF/XML file
		model.read(in, null);
		logger.info("===RDF READ=== " + rdfInName);
		
		Property text = model.getProperty(textProperty);
		ResIterator resItr = model.listResourcesWithProperty(text);	
		while (resItr.hasNext()) {
			Resource res = resItr.next();
			Matcher matcher = WhatizitAnnotator.excludedSections.matcher(res.getURI().toString());
        	if (matcher.find()) {
        		continue; //excluded sections will not be annotated
        	}
        	annotateWithWhatizit(this.pipeline, res.getProperty(text).getObject().toString(), res.getURI().toString());
		}
		logger.debug("===ALL SECTIONS READ=== " + rdfInName);
		logger.info("===SECTIONS ANNOTATED=== " + rdfInName);
		printSummary();
		AnnotationOWLDAO dao = new AnnotationOWLDAO();
		lstAnnotations = dao.insertAnnotations(datasetURL, BASE_URL, annotationsTable, rdfOutName, true, false);
		error = annotationsTable.size() - lstAnnotations.size();
		if (error != 0) {
			logger.info("==ERROR writing annotations NCBO== " + error + " annotations were not created, check the logs starting by 'Annotation not inserted' for more information");			
		}
		return new File(rdfOutName);
	}
	/**
	 * Annotates a text with a whatizit pipeline.
	 * @param pipeline
	 * @param paragraph
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void annotateWithWhatizit(String pipeline, String paragraph, String context) throws Exception {
		//System.out.println("PIPELINE: " + pipeline);
		//System.out.println("PARAGRAPH: " + paragraph);
        String annotatedParagraph = WSClientEBI.whatizit(paragraph, pipeline);
        //System.out.println("ANNOTATED PARAGRAPH: " + annotatedParagraph);
        //System.out.println("FLAG: " + paragraph);
        if ((paragraph != null) && (paragraph.length() != 0)) {
        	SAXBuilder builder = new SAXBuilder();
            Reader reader = new StringReader(annotatedParagraph);
            Document doc = builder.build(reader);
            Element root = doc.getRootElement(); //document                
    		List sent = root.getChild("text").getChildren("SENT");
            for (Object objSent:sent) {
                Element element = ((Element)objSent).getChild("plain");
                List annotations = element.getChildren();
                for (Object annotation: annotations) {                	
                    Element ann = (Element)annotation;
                    Attribute ids = ann.getAttribute("ids");
                    String idsValue = ids.getValue();
                    String content = ((Text) ann.getContent().get(0)).getText(); 
                    if (stopWords.contains(content.toUpperCase())) {
                    	continue;
                    }
                    //System.out.println("FLAG: " + content);
                    //content = content.trim();
                    if (paragraph.indexOf("[" + content + "]") != -1) { //is a reference
                    	continue;                    	
                    } else if ( (content.charAt(0) == '[') && (content.charAt(content.length()-1) == ']')) {
                    	continue;
                    } else {
                    	try {
	                    	Pattern pattern = Pattern.compile("\\[(.*?)\\Q" + content + "\\E(.*?)\\]");
	                    	Matcher matcher = pattern.matcher(paragraph);
	                    	if (matcher.find()) {
	                    		continue;
	                    	}
                    	} catch (Exception e) {}
                    }
                    processAnnotation(ann, content, idsValue, context);
                }
            }
        }                
	}
	
	private void processAnnotation(Element ann, String content, String idsValue, String context) throws URISyntaxException {
		//process annotations according to its type
        ExactQualifier annot = null;
        totalTerms++;
        if ( (Config.getWiiCHEBI()) && 
    		(ann.getName().equals(WhatizitType.CHEBI.getType())) ) {
            //System.out.println("CHEBI: ids " + idsValue + " - " + content);     
        	annot = new ExactQualifier();                				
			annot.setAuthor(authorAgent);
			annot.setCreator(creatorAgent);
			String body = content;
			annot.setBody(body);
			Collection<Topic> topics = new ArrayList<Topic>();
			for (String uniTerm:idsValue.split("[ ,]")) {
				Topic topic = new Topic();
				topic.setName(body);
				topic.setNameSpace(new URI(ConfigPrefix.getNS("CHEBI") + ":" + uniTerm)); //chebi: http://purl.obolibrary.org/obo/CHEBI_
				topic.setURL(new URI(ConfigPrefix.getURL("CHEBI") + uniTerm));
				topics.add(topic);
				totalChebi++;
			}
			annot.setTopic(topics);
			FoafDocument document = new FoafDocument();
			document.setId(new URI(doiURL));
			annot.setResource(document);
			annot.setDocumentID(documentId);
			annot.setCreationDate(Calendar.getInstance());        			
        } else if (ann.getName().equals(WhatizitType.E.getType())) {//TODO
            Attribute attr = ann.getAttribute("sem");
            if ((attr != null) && (attr.getValue().equals("disease"))) {
                String type = ann.getAttribute("disease_type").getValue();
                //System.out.println("E-DISEASE: ids " + idsValue + " - sem: disease - type: " + type + " - " + content);
                annot = new ExactQualifier();				
				annot.setAuthor(authorAgent);
				annot.setCreator(creatorAgent);
				String body = content;
				annot.setBody(body);
				Collection<Topic> topics = new ArrayList<Topic>();
				for (String uniTerm:idsValue.split("[ ,]")) {
					if (uniTerm.startsWith("C")) {
						Topic topic = new Topic();
						topic.setName(body); //http://berkeleybop.org/obo/UMLS_TYPE:C0702154
						topic.setNameSpace(new URI(ConfigPrefix.getNS("UMLS_ID") + ":" + uniTerm)); //http://www.fpnotebook.com/asp3/cui.aspx?q=C0702154
						topic.setURL(new URI(ConfigPrefix.getURL("UMLS_ID") + uniTerm));
						//topic.getSeeAlso().add(new URI("http://www.fpnotebook.com/asp3/cui.aspx?q=" + uniTerm));
						topic.setComment("Disease type is: " + type);
						topics.add(topic);
						totalE++;
					}					
				}				
				annot.setTopic(topics);
				FoafDocument document = new FoafDocument();
				document.setId(new URI(doiURL));
				annot.setResource(document);
				annot.setDocumentID(documentId);
				annot.setCreationDate(Calendar.getInstance());
				//annot.setComment("Disease type is: " + type);
				if ((topics == null) || (topics.size() == 0)) {
					annot = null;
				}
            }
        } else if ( (Config.getWiiGO()) && 
        			(ann.getName().equals(WhatizitType.GO.getType())) ) {
        	String onto = ann.getAttribute("onto").getValue();
        	annot = new ExactQualifier();			
			annot.setAuthor(authorAgent);
			annot.setCreator(creatorAgent);
			String body = content;
			annot.setBody(body);
			Collection<Topic> topics = new ArrayList<Topic>();
			for (String goTerm:idsValue.split("[ ,]")) {
				Topic topic = new Topic();
				topic.setName(body);
				topic.setNameSpace(new URI(goTerm));
				try {
					String goTermId = goTerm.substring(goTerm.indexOf(':') + 1);
					topic.setURL(new URI(ConfigPrefix.getURL("GO") + goTermId));
					totalGO++;
				} catch (Exception e) { }            				 
				topics.add(topic);
			}
			annot.setTopic(topics);
			FoafDocument document = new FoafDocument();
			document.setId(new URI(doiURL));
			annot.setResource(document);
			annot.setDocumentID(documentId);
			annot.setCreationDate(Calendar.getInstance());
			annot.setComment("GO ontology: " + onto);
        } else if (ann.getName().equals(WhatizitType.UNIPROT.getType())) {
            //System.out.println("UNIPROT: ids " + idsValue + " - " + content);
            annot = new ExactQualifier();			
			annot.setAuthor(authorAgent);
			annot.setCreator(creatorAgent);
			String body = content;
			annot.setBody(body);
			Collection<Topic> topics = new ArrayList<Topic>();
			for (String uniTerm:idsValue.split("[ ,]")) {
				Topic topic = new Topic();
				topic.setName(body);
				topic.setNameSpace(new URI(ConfigPrefix.getNS("UNIPROT") + ":" + uniTerm));//uniprot: http://purl.uniprot.org/uniprot/
				topic.setURL(new URI(ConfigPrefix.getURL("UNIPROT") + uniTerm));
				topics.add(topic);
				totalUniProt++;
			}
			annot.setTopic(topics);
			FoafDocument document = new FoafDocument();
			document.setId(new URI(doiURL));
			annot.setResource(document);
			annot.setDocumentID(documentId);
			annot.setCreationDate(Calendar.getInstance());
        } else if (ann.getName().equals(WhatizitType.SPECIES.getType())) {
            //System.out.println("SPECIES: ids " + idsValue + " - " + content);
        	totalSpecies++;
            annot = new ExactQualifier();			
			annot.setAuthor(authorAgent);
			annot.setCreator(creatorAgent);
			String body = content;
			annot.setBody(body);
			Collection<Topic> topics = new ArrayList<Topic>();
			Topic topic = new Topic();
			topic.setName(body);
			topic.setNameSpace(new URI(ConfigPrefix.getNS("UNIPROT_TAXONOMY") + ":" + idsValue)); //species: http://purl.uniprot.org/taxonomy/
			topic.setURL(new URI(ConfigPrefix.getURL("UNIPROT_TAXONOMY") + idsValue));
			topics.add(topic);
			
			Topic topic2 = new Topic();
			topic2.setName(body);
			topic2.setNameSpace(new URI(ConfigPrefix.getNS("NCBITaxon") + ":" + idsValue)); //species: http://purl.uniprot.org/taxonomy/
			topic2.setURL(new URI(ConfigPrefix.getURL("NCBITaxon") + idsValue));
			topics.add(topic2);
			annot.setTopic(topics);
			
			FoafDocument document = new FoafDocument();
			document.setId(new URI(doiURL));
			annot.setResource(document);
			annot.setDocumentID(documentId);
			annot.setCreationDate(Calendar.getInstance());
        } else if (ann.getName().equals(WhatizitType.DISEASE.getType())) {
            //System.out.println("DISEASE: ids " + idsValue + " - " + content);
            annot = new ExactQualifier();
			totalDisease++;
			annot.setAuthor(authorAgent);
			annot.setCreator(creatorAgent);
			String body = content;
			annot.setBody(body);
			Collection<Topic> topics = new ArrayList<Topic>();
			Topic topic = new Topic();
			topic.setName(body);
			topic.setURL(new URI("http://www.healthcentral.com/ency/408/" + idsValue + ".html"));
			topics.add(topic);
			annot.setTopic(topics);
			FoafDocument document = new FoafDocument();
			document.setId(new URI(doiURL));
			annot.setResource(document);
			annot.setDocumentID(documentId);
			annot.setCreationDate(Calendar.getInstance());
        } else if (ann.getName().equals(WhatizitType.DRUG.getType())) {
            String cat = ann.getAttribute("cat").getValue();
            //System.out.println("DRUG: ids " + idsValue + " - cat: " + cat + " - " + content);
            annot = new ExactQualifier();			
			annot.setAuthor(authorAgent);
			annot.setCreator(creatorAgent);
			String body = content;
			annot.setBody(body);
			Collection<Topic> topics = new ArrayList<Topic>();
			for (String uniTerm:idsValue.split("[ ,]")) {
				Topic topic = new Topic();
				topic.setName(body);
				topic.setURL(new URI("http://redpoll.pharmacy.ualberta.ca/drugbank/cgi-bin/getCard.cgi?CARD=" + uniTerm));
				topics.add(topic);
				totalDrug++;
			}
			annot.setTopic(topics);
			FoafDocument document = new FoafDocument();
			document.setId(new URI(doiURL));
			annot.setResource(document);
			annot.setDocumentID(documentId);
			annot.setCreationDate(Calendar.getInstance());
			annot.setComment("Drug category: " + cat);
        }
        if (annot != null) {
        	//System.out.println("ANNOT: " + annot);
        	int pos = annotationsTable.indexOf(annot);
        	if (pos != -1) {
        		Annotation a = annotationsTable.get(pos);
        		a.setFrequency(a.getFrequency() + 1);
            	if (context != null) {
            		//context (selector)
        			ElementSelector ses = new ElementSelector(a.getDocument());
        			ses.setElementURI(context);
        			ses.setSelector(a.getBody());
        			a.addContext(ses); 		
            	}
        		if (a.getComment().indexOf(annot.getComment()) == -1) {
        			a.setComment(a.getComment() + "; " + annot.getComment());
        		}                		
        	} else {
            	if (context != null) {                        		
            		//context (selector)
        			ElementSelector ses = new ElementSelector(annot.getDocument());
        			ses.setElementURI(context);
        			ses.setSelector(annot.getBody()); 
                	annot.addContext(ses);         		
            	}
        		annot.setFrequency(1);
        		annotationsTable.add(annot);
        	}
        	/*
        	Object[] info = annotationsTable.get(annot);
        	if (info == null) {
        		Object[] newInfo = {new Integer(1), annot.getComment()};
        		annotationsTable.put(annot, newInfo);
        	} else {
        		info[0] = (Integer)info[0] + 1;
        		info[1] = info[1].toString() + annot.getComment();
        		annotationsTable.put(annot, info);
        	}*/
        	//System.out.println("CONTAINS: " + annotationsTable.containsKey(annot));
        	//System.out.println("HASH: " + annotationsTable.size());
        }
	}
	
	/**
	 * Annotates a text with a whatizit pipeline.
	 * @param pipeline
	 * @param paragraph
	 * @throws Exception
	 */
	//@SuppressWarnings("rawtypes")
	/*private void annotateMultipleParagraphs(String pipeline, String paragraph, List<String> lstContext) throws Exception {						
		String annotatedParagraph = WSClientEBI.whatizit(paragraph, pipeline);
        System.out.println("PARAGRAPH: " + annotatedParagraph);
        SAXBuilder builder = new SAXBuilder();
        Reader reader = new StringReader(annotatedParagraph);
        Document doc = builder.build(reader);
        Element root = doc.getRootElement(); //document   
        System.out.println(root.getChild("text").getChildren());
		List sent = root.getChild("text").getChildren("SENT");
		int i = 0;
        for (Object objSent:sent) {
        	String context = lstContext.get(i);
			i++;
            Element element = ((Element)objSent).getChild("plain");
            List annotations = element.getChildren();
            //Same as the other
        }     
	}*/
	
	private void printSummary() {
		logger.info("===INFO: SUMMARY WII=== " 
			+ "UniProt: " + totalUniProt + "\t" 
			+ "Species: " + totalSpecies + "\t" 
			+ "GO: " + totalGO + "\t" 
			+ "Chebi: " + totalChebi + "\t" 
			+ "Disease: " + totalDisease + "\t" 
			+ "E-Disease: " + totalE + "\t" 
			+ "Drug: " + totalDrug + "\t" 
			+ "Total of annotations: " + total + "\t" 
			+ "Error: " + error + "\t"
			+ "Terms: " + totalTerms);
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//RDFAOAnnotator annotator = new WhatizitAnnotator("", "http://www.biotea.ws/elsevier/rdf_whatizit_ao/", "whatizitUkPmcAll", "http://dx.doi.org/10.1016/S0168-8227(01)00365-5", "10.1016/S0168-8227(01)00365-5");
		//String whatizit = "";
		//annotator.annotateWithWhatizit("E:/workspace/LD2RDF_SciVerse_Servlet/temp/S0168822701003655.rdf", "E:/workspace/LD2RDF_SciVerse_Servlet/temp/S0168822701003655_" + whatizit + ".rdf", new ArrayList<Annotation>());
		//annotator.getAnnotationsFromFile("E:/workspace/LD2RDF_SciVerse_Servlet/temp/S0168822701003655_whatizitUkPmcAll.rdf");
		//<([{\^-=$!|]})?*+.>
		String content = "D(+)-maltose";
		Pattern pattern = Pattern.compile("\\[(.*?)\\Q" + content + "\\E(.*?)\\]");
    	Matcher matcher = pattern.matcher("this is a text containing [" + content + "] the content");
    	System.out.println(matcher.find());
	}
}
