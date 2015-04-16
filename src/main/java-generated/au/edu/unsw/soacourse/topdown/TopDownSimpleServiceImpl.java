package au.edu.unsw.soacourse.topdown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.cxf.transport.servlet.ServletController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.support.ServletConfigPropertySource;

@WebService(endpointInterface = "au.edu.unsw.soacourse.topdown.TopDownSimpleService")
public class TopDownSimpleServiceImpl implements TopDownSimpleService {
  
  ObjectFactory objFactory = new ObjectFactory();
  
  @Autowired
  ServletContext sc;
  
  public ImportMarketDataResponse importMarketData(ImportMarketDataRequest req) throws ImportMarketFaultMsg {
    
    int a = 0;
    
    if (req.getSec().length() != 3) {
      
      String msg = "SEC code should be exactly 3 characters long";
      String code= "ERR_SEC";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new ImportMarketFaultMsg(msg,fault);
    }
    System.out.println("ASDASDASD");
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
    tempFile.getParentFile().mkdirs();

    try {
      /* Sample format:
       * http://real-chart.finance.yahoo.com/table.csv?s=BHP.AX&a=00&b=29&c=1988&d=03&e=15&f=2015&g=d&ignore=.csv
       * */
      
      /* Create URI str */
      String urlStr = "http://real-chart.finance.yahoo.com/table.csv";
      urlStr += "?s=" + req.sec;
      urlStr += startDateURI;
      urlStr += endDateURI;
      urlStr += "&g=d&ignore=.csv";

      
      /* Create new event set file */
      tempFile.createNewFile();

      FileWriter fw = new FileWriter(tempFile.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      
      /* Open stream to yahoo and write to file */
      URL url = new URL(urlStr);
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
      
      String strTemp = "";
      boolean first = true;
      while (null != (strTemp = br.readLine())) {
        if(first){
          bw.write("Sec," + strTemp);
          bw.write("\r\n");
          first = false;
        }else{
          
          /* TODO check assignment page for fault handling */
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
      
    } catch (Exception ex) {
      String msg = "Generic Fault";
      String code= "genericFault";
      
      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      ex.printStackTrace();
      fault.setErrtext(ex.getMessage());
      
      throw new ImportMarketFaultMsg(msg,fault);
    }
       
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
   
    if (!f.exists()) { 
      
      String msg = "Unknown eventSetId was given";
      String code = "ERR_EVENT";

      ServiceFaultType fault = objFactory.createServiceFaultType();
      fault.setErrcode(code);
      fault.setErrtext(msg);
      
      throw new DownloadFileFaultMsg(msg,fault);
    }
      
    DownloadFileResponse res = objFactory.createDownloadFileResponse();
    res.setDataURL("http://localhost:8080/EventSetDownloads/" + f.getName());
    
    return res;
  }
  
}
