package Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import API.com.project.api.Endpoints.APIEndPoints;
import API.com.project.api.Endpoints.EndPointRetriever;
import RestAssureHelper.RestAssuredHelper;
import Utilities.Reporter;

public class KeyFinder {
	
	byte[] jsonData = null;
	String jsonFile = null;
	
	public void setFilePath(String fileName)
	{
		jsonFile = fileName;
	}
	
	public void setJsonData() throws Exception
	{
		String filePath = System.getProperty("user.dir") + "\\resources\\regex\\"+ jsonFile+".json";
		try {
			jsonData = Files.readAllBytes(Paths.get(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	public boolean checkStringTypeKeyPresent(String keyToFound) throws IOException
	{
		boolean matchFound = false;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonData);
		JsonNode stringKeys = rootNode.get("keys");
		for(int i =0 ; i <= stringKeys.size()-1; i++)
		{
			if(stringKeys.get(i).get("keyName").textValue().equals(keyToFound))
				matchFound = true;
		}
		return matchFound;
	}	
	
	public static void main(String[] args) throws Exception {
		KeyFinder t = new KeyFinder();
		t.setFilePath("regConcentrations");
		t.setJsonData();
		t.checkStringTypeKeyPresent("123");
	}
}
