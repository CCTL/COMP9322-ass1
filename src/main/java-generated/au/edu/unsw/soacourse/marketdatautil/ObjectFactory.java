
package au.edu.unsw.soacourse.marketdatautil;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the au.edu.unsw.soacourse.marketdatautil package. 
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

    private final static QName _SummariseMarketDataFault_QNAME = new QName("http://marketdatautil.soacourse.unsw.edu.au", "summariseMarketDataFault");
    private final static QName _CurrencyConvertMarketDataFault_QNAME = new QName("http://marketdatautil.soacourse.unsw.edu.au", "currencyConvertMarketDataFault");
    private final static QName _VisualiseMarketDataFault_QNAME = new QName("http://marketdatautil.soacourse.unsw.edu.au", "visualiseMarketDataFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: au.edu.unsw.soacourse.marketdatautil
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SummariseMarketDataRequest }
     * 
     */
    public SummariseMarketDataRequest createSummariseMarketDataRequest() {
        return new SummariseMarketDataRequest();
    }

    /**
     * Create an instance of {@link SummariseMarketDataResponse }
     * 
     */
    public SummariseMarketDataResponse createSummariseMarketDataResponse() {
        return new SummariseMarketDataResponse();
    }

    /**
     * Create an instance of {@link CurrencyConvertMarketDataRequest }
     * 
     */
    public CurrencyConvertMarketDataRequest createCurrencyConvertMarketDataRequest() {
        return new CurrencyConvertMarketDataRequest();
    }

    /**
     * Create an instance of {@link CurrencyConvertMarketDataResponse }
     * 
     */
    public CurrencyConvertMarketDataResponse createCurrencyConvertMarketDataResponse() {
        return new CurrencyConvertMarketDataResponse();
    }

    /**
     * Create an instance of {@link VisualiseMarketDataRequest }
     * 
     */
    public VisualiseMarketDataRequest createVisualiseMarketDataRequest() {
        return new VisualiseMarketDataRequest();
    }

    /**
     * Create an instance of {@link VisualiseMarketDataResponse }
     * 
     */
    public VisualiseMarketDataResponse createVisualiseMarketDataResponse() {
        return new VisualiseMarketDataResponse();
    }

    /**
     * Create an instance of {@link ServiceFaultType }
     * 
     */
    public ServiceFaultType createServiceFaultType() {
        return new ServiceFaultType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://marketdatautil.soacourse.unsw.edu.au", name = "summariseMarketDataFault")
    public JAXBElement<ServiceFaultType> createSummariseMarketDataFault(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_SummariseMarketDataFault_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://marketdatautil.soacourse.unsw.edu.au", name = "currencyConvertMarketDataFault")
    public JAXBElement<ServiceFaultType> createCurrencyConvertMarketDataFault(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_CurrencyConvertMarketDataFault_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://marketdatautil.soacourse.unsw.edu.au", name = "visualiseMarketDataFault")
    public JAXBElement<ServiceFaultType> createVisualiseMarketDataFault(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_VisualiseMarketDataFault_QNAME, ServiceFaultType.class, null, value);
    }

}
