package au.edu.unsw.soacourse.topdown;

import javax.jws.WebService;

@WebService(endpointInterface = "au.edu.unsw.soacourse.topdown.TopDownSimpleService")
public class TopDownSimpleServiceImpl implements TopDownSimpleService {
  
  ObjectFactory objFactory = new ObjectFactory();
  
  public ImportMarketDataResponse importMarketData(ImportMarketDataRequest req) throws ImportMarketFaultMsg {

    if (req.getSec().length() != 3) {
      
      String msg = "SEC code should be exactly 3 characters long";
      String code= "ERR_SEC";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new ImportMarketFaultMsg(msg,fault);
    }
    
    StringBuilder sbf = new StringBuilder();
    sbf.append("Security Code: ").append(req.sec).append("\r\n");
    sbf.append("Start date: ").append(req.startDate).append("\r\n");
    sbf.append("End date: ").append(req.endDate).append("\r\n");
    sbf.append("Data source: ").append(req.dataSource).append("\r\n");
    
    ImportMarketDataResponse res = objFactory.createImportMarketDataResponse();
    res.setReturn(sbf.toString());
   
    return res;
  }

  public DownloadFileResponse downloadFile(DownloadFileRequest req) throws DownloadFileFaultMsg {

    /* TODO: check if event set is known */
    if (!req.getEventSetID().equals("abc-abc-111")) {
     
      String msg = "Unknown eventSetId was given";
      String code = "ERR_EVENT";

      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new DownloadFileFaultMsg(msg,fault);
    }
    
    StringBuilder sbf = new StringBuilder();
    sbf.append("EventSet Id: ").append(req.eventSetID).append("\r\n");

    DownloadFileResponse res = objFactory.createDownloadFileResponse();
    res.setReturn(sbf.toString());
    
    return res;
  }
}