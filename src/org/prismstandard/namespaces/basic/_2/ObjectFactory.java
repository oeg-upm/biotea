//
// This file was sciverse.jaxb.generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.28 at 12:10:14 AM BST 
//


package org.prismstandard.namespaces.basic._2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * sciverse.jaxb.generated in the org.prismstandard.namespaces.basic._2 package. 
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

    private final static QName _StartingPage_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "startingPage");
    private final static QName _AggregationType_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "aggregationType");
    private final static QName _PublicationName_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "publicationName");
    private final static QName _EndingPage_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "endingPage");
    private final static QName _Issn_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "issn");
    private final static QName _IssueIdentifier_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "issueIdentifier");
    private final static QName _Volume_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "volume");
    private final static QName _Doi_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "doi");
    private final static QName _CoverDate_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "coverDate");
    private final static QName _Copyright_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "copyright");
    private final static QName _Url_QNAME = new QName("http://prismstandard.org/namespaces/basic/2.0/", "url");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.prismstandard.namespaces.basic._2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Edition }
     * 
     */
    public Edition createEdition() {
        return new Edition();
    }

    /**
     * Create an instance of {@link Number }
     * 
     */
    public Number createNumber() {
        return new Number();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "startingPage")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createStartingPage(String value) {
        return new JAXBElement<String>(_StartingPage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "aggregationType")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAggregationType(String value) {
        return new JAXBElement<String>(_AggregationType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "publicationName")
    public JAXBElement<String> createPublicationName(String value) {
        return new JAXBElement<String>(_PublicationName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "endingPage")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createEndingPage(String value) {
        return new JAXBElement<String>(_EndingPage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "issn")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIssn(String value) {
        return new JAXBElement<String>(_Issn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "issueIdentifier")
    public JAXBElement<String> createIssueIdentifier(String value) {
        return new JAXBElement<String>(_IssueIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "volume")
    public JAXBElement<String> createVolume(String value) {
        return new JAXBElement<String>(_Volume_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "doi")
    public JAXBElement<String> createDoi(String value) {
        return new JAXBElement<String>(_Doi_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "coverDate")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCoverDate(String value) {
        return new JAXBElement<String>(_CoverDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "copyright")
    public JAXBElement<String> createCopyright(String value) {
        return new JAXBElement<String>(_Copyright_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://prismstandard.org/namespaces/basic/2.0/", name = "url")
    public JAXBElement<String> createUrl(String value) {
        return new JAXBElement<String>(_Url_QNAME, String.class, null, value);
    }

}
