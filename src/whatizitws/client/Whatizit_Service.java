
package whatizitws.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

import ws.biotea.ld2rdf.util.Config;




/**
 * This class was jaxb.generated by the JAXWS SI.
 * JAX-WS RI 2.0_01-b59-fcs
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "whatizit", targetNamespace = "http://www.ebi.ac.uk/webservices/whatizit/ws", wsdlLocation = "whatizit.wsdl")
public class Whatizit_Service
    extends Service
{

    private final static URL WHATIZIT_WSDL_LOCATION;

    static {
        URL url = null;
        try {
        	File file = new File(Config.getWSDLLocation() + "whatizit.wsdl");
            url = file.toURL(); //new URL("file:/" + Config.getWebInfDir() + "whatizit.wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        WHATIZIT_WSDL_LOCATION = url;
    }

    public Whatizit_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Whatizit_Service() {
        super(WHATIZIT_WSDL_LOCATION, new QName("http://www.ebi.ac.uk/webservices/whatizit/ws", "whatizit"));
    }

    /**
     * 
     * @return
     *     returns Whatizit
     */
    @WebEndpoint(name = "pipeline")
    public Whatizit getPipeline() {
    	QName name = new QName("http://www.ebi.ac.uk/webservices/whatizit/ws", "pipeline");
        return (Whatizit)super.getPort(new QName("http://www.ebi.ac.uk/webservices/whatizit/ws", "pipeline"), Whatizit.class);
    }

}
