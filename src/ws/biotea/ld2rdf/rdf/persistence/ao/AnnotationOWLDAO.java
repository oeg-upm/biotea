/**
 * 
 */
package ws.biotea.ld2rdf.rdf.persistence.ao;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.stanford.smi.protege.exception.OntologyLoadException;

import org.apache.log4j.Logger;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.rdf.model.ao.ElementSelector;
import ws.biotea.ld2rdf.rdf.model.ao.OffsetRangeTextSelector;
import ws.biotea.ld2rdf.rdf.model.ao.Selector;
import ws.biotea.ld2rdf.rdf.model.ao.StartEndElementSelector;
import ws.biotea.ld2rdf.rdfGeneration.RDFHandler;
import ws.biotea.ld2rdf.rdfGeneration.Exception.RDFModelIOException;
import ws.biotea.ld2rdf.util.ClassesAndProperties;
import ws.biotea.ld2rdf.util.Config;
import ws.biotea.ld2rdf.util.ConfigPrefix;
import ws.biotea.ld2rdf.util.GenerateMD5;
import ws.biotea.ld2rdf.util.Prefix;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leylajael
 *
 */
public class AnnotationOWLDAO implements AnnotationDAO {
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * Constructor.
	 */
	public AnnotationOWLDAO() {}
	//Insert, delete, update
	/**
	 * Adds an annotation to the model.
	 * @return
	 * @throws URISyntaxException 
	 * @throws URISyntaxException
	 * @throws OntologyLoadException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public URI insertAnnotation(String datasetURL, String baseURL, Annotation annotation, String id, String fileOutName, boolean empty, boolean blankNode) throws URISyntaxException, FileNotFoundException, ClassNotFoundException, OntologyLoadException {
		ConnectionLDModel conn = new ConnectionLDModel();
		Model model = conn.openJenaModel(fileOutName, empty);		
		createAnnotationInModel(model, datasetURL, baseURL, annotation, id, blankNode);
		//logger.debug("BIOTEA - New annotation added: " + annotation.getUri());
		conn.closeAndWriteJenaModel();
		return (annotation.getUri());
	}
	/**
	 * Inserts a list of annotations.
	 * @param baseURL
	 * @param list
	 * @param empty
	 * @throws OntologyLoadException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 * @throws RDFModelIOException 
	 * @throws Exception
	 */
	public List<Annotation> insertAnnotations(String datasetURL, String baseURL, List<Annotation> list, String fileOutName, boolean empty, boolean blankNode) throws FileNotFoundException, ClassNotFoundException, OntologyLoadException, RDFModelIOException {
		List<Annotation> inserted = new ArrayList<Annotation>();
		ConnectionLDModel conn = new ConnectionLDModel();
		Model model = conn.openJenaModel(fileOutName, empty);	
		for (Annotation annotation: list) {
			try {
				createAnnotationInModel(model, datasetURL, baseURL, annotation, null, blankNode);				
				inserted.add(annotation);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("- ERROR - Annotation not inserted: There has been an error inserting annotation " + annotation + ", error: " + e.getLocalizedMessage());
			}
		}
		try {
			conn.closeAndWriteJenaModel();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.fatal("- FATAL - Annotations model " + fileOutName + " was not closed/saved: " + e.getMessage());
			throw new RDFModelIOException(e);
		}
		//File file = new File(fileOutName);
		logger.info("==Annotated RDF ==" + fileOutName + " ANNOT: " + inserted.size());
		if (inserted.size() == 0) {
			File file = new File(fileOutName);
			file.delete();
		}
		return inserted;
	}
	/**
	 * 
	 * @param model
	 * @param baseURL
	 * @param annotation
	 * @param id
	 * @throws URISyntaxException
	 */
	private void createAnnotationInModel(Model model, String datasetURL, String baseURL, Annotation annotation, String id, boolean blankNode) throws URISyntaxException {
		//OntClass annotationClass = model.getOntClass(Annotation.ANNOTATION_CLASS);
		Property opType = model.getProperty(Config.OP_RDF_TYPE);
		Property opHasSTY = model.getProperty(ClassesAndProperties.UMLS_HAS_STY.getURLValue());
		Property dpBody = model.getProperty(Annotation.ANNOTATION_DP_BODY);
		Property opContext = model.getProperty(Annotation.ANNOTATION_OP_CONTEXT);
		Property opHasTopic = model.getProperty(Annotation.ANNOTATION_OP_HAS_TOPIC);
		Property opAnnotatesResource = model.getProperty(Annotation.ANNOTATION_OP_ANNOTATES_RESOURCE);
		Property dpCreatedOn = model.getProperty(Annotation.ANNOTATION_DP_CREATED_ON);
		Property opCreatedBy = model.getProperty(Annotation.ANNOTATION_OP_CREATED_BY);
		Property opAuthoredBy = model.getProperty(Annotation.ANNOTATION_OP_AUTHORED_BY);
		Property dpLabel = model.getProperty(Annotation.ANNOTATION_DP_LABEL);
		Property rdfsComment = model.getProperty(Annotation.RDFS_COMMENT);
		Property siocNumItem = model.getProperty(Annotation.SIOC_NUMBER_ITEM);
		Property opSeeAlso = model.getProperty(Annotation.RDFS_SEE_ALSO);
		Property opSameAs = model.getProperty(Annotation.OWL_SAME_AS);
		Property dpCUI = model.getProperty(ClassesAndProperties.UMLS_CUI.getURLValue());
		Property dpTUI = model.getProperty(ClassesAndProperties.UMLS_TUI.getURLValue());
		Property dpScore = model.getProperty(Annotation.DP_SCORE);
		//Property opInDataset = model.getProperty(Annotation.VOID_IN_DATASET);
        //Property opIsReferencedBy = model.getProperty(Annotation.DC_IS_REFERENCED_BY);
		
		//Annotation, Body (literal), and createdOn (date)
		if (id == null) {
			annotation.setId(
				GenerateMD5.getInstance().getMD5Hash(
					annotation.getBody() + "_" + annotation.getCreator().toString() + "_" + 
					annotation.getDocument().toString() + "_" + annotation.getTopic()
				) + "_" + annotation.getBody().replaceAll(RDFHandler.CHAR_NOT_ALLOWED, "-"));
		}
		String idPrefix = Annotation.ANNOTATION_ID;
		try {
			idPrefix = annotation.getClass().getField("ANNOTATION_ID").get(null).toString();
		} catch (Exception e) {} 
		String annotationClazz = Annotation.ANNOTATION_CLASS;
		try {
			annotationClazz = annotation.getClass().getField("ANNOTATION_CLASS").get(null).toString();
		} catch (Exception e) {}
				
		Resource annotationRes;
		Resource annotationClass = model.createResource(annotationClazz);
		//Resource basicAnnotationClass = model.createResource(Annotation.ANNOTATION_CLASS); 
		//Resource annotationRes = model.createIndividual(resourceURI, annotationClass).addLiteral(dpBody, annotation.getBody());
		if (blankNode) {
			if (annotation.getDocumentID() != null) {
				annotation.setNodeId(annotation.getDocumentID() + "_" + idPrefix + annotation.getId());
			} else {
				annotation.setNodeId(idPrefix + annotation.getId());
			}
			annotation.setUri(null);
			annotationRes = model.createResource(new AnonId(annotation.getNodeId()));
			annotationRes.addProperty(opType, annotationClass);
			/*if (!annotationClazz.equals(Annotation.ANNOTATION_CLASS)) {
				annotationRes.addProperty(opType, basicAnnotationClass);
			}*/
		} else {
			annotation.setNodeId(null);
			String resourceURI = baseURL + annotation.getId();
			annotation.setUri(new URI(resourceURI));
			annotationRes = model.createResource(annotation.getUri().toString(), annotationClass);
			if (!annotationClazz.equals(Annotation.ANNOTATION_CLASS)) {
				annotationRes.addProperty(opType, annotationClass);
			}
		}
		//annotationRes.addProperty(opInDataset, annotationClass);
		annotationRes.addLiteral(dpBody, annotation.getBody());
		annotationRes.addLiteral(dpCreatedOn, annotation.getCreationDate());
		if (annotation.getLabel() != null) {
			annotationRes.addLiteral(dpLabel, annotation.getLabel());
		}
		//comments and number_items
		if ((annotation.getComment() != null) && (annotation.getComment().length() != 0)) {
			annotationRes.addLiteral(rdfsComment, annotation.getComment());
		}
		//annotationRes.addLiteral(siocNumItem, annotation.getFrequency()+0L);
		annotationRes.addProperty(siocNumItem, "" + annotation.getFrequency(), XSDDatatype.XSDint);
		if (annotation.getScore() != null) {
			annotationRes.addProperty(dpScore, "" + annotation.getScore(), XSDDatatype.XSDdouble);
		}
		//Context
		//OffsetRangeTextSelectorOWL ortsOWL = new OffsetRangeTextSelectorOWL();
		//XPointerSelectorOWL xpointerOWL = new XPointerSelectorOWL();
		SelectorDAO<StartEndElementSelector> startEndElementOWL = new StartEndElementSelectorOWL();
		SelectorDAO<ElementSelector> elementOWL = new ElementSelectorOWL();
		SelectorDAO<OffsetRangeTextSelector> offsetSelectorOWL = new OffsetRangeTextSelectorOWL();
		for (Selector selector:annotation.getContext()) {
			/*if (selector instanceof OffsetRangeTextSelector) {
				OffsetRangeTextSelector orTextSelector = (OffsetRangeTextSelector) selector;
				Resource context = ortsOWL.addSelector(orTextSelector, model);
				annotationRes.addProperty (opContext, context);
			} else if (selector instanceof XPointerSelector) {
				XPointerSelector xpointerSelector = (XPointerSelector) selector;
				xpointerSelector.setSelector("ld:tag id=\"" + annotation.getId() + "\"");
				Resource context = xpointerOWL.addSelector(xpointerSelector, model);
				annotationRes.addProperty (opContext, context);
			}*/
			if (selector instanceof StartEndElementSelector) {
				if (Config.keepStartEnd()) {
					StartEndElementSelector ses = (StartEndElementSelector)selector;
					Resource resContext = startEndElementOWL.addSelector(ses, baseURL, model);
					model.add(annotationRes, opContext, resContext);
				} else {
					StartEndElementSelector temp = (StartEndElementSelector)selector;
					ElementSelector ses = new ElementSelector(temp.getDocument());
					ses.setDocumentId(temp.getDocumentId());
					ses.setElementURI(temp.getElementURI());
					ses.setSelector(temp.getSelector());
					ses.setUri(temp.getUri());
					Resource resContext = elementOWL.addSelector(ses, baseURL, model);
					model.add(annotationRes, opContext, resContext);
				}				
			} else if (selector instanceof ElementSelector) {
				ElementSelector ses = (ElementSelector)selector;
				Resource resContext = elementOWL.addSelector(ses, baseURL, model);
				model.add(annotationRes, opContext, resContext);
			} else if (selector instanceof OffsetRangeTextSelector) {
				OffsetRangeTextSelector ses = (OffsetRangeTextSelector)selector;
				Resource resContext = offsetSelectorOWL.addSelector(ses, baseURL, model);				
				model.add(annotationRes, opContext, resContext);
			}
		}		
		//Topics
		for (ws.biotea.ld2rdf.rdf.model.ao.Topic topic:annotation.getTopic()) {
			Resource resTopic = model.createResource(topic.getURL().toString());
			annotationRes.addProperty(opHasTopic, resTopic);
			if (Config.withBio()) {
				String strIdentifier = Prefix.toIdentifiersOrg(topic.getNameSpace().toString());
				if (strIdentifier != null) {
					Resource resIdentifier = model.createResource(strIdentifier);
					resTopic.addProperty(opSameAs, resIdentifier);
				}
	            strIdentifier = Prefix.toBio2RDFOrg(topic.getNameSpace().toString());
	            if (strIdentifier != null) {
	                Resource resIdentifier = model.createResource(strIdentifier);
	                resTopic.addProperty(opSameAs, resIdentifier);
	            }
	            String cui = Prefix.getCUI(topic.getNameSpace().toString());
	            if (cui != null) {
	            	resTopic.addLiteral(dpCUI, cui);
	            	if ((topic.getUmlsType() != null) && (topic.getUmlsType().length() != 0)) {
	            		resTopic.addLiteral(dpTUI, topic.getUmlsType());
	            		Resource typeClass = model.createResource(ConfigPrefix.getURL("UMLS_TYPE") + topic.getUmlsType());
	            		resTopic.addProperty(opType, typeClass);
	            		typeClass = model.createResource(ClassesAndProperties.UMLS_STY.getURLValue() + "/" + topic.getUmlsType());
	            		resTopic.addProperty(opHasSTY, typeClass);
	            	}
	            }
	            /*strIdentifier = Prefix.isUMLS(topic.getNameSpace().toString());
	            if (strIdentifier != null) {
	                Resource resIdentifier = model.createResource(strIdentifier);
	                resTopic.addProperty(opSameAs, resIdentifier);
	                if (strIdentifier.startsWith(Config.getUmlsCuiURL()) && (topic.getUmlsType() != null) 
	                		&& (topic.getUmlsType().length() != 0)) {
	                	Resource typeClass = model.createResource(Config.getUmlsTypeURL() + topic.getUmlsType());
	                	resIdentifier.addProperty(opType, typeClass);
	                }
	            }*/ 
			}                     
			for (URI seeAlso: topic.getSeeAlso()) {
				Resource resIdentifier = model.createResource(seeAlso.toString());
				resTopic.addProperty(opSeeAlso, resIdentifier);
			}
			if ((topic.getComment() != null) && (topic.getComment().length() != 0)) {
				resTopic.addLiteral(rdfsComment, topic.getComment());
			}
		}
		//annotated resource
		Resource resDocument = model.createResource(annotation.getDocument().getUri().toString()); 
		annotationRes.addProperty(opAnnotatesResource, resDocument);
		//created by
		Resource resCreator = model.createResource(annotation.getCreator().getUri().toString());
		annotationRes.addProperty(opCreatedBy, resCreator);
		//authored by
		Resource resAuthor = model.createResource(annotation.getAuthor().getUri().toString());
		annotationRes.addProperty(opAuthoredBy, resAuthor);
	}
	public void deleteAnnotation(String baseURL, String id, String uri, String fileOut, boolean empty) throws Exception {
		/*ConnectionLDModel conn = new ConnectionLDModel();
		JenaOWLModel owlModel = conn.openOWLModel(uri, fileOut, empty);
		
		String resourceURI = baseURL + "Annotation_" + id;
		//Delete
		OWLIndividual ind = owlModel.getOWLIndividual(resourceURI);
		ind.delete();
		//Delete selectors
		OffsetRangeTextSelectorOWL ortsOWL = new OffsetRangeTextSelectorOWL();	
		ortsOWL.deleteAnnotationSelector(id, owlModel);
		XPointerSelectorOWL xpointerOWL = new XPointerSelectorOWL();
		xpointerOWL.deleteAnnotationSelector(id, owlModel);
		
		conn.closeAndWriteOWLModel();*/
	}
	public URI updateAnnotation(String datasetURL, String baseURL, Annotation annotation, String uri, String fileOut, boolean empty) throws Exception {
		this.deleteAnnotation(baseURL, annotation.getId(), uri, fileOut, empty);
		return (this.insertAnnotation(datasetURL, baseURL, annotation, annotation.getId(), fileOut, empty, annotation.getUri() == null));
	}
	public static void main(String[] args) throws Exception {
		System.out.println(XSDDatatype.XSDnonNegativeInteger);
		System.out.println(XSDDatatype.XSDnonNegativeInteger.getURI());
//		AnnotationDAO dao = new AnnotationOWLDAO();
//		Annotation annot = new Annotation();
//		FoafAgent agent = new FoafAgent();
//		agent.setId(new URI(RDFHandler.BASE_FOAF + "whatizitUkPmcAll"));
//		annot.setCreator(agent);
//		annot.setBody("body test");
//		annot.setComment("comment test");
//		//annot.getDirectContext().add(new URI("http://www.biotea.ws/pubmedOpenAccess/rdf/paper_PMC2940432/Imagining-Childhood_paragraph_3"));
//		Collection<Topic> topics = new ArrayList<Topic>();
//		Topic topic = new Topic();
//		topic.setName("body chebi test");
//		topic.setNameSpace(new URI("chebi:12345")); //chebi: http://purl.obolibrary.org/obo/CHEBI_
//		topic.setURL(new URI("http://purl.obolibrary.org/obo/CHEBI_"));
//		topics.add(topic);
//		annot.setTopic(topics);
//		FoafDocument document = new FoafDocument();
//		document.setId(new URI("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2940432"));
//		annot.setResource(document);
//		annot.setCreationDate(Calendar.getInstance());
//		dao.insertAnnotation("http://base.com/", annot, null, "E:/workspace/LD2RDF_SciVerse_Servlet/temp/testAO.rdf", true, true);
	}	
}
