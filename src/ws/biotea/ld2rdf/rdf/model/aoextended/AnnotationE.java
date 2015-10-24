/**
 * 
 */
package ws.biotea.ld2rdf.rdf.model.aoextended;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;

/**
 * @author leylajael
 *
 */
public class AnnotationE extends Annotation {
	private static final long serialVersionUID = 1L;
	private int frequency;

	public AnnotationE() {
		super();
	}

	/**
	 * Returns the frequency.
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Sets the value for the frequency.
	 * @param frequence the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("[\\'" + this.getId() + "\\', \\'" + this.getBody() + "\\', \\'" + this.getFrequency() + "\\']");
	}
}
