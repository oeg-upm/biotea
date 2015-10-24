package ws.biotea.ld2rdf.rdf.model.ao;

import ws.biotea.ld2rdf.util.Conversion;
import ws.biotea.ld2rdf.util.Prefix;

import java.io.Serializable;
import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;



/**
 * Annotation: This class represents a general annotation on a Document.
 * @author leylajael@gmail.com
 */
public class Annotation implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 * TODO
	 * 1. When someone agrees to an annotation, it is possible to add topics, etc. 
	 * and we want to know who added what --> copy the annotation
	 * 2. When the creator changes the body, what happens with the agrees? --> 
	 * Create a new annotation, keep the old one (new type)
	 * */
	/* Attributes */
	private String id;
	/**
	 * Label - short name with no URI (optional)
	 */
	private String label;
	/**
	 * URI
	 */
	private URI uri;
	/**
	 * If it is a blank node, it could have a node id
	 */
	private String nodeId;
	/**
	 * Body (tag) of this annotation, only one
	 */
	private String body;
	/**
	 * Topics of this annotation (semantic associations)
	 */
	private Collection<Topic> topic = new Vector<Topic>();
	/**
	 * Contexts of this annotation (portions of the document)
	 */
	private Collection<Selector> context = new Vector<Selector>();
	/**
	 * Annotated resource
	 */
	private FoafDocument document;
	private String documentID;
	/**
	 * Creation date of this annotation
	 */
	private Calendar creationDate;
	/**
	 * Comments
	 * */
	private String comment = "";
	/**
	 * Number of annotations within a paper.
	 */
	private int frequency;
	/**
	 * Creator of this annotation
	 */
	private FoafAgent creator;
	/**
	 * Author of this annotation
	 */
	private FoafAgent author;
	/**
	 * Score for this annotation (usually for non-dictionary annotators)
	 */
	private Double score = null;
	/* OWL Descriptors */
	public final static String ANNOTATION_CLASS = Prefix.AO_CORE.getURL() + "Annotation";
	public final static String ANNOTATION_DP_BODY = Prefix.AO_CORE.getURL() + "body"; //TODO OP?
	public final static String ANNOTATION_OP_CONTEXT = Prefix.AO_CORE.getURL() + "context";
	public final static String ANNOTATION_OP_HAS_TOPIC = Prefix.AO_CORE.getURL() + "hasTopic";
	public final static String ANNOTATION_OP_ANNOTATES_RESOURCE = Prefix.AO_CORE.getURL() + "annotatesResource";
	public final static String ANNOTATION_DP_CREATED_ON = Prefix.PAV.getURL() + "createdOn"; //TODO: OP?
	public final static String ANNOTATION_OP_CREATED_BY = Prefix.PAV.getURL() + "createdBy";
	public final static String ANNOTATION_OP_AUTHORED_BY = Prefix.PAV.getURL() + "authoredBy";
	public final static String ANNOTATION_DP_LABEL = Prefix.RDFS.getURL() + "label";
	public final static String ANNOTATION_ID = "Annotation_";
	public final static String ANNOTATION_TYPE = "Annotation";
	public final static String RDFS_COMMENT = Prefix.RDFS.getURL() + "comment";
	public final static String SIOC_NUMBER_ITEM = Prefix.BIOTEA.getURL() + "occurrences";
	public final static String RDFS_SEE_ALSO = Prefix.RDFS.getURL() + "seeAlso";
	public final static String OWL_SAME_AS = Prefix.OWL.getURL() + "sameAs";
    public final static String DC_IS_REFERENCED_BY = Prefix.DCTERMS.getURL() + "isReferencedBy";
    public final static String VOID_IN_DATASET = Prefix.VOID.getURL() + "inDataset"; 
    public final static String DP_SCORE = Prefix.BIOTEA.getURL() + "score"; 

	/**
	 * Returns the uri.
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}
	/**
	 * Sets the value for the id.
	 * @param id the id to set
	 */
	public void setUri(URI id) {
		this.uri = id;
	}
	/**
	 * Returns the body.
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * Sets the value for the body.
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * Returns the topic.
	 * @return the topic
	 */
	public Collection<Topic> getTopic() {
		return topic;
	}
	/**
	 * Sets the value for the topic.
	 * @param topic the topic to set
	 */
	public void setTopic(Collection<Topic> topic) {
		this.topic = topic;
	}
	/**
	 * Adds the value for the topic.
	 * @param topic the topic to set
	 */
	public void addTopic(Topic topic) {
		this.topic.add(topic);
	}
	/**
	 * Returns the context.
	 * @return the context
	 */
	public Collection<Selector> getContext() {
		return context;
	}
	/**
	 * Sets the value for the context.
	 * @param context the context to set
	 */
	public void setContext(Collection<Selector> context) {
		this.context = context;
	}
	/**
	 * Adds the value for the context.
	 * @param context the context to set
	 */
	public void addContext(Selector context) {
		this.context.add(context);
	}
	/**
	 * Returns the resource.
	 * @return the resource
	 */
	public FoafDocument getDocument() {
		return document;
	}
	/**
	 * Sets the value for the resource.
	 * @param resource the resource to set
	 */
	public void setResource(FoafDocument resource) {
		this.document = resource;
	}
	/**
	 * Returns the creationDate (dd/mm/yyyy).
	 * @return the creationDate
	 */
	public String getCreationDateAsString() {
		return (Conversion.calendarToString(this.creationDate));
	}
	/**
	 * Sets the value for the creationDate xsd:dateTime (2010-10-24T12:48:42.361Z).
	 * @param creationDate the creationDate to set 
	 * @throws Exception 
	 */
	public void setCreationDateFromXSDDateTimeString(String creationDate) throws Exception {
		this.creationDate = Conversion.xsdDateTimeToCalendar(creationDate);
	}
	/**
	 * Returns the creator.
	 * @return the creator
	 */
	public FoafAgent getCreator() {
		return creator;
	}
	/**
	 * Returns the author.
	 * @return the author
	 */
	public FoafAgent getAuthor() {
		return author;
	}
	/**
	 * Sets the value for the creator.
	 * @param creator the creator to set
	 */
	public void setCreator(FoafAgent creator) {
		this.creator = creator;
	}
	/**
	 * Sets the value for the author.
	 * @param creator the author to set
	 */
	public void setAuthor(FoafAgent author) {
		this.author = author;
	}
	/**
	 * Returns the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the value for the id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Returns the creationDate.
	 * @return the creationDate
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}
	/**
	 * Sets the value for the creationDate.
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * Returns the label.
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * Sets the value for the label.
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Annotation) {
			Annotation a = (Annotation)obj;		
			/*System.out.println("" + this.body.equals(a.body) + 
				this.creator.getUri().toString().equals(a.creator.getUri().toString()) +  
				this.document.getUri().toString().equals(a.document.getUri().toString()) +
				(this.topic.size() == a.topic.size()) +
				this.topic.containsAll(a.topic));*/
			if ((this.getUri() != null) && (a.getUri() != null) &&
					this.getUri().toString().equals(a.getUri().toString())) {
				return true;
			}
			return (this.body.equals(a.body) && 
				this.creator.getUri().equals(a.creator.getUri()) && 
				this.document.getUri().equals(a.document.getUri()) //&&
				//this.topic.size() == a.topic.size() &&
				//this.context.size() == a.context.size() &&
				//this.directContext.size() == a.directContext.size() &&
				//this.topic.containsAll(a.topic) //&& 
				//this.context.containsAll(a.context) &&
				//this.directContext.containsAll(a.directContext)
			);
		} else {
			//System.out.println("NOT ANNOTATION");
			return (false);
		}
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	@Override
	public String toString() {
		String str = "[\\'" + this.getId() + "\\'" +
			"; \\'" + this.getNodeId() + "\\'" +
			"; \\'" + this.getBody() + "\\'" +
			"; \\'" + this.getTopic() + "\\'" +
			"; \\'" + this.getContext() + "\\'" +
			"; \\'" + this.getComment() + "\\'" +
			"; \\'" + this.getFrequency() + "\\']";
		return (str);
	}
	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the documentID
	 */
	public String getDocumentID() {
		return documentID;
	}
	/**
	 * @param documentID the documentID to set
	 */
	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}
	/**
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}
}
