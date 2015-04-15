
package au.edu.unsw.soacourse.topdown;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the au.edu.unsw.soacourse.topdown package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: au.edu.unsw.soacourse.topdown
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImportMarketDataRequest }
     * 
     */
    public ImportMarketDataRequest createImportMarketDataRequest() {
        return new ImportMarketDataRequest();
    }

    /**
     * Create an instance of {@link ImportMarketDataResponse }
     * 
     */
    public ImportMarketDataResponse createImportMarketDataResponse() {
        return new ImportMarketDataResponse();
    }

    /**
     * Create an instance of {@link DownloadFileRequest }
     * 
     */
    public DownloadFileRequest createDownloadFileRequest() {
        return new DownloadFileRequest();
    }

    /**
     * Create an instance of {@link DownloadFileResponse }
     * 
     */
    public DownloadFileResponse createDownloadFileResponse() {
        return new DownloadFileResponse();
    }

}
