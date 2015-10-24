/* **
 * This code has been designed/implemented and is maintained by:
 * 
 * Miguel Arregui (miguel.arregui@gmail.com)
 * 
 * Any comments and/or feedback are welcome and encouraged. 
 * 
 * Started on:     9 May 2006.
 * Last reviewed:  7 June 2006.
 */


package whatizitws.core;


import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import com.sun.xml.ws.developer.JAXWSProperties;

import whatizitws.client.Search;
import whatizitws.client.SelectItem;
import whatizitws.client.Whatizit;
import whatizitws.client.WhatizitException_Exception;
import whatizitws.client.Whatizit_Service;


public class SoapClient {
	
  public static void main (String [] args) throws Exception {
  	
  	// Get the WHATIZIT service end point (always like this) 
    Whatizit_Service service = new Whatizit_Service();    
    Whatizit whatizit = service.getPipeline();              
    
    // Mtom and session maintain flag
    BindingProvider bindingProvider = (BindingProvider)whatizit;   
    ((SOAPBinding)bindingProvider.getBinding ()).setMTOMEnabled(true);
    Map <String,Object> requestContext = bindingProvider.getRequestContext();
    requestContext.put(JAXWSProperties.MTOM_THRESHOLOD_VALUE, new Integer(0));
    
    // Check what pipelines are available (their *name* and what they do)
    /*List <SelectItem> pipesStatus = whatizit.getPipelinesStatus();
    Iterator<SelectItem> it = pipesStatus.iterator();
    while (it.hasNext()){
    	SelectItem i = it.next();    
    	System.out.println("PipelineName: " + i.getLabel());
    	System.out.println("Description: " + i.getDescription());    	
    	System.out.println("Available: " + !i.isDisabled());
    	System.out.println("");
    }*/
        
    // Those pipelines which are not disabled can be accessed by *name*
    /*String pipelineName = "whatizitSwissprotPOS";
    String text = "NPY, BRCA1, Brca2 and Ca2 eat carrots";
    boolean convertToHtml = false; // vs. leave it as plain XML
    String result = whatizit.contact(pipelineName, text, convertToHtml);
    System.out.println(result);*/
        
    // Query by pmid
   /* String pmid = "121212"; // The number is a Pubmed accession key
    String xml = whatizit.queryPmid(pipelineName, pmid);
    System.out.println(xml);*/
    
    
    // Query using the whole syntax power of lucene
    String pipelineName = "whatizitSwissprot";
    Search params = new Search();
    params.setPipelineName(pipelineName);
    params.setQuery("P56817");
    params.setLimit(5);
    try {
    	// using text/xml mime type
    	StreamSource resp = (StreamSource)whatizit.search(params);
    	InputStream is = resp.getInputStream();    	
    	byte [] bytes = new byte [1024];
    	int size = 0;
    	while ((size=is.read(bytes, 0, bytes.length)) != -1){ 
    		System.out.write(bytes, 0, size); 
    	}
    	is.close();
    }
    catch (WhatizitException_Exception e){
      System.out.println(e.getFaultInfo().getMessage());
    }
  }
}
// Eof