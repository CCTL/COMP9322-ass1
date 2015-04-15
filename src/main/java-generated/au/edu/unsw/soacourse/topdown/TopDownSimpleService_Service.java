package au.edu.unsw.soacourse.topdown;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-04-15T17:17:45.257+10:00
 * Generated source version: 3.0.4
 * 
 */
@WebServiceClient(name = "TopDownSimpleService", 
                  wsdlLocation = "file:/Users/AlbertWang/Documents/workspace/COMP9322-ass1/src/main/resources/wsdl/TopDownSimpleService.wsdl",
                  targetNamespace = "http://topdown.soacourse.unsw.edu.au") 
public class TopDownSimpleService_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://topdown.soacourse.unsw.edu.au", "TopDownSimpleService");
    public final static QName TopDownSimpleServiceSOAP = new QName("http://topdown.soacourse.unsw.edu.au", "TopDownSimpleServiceSOAP");
    static {
        URL url = null;
        try {
            url = new URL("file:/Users/AlbertWang/Documents/workspace/COMP9322-ass1/src/main/resources/wsdl/TopDownSimpleService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(TopDownSimpleService_Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/Users/AlbertWang/Documents/workspace/COMP9322-ass1/src/main/resources/wsdl/TopDownSimpleService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public TopDownSimpleService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public TopDownSimpleService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TopDownSimpleService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public TopDownSimpleService_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public TopDownSimpleService_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public TopDownSimpleService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns TopDownSimpleService
     */
    @WebEndpoint(name = "TopDownSimpleServiceSOAP")
    public TopDownSimpleService getTopDownSimpleServiceSOAP() {
        return super.getPort(TopDownSimpleServiceSOAP, TopDownSimpleService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TopDownSimpleService
     */
    @WebEndpoint(name = "TopDownSimpleServiceSOAP")
    public TopDownSimpleService getTopDownSimpleServiceSOAP(WebServiceFeature... features) {
        return super.getPort(TopDownSimpleServiceSOAP, TopDownSimpleService.class, features);
    }

}
