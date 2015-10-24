/**
 * 
 */
package ws.biotea.ld2rdf.rdfGeneration;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import org.apache.log4j.Logger;
import virtuoso.jena.driver.VirtModel;
import ws.biotea.ld2rdf.util.Prefix;
import ws.biotea.ld2rdf.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Leyla Garcia
 *
 */
public class Bio2RDFGeneration {
	private static Logger logger = Logger.getLogger(NCBOAnnotatorClient.class);
	//private static final String UNIPROT_ENDPOINT = "http://uniprot.bio2rdf.org/sparql";
	private static final String CHEBI_ENDPOINT = "http://chebi.bio2rdf.org/sparql";
	private static final String GO_ENDPOINT = "http://go.bio2rdf.org/sparql";
	private static final String MESH_ENDPOINT = "http://mesh.bio2rdf.org/sparql";
	private static final String OMIM_ENDPOINT = "http://omim.bio2rdf.org/sparql";
	private static final String NCIT_ENDPOINT = "http://ncithesaurus.bio2rdf.org/sparql";
	public void createAndUploadBio2RDFFromAnnotations(String annotationEndPoint, String paperUrlId, String outRDFFileName, String graph) throws URISyntaxException, IOException {
		String outRDFFileNameTemp;
		File file;
		try {
			outRDFFileNameTemp = outRDFFileName.substring(0, outRDFFileName.length()-4) + "_chebi.rdf";
			file = createBio2RDFFromAnnotations(annotationEndPoint, CHEBI_ENDPOINT, paperUrlId, outRDFFileNameTemp, "chebi"); 
			if (file != null) this.uploadRDFToVirtuoso(outRDFFileNameTemp, graph);
		} catch (Exception e) {logger.error(e.getMessage());}
		/*
		try {
			outRDFFileNameTemp = outRDFFileName.substring(0, outRDFFileName.length()-4) + "_go.rdf";
			file = createBio2RDFFromAnnotations(annotationEndPoint, GO_ENDPOINT, paperUrlId, outRDFFileNameTemp, "go");
			if (file != null) this.uploadRDFToVirtuoso(outRDFFileNameTemp, graph);
		} catch (Exception e) {logger.error(e.getMessage());}
		try {
			outRDFFileNameTemp = outRDFFileName.substring(0, outRDFFileName.length()-4) + "_mesh.rdf";
			file = createBio2RDFFromAnnotations(annotationEndPoint, MESH_ENDPOINT, paperUrlId, outRDFFileNameTemp, "mesh");
			if (file != null) this.uploadRDFToVirtuoso(outRDFFileNameTemp, graph);
		} catch (Exception e) {logger.error(e.getMessage()); e.printStackTrace();}
		try {
			outRDFFileNameTemp = outRDFFileName.substring(0, outRDFFileName.length()-4) + "_omim.rdf";
			file = createBio2RDFFromAnnotations(annotationEndPoint, OMIM_ENDPOINT, paperUrlId, outRDFFileNameTemp, "omim");
			if (file != null) this.uploadRDFToVirtuoso(outRDFFileNameTemp, graph);
		} catch (Exception e) {logger.error(e.getMessage());}
		try {
			outRDFFileNameTemp = outRDFFileName.substring(0, outRDFFileName.length()-4) + "_ncit.rdf";
			file = createBio2RDFFromAnnotations(annotationEndPoint, NCIT_ENDPOINT, paperUrlId, outRDFFileNameTemp, "ncithesaurus");
			if (file != null) this.uploadRDFToVirtuoso(outRDFFileNameTemp, graph);
		} catch (Exception e) {logger.error(e.getMessage());}
		*/
	}
	private void uploadRDFToVirtuoso(String outRDFFileNameTemp, String graph) {
		String conn_str = Config.getVirtuosoURL();
		//VirtGraph graph = new VirtGraph ("Example3", "jdbc:virtuoso://localhost:1111", "dba", "dba");
		try {
			logger.info("==Uploading== - " + outRDFFileNameTemp);
			
			Model md = VirtModel.openDatabaseModel(graph, conn_str, Config.getVirtuosoUser(), Config.getVirtuosoPasswd());
			md.read(new FileInputStream(outRDFFileNameTemp), "http://www.biotea.ws/");
			logger.info("==Uploaded== - " + outRDFFileNameTemp);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("An error has occurred while uploading the RDF (" + outRDFFileNameTemp + "): " + e.getMessage());
		}
	}
	private File createBio2RDFFromAnnotations(String annotationEndPoint, String bio2rdfEndpoint, String paperUrlId, String outRDFFileName, String prefix) throws URISyntaxException, IOException {
		logger.info("===Create Bio2RDF " + prefix + " ===");
		//retrieve topics
		String prefixURL = Prefix.getByNS(prefix).getURL();
		String queryString = Prefix.prefixesBio2RDF() +
			" SELECT distinct ?topic " +
			" WHERE { " +
			"  ?s a ao:Qualifier . " +
			"  ?s ao:hasTopic ?topic . " +
			"  ?s ao:annotatesResource <" + paperUrlId + ">" + 
			//"  FILTER (regex(str(?topic), '^uniprot|chebi|go|omim|mesh|ncithesaurus', 'i')) " +
			"  FILTER (regex(str(?topic), '^" + prefixURL + "', 'i')) " +
			"}";
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.sparqlService(annotationEndPoint, query) ;
		ResultSet result = qexec.execSelect();
		String filter = "FILTER(";	
		if (!result.hasNext()) {
			//System.out.println("No chebi terms");
			return null;
		}
		//System.out.println("TOTAL: " + result.getRowNumber());
		while (result.hasNext()) {
			QuerySolution qs = result.next();
			String topic = Prefix.convertToNSAndTerm(qs.getResource("topic").getURI());
			filter += "(?s = <http://bio2rdf.org/" + topic + ">) || ";
		}
		filter += "(?s = <http://bio2rdf.org/uniprot:XYZ>) )";
		logger.info("Annotations retrieved and filter built");
		//retrieve bio2rdf
		queryString = "CONSTRUCT {?s ?p ?o } " +
			"where { ?s ?p ?o . " + filter + " }";		
		System.out.println(queryString);		
		//JENA construct
		query = QueryFactory.create(queryString) ;
		qexec = QueryExecutionFactory.sparqlService(bio2rdfEndpoint, query) ;
		//System.out.println("to build");
		Model resultModel = qexec.execConstruct() ;
		//System.out.println("built");
		qexec.close();	
		logger.info("Bio2RDF built");
		FileOutputStream myFile = new FileOutputStream(outRDFFileName);
		resultModel.setNsPrefixes(Prefix.prefixesMapAll());
		resultModel.write(myFile, "RDF/XML-ABBREV");
		try { myFile.close(); } catch (Exception ee ) {};
		return (new File(outRDFFileName));
		//Retrieve data from URL
//		String bio2rdfURL = "http://uniprot.bio2rdf.org/sparql?default-graph-uri=&" +
//			"query=" + queryString + 
//			"&format=application%2Frdf%2Bxml&debug=on&timeout=";
//		String rdf = URLManager.reader(bio2rdfURL, true);
//			PrintStream ps = new PrintStream(new FileOutputStream(outRDFFileName));
//			ps.print(rdf);

//		URL queryURL;
//		URLConnection urlConn;
//		String msg = "default-graph-uri=&" + "query=" + URLEncoder.encode(queryString, "UTF-8") + 
//			"&format=" + URLEncoder.encode("application/rdf+xml", "UTF-8") + "&debug=on&timeout=";
//		//msg = URLEncoder.encode(msg, "UTF-8");
//		byte[] msgAsBytes = msg.getBytes();
//
//		try {
//			queryURL = new URL("http://uniprot.bio2rdf.org/sparql");//http://uniprot.bio2rdf.org/sparql http://72.167.51.20:8890/sparql
//			urlConn = queryURL.openConnection();
//			((java.net.HttpURLConnection) urlConn).setRequestMethod("POST");
//
//			urlConn.setDoOutput(true);
//			urlConn.setDoInput(true);
//			urlConn.setUseCaches(false);
//
//			OutputStream oStream = urlConn.getOutputStream();
//			oStream.write(msgAsBytes);
//			oStream.flush();
//			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//
//			String aLine = "";
//			int i = 0;
//			while ((aLine = in.readLine()) != null) {
//				System.out.println(aLine);
//				i++;
//				if (i==20) break;
//			}
//			in.close();
//			oStream.close();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//		// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			
	}
}
