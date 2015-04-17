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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		// http://www.xe.com/currencytables/?from=AUD&date=2014-08-20
		String targetDate = parameters.getTargetDate();
		Double conversionRate = null;
		
		try {
			// Set up url connection
			URL url = new URL("http://www.xe.com/currencytables/?from=AUD&date=" + targetDate);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
			
			// Read from page.
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			
			/* Pattern matcher */
		  Pattern pattern = Pattern.compile(parameters.getTargetCurrency() + "</a></td><td>[a-zA-Z\\s]+</td><td class=\"ICTRate\">[0-9\\.]+");
      
		  /* TWD</a></td><td>Taiwan New Dollar</td><td class="ICTRate">24.1739937142 */
		  String matchedStr = "";
			
			while ((line = br.readLine()) != null) {
			  Matcher matcher = pattern.matcher(line);
			  if(matcher.find()){
			    matchedStr = matcher.group();
          break;
        }
			}
			br.close();

		  pattern = Pattern.compile("[0-9\\.]+");
		  Matcher matcher = pattern.matcher(matchedStr);
		  if(matcher.find()){
		    matchedStr = matcher.group();
		  }else{
		    String msg = "Invalid Currency Code";
	      String code = "CURR_ERR";

	      ServiceFaultType fault = objFactory.createServiceFaultType();
	      fault.setErrcode(code);
	      fault.setErrtext(msg);

	      throw new CurrencyConvertMarketDataFaultMsg(msg,fault);
		  }
		  
		  conversionRate = Double.parseDouble(matchedStr);
			
		} catch (Exception e) {
		  String msg = "A generic service fault has occurred";
      String code= "GEN_ERR";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new CurrencyConvertMarketDataFaultMsg(msg,fault);
		}
		
	    /* Event set filename */    
	    File oldFile = new File(System.getProperty("catalina.home") + File.separator + 
                              "webapps" + File.separator + 
                              "ROOT" + File.separator + 
                              "EventSetDownloads" + File.separator +
                              parameters.getEventSetId() + ".csv");
	    
	    String fileName = parameters.getEventSetId().substring(0,3) + "_" + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()).toString();
	    File newFile = new File(System.getProperty("catalina.home") + File.separator + 
	                            "webapps" + File.separator + 
	                            "ROOT" + File.separator + 
	                            "EventSetDownloads" + File.separator +
	                            fileName + ".csv");
	  
	    
	    /* Make previous directories if they don't exist */
	    newFile.getParentFile().mkdirs();
	    
	    try {
		    /* Create new event set file */
  			newFile.createNewFile();
  			
  			BufferedWriter bw = new BufferedWriter(new FileWriter(newFile.getAbsoluteFile()));
  			BufferedReader br = new BufferedReader(new FileReader(oldFile.getAbsoluteFile()));
  			
  			String strTemp = "";
        boolean first = true;
        while (null != (strTemp = br.readLine())) {
          
          /* If first line do nothing */
          if(first){
            bw.write(strTemp);
            bw.write("\r\n");
            first = false;
            
          /* Else remove AUD and convert data */
          }else{
            if(!strTemp.contains("AUD")){
              bw.close();
              br.close();
              
              String msg = "The file already contains converted prices";
              String code= "PRICE_ERR";
              
              ServiceFaultType fault = objFactory.createServiceFaultType();
              fault.setErrcode(code);
              fault.setErrtext(msg);
              
              throw new CurrencyConvertMarketDataFaultMsg(msg,fault);
            }
            
            String[] data = strTemp.split(",");
            bw.write(data[0]); //sec
            bw.write("," + data[1]); //date
            bw.write("," + parameters.getTargetCurrency() + multiplyAndRound(data[2].substring(3),conversionRate)); //open
            bw.write("," + parameters.getTargetCurrency() + multiplyAndRound(data[3].substring(3),conversionRate)); //high
            bw.write("," + parameters.getTargetCurrency() + multiplyAndRound(data[4].substring(3),conversionRate)); //low
            bw.write("," + parameters.getTargetCurrency() + multiplyAndRound(data[5].substring(3),conversionRate)); //close
            bw.write("," + data[6]); //volume
            bw.write("," + parameters.getTargetCurrency() + multiplyAndRound(data[7].substring(3),conversionRate)); //adj close
            bw.write("\r\n");
          }
                
        }
        
        bw.close();
        br.close();
        
	    } catch (CurrencyConvertMarketDataFaultMsg e){
	      throw e;
	    } catch (Exception e) {
	      String msg = "A generic service fault has occurred";
	      String code= "GEN_ERR";
	      
	      ServiceFaultType fault = objFactory.createServiceFaultType();
	      fault.setErrcode(code);
	      fault.setErrtext(msg);
	      
	      throw new CurrencyConvertMarketDataFaultMsg(msg,fault);
	    } 
	    
	    CurrencyConvertMarketDataResponse res = objFactory.createCurrencyConvertMarketDataResponse();
	    res.setEventSetId(fileName);
	    
		return res;
	}

	public String multiplyAndRound(String str, Double d){
	  return new DecimalFormat("#.##").format(Double.parseDouble(str)*d);
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
					  
					throw new SummariseMarketDataFaultMsg(msg,fault);
				}
			}
		} catch (FileNotFoundException e) {
		  String msg = "Invalid eventSetId";
      String code= "EVENT_ERR";
        
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
        
      throw new SummariseMarketDataFaultMsg(msg,fault);
		} catch (IOException e) {
		  String msg = "Error reading file";
      String code= "IO_ERR";
        
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
        
      throw new SummariseMarketDataFaultMsg(msg,fault);
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
