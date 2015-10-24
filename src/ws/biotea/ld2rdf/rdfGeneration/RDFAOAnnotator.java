/**
 * 
 */
package ws.biotea.ld2rdf.rdfGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


import org.apache.log4j.Logger;

import ws.biotea.ld2rdf.rdf.model.ao.Annotation;
import ws.biotea.ld2rdf.rdf.model.ao.Topic;
import ws.biotea.ld2rdf.util.ClassesAndProperties;
import ws.biotea.ld2rdf.util.Prefix;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author Leyla Garcia
 *
 */
public abstract class RDFAOAnnotator {
	protected final Logger logger = Logger.getLogger("RDFAOAnnotator");
	protected static String BIBO_ABSTRACT = Prefix.BIBO.getURL() + "abstract";
	protected static String DOCO_PARAGRAPH = Prefix.DOCO.getURL() + "Paragraph";
	protected static String RDF_TYPE = ClassesAndProperties.RDF_TYPE.getURLValue();
	protected static Pattern excludedSections = Pattern.compile("([aA]cknowledgements)|([cC]ompeting[-]interests)|([aA]uthor)(s-|--|-s|)(-contributions)|([aA]bbreviations)"); //we are replacing spaces and other chars such as ' by -
	protected List<Annotation> annotationsTable;

	/**
	 * Annotates an rdf file with whatizit and write annotations to an rdf with AO.
	 * @param rdfInName
	 * @param rdfOutName
	 * @return
	 * @throws Exception
	 */
	public abstract File annotate(String rdfInName, String rdfOutName, List<Annotation> lstAnnotations, String textProperty) throws Exception ;
	/**
	 * 
	 * @param rdfInName
	 * @return
	 * @throws URISyntaxException
	 */
	public List<Annotation> getAnnotationsFromFile(String rdfInName) throws URISyntaxException {
		List<Annotation> annotations = new ArrayList<Annotation>();
		logger.info("===annotateWithAnnotator=== " + rdfInName);
		// create an empty model
		Model model = ModelFactory.createDefaultModel();
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(rdfInName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + rdfInName + " not found");
		}
		// read the RDF/XML file
		model.read(in, null);
		logger.info("===RDF READ=== " + rdfInName);
		
		String queryString = "PREFIX ao:<http://purl.org/ao/core/> " +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX sioc:<http://rdfs.org/sioc/ns#> " +
				"SELECT ?s ?body ?topic ?comment ?num WHERE { " +
				"    ?s rdf:type \"http://purl.org/ao/core/Qualifier\" . " +
				"    ?s ao:hasTopic ?topic . " +
				"    ?s sioc:num_items ?num . " +
				"    ?s ao:body ?body . " +
				"    OPTIONAL {?s rdfs:comment ?comment} " +
				"}";
		
		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		com.hp.hpl.jena.query.ResultSet results = qe.execSelect();		
		
		while (results.hasNext()) {
			try {
				QuerySolution sol = results.next();
				Annotation a = new Annotation();
				a.setUri(new URI(sol.get("s").toString()));
				String node = sol.get("body").toString();
				a.setBody(node.substring(0, node.indexOf("^^")));//.substring(0, node.indexOf("^^"))
				RDFNode rdfNode = sol.get("comment"); 
				if (rdfNode != null) {
					node = rdfNode.toString();
					a.setComment(node.substring(0, node.indexOf("^^")));
				} 				
				node = sol.get("num").toString();
				node = node.substring(0, node.indexOf("@"));
				a.setFrequency(Integer.parseInt(node));
				node = sol.get("topic").toString();
				Topic topic = new Topic();
				topic.setURL(new URI(node));
				int pos = annotations.indexOf(a); 
				if (pos != -1) {
					annotations.get(pos).getTopic().add(topic);
				} else {
					a.getTopic().add(topic);
					annotations.add(a);
				}				
			} catch (Exception e) {continue;}
		}
		qe.close();
		return annotations;
	}
	
	/**
	 * 
	 * @param rdfInName
	 * @param outFileName
	 * @return
	 * @throws IOException
	 */
	public File createBio2RDF(String rdfInName, String outFileName) throws IOException {
		logger.info("===createBio2RDFFromAnnotator=== " + rdfInName);
		// create an empty model
		Model model = ModelFactory.createDefaultModel();
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(rdfInName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + rdfInName + " not found");
		}
		// read the RDF/XML file
		model.read(in, null);
		logger.info("===RDF READ=== " + rdfInName);
		
		String queryString = "PREFIX ao:<http://purl.org/ao/core/> " +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX sioc:<http://rdfs.org/sioc/ns#> " +
				"SELECT distinct ?topic WHERE { " +
				"    ?s rdf:type \"http://purl.org/ao/core/Qualifier\" . " +
				"    ?s ao:hasTopic ?topic" +
				"}";
		
		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		com.hp.hpl.jena.query.ResultSet results = qe.execSelect();		
		logger.info("===SPARQL RESULT READY=== " + rdfInName);
		Model modelTemp, modelAll;
		modelAll = null;
		while (results.hasNext()) {
			try {
				QuerySolution sol = results.next();
				RDFNode rdfNode = sol.get("topic");
				if (rdfNode != null) {
					String node = rdfNode.toString();
					if (node.startsWith("uniprot:") || 
						node.startsWith("go:") || node.startsWith("GO:") ||
						node.startsWith("taxonomy:") || 
						node.startsWith("chebi:")) {
						URL uri1 = new URL("http://bio2rdf.org/rdfxml/" + node);
						URLConnection tc = uri1.openConnection();
						modelTemp = ModelFactory.createDefaultModel();
						modelTemp.read(tc.getInputStream(), RDFS.getURI());
						if (modelAll == null) {
							modelAll = modelTemp;
						} else {
							modelAll = modelAll.union(modelTemp);
						}
					}				
				}
			} catch(Exception e) {}			
		}		
		qe.close();
		FileWriter fw;
		File fo = new File(outFileName);
		fw = new FileWriter(fo);
		if (modelAll != null) {
			modelAll.write(fw, "RDF/XML");
		}
		logger.info("===END RDF READ=== " + rdfInName);
		return (fo);
	}	
}
