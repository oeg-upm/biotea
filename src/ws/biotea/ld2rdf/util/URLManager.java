package ws.biotea.ld2rdf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class URLManager {
	/**
	 * Returns the file/URL content as a String.
	 * @param urlName URL.
	 * @return HTML code as a string.
	 * @throws IOException
	 */
	public static String reader(String urlName, boolean isUrl) throws IOException {
		BufferedReader in;
		if (isUrl) {
			URL url = new URL(urlName);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		} else {
			in = new BufferedReader(new FileReader(urlName));
		}
		
		String inputLine, output = "";
		while ((inputLine = in.readLine()) != null) {
		    output += inputLine;
		}
		
		in.close();
		return (output);
	}
	
	/**
	 * Performs an XSLT transformation for a given URL and saves it to a file. 
	 * @param urlStrXslt XSLT url.
	 * @param urlStrXml XML url.
	 * @param strResult Result file.
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void transform (String urlStrXslt, String urlStrXml, String strResult) throws TransformerException {
		Source xsltSource = new StreamSource(urlStrXslt);
		Source xmlSource = new StreamSource(urlStrXml);
		File resultFile = new File(strResult);
		Transformer xsltTransformer;
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			//System.out.println(xsltSource + " - " + tf);
			xsltTransformer = tf.newTransformer(xsltSource);
			//System.out.println(tf.getErrorListener() + " - " + xsltTransformer + " - " + tf.newTransformer(xsltSource));
			//System.out.println(urlStrXslt + " - " + urlStrXml + " - " + resultFile);
			Result result = new StreamResult(resultFile);
			//System.out.println(xsltTransformer + " - " + xmlSource + " - " + result);
			xsltTransformer.transform(xmlSource, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-jaxb.generated catch block
			e.printStackTrace();
			throw new TransformerConfigurationException(e.getMessage());
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-jaxb.generated catch block
			e.printStackTrace();
			throw new TransformerFactoryConfigurationError(e.getMessage());
		} catch (TransformerException e) {
			// TODO Auto-jaxb.generated catch block
			e.printStackTrace();
			throw new TransformerException(e.getMessage());
		}
	}
	/**
	 * Performs an XSLT transformation for a given URL and saves it to a file. 
	 * @param urlStrXslt XSLT url.
	 * @param urlStrXml XML url.
	 * @param strResult Result file.
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws SQLException 
	 */
	public static void transform (String urlStrXslt, SQLXML sqlxml, String strResult) throws TransformerFactoryConfigurationError, TransformerException, SQLException {
		Source xsltSource = new StreamSource(urlStrXslt);
		File resultFile = new File(strResult);
		Transformer xsltTransformer = TransformerFactory.newInstance().newTransformer(xsltSource);
		Result result = new StreamResult(resultFile);
		//Source source = sqlxml.getSource(null);
		DOMSource domSource = sqlxml.getSource(DOMSource.class);
		xsltTransformer.transform(domSource, result);
	}
	/**
	 * Writes a string into a given file.
	 * @param str String to be written.
	 * @param fileName File name.
	 * @throws IOException
	 */
	public static void createFile(String str, String fileName) throws IOException {
		PrintWriter pw = new PrintWriter(new File(fileName), "ISO-8859-1");
		pw.write(str);
		pw.close();
	}
	
	public static void main(String[] args) throws Exception {
		//System.out.println(URLManager.reader("http://www.scientifik.info/cgi-bin/search.pl?query=p53%20cancer", true));
		//http://192.168.1.37:8080/LDProject/resources/art501plainxhtml.xsl - http://192.168.1.37:8080/LDProject/papers/S0014-5793(03)00386-7/S0014-5793(03)00386-7.xml - C:\proyectos\LDProject\temp\1388_out.html
		URLManager.transform("http://localhost:8080/LDProject/resources/art501plainxhtml.xsl", "http://localhost:8080/LDProject/papers/S0014-5793(03)00386-7/S0014-5793(03)00386-7.xml", "C:/proyectos/LDProject/temp/1388_out.html");
		System.out.println("FIN");
	}
}
