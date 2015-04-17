package au.edu.unsw.soacourse.marketdatautil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MarketDataUtilServiceImpl implements MarketDataUtilService {

	@Override
	public VisualiseMarketDataResponse visualiseMarketData(
			VisualiseMarketDataRequest parameters)
			throws VisualiseMarketDataFaultMsg {
		// TODO Auto-generated method stub
		return null;
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
		
		ObjectFactory factory = new ObjectFactory();
		SummariseMarketDataResponse response = factory.createSummariseMarketDataResponse();
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
