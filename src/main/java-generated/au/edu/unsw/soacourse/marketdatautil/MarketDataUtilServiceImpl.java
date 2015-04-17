package au.edu.unsw.soacourse.marketdatautil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		// http://www.xe.com/currencytables/?from=AUD&date=2014-08-20
		String targetDate = parameters.getTargetDate();
		
		try {
			// Set up url connection
			URL url = new URL("http://www.xe.com/currencytables/?from=AUD&date=" + targetDate);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
			
			// Read from page.
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				// Parse html here.
				System.out.println(line);
			}
			br.close();
		} catch (MalformedURLException e) {
			// SOAP FAULTS
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    /* Event set filename */
	    String fileName = parameters.getEventSetId().substring(0,3) + "_" + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()).toString();
	    File tempFile = new File(System.getProperty("catalina.home") + File.separator + 
	                             "webapps" + File.separator + 
	                             "ROOT" + File.separator + 
	                             "EventSetDownloads" + File.separator +
	                             fileName + ".csv");
	    
	    /* Make previous directories if they don't exist */
	    tempFile.getParentFile().mkdirs();
	    
	    try {
		    /* Create new event set file */
			tempFile.createNewFile();
			
			FileWriter fw = new FileWriter(tempFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			// Write to new file here with converted columns.
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    CurrencyConvertMarketDataResponse res = objFactory.createCurrencyConvertMarketDataResponse();
	    res.setEventSetId(fileName);
	    
		return res;
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
				if (currentLine == 1) {
					response.setEndDate(secInfo[1]);
				}
				currentLine++;
			}
			if (secInfo != null) {
				response.setStartDate(secInfo[1]);
				response.setSec(secInfo[0]);
				response.setCurrencyCode(secInfo[2].substring(0, 3));
				response.setEventSetId(parameters.getEventSetId());
				response.setFileSize((file.length() / 1024) + "K");
				
				// Flips the date around
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfResult = new SimpleDateFormat("dd-MM-yyyy");
				try {
					response.setStartDate(sdfResult.format(sdf.parse(response.getStartDate())));
					response.setEndDate(sdfResult.format(sdf.parse(response.getEndDate())));
				} catch (ParseException e) {
					String msg = "Invalid date format";
					String code= "DATE_ERR";
					  
					ServiceFaultType fault = objFactory.createServiceFaultType();
					fault.setErrcode(code);
					fault.setErrtext(msg);
					  
					e.printStackTrace();
					throw new SummariseMarketDataFaultMsg(msg,fault);
				}
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
