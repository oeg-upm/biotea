package ws.biotea.ld2rdf.rdfGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedSet;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.rdf.model.ao.ElementSelector;
import ws.biotea.ld2rdf.rdf.model.ao.ExactQualifier;
import ws.biotea.ld2rdf.rdf.model.ao.FoafAgent;
import ws.biotea.ld2rdf.rdf.model.ao.FoafDocument;
import ws.biotea.ld2rdf.rdf.model.ao.StartEndElementSelector;
import ws.biotea.ld2rdf.rdf.model.ao.Topic;
import ws.biotea.ld2rdf.rdf.persistence.ao.AnnotationOWLDAO;
import ws.biotea.ld2rdf.util.Config;
import ws.biotea.ld2rdf.util.ConfigPrefix;
import ws.biotea.ld2rdf.util.ncbo.annotator.Ontology;
import ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.AnnotationCollection;
import ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.Annotations;
import ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.Empty;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class NCBOAnnotatorClient extends RDFAOAnnotator { 
	private static Logger logger = Logger.getLogger(NCBOAnnotatorClient.class);
	private static final String annotatorURL = Config.getNCBOServiceURL(); //"http://rest.bioontology.org/obs/annotator";
	private static final String ontologiesToAnnotate = Ontology.getInstance().getAllAcronym(); //Ontology.getAllVirtualId();
	public static final String BASE_FOAF_NCBO_ANNOTATOR = Config.getNCBOAnnotatorURL();//"http://bioportal.bioontology.org/annotator/";
	private String BASE_URL;
	private String datasetURL;
	private FoafAgent authorAgent;
	private FoafAgent creatorAgent;
	private String doiURL;
	private String documentId;
	private List<Annotation> annotationsTable;
	private int error = 0;
	//http://www.ranks.nl/resources/stopwords.html
	private final static String stopWords = Config.getNCBOStopwords();		
	
	public NCBOAnnotatorClient(String datasetURL, String baseUrl, String doiURL, String documentId, String creatorAgent) throws URISyntaxException {
		this.datasetURL = datasetURL;
		this.BASE_URL = baseUrl;
		this.authorAgent = new FoafAgent();
		this.authorAgent.setId(new URI(NCBOAnnotatorClient.BASE_FOAF_NCBO_ANNOTATOR));
		this.creatorAgent = new FoafAgent();
		this.creatorAgent.setId(new URI(creatorAgent));
		this.doiURL = doiURL;
		this.documentId = documentId;
		this.annotationsTable = new ArrayList<Annotation>();		
	}
	
	/**
	 * Annotates an rdf file with whatizit and write annotations to an rdf with AO.
	 * @param rdfInName
	 * @param rdfOutName
	 * @return
	 * @throws Exception
	 */
	public File annotate(String rdfInName, String rdfOutName, List<Annotation> lstAnnotations, String textProperty) throws Exception {	
		//logger.info("===annotateWithNCBO=== " + rdfInName);
		// create an empty model
		Model model = ModelFactory.createDefaultModel();
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(rdfInName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + rdfInName + " not found");
		}
		// read the RDF/XML file
		model.read(in, null);
		logger.debug("===RDF READ=== " + rdfInName);
		
		Property text = model.getProperty(textProperty);
		ResIterator resItr = model.listResourcesWithProperty(text);
		//String paragraph = "";
		//SortedSet<ParagraphContext> allContext = new TreeSet<ParagraphContext>();
		while (resItr.hasNext()) {
			Resource res = resItr.next();
			Matcher matcher = WhatizitAnnotator.excludedSections.matcher(res.getURI().toString());
        	if (matcher.find()) {
        		continue; //excluded sections will not be annotated
        	} else {
        		//paragraph by paragraph
        		String textToAnnotate = res.getProperty(text).getObject().toString();
        		String urlToAnnotate = res.getURI().toString();
        		if ((textToAnnotate != null) && (textToAnnotate.length() != 0)) {
        			boolean writeDown = annotateWithNCBO(textToAnnotate, urlToAnnotate);
                	if (!writeDown) {
                		logger.warn("- WARNING PARAGRAPH - NCBO annotations for " + this.documentId + "(" + urlToAnnotate + ")");
                	}
        		}        		
            	//aggregate paragraphs, annotate later
//        		String para = res.getProperty(text).getObject().toString();
//        		para = para.replaceAll("[^\\p{Alpha}\\p{Z}\\p{P}\\p{N}]", "_");        	
//            	para = URLEncoder.encode(para, Publication2RDF.UTF_ENCODING);
//            	para = para.replace("+", " ");
//        		paragraph += "\n" + para;
//        		allContext.add(new ParagraphContext(res.getURI().toString(), paragraph.length()));
        	}
		}
		//we annotate as much as we can, some paragraphs can be omitted
//		if (writeDown) {
			logger.debug("===ALL SECTIONS READ=== " + rdfInName);
			logger.info("===SECTIONS ANNOTATED=== " + rdfInName);
			AnnotationOWLDAO dao = new AnnotationOWLDAO();
			lstAnnotations = dao.insertAnnotations(datasetURL, BASE_URL, annotationsTable, rdfOutName, true, false);
			error = annotationsTable.size() - lstAnnotations.size();
			if (error != 0) {
				logger.info("==ERROR writing annotations NCBO== " + error + " annotations were not created, check the logs starting by 'Annotation not inserted' for more information");			
			}
//		}
		//all aggregate paragraphs are here annotated
//		logger.debug("===ALL SECTIONS READ=== " + rdfInName);
//		annotateWithNCBO(paragraph, allContext);
//		logger.info("===SECTIONS ANNOTATED=== " + rdfInName);
//		AnnotationOWLDAO dao = new AnnotationOWLDAO();
//		lstAnnotations = dao.insertAnnotations(datasetURL, BASE_URL, annotationsTable, rdfOutName, true, false);
//		error = annotationsTable.size() - lstAnnotations.size();
//		if (error != 0) {
//			logger.info("==ERROR writing annotations NCBO== " + error + " annotations were not created, check the logs starting by 'Annotation not inserted' for more information");			
//		}
		return new File(rdfOutName);		
	}
	
	/**
	 * Annotates a long paragraph corresponding to multiple contexts.
	 */
	@Deprecated
    private boolean annotateWithNCBO(String paragraph, SortedSet<ParagraphContext> allContext) {
        try {        	        	
            HttpClient client = new HttpClient();
            client.getParams().setParameter(HttpMethodParams.USER_AGENT, "Annotator Client Scientific Publications");  //Set this string for your application 
            
            PostMethod method = new PostMethod(annotatorURL);
            
            // Configure the form parameters
            method.addParameter("stop_words",stopWords);
            method.addParameter("minimum_match_length","3");
            method.addParameter("exclude_numbers", "true");
            method.addParameter("ontologies", ontologiesToAnnotate);            
            method.addParameter("text", paragraph);
            method.addParameter("format", "xml"); //Options are 'text', 'xml', 'tabDelimited'   
            method.addParameter("apikey", Config.getNCBOAPIKey());

            // Execute the POST method
            int statusCode = client.executeMethod(method);
            
            if( statusCode != -1 ) {
                try {
	                InputStream annotatedParagraph = method.getResponseBodyAsStream();
	                //InputStream temporal  = method.getResponseBodyAsStream();
	                //testing	
	                /*
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                byte[] buf = new byte[1024];
	                int n = 0;
	                while ((n = temporal.read(buf)) >= 0) {
	                    baos.write(buf, 0, n);
	                }
	                byte[] content = baos.toByteArray();
	                InputStream annotatedParagraph = new ByteArrayInputStream(content);	  
	                */              
	                if (annotatedParagraph != null) {	                	
	                	//Reader reader = new StringReader(annotatedParagraph);
	            		JAXBContext jc = JAXBContext.newInstance("ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated");
	            		Unmarshaller unmarshaller = jc.createUnmarshaller();
	            		AnnotationCollection xml;
	            		Object obj = new Object();
	            		try {	            			
	            			obj = unmarshaller.unmarshal(annotatedParagraph);
	            			if (obj instanceof Empty) {
	            				return true; //no annotations were found but everything was ok with the response
	            			}
	            			xml = (AnnotationCollection)obj; //otherwise, AnnotationCollection should be the unmarshalled object
	            		} catch (Exception e) {
	            			/*
	            			InputStream temp = new ByteArrayInputStream(content);
	            			String line;
	    	                BufferedReader reader = new BufferedReader(new InputStreamReader(temp, "UTF-8"));
	    	                while ((line = reader.readLine()) != null) {
	    	                	System.out.println(line);
	    	                }
	    	                reader.close();
	    	                System.out.println(paragraph);
	    	                */
            				logger.fatal("- FATAL DTD ERROR ANNOTATOR - NCBO annotations for " + this.documentId + " cannot be unmarshalled: " + e.getMessage() + " - class: " + obj.getClass());
	            			return false;			
	            		}
	            		logger.debug("---Annotations unmarshalled---");	
	            		List<ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.Annotation> lstAnn = xml.getAnnotation();//one annotation = one topic
	            		for (ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.Annotation ann: lstAnn) {
	            			String fullId = ann.getAnnotatedClass().getId();
	            			List<Annotations> lstAnnotations = ann.getAnnotationsCollection().getAnnotations();
	            			for (Annotations annotation: lstAnnotations) {
	            				String body = annotation.getText();        						
								//System.out.println("ANNOT 1: " + fullId + " - " + body);
								ws.biotea.ld2rdf.util.ncbo.annotator.NCBOOntology onto = ws.biotea.ld2rdf.util.ncbo.annotator.Ontology.getInstance().getOntologyByURL(fullId);
								if (onto == null) { //For NDFRT is possible to get UMLS elements, those are excluded
									continue;
								}	
								//System.out.println("URL: " + onto + "-" +  onto.getUrl());
								String partialId = fullId.substring(onto.getURL().length());
								if (partialId.startsWith("/")) {
									partialId = partialId.substring(1);
								}
								//System.out.println("ANNOT 2: " + partialId + " - " + body);
								//annot: creator, body, resource, date
								ExactQualifier annot = new ExactQualifier();
								annot.setAuthor(authorAgent);
								annot.setCreator(creatorAgent);	            							
		            			annot.setBody(body);
		            			FoafDocument document = new FoafDocument();
		            			document.setId(new URI(doiURL));
		            			annot.setResource(document);
		            			annot.setDocumentID(documentId);
		            			annot.setCreationDate(Calendar.getInstance());	
		            			//topic
		        				Topic topic = new Topic();
		        				topic.setName(body);
		        				topic.setNameSpace(new URI(onto.getNS() + ":" + partialId));
		        				topic.setURL(new URI(onto.getURL() + partialId));
		        				Topic topic2 = null;
		        				if (onto.getNS().equals(ConfigPrefix.getNS("NCBITaxon"))) {
		        					topic2 = new Topic();
		                			topic2.setName(body);
		                			topic2.setNameSpace(new URI(ConfigPrefix.getNS("UNIPROT_TAXONOMY") + ":" + partialId)); //species: http://purl.uniprot.org/taxonomy/
		                			topic2.setURL(new URI(ConfigPrefix.getURL("UNIPROT_TAXONOMY") + partialId));
		        				}
		        				//Go to the annotations table		        			
		        				if (annot != null) {
		        					int to = annotation.getTo().intValue();
		        					int from = annotation.getFrom().intValue();
		        					String urlContext = null;
	        	            		for (ParagraphContext context: allContext) {
	        	            			if (to <= context.globalEnd) {
	        	            				urlContext = context.url;
	        	            				break;
	        	            			}
	        	            		}
		        	            	//System.out.println("ANNOT: " + annot);
		        	            	int pos = annotationsTable.indexOf(annot);
		        	            	if (pos != -1) {
		        	            		Annotation a = annotationsTable.get(pos);
		        	            		if (!a.getTopic().contains(topic)) {
		        	            			a.addTopic(topic);		        	            			
		        	            		}
		        	            		if (topic2 != null) {
		        	            			if (!a.getTopic().contains(topic2)) {
	    	        	            			a.addTopic(topic2);
	    	        	            		}
		        	            		}		        	            		
		        	            		if (urlContext != null) {
		        	                		//context (selector)
		        	            			StartEndElementSelector ses = new StartEndElementSelector(a.getDocument());
		        	            			ses.setElementURI(urlContext);
		        	            			ses.setSelector(a.getBody());
		        	            			ses.setStart(from);
		        	            			ses.setEnd(to);
		        	            			if (!a.getContext().contains(ses)) {
		        	            				a.addContext(ses); 		        	            				
		        	            			}		        	            			
		        	                	}  
		        	            		a.setFrequency(a.getContext().size());
		        	            	} else {
	        	            			annot.addTopic(topic);
	        	            			if (topic2 != null) {
	        	            				annot.addTopic(topic2);
	        	            			}
	        	            			if (urlContext != null) {                        		
	        	            				StartEndElementSelector ses = new StartEndElementSelector(annot.getDocument());
		        	            			ses.setElementURI(urlContext);
		        	            			ses.setSelector(annot.getBody());
		        	            			ses.setStart(from);
		        	            			ses.setEnd(to);
		        	            			annot.addContext(ses);
	        	            				annot.setFrequency(1);     		
	        	                    	}
	        	                		annotationsTable.add(annot);
		        	            	}
		        	            }
	            			}						
	            		}
	                }
	                method.releaseConnection();
	                method = null;
                } catch( Exception e ) {
                    //e.printStackTrace();
                    logger.info("===ERROR NCBO Annotator (" + this.documentId + ")=== " +e.getMessage());
                    return false;
                }
            } else {
            	logger.info("===ERROR??? NCBO Annotator (" + this.documentId + ")=== (status code != 1) ");
                return false;
            }
        } catch ( Exception e ) {
        	//e.printStackTrace();
            logger.info("===ERROR NCBO Annotator (" + this.documentId + ")=== (http call)" + e.getMessage());
            return false;
        }
        return true;
    }
	
	/**
	 * Annotate a short paragraph corresponding only to one context.
	 */
    private boolean annotateWithNCBO(String paragraph, String urlContext) {
        try {        	
        	paragraph = paragraph.replaceAll("[^\\p{Alpha}\\p{Z}\\p{P}\\p{N}]", "_");        	
        	paragraph = URLEncoder.encode(paragraph, Publication2RDF.UTF_ENCODING);
        	paragraph = paragraph.replace("+", " ");
        	//System.out.println("TO ANNOT: " + urlContext + "\n" + paragraph);
            HttpClient client = new HttpClient();
            client.getParams().setParameter(HttpMethodParams.USER_AGENT, "Annotator Client Scientific Publications");  //Set this string for your application 
            
            PostMethod method = new PostMethod(annotatorURL);
            
            // Configure the form parameters
            method.addParameter("stopWords",stopWords);
            method.addParameter("minimum_match_length","3");
            method.addParameter("ontologies", ontologiesToAnnotate);            
            method.addParameter("text", paragraph);
            method.addParameter("format", "xml"); //Options are 'text', 'xml', 'tabDelimited'   
            method.addParameter("apikey", Config.getNCBOAPIKey());

            // Execute the POST method
            int statusCode = client.executeMethod(method);
            
            if( statusCode != -1 ) {
                try {
	                InputStream annotatedParagraph = method.getResponseBodyAsStream();
	                //InputStream temporal  = method.getResponseBodyAsStream();
	                //testing
	                /*
	                String line;
	                BufferedReader reader = new BufferedReader(new InputStreamReader(annotatedParagraph, "UTF-8"));
	                BufferedWriter output = new BufferedWriter(new FileWriter("D:/workspace/biotea_rdf4pmc/temp/AO_annotations/ncbo1.xml", true));
	                while ((line = reader.readLine()) != null) {
	                	//System.out.println(line);
	                	output.append(line + "\n");
	                }
	                output.close();
	                annotatedParagraph = null;
	                */
	                //testing
	                /*
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                byte[] buf = new byte[1024];
	                int n = 0;
	                while ((n = temporal.read(buf)) >= 0) {
	                    baos.write(buf, 0, n);
	                }
	                byte[] content = baos.toByteArray();
	                InputStream annotatedParagraph = new ByteArrayInputStream(content);	  
	                  */           
	                if (annotatedParagraph != null) {	                	
	                	//Reader reader = new StringReader(annotatedParagraph);
	            		JAXBContext jc = JAXBContext.newInstance("ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated");
	            		Unmarshaller unmarshaller = jc.createUnmarshaller();
	            		AnnotationCollection xml;
	            		Object obj = new Object();
	            		try {	            			
	            			obj = unmarshaller.unmarshal(annotatedParagraph);
	            			if (obj instanceof Empty) {
	            				return true; //no annotations were found but everything was ok with the response
	            			}
	            			xml = (AnnotationCollection)obj; //otherwise, AnnotationCollection should be the unmarshalled object
	            		} catch (Exception e) {
	            			/*
	            			InputStream temp = new ByteArrayInputStream(content);
	            			String line;
	    	                BufferedReader reader = new BufferedReader(new InputStreamReader(temp, "UTF-8"));
	    	                while ((line = reader.readLine()) != null) {
	    	                	System.out.println(line);
	    	                }
	    	                reader.close();
	    	                System.out.println(paragraph);
	    	                */
            				logger.fatal("- FATAL DTD ERROR ANNOTATOR - NCBO annotations for " + this.documentId + "(" + urlContext + ") cannot be unmarshalled: " + e.getMessage() + " - class: " + obj.getClass());
	            			return false;			
	            		}
	            		logger.debug("---Annotations unmarshalled---");	
	            		List<ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.Annotation> lstAnn = xml.getAnnotation();
	            		for (ws.biotea.ld2rdf.util.ncbo.annotator.jaxb.newgenerated.Annotation ann: lstAnn) {
	            			String fullId = ann.getAnnotatedClass().getId();
	            			List<Annotations> lstAnnotations = ann.getAnnotationsCollection().getAnnotations();
	            			if (lstAnnotations.size() != 0) {
	            				String body = lstAnnotations.get(0).getText();
	            				//int from = annotation.getFrom().intValue();
        						//int to = annotation.getTo().intValue();
								//System.out.println("ANNOT 1: " + fullId + " - " + body);
								ws.biotea.ld2rdf.util.ncbo.annotator.NCBOOntology onto = ws.biotea.ld2rdf.util.ncbo.annotator.Ontology.getInstance().getOntologyByURL(fullId);
								if (onto == null) { //For NDFRT is possible to get UMLS elements, those are excluded
									continue;
								}	
								//System.out.println("URL: " + onto + "-" +  onto.getUrl());
								String partialId = fullId.substring(onto.getURL().length());
								if (partialId.startsWith("/")) {
									partialId = partialId.substring(1);
								}
								//System.out.println("ANNOT 2: " + partialId + " - " + body);
								//annot: creator, body, resource, date
								ExactQualifier annot = new ExactQualifier();
								annot.setAuthor(authorAgent);
								annot.setCreator(creatorAgent);	            							
		            			annot.setBody(body);
		            			FoafDocument document = new FoafDocument();
		            			document.setId(new URI(doiURL));
		            			annot.setResource(document);
		            			annot.setDocumentID(documentId);
		            			annot.setCreationDate(Calendar.getInstance());		            			
		            			//topic
		        				Topic topic = new Topic();
		        				topic.setName(body);
		        				topic.setNameSpace(new URI(onto.getNS() + ":" + partialId));
		        				topic.setURL(new URI(onto.getURL() + partialId));
		        				Topic topic2 = null;
		        				if (onto.getNS().equals(ConfigPrefix.getNS("NCBITaxon"))) {
		        					topic2 = new Topic();
		                			topic2.setName(body);
		                			topic2.setNameSpace(new URI(ConfigPrefix.getNS("UNIPROT_TAXONOMY") + ":" + partialId)); //species: http://purl.uniprot.org/taxonomy/
		                			topic2.setURL(new URI(ConfigPrefix.getURL("UNIPROT_TAXONOMY") + partialId));
		        				}
		        				annot.setFrequency(lstAnnotations.size());	
		        				//Go to the annotations table
		        				if (annot != null) {
		        	            	//System.out.println("ANNOT: " + annot);
		        	            	int pos = annotationsTable.indexOf(annot);
		        	            	if (pos != -1) {
		        	            		Annotation a = annotationsTable.get(pos);
		        	            		if (!a.getTopic().contains(topic)) {
		        	            			a.addTopic(topic);		        	            			
		        	            		}
		        	            		if (topic2 != null) {
		        	            			if (!a.getTopic().contains(topic2)) {
	    	        	            			a.addTopic(topic2);
	    	        	            		}
		        	            		}
		        	            		if (urlContext != null) {
		        	                		//context (selector)
		        	            			ElementSelector ses = new ElementSelector(a.getDocument());
		        	            			ses.setElementURI(urlContext);
		        	            			ses.setSelector(a.getBody());		        	            			
		        	            			if (!a.getContext().contains(ses)) {
		        	            				a.addContext(ses); 
		        	            				a.setFrequency(a.getFrequency() + annot.getFrequency());
		        	            			}
		        	                	}  
		        	            	} else {
	        	            			annot.addTopic(topic);
	        	            			if (topic2 != null) {
	        	            				annot.addTopic(topic2);
	        	            			}
	        	            			if (urlContext != null) {                        		
	        	                    		//context (selector)
	        	                			ElementSelector ses = new ElementSelector(annot.getDocument());
	        	                			ses.setElementURI(urlContext);
	        	                			ses.setSelector(annot.getBody()); 
	        	                        	annot.addContext(ses);         		
	        	                    	}
	        	                		annotationsTable.add(annot);
		        	            	}
		        	            }
	            			}						
	            		}
	                }
	                method.releaseConnection();
	                method = null;
                } catch( Exception e ) {
                    //e.printStackTrace();
                    logger.info("===ERROR NCBO Annotator (" + this.documentId + ")=== " +e.getMessage());
                    return false;
                }
            } else {
            	logger.info("===ERROR??? NCBO Annotator (" + this.documentId + ")=== (status code != 1) ");
                return false;
            }
        } catch ( Exception e ) {
        	//e.printStackTrace();
            logger.info("===ERROR NCBO Annotator (" + this.documentId + ")=== (http call)" + e.getMessage());
            return false;
        }
        return true;
    }
    @Deprecated
    private class ParagraphContext implements Comparable<ParagraphContext>{
    	String url;
    	int globalEnd;
    	ParagraphContext (String url, int globalEnd) {
    		this.url = url;
    		this.globalEnd = globalEnd;
    	}
    	public String toString() {
    		return globalEnd + " - " + url;
    	}
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ParagraphContext para) {
			return this.globalEnd - para.globalEnd;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + globalEnd;
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ParagraphContext other = (ParagraphContext) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (globalEnd != other.globalEnd)
				return false;
			else 
				return true;
		}
		private NCBOAnnotatorClient getOuterType() {
			return NCBOAnnotatorClient.this;
		}
		
    }

    
    public static void main(String[] args) throws Exception {
    	NCBOAnnotatorClient ao = new NCBOAnnotatorClient("", "http://base_URL.test/ncboAnnotator/", "http://doiURL.org/1234", "1234", "creatorAgent");
//    	ao.annotate("D:\\workspace\\biotea_rdf4pmc\\temp\\PMC2621306_sections.rdf", "D:\\workspace\\biotea_rdf4pmc\\temp\\AO_annotations/PMC2621306_sections_ncboAnnotator.rdf", new ArrayList<Annotation>(), ClassesAndProperties.RDFS_PROP_COMMENT.getURLValue());
    	ao.annotateWithNCBO("measured melanoma activity measured brain stress cancer", "123");
/*    	String paragraph = "diabetes mellitus + sugar = not good \n at all";
    	paragraph = paragraph.replaceAll("[^\\p{Alpha}\\p{Z}\\p{P}\\p{N}]", "_");
    	System.out.println(paragraph);
    	paragraph = URLEncoder.encode(paragraph, Publication2RDF.UTF_ENCODING);
    	System.out.println(paragraph);
    	paragraph = paragraph.replace("+", " ");
    	System.out.println(paragraph);
    	System.out.println(Ontology.getInstance().getAllAcronym());
*/
    }
}