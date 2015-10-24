/**
 * 
 */
package whatizitws.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.ChunkedInputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpClientWhatizitServlet {
	private static String output;

	public static void main(String[] args) throws Exception {
		System.out.println("inicio");
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.socket.timeout", new Integer(20));
		System.out.println("HttpClient created");
		
		output = "C:/proyectos/LDProject/temp/1388_whatizitEBIMed_out_servlet.xml";//args[0];
		InputStreamRequestEntity input = new InputStreamRequestEntity(new FileInputStream("C:/proyectos/LDProject/temp/1388_whatizitEBIMed.xml")); 
		System.out.println("Outpur and Input");
		
		PostMethod httppost = new PostMethod(
				"http://www.ebi.ac.uk/webservices/whatizit/pipe");
		System.out.println("postMethod");
		
		httppost.setRequestEntity(input);
		System.out.println("request Entity");
		//httppost.setRequestEntity(new InputStreamRequestEntity(System.in));
		try {
			client.executeMethod(httppost);
			System.out.println("status: " + httppost.getStatusCode());
			if (httppost.getStatusCode() == HttpStatus.SC_OK) {
				InputStream chunkedResponseData = new ChunkedInputStream(
						httppost.getResponseBodyAsStream());
				convertStreamToString(chunkedResponseData);
				System.out.println("converted");
			} else {
				System.out.println("Unexpected failure: "
						+ httppost.getStatusLine().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			httppost.releaseConnection();
		}

	}

	public static void convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			String line;
			BufferedReader reader = null;
			BufferedWriter bufferedWriter = null;

			try {
				reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				bufferedWriter = new BufferedWriter(new FileWriter(output));
				while ((line = reader.readLine()) != null) {
					bufferedWriter.write(line);
					bufferedWriter.write("\n");
				}
			} finally {
				is.close();
				if (reader != null)
					reader.close();
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}

			}
		}
	}
}
