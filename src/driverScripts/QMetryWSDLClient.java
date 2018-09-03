package driverScripts;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

import com.qmetry.webservices.Entity;
import com.qmetry.webservices.QMetryWS;
import com.qmetry.webservices.QMetryWSPortType;
import com.qmetry.webservices.TestCaseEntity;
import com.qmetry.webservices.TestSuiteEntity;
import com.qmetry.webservices.TestSuiteRunByPlatform;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class QMetryWSDLClient 
{
	private final static URL QMETRY_WSDL_LOCATION;
	public static String QMETRY_USERNAME;
	public static String QMETRY_PASSWORD;
	public static String qmetryProject;
	public static String qmetryRelease;
	public static String qmetryBuild;
	public static String qmetryTestSuite;
	public static String qmetryPlatform;
	public static String qmetryExecuteFlag;
    static 
	{
        URL url = null;
        try 
		{
            // QMetry Instance URL
			url = new URL("https://highwire.qmetry.com/qmetrypro/WEB-INF/ws/service.php?wsdl");
        } 
		catch (MalformedURLException e) 
		{
            e.printStackTrace();
        }
        
        
        
        QMETRY_WSDL_LOCATION = url;
		//QMETRY_USERNAME = "admin";				// QMetry Username
		//QMETRY_PASSWORD = "admin1@#4";			// QMetry Password
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main() 
	{
    	boolean flagQmetryParamCheck = false;
		try {
			flagQmetryParamCheck = getQmetryParameters();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (flagQmetryParamCheck == true)
    	{
				QMetryWS qmetryService = new QMetryWS(QMETRY_WSDL_LOCATION, new QName("http://www.wso2.org/php", "QMetryWS"));
				QMetryWSPortType qmetryPort = qmetryService.getQMetryWSSOAPPortHttp ();
				
				/*Map<String, Object> req_ctx = ((BindingProvider)qmetryPort).getRequestContext();
		        Map<String, List<String>> headers = new HashMap<String, List<String>>();
				
		        headers.put("X_QMetry_App", Collections.singletonList("QMetryWSDLClient"));
				
		        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);*/
				
				// Login to QMetry and get authentication token
				String token = null;
				try
				{
					token = qmetryPort.login (QMETRY_USERNAME, QMETRY_PASSWORD);
					System.out.println ("Logged into QMetry with Token: " + token);
				
					// In QMetry test assets are always associated with Project, Release and Build
					// To get any test asset, first we need to tell QMetry about from which Project, Release and Build
					// we want to fetch test asset
					//final String qmetryProject = "Petstore";		// Replace it with your project name in QMetry
					//final String qmetryRelease = "PetstoreRel1";	// Replace it with your release name in QMetry
					//final String qmetryBuild = "PetstoreBld1";		// Replace it with your build name in QMetry
					final String message = qmetryPort.setScope (token, qmetryProject, qmetryRelease, qmetryBuild);
					System.out.println (message);
					
					// To execute any Test Case, we need following information
					// TestSuite ID: to identify which test suite we are executing
					// Platform ID: to identify, against which platform we are executing this Test Case
					// TestCase ID: to identify which Test Case we are going to execute
					// Test Case Run Status: Whether test case is pass, fail, blocked or notrun
					
					// As an end user, we don't know IDs of these entities, but we do know their names
					// So below code will try to get ID from their name
					
					// First get ID of Test Suite with Name "Browsing PetStore"
					List testSuites = qmetryPort.listTestSuites (token, qmetryTestSuite);
					// If you execute bellow call, then it will list all Test Suites for selected build, in that case you need to
					// iterate through all test suites and compare name and get ID
					//List testSuites = qmetryPort.listTestSuites (token, "");
					
					if (testSuites.size () == 0)
					{
						throw new Exception ("No Test Suite found");
					}
					else if (testSuites.size () > 1)
					{
						throw new Exception ("More than 1 Test Suite found");
					}
					
					// Iterate through all Test Suite returned by "listTestSuites" call
					Iterator testSuiteIterator = testSuites.iterator ();
					TestSuiteEntity testSuiteEntity = null;
					while (testSuiteIterator.hasNext ())
					{
						testSuiteEntity = (TestSuiteEntity) testSuiteIterator.next ();
					}
					
					// get Test Suite Id
					final int testSuiteId = testSuiteEntity.getId ();
					
					// Say for example, we want to execute this Test Suite on "IE" platform
					// First find out that is Platform "IE" is linked with this Test Suite or NOT
					// If its not linked, we will link platform "IE" with this Test Suite
					
					// first find out how many platforms are linked with this Test Suite
					List linkedPlatformList = qmetryPort.listPlatformsByTestSuite (token, testSuiteId);
					Iterator linkedPlatforms = linkedPlatformList.iterator ();
					int linkedPlatform = 0;
					if (linkedPlatformList.size () > 0)
					{
						while (linkedPlatforms.hasNext ())
						{
							TestSuiteRunByPlatform platformDetails = (TestSuiteRunByPlatform) linkedPlatforms.next ();
							
							// If platform "IE" is already linked, then store ID of platform IE as linkedPlatform variable
							if (platformDetails.getName().equalsIgnoreCase(qmetryPlatform))
							{
								linkedPlatform = platformDetails.getId ();
								break;
							}
						}
					}
					
					// If value of linkedPlatform variable is 0, that means either platform IE is not linked with 
					// this test suite Or there is no platform linked with this test suite
					// So get all platform configured with this project, and get ID of platform "IE"
					if (linkedPlatform == 0)
					{
						// Fetch all platforms configured with this project
						List allPlatformList = qmetryPort.listCustomizedList (token);
						Iterator allPlatforms = allPlatformList.iterator ();
						if (allPlatformList.size () == 0)
						{
							throw new Exception ("No platform configured with this project");
						}
						
						while (allPlatforms.hasNext ())
						{
							Entity platform = (Entity) allPlatforms.next ();
							if (platform.getName().equalsIgnoreCase(qmetryPlatform))
							{
								linkedPlatform = platform.getId ();
								break;
							}
						}
						
						// Now at this point, if we still do not have Platform ID, that means, platform "IE" does not exists in this project
						if (linkedPlatform == 0)
						{
							throw new Exception ("Platform does not exists with this project");
						}
						
						// Link this platform with Test Suite
						final int linkId = qmetryPort.linkPlatformToTestSuite (token, testSuiteId, linkedPlatform);
						System.out.println ("Platform ID: " + linkedPlatform + " linked with Test Suite ID: " + testSuiteId + " (Internal Link ID: " + linkId + ")");
					}
					
					// So we have now TestSuite ID and Platform ID, now we want Test Case ID
					// So we want to execute Test Case with name "Browsing PetStore"
					
					com.qmetry.webservices.KeyValuePair searchKey = new com.qmetry.webservices.KeyValuePair();
					searchKey.setKey ("search");
					searchKey.setValue (RunTestCases.currTestDescription.trim());
					
					List searchList = new ArrayList ();
					searchList.add (searchKey);
					
					List testCases = qmetryPort.listTestCases (token, searchList);
					// If you execute bellow call, then it will list all Test Cases for selected build, in that case you need to
					// iterate through all test cases and compare name and get ID
					//List testCases = qmetryPort.listTestCases (token, "");
					
					if (testCases.size () == 0)
					{
						throw new Exception ("No Test Case found");
					}
					else if (testCases.size () > 1)
					{
						int i;
						//throw new Exception ("More than 1 Test Case found");
						for (i=1 ; i<testCases.size();i++)
						{
							testCases.remove(testCases.size()-1);
						}
					}
					
					// Iterate through all Test Suite returned by "listTestSuites" call
					Iterator testCaseIterator = testCases.iterator ();
					TestCaseEntity testCaseEntity = null;
					while (testCaseIterator.hasNext ())
					{
						testCaseEntity = (TestCaseEntity) testCaseIterator.next ();
					}
					
					// get Test Case Id
					final int testCaseId = testCaseEntity.getId ();
					
					// Make sure above Test Case is already linked with this Test Suite
					// If its not, then you can uncomment below code to link it with this Test Suite
					/*
					try
					{
						boolean isTestCaseLined = qmetryPort.linkTestCaseWithTestSuite (token, testCaseId, testSuiteId);
					}
					catch (javax.xml.ws.soap.SOAPFaultException linkEx)
					{
						System.out.println ("WARNING while linking TestCase with TestSuite: " + linkEx.getMessage ());
					}
					*/
					
					// Ok, now we have everything, lets execute test case and make it PASS
					// you can also specify pass, fail, blocked and notrun as status
					final String testCaseExecutionStatus = RunTestCases.flagTestCaseStatus;
					final String testCaseExecutionComments = "This Test Case executed";
					boolean execute = qmetryPort.executeTestCaseWithComments (token, testSuiteId, linkedPlatform, testCaseId, testCaseExecutionStatus, testCaseExecutionComments);
					if (execute)
					{
						System.out.println ("Test Case " +RunTestCases.currTestDescription +" has been executed and status updated on Qmetry.");
					}
					
				}
				catch (javax.xml.ws.soap.SOAPFaultException soapEx)
				{
					System.out.println ("ERROR : " + soapEx.getMessage ());
					//System.exit (1);
				}
				catch (Exception ex)
				{
					System.out.println ("ERROR : " + ex.getMessage ());
					//System.exit (1);
				}
    	}
	    else
	    {
	    	//Reporter.log("Warning: Qmetry parameter(s) not defined or Qmetry_Update flag is set to NO.");
	    	System.out.println("Warning: Qmetry parameter(s) not defined or Qmetry_Update flag is set to NO.");
	    }
    }
    
    public static boolean getQmetryParameters() throws IOException 
    {
    	// gets the current folder
    	
    	String varPath = driverScripts.FileReadWrite.getTemplateProjectFolder();
		String TestCasesXls = varPath+"\\src\\project_Files\\TestCases.xls";
    	String [][] arrQmetryParameters = ReadExcel.main(TestCasesXls,"Qmetry_Details");// contains qmetry parameters
    	QMETRY_USERNAME = arrQmetryParameters[1][1].trim();
    	QMETRY_PASSWORD = arrQmetryParameters[2][1].trim();
    	qmetryProject = arrQmetryParameters[3][1].trim();
    	qmetryRelease = arrQmetryParameters[4][1].trim();
    	qmetryBuild = arrQmetryParameters[5][1].trim();
    	qmetryTestSuite = arrQmetryParameters[6][1].trim();
    	qmetryPlatform = arrQmetryParameters[7][1].trim();
    	qmetryExecuteFlag = arrQmetryParameters[8][1].trim().toUpperCase().toString(); 
    	
    	if (!qmetryExecuteFlag.equals("YES") || QMETRY_USERNAME=="" || QMETRY_PASSWORD=="" || qmetryProject=="" || qmetryRelease=="" || qmetryBuild=="" || qmetryTestSuite=="" || qmetryPlatform=="")
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
}

