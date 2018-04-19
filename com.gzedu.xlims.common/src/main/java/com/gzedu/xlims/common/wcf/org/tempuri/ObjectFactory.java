
package com.gzedu.xlims.common.wcf.org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tempuri package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DataSynchSynchDATA_QNAME = new QName("http://tempuri.org/", "synchDATA");
    private final static QName _DataSynchFunctionType_QNAME = new QName("http://tempuri.org/", "functionType");
    private final static QName _DataSynchKey_QNAME = new QName("http://tempuri.org/", "key");
    private final static QName _DataSynchOperatingUserName_QNAME = new QName("http://tempuri.org/", "operatingUserName");
    private final static QName _DataSynchResponseDataSynchResult_QNAME = new QName("http://tempuri.org/", "DataSynchResult");
    private final static QName _DataSynchResponseOutMSG_QNAME = new QName("http://tempuri.org/", "outMSG");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tempuri
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataSynch }
     * 
     */
    public DataSynch createDataSynch() {
        return new DataSynch();
    }

    /**
     * Create an instance of {@link DataSynchResponse }
     * 
     */
    public DataSynchResponse createDataSynchResponse() {
        return new DataSynchResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "synchDATA", scope = DataSynch.class)
    public JAXBElement<String> createDataSynchSynchDATA(String value) {
        return new JAXBElement<String>(_DataSynchSynchDATA_QNAME, String.class, DataSynch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "functionType", scope = DataSynch.class)
    public JAXBElement<String> createDataSynchFunctionType(String value) {
        return new JAXBElement<String>(_DataSynchFunctionType_QNAME, String.class, DataSynch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "key", scope = DataSynch.class)
    public JAXBElement<String> createDataSynchKey(String value) {
        return new JAXBElement<String>(_DataSynchKey_QNAME, String.class, DataSynch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "operatingUserName", scope = DataSynch.class)
    public JAXBElement<String> createDataSynchOperatingUserName(String value) {
        return new JAXBElement<String>(_DataSynchOperatingUserName_QNAME, String.class, DataSynch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "DataSynchResult", scope = DataSynchResponse.class)
    public JAXBElement<String> createDataSynchResponseDataSynchResult(String value) {
        return new JAXBElement<String>(_DataSynchResponseDataSynchResult_QNAME, String.class, DataSynchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "outMSG", scope = DataSynchResponse.class)
    public JAXBElement<String> createDataSynchResponseOutMSG(String value) {
        return new JAXBElement<String>(_DataSynchResponseOutMSG_QNAME, String.class, DataSynchResponse.class, value);
    }

}
