package RestAssureHelper;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import API.com.project.api.Endpoints.APIEndPoints;
import API.com.project.api.Endpoints.EndPointRetriever;
import Parser.Parser;
import Utilities.Reporter;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.testng.Assert;
import org.testng.annotations.*;

/***
 * 
 *  This class is a general wrapper above the rest assured API.
 *  
 *  The approach that we will be taking is that
 *  
 *  - We will make a request using Rest Assured get function
 *  - We will be storing the response inside a local variable
 *  - We will fetch the necessary values from the response and will make assertions on that
 *  
 *  Note: All the functions will take response as an argument to make it scalable for the future
 */


@SuppressWarnings("unused")
public class RestAssuredHelper {

	Response res;	
	boolean printApiResponse = true;
	
	public void getRequestWithAuthorization(String endPoint) throws Exception
	{
		Reporter.addStep("  ");
		Reporter.addStep("Request sent to \n" + endPoint);
		try
		{
			String accesssToken = "c56e686b-3d68-418c-ad3a-b6f700733a3d";
			String headerValue = "Bearer " + accesssToken;
			this.res = null;
		    this.res = given().header("Authorization" ,headerValue).get(endPoint);
		    if(printApiResponse)
		    {
				Reporter.addStep("Response returned");
				Reporter.addStep(this.res.body().asString());
		    }
		    else
				Reporter.addStep("To print API response set printAPIresponse boolean to True "
						+ "in RestAssuredHelper class");

		}
		catch(Exception e){e.printStackTrace(); throw e;}
	}
	
	
	public void getRequest(String endPoint) throws Exception
	{
		Reporter.addStep("  ");
		Reporter.addStep("Request sent to \n" + endPoint);
		try
		{
			this.res = null;
		    this.res = get(endPoint);
		    if(printApiResponse)
		    {
				Reporter.addStep("Response returned");
				Reporter.addStep(this.res.body().asString());
		    }
		    else
				Reporter.addStep("To print API response set printAPIresponse boolean to True "
						+ "in RestAssuredHelper class");

		}
		catch(Exception e){e.printStackTrace(); throw e;}
	}
	
	public Response returnLastResponse()
	{
		return res;
	}
	
	public void  assertStatusCodeForResponse(Response response , int expectedStatusCode)
	{
		Reporter.addStep("Status Code from API  " + response.andReturn().statusCode());

		Assert.assertEquals(response.andReturn().statusCode() , expectedStatusCode ,
				"Expected and Actual Status code didn't match");
	}	
	
	public void checkIfHeaderIsPresentInResponse(Response res , String headerStringToMatch)
	{
		boolean isHeaderPresent = false;		
		Headers allHeaders = res.getHeaders();
				
		System.out.println(headerStringToMatch);
		Reporter.addStep("Looking for header" + headerStringToMatch);

		for(Header h : allHeaders)
		{
			if(h.getName().equalsIgnoreCase(headerStringToMatch))
				isHeaderPresent = true;
		}
		
		String inCaseOFError = "Header with name '" + headerStringToMatch.toUpperCase() + "' was not found in the response";
		Assert.assertEquals(isHeaderPresent , true ,inCaseOFError);
		
	}

	public String returnValueForHeaderInResponse(Response res , String headerStringToMatch)
	{
		Reporter.addStep("Looking for header" + headerStringToMatch);

		boolean isHeaderPresent = false;		
		Headers allHeaders = res.getHeaders();
		String valToReturn= null;
				
		for(Header h : allHeaders)
		{
			if(h.getName().equalsIgnoreCase(headerStringToMatch))
				valToReturn = h.getValue();
		}
		
		Reporter.addStep("Value Returned for header " + valToReturn);

		return valToReturn;
	}

	public void checkArraySizeInResponse(byte[] byteArray, int lengthOfArray)
	{
		Parser p = new Parser();
		p.returnJsonArrayLength(byteArray);
		Assert.assertEquals(p.returnJsonArrayLength(byteArray), lengthOfArray , 
				"No of records didn't matched");
	}
	

}
