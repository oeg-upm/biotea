package ws.biotea.ld2rdf.rdf.model.ao;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Serializable {
	private static final long serialVersionUID = 1L;
	/* Attributes */
	/**
	 * URI
	 */
	private URI nameSpace;
	private URI url;
	private List<URI> seeAlso = new ArrayList<URI>();
	private String umlsType;
	private String comment;
	/**
	 * Body (tag) of this annotation, only one
	 */
	private String name;	
	/**
	 * Returns the nameSpace.
	 * @return the nameSpace
	 */
	public URI getNameSpace() {
		return nameSpace;
	}
	/**
	 * Sets the value for the nameSpace.
	 * @param nameSpace the nameSpace to set
	 */
	public void setNameSpace(URI namespace) {
		this.nameSpace = namespace;
	}
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the value for the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Topic) {
			Topic t = (Topic)obj;
			return (this.nameSpace.equals(t.nameSpace) && this.name.equals(t.name));
		} else {
			return false;
		}
	}
	
	public String toString() {
		return (this.nameSpace.toString());
	}
	/**
	 * @return the url
	 */
	public URI getURL() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setURL(URI url) {
		this.url = url;
	}
	/**
	 * @return the seeAlso
	 */
	public List<URI> getSeeAlso() {
		return seeAlso;
	}
	/**
	 * @param seeAlso the seeAlso to set
	 */
	public void setSeeAlso(List<URI> seeAlso) {
		this.seeAlso = seeAlso;
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
	 * @return the type
	 */
	public String getUmlsType() {
		return umlsType;
	}
	/**
	 * @param type the type to set
	 */
	public void setUmlsType(String umlsType) {
		this.umlsType = umlsType;
	}	
}
