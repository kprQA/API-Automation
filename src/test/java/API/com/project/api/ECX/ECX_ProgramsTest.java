package API.com.project.api.ECX;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Parser.Parser;
import RestAssureHelper.RestAssuredHelper;
import Utilities.Reporter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import API.com.project.api.BaseTest;
import API.com.project.api.Endpoints.APIEndPoints;
import API.com.project.api.Endpoints.EndPointRetriever;
import API.com.project.api.Endpoints.HeaderNames;

import org.testng.annotations.*;
@SuppressWarnings("unused")

public class ECX_ProgramsTest extends BaseTest{
	
	EndPointRetriever endPoint = new EndPointRetriever();
	RestAssuredHelper assured = new RestAssuredHelper();
	Response res = null;
	HeaderNames header = new HeaderNames();
	
	Parser parser = new Parser();

	@BeforeClass
	public void setUp()
	{
		parser.setAPIJsonFile("regProgram");
	}


	@Test public void
	T1_test_Http_Status_Code_For_The_Programs_API() throws Exception
	{
		try{
		  	Reporter.startTest("Programs ", "Check HTTP Status code is 200");

			String getProgramsEndPoint = endPoint.returnEndPoint(APIEndPoints.PROGRAMS.toString(),false);	
		    assured.getRequest(getProgramsEndPoint);
			this.res = assured.returnLastResponse();
			assured.assertStatusCodeForResponse(res,endPoint.HTTP_STATUS_OK);
		}
		catch (Exception e)
		{
			Reporter.addStep(e.getMessage());
			throw e;
		}

	}
	
	@Test public void 
	T2_test_All_Headers_Are_Present_In_The_Response() throws Exception
	{
		try{
		  	Reporter.startTest("Programs ", "Check All headers are present");

			assured.checkIfHeaderIsPresentInResponse(res,header.returnSkippedRecordsHeader());
			assured.checkIfHeaderIsPresentInResponse(res,header.returnTotalRecordsHeader());
			assured.checkIfHeaderIsPresentInResponse(res,header.returnTotalRecordsInResponseHeader());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}
	}
	
	@Test() public void
	T3_test_More_Than_Records_Are_Returned_From_The_Call() throws Exception
	{
		try{
		  	Reporter.startTest("Programs ", "Check that API response returns record");
			int recCount = Integer.valueOf(assured.returnValueForHeaderInResponse(res,header.returnTotalRecordsHeader()));
			boolean greaterThan0=false;
			
			if(recCount > 0)
				greaterThan0 = true;
			
			Assert.assertEquals(greaterThan0, true , "No record returned from the API response");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}
	}

	@Test() public void
	T4_test_OffSet_And_Max_When_Set_To_Same_Value_Recors_Are_Displayed_While_Using_Pagesize() throws Exception
	{
		try{
			Reporter.startTest("Programs ", "Check that API response returns * records when min is set to 0 and post fix to 8");
			String getProgramsEndPoint = endPoint.returnEndPoint(APIEndPoints.PROGRAMS.toString(),false)+
					endPoint.returnEndPoint(APIEndPoints.LIMIT_RECORDS_POSTFIX.toString(),true);	
			getProgramsEndPoint=getProgramsEndPoint.replace("minValue", "0");
			getProgramsEndPoint=getProgramsEndPoint.replace("maxValue", "8");
			System.out.println(getProgramsEndPoint);
		    assured.getRequest(getProgramsEndPoint);
			this.res = assured.returnLastResponse();
			assured.assertStatusCodeForResponse(res,endPoint.HTTP_STATUS_OK);
			assured.checkArraySizeInResponse(res.body().asByteArray(), 8);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}

	}
	@Test() public void
	T5_test_Check_A_SpecificRecord() throws Exception
	{
		try {
			//Validates record for a specific one
			Reporter.startTest("Programs ", "Validates API response for a specific record");
			String getProgramsEndPoint = endPoint.returnEndPoint(APIEndPoints.PROGRAMS.toString(),false);	
		    assured.getRequest(getProgramsEndPoint);
			this.res = assured.returnLastResponse();
			parser.checkKeyValueForResponseInRecordNumber(res.body().asByteArray(), 31);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}
	}
	
	@Test() public void
	T6_test_Check_Records_In_Range() throws Exception
	{
		try{
		  	Reporter.startTest("Programs ", "Validates API response for records in range");
			//Validates record for a specific range
			String getProgramsEndPoint = endPoint.returnEndPoint(APIEndPoints.PROGRAMS.toString(),false);
			System.out.println(getProgramsEndPoint);
		    assured.getRequest(getProgramsEndPoint);
			this.res = assured.returnLastResponse();
			parser.checkValuesForKeysInResponseInsideRecordRange(res.body().asByteArray(), 11, 35);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}
	
	}
	

	@Test() public void
	T7_test_No_Of_Records_If_Offset_And_Max_Are_Same() throws Exception
	{
		try{
		    Reporter.startTest("Programs ", "Check that no of recorda re same if both Offset +"
			   		+ "and max in request are same if set to same no");

			    String getProgramsEndPoint = endPoint.returnEndPoint(APIEndPoints.PROGRAMS.toString(),false)+
						endPoint.returnEndPoint(APIEndPoints.LIMIT_RECORDS_POSTFIX.toString(),true);
			    
			    Reporter.addStep("OFFSET and MAx Set to same value 12");
			    
				getProgramsEndPoint=getProgramsEndPoint.replace("minValue", "12");
				getProgramsEndPoint=getProgramsEndPoint.replace("maxValue", "12");
			    assured.getRequest(getProgramsEndPoint);
				this.res = assured.returnLastResponse();
				assured.assertStatusCodeForResponse(res,endPoint.HTTP_STATUS_OK);
				
				assured.checkArraySizeInResponse(res.body().asByteArray(), 12);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}
	
	}

	@Test() public void
	T8_test_No_Of_Records_If_OfSet_IS_SET_TO_0() throws Exception
	{
		try{
			Reporter.startTest("Programs ", "Check that no of records if Offset is set to 0");
		    String getProgramsEndPoint = endPoint.returnEndPoint(APIEndPoints.PROGRAMS.toString() 
		    		,false) +"??offset=0";
		    
		    Reporter.addStep("Checcking value against page max size parameter");
		    assured.getRequest(getProgramsEndPoint);
			this.res = assured.returnLastResponse();
			assured.assertStatusCodeForResponse(res,endPoint.HTTP_STATUS_OK);
			
			String valForMaxSize = assured.returnValueForHeaderInResponse(res, 
					header.returnTotalRecordsInResponseHeader());
			
			Assert.assertEquals(valForMaxSize, "500", "Max size header value didn't match"
					+ " when offset was set to 0"
					+ " and no other value was passed");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Reporter.addStep(e.getMessage());
			throw e;
		}

		
	}
}
