
package au.edu.unsw.soacourse.marketdatautil;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-04-16T19:34:59.150+10:00
 * Generated source version: 3.0.4
 */

@WebFault(name = "visualiseMarketDataFault", targetNamespace = "http://marketdatautil.soacourse.unsw.edu.au")
public class VisualiseMarketDataFaultMsg extends Exception {
    
    private au.edu.unsw.soacourse.marketdatautil.ServiceFaultType visualiseMarketDataFault;

    public VisualiseMarketDataFaultMsg() {
        super();
    }
    
    public VisualiseMarketDataFaultMsg(String message) {
        super(message);
    }
    
    public VisualiseMarketDataFaultMsg(String message, Throwable cause) {
        super(message, cause);
    }

    public VisualiseMarketDataFaultMsg(String message, au.edu.unsw.soacourse.marketdatautil.ServiceFaultType visualiseMarketDataFault) {
        super(message);
        this.visualiseMarketDataFault = visualiseMarketDataFault;
    }

    public VisualiseMarketDataFaultMsg(String message, au.edu.unsw.soacourse.marketdatautil.ServiceFaultType visualiseMarketDataFault, Throwable cause) {
        super(message, cause);
        this.visualiseMarketDataFault = visualiseMarketDataFault;
    }

    public au.edu.unsw.soacourse.marketdatautil.ServiceFaultType getFaultInfo() {
        return this.visualiseMarketDataFault;
    }
}
