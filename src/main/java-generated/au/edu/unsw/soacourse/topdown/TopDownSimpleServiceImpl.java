package au.edu.unsw.soacourse.topdown;

import javax.jws.WebService;

@WebService(endpointInterface = "au.edu.unsw.soacourse.topdown.TopDownSimpleService")
public class TopDownSimpleServiceImpl implements TopDownSimpleService {
  
  ObjectFactory objFactory;
  
  public ImportMarketDataResponse importMarketData(ImportMarketDataRequest req) {

    ImportMarketDataResponse res = objFactory.createImportMarketDataResponse();
    
    StringBuilder sbf = new StringBuilder();
    sbf.append("Security Code: ").append(req.sec).append("\r\n");
    sbf.append("Start date: ").append(req.startDate).append("\r\n");
    sbf.append("End date: ").append(req.endDate).append("\r\n");
    sbf.append("Data source: ").append(req.dataSource).append("\r\n");
    
    res.setReturn(sbf.toString());
    
    return res;
  }

  public DownloadFileResponse downloadFile(DownloadFileRequest req) {
      
    DownloadFileResponse res = objFactory.createDownloadFileResponse();

    StringBuilder sbf = new StringBuilder();
    sbf.append("EventSet Id: ").append(req.eventSetID).append("\r\n");
    
    res.setReturn(sbf.toString());
    
    return res;
  }
}