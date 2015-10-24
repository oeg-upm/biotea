package ws.biotea.ld2rdf.util;

public enum ClassesAndProperties {
	FOAF_PERSON(Prefix.FOAF.getNS(), Prefix.FOAF.getURL(), "Person"),
	FOAF_AGENT(Prefix.FOAF.getNS(), Prefix.FOAF.getURL(), "Agent"), 
	RDFS_PROP_COMMENT(Prefix.RDFS.getNS(), Prefix.RDFS.getURL(), "comment"),
	RDF_PROP_VALUE(Prefix.RDF.getNS(), Prefix.RDF.getURL(), "value"),
	RDF_TYPE(Prefix.RDF.getNS(), Prefix.RDF.getURL(), "type"),
	RDFS_RESOURCE(Prefix.RDFS.getNS(), Prefix.RDFS.getURL(), "resource"),
	CNT_PROP_CONTENT(Prefix.CNT.getNS(), Prefix.CNT.getURL(), "chars"),
	UMLS_HAS_STY(ConfigPrefix.getNS("UMLS"), ConfigPrefix.getURL("UMLS"), "hasSTY"),
	UMLS_CUI(ConfigPrefix.getNS("UMLS"), ConfigPrefix.getURL("UMLS"), "cui"),
	UMLS_TUI(ConfigPrefix.getNS("UMLS"), ConfigPrefix.getURL("UMLS"), "tui"),
	UMLS_STY(ConfigPrefix.getNS("UMLS"), ConfigPrefix.getURL("UMLS"), "sty"),
	DCTERMS_PROP_REFERENCES(Prefix.DCTERMS.getNS(), Prefix.DCTERMS.getURL(), "references");

	String value;
	String ns;
	String url;
	private ClassesAndProperties(String ns, String url, String value) {
		this.value = value;
		this.ns = ns;
		this.url = url;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}	

	public String getNSValue() {
		return this.ns + ":" + this.value;
	}
	public String getURLValue() {
		return this.url + this.value;
	}
}
