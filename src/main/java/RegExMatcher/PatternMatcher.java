package RegExMatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Utilities.Reporter;

public class PatternMatcher {

	 /*
	  * We use this class to match key data by using regex. 
	  * All the regex are defined in RegExPatternClass 
	  */
	
	RegExPatterns regExPattern = new RegExPatterns();
		
	public boolean patternMatch(String regex , String text)
	{
		boolean matchStatus = false;
		if  (text.matches(regex)) {
		    matchStatus = true;
		} else {
		}
	    Reporter.addStep("     ");
		Reporter.addStep("RegEx used       : " + regex + "   to Match Value    : " + text);	
		if(matchStatus)
		    Reporter.addStep("****Pattern Matched******");
		else
		    Reporter.addStep("****Pattern NOT  Matched******");
		return matchStatus;	
	}
	
	
	String endpointToReturn = ""; 	
	public String returnEndpoint(String endpoint) throws IOException
	{
		String filePath = System.getProperty("user.dir") + "\\resources\\json-templates\\Urls\\Endpoints.json";
		byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonData);
		JsonNode idNode = rootNode.path(endpoint);
		endpointToReturn = idNode.asText();
		return endpointToReturn;
	}	

	public void matchValueForKeyAsString(String key, String text)
	{
		System.out.println("Matching key :" + key);
		Reporter.addStep("Matching key :" + key);
		String msgInCseofFailure = "Value didn't match for  the " + key +" key. Value in API " + text;
		Assert.assertEquals(patternMatch(regExPattern.genericRegexPattern,text),true, msgInCseofFailure);		
	}
}

