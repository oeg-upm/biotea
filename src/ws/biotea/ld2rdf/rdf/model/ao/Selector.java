/**
 * 
 */
package ws.biotea.ld2rdf.rdf.model.ao;

import java.io.Serializable;
import java.net.URI;

import ws.biotea.ld2rdf.util.Prefix;


/**
 * @author leylajael
 *
 */
public abstract class Selector implements Serializable {
	private static final long serialVersionUID = 1L;
	/* Attributes */
	private String id;
	/**
	 * URI
	 */
	private URI uri;
	private String nodeId;
	private FoafDocument document;
	private String documentId;
	/* OWL Descriptors */
	public final static String SELECTOR_CLASS = Prefix.AO_CORE.getURL() + "Selector";
	public final static String SELECTOR_OP_ON_RESOURCE = Prefix.AO_CORE.getURL() + "onResource";
	
	/**
	 * Constructor
	 * @param uri URI of this Selector
	 * @param document Document pointed by this Selector
	 */
	public Selector(FoafDocument document) {
		this.document = document;
	}
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
	 * Returns the document.
	 * @return the document
	 */
	public FoafDocument getDocument() {
		return document;
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
	protected abstract String getSelector();
	protected abstract void setSelector(String str);
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
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}
