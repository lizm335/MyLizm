
package com.gzedu.xlims.common.wcf.org.tempuri;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "APPDataSynchService", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://106.15.138.248/TeachingManagement.Service/APPDataSynchService.svc?wsdl")
public class APPDataSynchService
    extends Service
{

    private final static URL APPDATASYNCHSERVICE_WSDL_LOCATION;
    private final static WebServiceException APPDATASYNCHSERVICE_EXCEPTION;
    private final static QName APPDATASYNCHSERVICE_QNAME = new QName("http://tempuri.org/", "APPDataSynchService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://106.15.138.248/TeachingManagement.Service/APPDataSynchService.svc?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        APPDATASYNCHSERVICE_WSDL_LOCATION = url;
        APPDATASYNCHSERVICE_EXCEPTION = e;
    }

    public APPDataSynchService() {
        super(__getWsdlLocation(), APPDATASYNCHSERVICE_QNAME);
    }

    public APPDataSynchService(WebServiceFeature... features) {
        super(__getWsdlLocation(), APPDATASYNCHSERVICE_QNAME, features);
    }

    public APPDataSynchService(URL wsdlLocation) {
        super(wsdlLocation, APPDATASYNCHSERVICE_QNAME);
    }

    public APPDataSynchService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, APPDATASYNCHSERVICE_QNAME, features);
    }

    public APPDataSynchService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public APPDataSynchService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IAPPDataSynchService
     */
    @WebEndpoint(name = "BasicHttpBinding_IAPPDataSynchService")
    public IAPPDataSynchService getBasicHttpBindingIAPPDataSynchService() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IAPPDataSynchService"), IAPPDataSynchService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IAPPDataSynchService
     */
    @WebEndpoint(name = "BasicHttpBinding_IAPPDataSynchService")
    public IAPPDataSynchService getBasicHttpBindingIAPPDataSynchService(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IAPPDataSynchService"), IAPPDataSynchService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (APPDATASYNCHSERVICE_EXCEPTION!= null) {
            throw APPDATASYNCHSERVICE_EXCEPTION;
        }
        return APPDATASYNCHSERVICE_WSDL_LOCATION;
    }

}