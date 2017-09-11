package API.com.project.api;

import org.apache.commons.mail.EmailException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import Utilities.Reporter;;

public class BaseTest {
	
	@BeforeSuite public void
	init()
	{
		Reporter.init();
		Reporter.setFileName("API Test");
	}
	
	@AfterMethod public void 
	writeAll(ITestResult c) {
		Reporter.writeEachTestResult(c);
	}

	@AfterSuite public void
	tearDown() throws Exception
	{
	  	Reporter.writeAll();
	  	Reporter.sendEmail();
	  	Reporter.closeExtent();
	  	
	}

}
