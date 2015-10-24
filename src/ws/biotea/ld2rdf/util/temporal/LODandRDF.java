package ws.biotea.ld2rdf.util.temporal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerException;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import ws.biotea.ld2rdf.rdf.model.aoextended.AnnotationE;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

public class LODandRDF {
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws SQLException 
	 * @throws TransformerException 
	 */
	public static void main(String[] args) throws JDOMException, IOException, SQLException, TransformerException {
		Hashtable<String,AnnotationE> goHash = new Hashtable<String,AnnotationE>();
		Hashtable<String,AnnotationE> speciesHash = new Hashtable<String,AnnotationE>();
		Hashtable<String,AnnotationE> uniprotHash = new Hashtable<String,AnnotationE>();
		
		File dir = new File("E:/workspace/LDProject/temp");
	    System.out.println(dir.length());
		for (File paper:dir.listFiles()) {
			try {
				String base = paper.getName();
				String ext = base.substring(base.length()-4);
				base = base.substring(0, base.length()-4);
				if (ext.equals(".xml")) {
					String xmlName = "E:/workspace/LDProject/temp/" + base + ".xml";
					String rdfName = "E:/workspace/LDProject/temp/" + base + ".rdf";
					System.out.println("INICIO " + base);
					fill(xmlName, goHash, speciesHash, uniprotHash, rdfName);
					System.out.println("FIN");
				}
			} catch(Exception e) {}
		}
		/*String xmlName = "E:/workspace/LDProject/temp/S0014-5793(04)00079-1_whatizitEBIMed_out.xml";
		String rdfName = "E:/workspace/LDProject/temp/S0014-5793(04)00079-1_whatizitEBIMed_rdf.rdf";
		//este método llena los vectores
		System.out.println("INICIO");
		fill(xmlName, goHash, speciesHash, uniprotHash, rdfName);
		System.out.println("FIN");*/
	}
	private static void generateRDF(Hashtable<String,AnnotationE> goHash, Hashtable<String,AnnotationE> speciesHash, Hashtable<String,AnnotationE> uniprotHash, String rdfName) throws IOException {
		//http://bio2rdf.org/rdfxml/go:0043037 -- GO:0016021
		//http://bio2rdf.org/rdfxml/taxon:9606 -- 29295
		//http://bio2rdf.org/rdfxml/uniprot:P26838 -- O93740,P33969,P33971,P33972
		
		Model modelTemp, model;
		model = null;
		Enumeration<String> keys = goHash.keys();
		//System.out.println("GO: " + goHash.size());
	    while( keys.hasMoreElements() ){
	    	String myKey = keys.nextElement();
	    	String[] myIds = myKey.split(",");
	    	for (int j = 0; j < myIds.length; j++) {
				String term = myIds[j];
				URL uri1 = new URL("http://bio2rdf.org/rdfxml/" + term.trim());
				URLConnection tc = uri1.openConnection();
				modelTemp = ModelFactory.createDefaultModel();
				modelTemp.read(tc.getInputStream(), RDFS.getURI());
				if (model == null) {
					model = modelTemp;
				} else {
					model = model.union(modelTemp);
				}
				//model = model == null ? modelTemp : model.union(modelTemp);
			}
	    }
	    keys = speciesHash.keys();
	    System.out.println("SPECIES: " + speciesHash.size());
	    while( keys.hasMoreElements() ){
	    	String myKey = keys.nextElement();
	    	String[] myIds = myKey.split(",");
	    	for (int i = 0; i < myIds.length; i++) {
				String term = myIds[i];
				URL uri1 = new URL("http://bio2rdf.org/rdfxml/taxon:" + term.trim());
				URLConnection tc = uri1.openConnection();
				modelTemp = ModelFactory.createDefaultModel();
				modelTemp.read(tc.getInputStream(), RDFS.getURI());
				model = model == null ? modelTemp : model.union(modelTemp);
			}
	    }
	    keys = uniprotHash.keys();
	    //System.out.println("UNIPROT: " + uniprotHash.size());
	    while( keys.hasMoreElements() ){
	    	String myKey = keys.nextElement();
	    	String[] myIds = myKey.split(",");
	    	//System.out.println(myIds.length);
	    	for (int i = 0; i < myIds.length; i++) {
				String term = myIds[i];
				URL uri1 = new URL("http://bio2rdf.org/rdfxml/uniprot:" + term.trim());
				URLConnection tc = uri1.openConnection();
				modelTemp = ModelFactory.createDefaultModel();
				modelTemp.read(tc.getInputStream(), RDFS.getURI());
				model = model == null ? modelTemp : model.union(modelTemp);
			}
	    }
		FileWriter fw;
		File fo = new File(rdfName);
		fw = new FileWriter(fo);
		model.write(fw, "RDF/XML");
		//model.write(System.out, "RDF/XML");
	}
	public static void fill(String url, Hashtable<String,AnnotationE> goHash, Hashtable<String,AnnotationE> speciesHash, Hashtable<String,AnnotationE> uniprotHash, String rdfName ) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		   
		Document doc = builder.build(url);
		Element root = doc.getRootElement();
		fillVectors(root, goHash, speciesHash, uniprotHash);
		/*
		convertToVector(goHash, goVect);
		convertToVector(speciesHash, speciesVect);
		convertToVector(uniprotHash, uniprotVect);
		*/
		//Generate general rdf
		if ((rdfName != null) && ((new File(rdfName).exists() == false))) {
			generateRDF(goHash, speciesHash, uniprotHash, rdfName);
		}
	}
	
	 @SuppressWarnings("unchecked")
	private static void fillVectors(Element current, Hashtable<String,AnnotationE> goHash, Hashtable<String,AnnotationE> speciesHash, Hashtable<String,AnnotationE> uniprotHash) {
		   
		    if(current.getName().equalsIgnoreCase("go")){
		    	String myId = current.getAttributeValue("ids"); //GO:0016021
			    if(goHash.get(myId)!= null){
			    	AnnotationE a = goHash.get(myId);
			    	a.setFrequency(a.getFrequency() + 1);
			    }
			    else{
			    	AnnotationE a = new AnnotationE();
			    	a.setLabel(myId); //GO:0016021
			    	a.setBody(current.getText());//description, tag text
			    	a.setFrequency(1);
			    	goHash.put(myId, a);
			    }
		    }
		    if(current.getName().equalsIgnoreCase("species")){
		    	String myId = current.getAttributeValue("ids"); //29295
		    	if(speciesHash.get(myId)!= null){
			    	AnnotationE a = speciesHash.get(myId);
			    	a.setFrequency(a.getFrequency() + 1);
			    }
			    else{
			    	AnnotationE a = new AnnotationE();
			    	a.setLabel(myId); //29295
			    	a.setBody(current.getText());//description, tag text
			    	a.setFrequency(1);
			    	speciesHash.put(myId, a);
			    }
		    }
		    if(current.getName().equalsIgnoreCase("uniprot")){
		    	String myId = current.getAttributeValue("ids"); //O93740,P33969,P33971,P33972
		    	if(uniprotHash.get(myId)!= null){
			    	AnnotationE a = uniprotHash.get(myId);
			    	a.setFrequency(a.getFrequency() + 1);
			    }
			    else{
			    	AnnotationE a = new AnnotationE();
			    	a.setLabel(myId); //O93740,P33969,P33971,P33972
			    	a.setBody(current.getText());//description, tag text
			    	a.setFrequency(1);
			    	uniprotHash.put(myId, a);
			    }		
			}
		    List children = current.getChildren();
		    Iterator iterator = children.iterator();
		    while (iterator.hasNext()) {
		      Element child = (Element) iterator.next();
		      fillVectors(child, goHash, speciesHash, uniprotHash);
		    }
	 }

		/*private static void convertToVector(Hashtable<String, String[]> myHash, Vector<Annotation> myVector ){			
		    Enumeration<String> keys = myHash.keys();
		    Enumeration<String[]> values = myHash.elements();
		    while( keys.hasMoreElements() ){
		    	Annotation a = new Annotation();
		    	a.setAnnotation(keys.nextElement().toString());
		    	String[] obj = values.nextElement();
		    	int cont = (int)Integer.parseInt(obj[0]);
		    	a.setDescription(obj[1]);
		    	a.setFrequence(cont); 
		    	myVector.add(a);
		    }
		}*/

}
