package API.com.project.api.Endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EndPointRetriever {

	String propertyFileName = "URL.properties";
	String errorOccuredString = "Error occured while reading from property file";
	
	public int HTTP_STATUS_OK = 200;
		
	APIEndPoints endPoints;
	
	public String returnEndPoint(String endPoint ,boolean addPostfix)
	{
		Properties prop = new Properties();
		InputStream input = null;
		String endPointToReturn = null;

		try {

		    input = getClass().getClassLoader().getResourceAsStream(propertyFileName);
		    // load a properties file
		    prop.load(input);
		    System.out.println(APIEndPoints.BASE_URL.toString());
		    if(!addPostfix)
		      endPointToReturn = prop.getProperty(APIEndPoints.BASE_URL.toString())+
		    		prop.getProperty(endPoint);
		    else
			      endPointToReturn = prop.getProperty(endPoint);
		    	
		    return endPointToReturn;
		} catch (IOException ex) {
		    ex.printStackTrace();
		    return errorOccuredString;

		} finally {
		    if (input != null) {
		        try {
		            input.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}	
}
