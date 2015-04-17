package au.edu.unsw.soacourse.marketdatautil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MarketDataUtilServiceImpl implements MarketDataUtilService {

  ObjectFactory objFactory = new ObjectFactory();
  
	@Override
	public VisualiseMarketDataResponse visualiseMarketData(
			VisualiseMarketDataRequest parameters)
			throws VisualiseMarketDataFaultMsg {
	   /* Check if event file exists */   
    File f = new File(System.getProperty("catalina.home") + File.separator + 
                      "webapps" + File.separator + 
                      "ROOT" + File.separator + 
                      "EventSetDownloads" + File.separator +
                      parameters.getEventSetId() + ".csv");
    
    /* Throw fault if file doesn't exist */ 
    if (!f.exists()) { 
      String msg = "Unknown eventSetId was given";
      String code = "ERR_EVENT";

      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new VisualiseMarketDataFaultMsg(msg,fault);
    }
    
    /* Create response */
    VisualiseMarketDataResponse res = objFactory.createVisualiseMarketDataResponse();
    res.setDataURL("http://localhost:8080/EventSetDownloads/" + f.getName());
    
    return res;
	}

	@Override
	public CurrencyConvertMarketDataResponse currencyConvertMarketData(
			CurrencyConvertMarketDataRequest parameters)
			throws CurrencyConvertMarketDataFaultMsg {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SummariseMarketDataResponse summariseMarketData(
			SummariseMarketDataRequest parameters)
			throws SummariseMarketDataFaultMsg {
		String filePath = System.getProperty("catalina.home") + File.separator + 
				"webapps" + File.separator + 
                "ROOT" + File.separator + 
                "EventSetDownloads" + File.separator +
                parameters.getEventSetId() + ".csv";
		BufferedReader br = null;
		String line = "";
		File file = new File(filePath);
		
		SummariseMarketDataResponse response = objFactory.createSummariseMarketDataResponse();
		try {
			br = new BufferedReader(new FileReader(filePath));
			int currentLine = 0;
			String[] secInfo = null;
			while ((line = br.readLine()) != null) {
				secInfo = line.split(",");
				if (currentLine == 0) {
					response.setEndDate(secInfo[1]);
				}
				currentLine++;
			}
			if (secInfo != null) {
				response.setStartDate(secInfo[1]);
				response.setSec(secInfo[0]);
				response.setCurrencyCode(secInfo[2].substring(0, 2));
				response.setEventSetId(parameters.getEventSetId());
				response.setFileSize((file.length() / 1024) + "K");
			}
		} catch (FileNotFoundException e) {
			throw new SummariseMarketDataFaultMsg("EventSetID is invalid");
		} catch (IOException e) {
			throw new SummariseMarketDataFaultMsg("Error reading file");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// Client doesn't care if we can't close br.
				}
			}
		}
		
		return response;
	}

}
