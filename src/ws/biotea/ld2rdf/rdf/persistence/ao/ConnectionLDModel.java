package ws.biotea.ld2rdf.rdf.persistence.ao;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ws.biotea.ld2rdf.util.Prefix;



import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import edu.stanford.smi.protege.exception.OntologyLoadException;

public class ConnectionLDModel {
	//Living Document models
	private Model ldModel;
	//private Model ldOWLModel;
	private String fileName;
	public static Map<String, String> prefixes = Prefix.prefixesMapAll();
	
	/**
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public ConnectionLDModel() {
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws OntologyLoadException 
	 */
	public Model openJenaModel(String inName, boolean empty) throws FileNotFoundException, ClassNotFoundException, OntologyLoadException {
		this.fileName = inName;
		/*this.ldModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		if (empty) {
			this.ldModel.setNsPrefixes(prefixes);
		} else {
			this.ldModel.read(in, null, "RDF/XML-ABBREV");
		}
		return (this.ldModel);*/
		this.ldModel = ModelFactory.createDefaultModel();
		if (empty) {
			this.ldModel.setNsPrefixes(prefixes);
		} else {
			// use the FileManager to find the input file
			InputStream in = FileManager.get().open(inName);
			if (in == null) {
			    throw new IllegalArgumentException("File: " + inName + " not found");
			}
			// read the RDF/XML file
			this.ldModel.read(in, null, "RDF/XML-ABBREV");
		}		
		return (this.ldModel);
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public void closeAndWriteJenaModel() throws FileNotFoundException {	
		FileOutputStream myFile = new FileOutputStream(this.fileName);
		this.ldModel.setNsPrefixes(prefixes);
		this.ldModel.write(myFile, "RDF/XML-ABBREV"); 
		try {
			myFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws OntologyLoadException 
	 */
	/*public JenaOWLModel openOWLModel(String uri, String fileName, boolean empty) throws FileNotFoundException, ClassNotFoundException, OntologyLoadException {
		this.fileName = fileName;
		if (empty) {
			this.ldOWLModel = ProtegeOWL.createJenaOWLModel();
			this.ldModel = this.ldOWLModel.getOntModel();
			this.ldModel.setNsPrefixes(prefixes);
		} else {
			this.ldOWLModel = ProtegeOWL.createJenaOWLModelFromURI(uri);
			this.ldModel = this.ldOWLModel.getOntModel();
		}			
		return this.ldOWLModel;
	}*/
	/**
	 * 
	 * @throws FileNotFoundException
	 */
	/*public void closeAndWriteOWLModel() throws FileNotFoundException {
		FileOutputStream myFile = new FileOutputStream(this.fileName);
		this.ldModel = this.ldOWLModel.getOntModel();		
		this.ldModel.write(myFile, "RDF/XML-ABBREV", null); 
	}*/
	/**
	 * Returns the hypertagModel.
	 * @return the hypertagModel.
	 */
	/*public OntModel getLDJenaModel() {
		return this.ldModel;
	}*/
	/**
	 * @return the hypertagOWLModel
	 */
	public Model getLDJenaModel() {
		return ldModel;
	}
	
}
