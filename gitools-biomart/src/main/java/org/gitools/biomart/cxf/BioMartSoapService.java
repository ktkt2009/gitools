
package org.gitools.biomart.cxf;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 2.1
 * Fri Mar 12 11:45:46 CET 2010
 * Generated source version: 2.1
 * 
 */

@WebServiceClient(name = "BioMartSoapService", targetNamespace = "http://www.biomart.org:80/MartServiceSoap", wsdlLocation = "file:/home/xavier/NetBeansProjects/GiTools/gitools-biomart/src/main/resources/wsdl/mart.wsdl")
public class BioMartSoapService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://www.biomart.org:80/MartServiceSoap", "BioMartSoapService");
    public final static QName BioMartSoapPort = new QName("http://www.biomart.org:80/MartServiceSoap", "BioMartSoapPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/xavier/NetBeansProjects/GiTools/gitools-biomart/src/main/resources/wsdl/mart.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/home/xavier/NetBeansProjects/GiTools/gitools-biomart/src/main/resources/wsdl/mart.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public BioMartSoapService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BioMartSoapService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BioMartSoapService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns MartServiceSoap
     */
    @WebEndpoint(name = "BioMartSoapPort")
    public MartServiceSoap getBioMartSoapPort() {
        return super.getPort(BioMartSoapPort, MartServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MartServiceSoap
     */
    @WebEndpoint(name = "BioMartSoapPort")
    public MartServiceSoap getBioMartSoapPort(WebServiceFeature... features) {
        return super.getPort(BioMartSoapPort, MartServiceSoap.class, features);
    }

}
