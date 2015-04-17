package au.edu.unsw.soacourse.marketdatautil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    
    
    /* Visualisation filename */
    File visualiseFile = new File(System.getProperty("catalina.home") + File.separator + 
                                  "webapps" + File.separator + 
                                  "ROOT" + File.separator + 
                                  "EventSetDownloads" + File.separator +
                                  parameters.getEventSetId() + ".html");
    
    /* Make previous directories if they don't exist */
    visualiseFile.getParentFile().mkdirs();
    
    /* Try to create new file */
    try {
      visualiseFile.createNewFile();   
      
      /* Generate html (read from csv, write to html) */
      BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
      BufferedWriter bw = new BufferedWriter(new FileWriter(visualiseFile.getAbsoluteFile()));   
      
      /* Write fileanme to html */
      bw.write("<!DOCTYPE html>\r\n");
      bw.write("<body>\r\n");
      bw.write("<b>EVENT FILE:</b><br>" + visualiseFile.getName() + "<br><br><br>\r\n");
      
      bw.write("<table style=\"width:80%\" border=\"1\">");
      
      boolean first = true;
      String line = null;
      while ((line = br.readLine()) != null) {
        bw.write("<tr>\r\n");
        
        /* Write column names */
        if (first) {
          for (String cell : line.split(",")) {
            bw.write("<th>" + cell + "</th>");
          }
          first = false;
          
        /* Write table body */
        } else {   
          for (String cell : line.split(",")) {
            bw.write("<td>" + cell + "</td>");
          }
        }
 
        bw.write("</tr>\r\n");
      }
      
      bw.write("</body>\r\n");
      
      br.close();
      bw.close();
    
    /* Throw fault if file can't be created/written to */
    } catch (IOException e) {           
      String msg = "Cannot create/edit generated html file";
      String code = "IO_ERR";

      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      e.printStackTrace();
      throw new VisualiseMarketDataFaultMsg(msg,fault);
    }
    
    /* Create response */
    VisualiseMarketDataResponse res = objFactory.createVisualiseMarketDataResponse();
    res.setDataURL("http://localhost:8080/EventSetDownloads/" + visualiseFile.getName());
    
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
