/**
 * 
 */
package ws.biotea.ld2rdf.rdf.model.ao;

import ws.biotea.ld2rdf.util.Prefix;


/**
 * @author leylajael
 *
 */
public abstract class TextSelector extends Selector {
	private static final long serialVersionUID = 1L;
	/* Attributes */
	/**
	 * Annotated text
	 */
	private String text;
	/* OWL Descriptors */
	public final static String TEXT_SELECTOR_CLASS = Prefix.AO_SELECTORS.getURL() + "TextSelector";
	public final static String TEXT_SELECTOR_DP_EXACT = Prefix.AO_SELECTORS.getURL() + "exact";
	/**
	 * Constructor.
	 * @param Id URI of this TextSelector
	 */
	public TextSelector(FoafDocument document) {
		super(document);
	}
	/**
	 * Returns the text.
	 * @return the text
	 */
	public String getSelector() {
		return text;
	}
	/**
	 * Sets the value for the text.
	 * @param text the text to set
	 */
	public void setSelector(String text) {
		this.text = text;
	}
}