/**
 * A Offset Range Text Selector identifies a string in a document through an offset 
            -  an integer indicating the distance (displacement) from the beginning of the document up until a 
            given element or point, within the same document - and a range - an integer indicating the number 
            of characters, starting from the offset, identified by the selector.
 */
package ws.biotea.ld2rdf.rdf.model.ao;

import ws.biotea.ld2rdf.util.ClassesAndProperties;
import ws.biotea.ld2rdf.util.Prefix;


/**
 * @author leylajael
 *
 */
public class OffsetRangeTextSelector extends TextSelector {
	private static final long serialVersionUID = 1L;
	/* Attributes */
	/**
	 * Offset: an integer indicating the distance (displacement) from the beginning of the document up until a 
            given element or point, within a document
	 */
	private long offset;
	/**
	 * Range: an integer indicating the number 
            of characters, starting from the offset, identified by the selector 
	 */
	private long range;
	/**
	 * URI where the annotation occurrs.
	 */
	private String elementURI;
	/* OWL Descriptors */
	public final static String OFFSET_RANGE_TEXT_SELECTOR_CLASS = Prefix.BIOTEA.getURL() + "OffsetRangeTextSelector";
	public final static String OFFSET_RANGE_TEXT_SELECTOR_DP_OFFSET = Prefix.AO_SELECTORS.getURL() + "offset";
	public final static String OFFSET_RANGE_TEXT_SELECTOR_DP_RANGE = Prefix.AO_SELECTORS.getURL() + "range";
	public final static String START_END_ELEMENT_SELECTOR_DP_LOCATOR = Prefix.DCTERMS.getURL() + ClassesAndProperties.DCTERMS_PROP_REFERENCES.getValue();
	
	/**
	 * Constructor.
	 * @param Id URI of this  OffsetRangeTextSelector
	 */
	public OffsetRangeTextSelector(FoafDocument document) {
		super(document);
	}
	/**
	 * Returns the offset.
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}
	/**
	 * Sets the value for the offset.
	 * @param offset the offset to set
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}
	/**
	 * Returns the range.
	 * @return the range
	 */
	public long getRange() {
		return range;
	}
	/**
	 * Sets the value for the range.
	 * @param range the range to set
	 */
	public void setRange(long range) {
		this.range = range;
	}
	/**
	 * @return the elementURI
	 */
	public String getElementURI() {
		return elementURI;
	}
	/**
	 * @param elementURI the elementURI to set
	 */
	public void setElementURI(String sectionURI) {
		this.elementURI = sectionURI;
	}
	public boolean equals(Object o) {
		if (o instanceof OffsetRangeTextSelector) {
			OffsetRangeTextSelector ses = (OffsetRangeTextSelector)o;
			return (this.offset == ses.offset) && (this.range == ses.range) && (this.elementURI.equals(ses.elementURI));
		}
		return false;
	}
	public String toString() {
		return "(" + this.offset + " -> " + this.range + ") - " + this.elementURI;
	}
}
