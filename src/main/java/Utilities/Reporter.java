package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import  org.testng.*;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;


@SuppressWarnings("unused")
public class Reporter {
	
	public static ExtentReports extent = null;
	public static ExtentTest test = null;
	public static String  f = System.getProperty("user.dir") + "\\resources\\styleSheets\\reporter.xml";
	public static String fileName=" ";
	public static String filePath = System.getProperty("user.dir") + "\\resources\\reports\\"+ fileName + " report.html";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<ITestResult> resultsArray = (ArrayList<ITestResult>) new ArrayList();
	public Reporter (){}
	
	public static void setFileName(String passedValue)
	{
		fileName = passedValue;
	}
	
	public static ExtentReports init()
	{
		if (extent == null){
			
	          String reportDir=System.getProperty("user.dir") + "\\resources\\reports\\"; 
	          File fileToDelete = new File(reportDir);
	          File[] files = fileToDelete.listFiles(); 
	          for (File f:files) 
	          {
	        	  if (f.isFile() && f.exists()) 
	        	  { 
	        		  f.delete();
	        		  System.out.println("File Delted");
	        	  }
	        	  else{
	        		  System.out.println("File was not detelted");
	        	  }
	           }  

			extent = new ExtentReports(filePath, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
			final File file  = new File(f);
			extent.loadConfig(file);
		}
		return extent;
	}
	
	public static void startTest(String testName, String description)
	{
		test=null;
		System.out.println("------------" + testName +" started----------------");
		test=extent.startTest(testName, description);
	}
	
	public static void log(LogStatus l, Throwable throwable)
	{
		test.log(LogStatus.INFO,throwable);
	}
	
	public static void addStep(String stepDetails)
	{
		System.out.println(stepDetails);
		test.log(LogStatus.INFO, stepDetails,"");
	}
	
	public static void addStepWithScreenshot(String stepDetails , String fileName)
	{
		System.out.println(stepDetails);
		test.log(LogStatus.INFO, stepDetails,"");
        test.log(LogStatus.INFO, "Screenshot after above step" + test.addScreenCapture(fileName));
	}
	
	
	public static void endTest()
	{
		extent.endTest(test);
		System.out.println("------------ ended ----------------");
	}
	
	public static void writeAll()
	{
		for (ITestResult result:resultsArray)
		{
			System.out.println("entered loop");
	        if (!result.isSuccess()) {
	    		test.log(LogStatus.FAIL, result.getThrowable());
	        }
	        if (result.isSuccess()) {
	        	if(result.getThrowable() == null)
	        		test.log(LogStatus.PASS, "<span class='label success'>Test case executed succesfully</span>");
	        }
		}
	}

	public static void closeExtent()
	{
          
		  extent.flush();
		  
		  DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYY HH-mm-ss");
		  //get current date time with Date()
		  Date date = new Date();
		  System.out.println(dateFormat.format(date));


		  String renameFile = System.getProperty("user.dir") + "\\resources\\reports\\"+ fileName +" report.html";
		  fileName=renameFile;
		  File oldName = new File(filePath);
	      File newName = new File(renameFile);
	      if(oldName.renameTo(newName)) {
	         System.out.println("renamed");
	      } else {
	         System.out.println("Error");
	      }
		extent.close();
	}
	
    public static void writeEachTestResultWithScreenshot(ITestResult result, String fileName) {
    	
        if (!result.isSuccess()) {
    		test.log(LogStatus.FAIL, result.getThrowable());
        }
        if (result.isSuccess()) {
        	if(result.getThrowable() == null)
        		test.log(LogStatus.PASS, "<span class='label success'>Test case executed succesfully</span>");
        }
        
        test.log(LogStatus.INFO, "Snapshot taken after test is completed: " + test.addScreenCapture(fileName));
               
        extent.endTest(test);

    	/*
    	resultsArray.add(result);
    	System.out.println("Size is" + resultsArray.size());
        */      
    }	

    public static void writeEachTestResult(ITestResult result) {
    	
        if (!result.isSuccess()) {
    		test.log(LogStatus.FAIL, result.getThrowable());
        }
        if (result.isSuccess()) {
        	if(result.getThrowable() == null)
        		test.log(LogStatus.PASS, "<span class='label success'>Test case executed succesfully</span>");
        }
        
               
        extent.endTest(test);

    }	
    public static void sendEmail() throws EmailException
    {
         StringBuilder contentBuilder = new StringBuilder();
         try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
        
          String content = contentBuilder.toString();
          
          EmailAttachment attachment = new EmailAttachment();
          attachment.setPath(fileName);
          attachment.setDisposition(EmailAttachment.ATTACHMENT);
         
          // Create the email message
          ImageHtmlEmail email = new ImageHtmlEmail();
          email.attach(attachment);
          email.setHostName("smtp.gmail.com"); //email server address
          email.setSSLCheckServerIdentity(true);
          email.addTo("paramk.qa@gmail.com", ""); // enter email addrss 
          email.setFrom("eshwark.testing@gmail.com", "Eshwar");
          
          String subject = " Your subject name";

          
          email.setSubject(subject);
          email.setMsg("Please find attachment for the Test Results \r\n");
          
          // send the email
          email.send();
          

    }

}
