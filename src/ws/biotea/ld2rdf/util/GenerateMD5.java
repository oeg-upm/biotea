/**
 * 
 */
package ws.biotea.ld2rdf.util;

import java.io.UnsupportedEncodingException;

import com.twmacinta.util.MD5;

public class GenerateMD5 {
	private MD5 md5;
	private static GenerateMD5 instance = new GenerateMD5();

	private GenerateMD5() {
		md5 = new MD5();
	}

	public static GenerateMD5 getInstance() {
		return instance;
	}

	public String getMD5Hash(String sequenceString) {
		try{
			synchronized (md5) {//do we really need thread safe here?
				md5.Init(); 
				md5.Update(sequenceString, "utf8");
				return md5.asHex();
			}
		}catch (UnsupportedEncodingException uce){
			return ("" + sequenceString.hashCode());
		}
	}
}
