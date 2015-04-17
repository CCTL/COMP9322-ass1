package au.edu.unsw.soacourse.topdown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

@WebService(endpointInterface = "au.edu.unsw.soacourse.topdown.TopDownSimpleService")
public class TopDownSimpleServiceImpl implements TopDownSimpleService {
  
  ObjectFactory objFactory = new ObjectFactory();
  
  public ImportMarketDataResponse importMarketData(ImportMarketDataRequest req) throws ImportMarketFaultMsg {
    
    /* Throw fault if not exactly 3 alphabetic chars */
    if (!req.getSec().matches("[a-zA-Z]{3}")) {
      String msg = "SEC code should be exactly 3 alphabetic characters long";
      String code= "SEC_ERR";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new ImportMarketFaultMsg(msg,fault);
    }
    
    Date parsedStartDate = null;
    Date parsedEndDate = null;
    
    /* Parse start and end dates */
    try {
      parsedStartDate = new SimpleDateFormat("dd-MM-yyyy").parse(req.startDate);
      parsedEndDate = new SimpleDateFormat("dd-MM-yyyy").parse(req.endDate);
    
    /* Throw fault if invalid date format */
    } catch (ParseException e) {
      String msg = "Invalid date format";
      String code= "DATE_ERR";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      e.printStackTrace();
      throw new ImportMarketFaultMsg(msg,fault);
    }
    
    /* Throw fault if end date < start date format */
    if (parsedEndDate.before(parsedStartDate)) {
      String msg = "End date is before start date";
      String code= "DATE_ERR";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new ImportMarketFaultMsg(msg,fault);
    }
    
    /* Convert start date to URI format */
    String startDateURI = "&a=" + req.startDate;
    startDateURI = startDateURI.replaceFirst("-", "&b=");
    startDateURI = startDateURI.replaceFirst("-", "&c=");
    
    /* Convert end date to URI format */
    String endDateURI = "&d=" + req.endDate;
    endDateURI = endDateURI.replaceFirst("-", "&e=");
    endDateURI = endDateURI.replaceFirst("-", "&f=");
    
    /* Event set filename */
    String fileName = req.getSec() + "_" + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()).toString();
    File tempFile = new File(System.getProperty("catalina.home") + File.separator + 
                             "webapps" + File.separator + 
                             "ROOT" + File.separator + 
                             "EventSetDownloads" + File.separator +
                             fileName + ".csv");
    
    /* Make previous directories if they don't exist */
    tempFile.getParentFile().mkdirs();

    try {
      /* Sample format:
       * http://real-chart.finance.yahoo.com/table.csv?s=BHP.AX&a=00&b=29&c=1988&d=03&e=15&f=2015&g=d&ignore=.csv
       * */
      
      /* Generate URI str */
      String urlStr = "http://real-chart.finance.yahoo.com/table.csv";
      urlStr += "?s=" + req.sec;
      urlStr += startDateURI;
      urlStr += endDateURI;
      urlStr += "&g=d&ignore=.csv";

      
      /* Create new event set file */
      tempFile.createNewFile();

      BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile.getAbsoluteFile()));
      
      /* Open stream to yahoo and write to file */
      URL url = new URL(urlStr);
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
      
      String strTemp = "";
      boolean first = true;
      while (null != (strTemp = br.readLine())) {
        
        /* If first line append "Sec" column name */
        if(first){
          bw.write("Sec," + strTemp);
          bw.write("\r\n");
          first = false;
          
        /* Else append Sec data + prepend AUD to relevant columns */
        }else{
          String[] data = strTemp.split(",");
          bw.write(req.sec); //sec
          bw.write("," + data[0]); //date
          bw.write(",AUD" + data[1]); //open
          bw.write(",AUD" + data[2]); //high
          bw.write(",AUD" + data[3]); //low
          bw.write(",AUD" + data[4]); //close
          bw.write("," + data[5]); //volume
          bw.write(",AUD" + data[6]); //adj close
          bw.write("\r\n");
        }
              
      }
      
      bw.close();
      
    /* Catch general exceptions */
    } catch (Exception e) {
      String msg = "A generic service fault has occurred";
      String code= "GEN_ERR";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      e.printStackTrace();
      throw new ImportMarketFaultMsg(msg,fault);
    }
    
    /* Create response */
    ImportMarketDataResponse res = objFactory.createImportMarketDataResponse();
    res.setEventSetId(fileName);
   
    return res;
  }
  

  public DownloadFileResponse downloadFile(DownloadFileRequest req) throws DownloadFileFaultMsg {

    /* Check if event file exists */   
    File f = new File(System.getProperty("catalina.home") + File.separator + 
                      "webapps" + File.separator + 
                      "ROOT" + File.separator + 
                      "EventSetDownloads" + File.separator +
                      req.getEventSetId() + ".csv");
    
    /* Throw fault if file doesn't exist */ 
    if (!f.exists()) { 
      String msg = "Unknown eventSetId was given";
      String code = "ERR_EVENT";

      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new DownloadFileFaultMsg(msg,fault);
    }
    
    /* Create response */
    DownloadFileResponse res = objFactory.createDownloadFileResponse();
    res.setDataURL("http://localhost:8080/EventSetDownloads/" + f.getName());
    
    return res;
  }
  
}
