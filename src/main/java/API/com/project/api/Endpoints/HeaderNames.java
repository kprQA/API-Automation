package API.com.project.api.Endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class HeaderNames {
	
	public String header_For_TotalRecords = APIHeaders.REC_TOTAL_COUNT.toString();
	public String header_For_SkippedRecords = APIHeaders.REC_SKIPPED.toString();
	public String header_For_TotalRecordsInResponse = APIHeaders.REC_MAX_SIZE.toString();
	
	String propertyFileName = "Header.properties";
	String errorOccuredString = "Error occured while reading from property file";

	public String returnHeader(String headerKey)
	{
		String headerNameToReturn = null;
		
		Properties prop = new Properties();
		InputStream input = null;

		try {

		    input = getClass().getClassLoader().getResourceAsStream(propertyFileName);
		    // load a properties file
		    prop.load(input);
		    headerNameToReturn = prop.getProperty(headerKey);		    
		    return headerNameToReturn;
		} catch (IOException ex) {
		    ex.printStackTrace();
		    return errorOccuredString;
		} 
	}
	
	public String  returnTotalRecordsHeader()
	{
		 return returnHeader(header_For_TotalRecords);
	}
	
	public String  returnTotalRecordsInResponseHeader()
	{
		 return returnHeader(header_For_TotalRecordsInResponse);
	}

	public String  returnSkippedRecordsHeader()
	{
		 return returnHeader(header_For_SkippedRecords);
	}

}
