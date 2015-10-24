package ws.biotea.ld2rdf.rdf.persistence.ao;

import java.net.URI;
import java.util.List;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;

public interface AnnotationDAO {
	/**
	 * Inserts a new annotation.
	 * @param Annotation Annotation to be updated.
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception
	 */
	public URI insertAnnotation(String datasetURL, String baseURL, Annotation annot, String id, String fileOut, boolean empty, boolean blankNode) throws Exception;
	/**
	 * Inserts a list of annotations.
	 * @param baseURL
	 * @param list
	 * @param fileOut
	 * @param empty
	 * @throws Exception
	 */
	public List<Annotation> insertAnnotations(String datasetURL, String baseURL, List<Annotation> list, String fileOut, boolean empty, boolean blankNode) throws Exception;
	/**
	 * Updates an annotation.
	 * @param Annotation Annotation to be updated.
	 * @throws Exception
	 */
	public URI updateAnnotation(String datasetURL, String baseURL, Annotation annot, String uri, String fileOut, boolean empty) throws Exception;
	/**
	 * Deletes an annotation given its id.
	 * @param id Annotation id to be deleted.
	 * @throws Exception
	 */
	public void deleteAnnotation(String baseURL, String id, String uri, String fileOut, boolean empty) throws Exception;
	/**
	 * Returns a vector of annotations given a collection of documents.
	 * @param docs Documents.
	 * @return Vector of annotations.
	 */
}
