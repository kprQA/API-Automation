package Parser;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.Assert;

import RegExMatcher.PatternMatcher;
import Utilities.Reporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public class Parser {
	
	
	/*
	 *  We are using Jackson parser to parse the JSON response
	 *  
	 *  JSON String returned by API in response is converted to byte array
	 *  We calculate the size of the array over here and return it to our test scripts
	 */
	
	byte[] dataString =null;
	PatternMatcher p = new PatternMatcher();
	String APIJsonFile = null;
	
	
	public void setAPIJsonFile(String file)
	{
		APIJsonFile = file;
	}
	
	int randomNoBetweenRange(int minimum , int maximum)
	{
		int randomNum = minimum + (int)(Math.random() * maximum); 
		return randomNum;
		
	}
		
	
	public int returnJsonArrayLength(byte[] byteArray)
	{
		int length = 0;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(byteArray);
			length = rootNode.size();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return length;
	}
	
	public void matchValueOfKeyBasedOnRegEx(String key, String val)
	{
		Reporter.addStep("Matching value for key " + key);
		Reporter.addStep("Value from API is      " + val);
		
		p = null;
		p = new PatternMatcher();
		p.matchValueForKeyAsString(key, val);
		
	}
	
	public void checkKeyValuesForResponse(byte[] byteArray) throws Exception
	{
		this.checkValuesForKeysInResponseInsideRecordRange(dataString, 0, byteArray.length-1);
	}

	public void checkKeyValueForResponseInRecordNumber(byte[] byteArray , int recordNo)
	throws Exception
	{
		dataString  = byteArray;
		try {
			this.checkValuesForKeysInResponseInsideRecordRange(dataString, recordNo-1, recordNo-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public void checkValuesForKeysInResponseInsideRecordRange(byte[] byteArray , int minValue , int maxValue)
			throws Exception
	{
		boolean dontCheckFurthurItsAnArray = false;
		KeyFinder keyChecker = new KeyFinder();
		keyChecker.setFilePath(APIJsonFile);
		keyChecker.setJsonData();
		
		dataString  = byteArray;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(dataString);
			int objectsInResponse = this.returnJsonArrayLength(dataString);
			
			if (objectsInResponse <=0)
				Assert.fail("API returned 0 response");
			
			Reporter.addStep("Total records        : " + objectsInResponse);
			Reporter.addStep("Inital record number : " + minValue);
			Reporter.addStep("Final record  number : " + maxValue);
			
			
			for (int i = minValue ; i<=maxValue; i++)
			{
				Iterator<Entry<String, JsonNode>> nodes = rootNode.get(i-1).fields();
				
				Reporter.addStep("\n\n Record at :" + i);
				
				while (nodes.hasNext()) {
				  Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
				  String keyValue = null;				  
				  if(entry.getValue().isNumber())
				  {
					  keyValue = String.valueOf(entry.getValue().asInt());
					  System.out.println("Key value is" + keyValue);
				  }
				  
				  else if(entry.getValue().isArray())
				  {
					  JsonNode arrNode = entry.getValue();
					  matchValueOfKeyBasedOnRegEx(entry.getKey().toString() , " ");

					  for(int r = 0 ; r<=arrNode.size()-1 ; r++)
					  {
						  System.out.println("Node found" + arrNode.get(r).toString());
						  ObjectMapper map2 = new ObjectMapper();
						  JsonNode nestedjson= map2.readTree(arrNode.get(r).toString().getBytes());
						  Iterator<Entry<String, JsonNode>> fieldsIterator = nestedjson.fields();
						  while (fieldsIterator.hasNext()) {
							 System.out.println("***************************Inside Nested Array*****************");
					         Map.Entry<String,JsonNode> field = fieldsIterator.next();
					         System.out.println("Nested Key: " + field.getKey() + "\tValue:" + field.getValue());
					         if(field.getValue().equals(null) || field.getValue().toString().equals("null"))
					         {
					        	 String nullAlert = "**** NULL ALERT for key : " + field.getKey();
					        	 Reporter.addStep(nullAlert);
					         }
					         else
					         {
								  if(entry.getValue().isNumber())
								  {
									  keyValue = String.valueOf(field.getValue().asInt());
									  System.out.println("Key value is" + keyValue);
								  }
								  else
								  {
									  keyValue = field.getValue().textValue();
								  }
								  String msgInCaseOfError = "Invalid key " + entry.getKey() + " found";
								  dontCheckFurthurItsAnArray = true;
								  Assert.assertEquals(keyChecker.checkStringTypeKeyPresent(entry.getKey()), true
										  , msgInCaseOfError);
								  matchValueOfKeyBasedOnRegEx(field.getKey().toString() , keyValue);
					         }
						  }
					  }
				  }
				  else
				  {
					  keyValue = entry.getValue().textValue();
				  }
				  
				  if(!entry.getValue().isArray() && !dontCheckFurthurItsAnArray)
				  {
				         if(entry.getValue().equals(null) || entry.getValue().toString().equals("null"))
				         {
				        	 String nullAlert = "**** NULL ALERT for key : " + entry.getKey();
				        	 Reporter.addStep(nullAlert);
				         }
				         else{
							  String msgInCaseOfError = "Invalid key " + entry.getKey() + " found";
							  Assert.assertEquals(keyChecker.checkStringTypeKeyPresent(entry.getKey()), true
									  , msgInCaseOfError);
							  matchValueOfKeyBasedOnRegEx(entry.getKey() , keyValue);
				         }
				  }
				  String toAddInReport = "key --> " + entry.getKey() + " value-->" + entry.getValue();
				  Reporter.addStep(toAddInReport);
				  
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} 
	}

}
