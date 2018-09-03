package driverScripts;

import org.testng.annotations.Test;
import java.awt.AWTException;
import java.io.IOException;
import java.util.Hashtable;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

public class RunTestCases  {		
	
	//declare all the variables
	public static WebDriver driver;
	public static String varResultPath;
	Hashtable<String, String> objRep;
	public static String keyWordXls,logfile,resultFileName;
	public static String objRepXls;
	public static String currTestCase;
	public static String testParameter;// test parameter  
	public static String testkeyword; // test keyword 
	String testObject,testStatus; // test object property  
	public double tcCount,tcFailCount,ncCount;
	public static RunTestCases runTestCases;
    public static String [][] arrTestCaseQmetry,arrTestCasesMain,arrTestCase,arrTestData;
	public static String currTestStep;
	public static String currTestDescription;
	public static String flagTestCaseStatus = "Pass";
	public static String varPath;
	public static long longTestSuiteStartTimeMilli;
	public static String strTestSuiteStartTime;
	public static int tcRow, iColObjectValue;
	public static double totalTc;
	public static double totalFailedTc;
	public static double totalPassedTc;
	public static double totalNotRunTc;
	public static double passRate;
	boolean testStepFlag, colFlag ;
	public static boolean driverFlag;
	int intProceedOnFailColumn;
	String currDataColumn,dataColumn;
 	
	public RunTestCases(String keyWordXls, String objRepXls) throws Exception {

		arrTestCaseQmetry = ReadExcel.main(keyWordXls,"Qmetry_Details");// contains qmetry project details
		String projectName = arrTestCaseQmetry[3][1];
		String timeStamp = SaveResult.dateTimeStamp();
		resultFileName = projectName+"_"+timeStamp;
      //SaveResult.copyTemplate(resultFileName); // copy result template and create new result file 
		
		SaveResult.main(); // copy result template and create new result file
		
		driverFlag=false;
		Reporter.log("Date : "+SaveResult.currentDate());
		strTestSuiteStartTime = SaveResult.currentTime();
		longTestSuiteStartTimeMilli = System.currentTimeMillis();
		tcCount=0;
		objRep = loadObjectRepository(objRepXls, "Repository"); //Load the object Repository
		//Read test cases 
		
		arrTestCasesMain = ReadExcel.main(keyWordXls,"Main");// contains list of tests cases 
		
		// Loop to iterate through all test cases 
			for (int i = 0; i < arrTestCasesMain.length; i++)
			{
				// Loop to find empty values 
				for (int j = 1; j < arrTestCasesMain[0].length; j++)
				{
					if (arrTestCasesMain[i][j]==""){
						arrTestCasesMain[i][j]="Empty";
					}
				}
			}
			
			//Loop to pick up the test case 
			for (int iRow = 1; iRow < arrTestCasesMain.length; iRow++)
			{
				int iColumnRunFlag = GetColumnIndex(arrTestCasesMain, "RunFlag");
				if (arrTestCasesMain[iRow][iColumnRunFlag].toUpperCase().equalsIgnoreCase("TRUE")) 
				{
					int intDataColumn = GetColumnIndex(arrTestCasesMain, "DataColumnName");// get column index
					currDataColumn = arrTestCasesMain[iRow][intDataColumn].trim();
					String [] myCol = currDataColumn.split(",");
					//Loop for multiple data column
					if (myCol.length>0)
					{
						for (int i=0; i<myCol.length; i++)
						{
							dataColumn =  myCol[i];
							if (dataColumn.contentEquals("Empty") ||  dataColumn.contentEquals("") )
							{
								int iColumnTestCaseId = GetColumnIndex(arrTestCasesMain, "TestCaseId");//gets the column 
								currTestCase = arrTestCasesMain[iRow][iColumnTestCaseId];// contains the current test case 
								int iColumnTestCaseDesc = GetColumnIndex(arrTestCasesMain, "Description");//gets the column 
								currTestDescription = arrTestCasesMain[iRow][iColumnTestCaseDesc];// contains the current test case
								/*Reporter.log("#############################################################");
								Reporter.log("Executing test   : "+currTestCase);
								Reporter.log("Test description : "+currTestDescription);
								Reporter.log("Start time       : "+SaveResult.currentTime());
								Reporter.log("#############################################################");*/
								tcCount++;
								ncCount++;
								if (myCol.length<2)
								{
									keywordLibrary.testStepError = keywordLibrary.testStepError + "\n"+"Please Provide Data Column Name in Main Sheet for test case"+currTestCase;
									//System.out.println("Please Provide Data Column Name in Main Sheet For "+currTestCase);
									break;
								}
								else
								{
									Reporter.log("Please Provide Data Column Name in Main Sheet for test case"+currTestCase);
								}
							}
							else
							{
								int iColumnTestCaseId = GetColumnIndex(arrTestCasesMain, "TestCaseId");//gets the column index
								currTestCase = arrTestCasesMain[iRow][iColumnTestCaseId];// contains the current test case 
								int iColumnTestCaseDesc = GetColumnIndex(arrTestCasesMain, "Description");//gets the column index
								currTestDescription = arrTestCasesMain[iRow][iColumnTestCaseDesc];// contains the current test case 
								SaveResult.writeTestCaseNameRow(RunTestCases.currTestCase+" - "+RunTestCases.currTestDescription);
								/*Reporter.log("#############################################################");
								Reporter.log("Executing test   : "+currTestCase);
								Reporter.log("Test description : "+currTestDescription);
								Reporter.log("Start time       : "+SaveResult.currentTime());
								Reporter.log("#############################################################");*/
								tcCount++;
								arrTestCase = ReadExcel.main(keyWordXls,currTestCase);
								flagTestCaseStatus = "Pass"; // set flag to true before executing each test case
								//Loop to read the test step values  
								CurrentTestCase:for (tcRow = 1; tcRow<arrTestCase.length; tcRow++)
								{
									//System.out.println("Current Step is :" +tcRow);
									iColObjectValue = GetColumnIndex(arrTestCase, "OutputValues");
									int iObjectColumn = GetColumnIndex(arrTestCase, "ObjectName");// get object name
									String testObjectName = arrTestCase[tcRow][iObjectColumn];
									testObject = objRep.get(testObjectName);// test object 
									int iKeywordColumn = GetColumnIndex(arrTestCase, "Keyword");// get column of keyword
									testkeyword = arrTestCase[tcRow][iKeywordColumn].trim(); // test keyword  
									int iTestStepColumn = GetColumnIndex(arrTestCase, "TSID");// get column index 
									currTestStep = arrTestCase[tcRow][iTestStepColumn];
									int iTestStepDescription = GetColumnIndex(arrTestCase, "Description");// get column index
									String currTestStepDescription = arrTestCase[tcRow][iTestStepDescription]; 
									int intDataValue = GetColumnIndex(arrTestCase, dataColumn);// get column index
									if (intDataValue==-1)
									{
										keywordLibrary.testStepError=keywordLibrary.testStepError + "\n"+("Data Column Name not found for Test Case "+currTestCase+ " in test case sheet.");
										//System.out.println("Data Column Name not found for Test Case"+currTestCase);
										break CurrentTestCase;
									}
									else
									{	
										driverFlag=true;
										String currDataValue = arrTestCase[tcRow][intDataValue].trim();
										if (currDataValue.contentEquals(""))
										{
											testParameter="";
											/*Reporter.log("-------------------------------------------------------------");
											Reporter.log("Executing test step - "+currTestStep);
											Reporter.log("Keyword - "+testkeyword);
											Reporter.log("Test object - "+testObject);*/
											testStepFlag = executeTcStep(testkeyword,testParameter, testObject);
											if (testStepFlag==false)
											{
												testStatus = "Fail";
												SaveResult.writeResultRow(currTestCase,currTestStep,currTestStepDescription,testkeyword,testParameter,testStatus);
											}
											else{
												testStatus = "Pass";
												SaveResult.writeResultRow(currTestCase,currTestStep,currTestStepDescription,testkeyword,testParameter,testStatus);
											}
											intProceedOnFailColumn = GetColumnIndex(arrTestCase, "ProceedOnFail");// get column index 
											if (testStepFlag==false)// check if test step failed
											{
												if(flagTestCaseStatus== "Pass")
												{
													tcFailCount++; //increment failed test cases count
												}
												flagTestCaseStatus = "Fail"; //mark test case as failed
											}
											if (testStepFlag==false && arrTestCase[tcRow][intProceedOnFailColumn].equals("NO"))
												{
													keywordLibrary.testStepError = keywordLibrary.testStepError + "\n"+"Test case "+currTestCase+ " failed at step "+currTestStep;
													Reporter.log("End time : "+SaveResult.currentTime());
													break CurrentTestCase;
												}


										}
										else
										{
											testParameter = currDataValue;
											/*Reporter.log("-------------------------------------------------------------");
											Reporter.log("Executing test step   - "+currTestStep);
											Reporter.log("Test step description - "+currTestStepDescription);
											Reporter.log("Keyword               - "+testkeyword);
											Reporter.log("Test object           - "+testObject);
											Reporter.log("Test Parameter        - "+testParameter);*/
											testStepFlag = executeTcStep(testkeyword,testParameter, testObject);
											if (testStepFlag==false)
											{
												testStatus = "Fail";
												SaveResult.writeResultRow(currTestCase,currTestStep,currTestStepDescription,testkeyword,testParameter,testStatus);
											}
											else{
												testStatus = "Pass";
												SaveResult.writeResultRow(currTestCase,currTestStep,currTestStepDescription,testkeyword,testParameter,testStatus);
											}
											int intProceedOnFailColumn = GetColumnIndex(arrTestCase, "ProceedOnFail");// get column index
											if (testStepFlag==false)// check if test step failed
											{
													if(flagTestCaseStatus== "Pass")
													{
														tcFailCount++;//increment failed test cases count
													}
													flagTestCaseStatus = "Fail";//mark test case as failed
											}
											if (testStepFlag==false && arrTestCase[tcRow][intProceedOnFailColumn].equals("NO"))
											{
												/*Reporter.log("-------------------------------------------------------------");
												Reporter.log("Test case "+currTestCase+ " failed at step "+currTestStep);
												Reporter.log("Aborting test case...");
												Reporter.log("-------------------------------------------------------------");
												Reporter.log("End time : "+SaveResult.currentTime());*/
												testStatus = "Fail";
												keywordLibrary.testStepError= keywordLibrary.testStepError + "\n"+"Test case "+currTestCase+ " failed at step "+currTestStep+"Aborting test case...";
												SaveResult.writeResultRow(currTestCase,currTestStep,currTestStepDescription,testkeyword,testParameter,testStatus);
												break CurrentTestCase;
											}
											
										}
									}
								}//End CurrentTestCase
							if(driverFlag==true){
							//call to qmetry to update test case status
							QMetryWSDLClient.main();}
							}//Test run end
						}//End loop for multiple data column
					}//End IF for multiple data column
					else
					{
						int iColumnTestCaseId = GetColumnIndex(arrTestCasesMain, "TestCaseId");//gets the column 
						currTestCase = arrTestCasesMain[iRow][iColumnTestCaseId];// contains the current test case 
						int iColumnTestCaseDesc = GetColumnIndex(arrTestCasesMain, "Description");//gets the column 
						currTestDescription = arrTestCasesMain[iRow][iColumnTestCaseDesc];// contains the current test case
						/*Reporter.log("#############################################################");
						Reporter.log("Executing test   : "+currTestCase);
						Reporter.log("Test description : "+currTestDescription);
						Reporter.log("Start time       : "+SaveResult.currentTime());
						Reporter.log("#############################################################");*/
						tcCount++;
						ncCount++;
						keywordLibrary.testStepError=keywordLibrary.testStepError + "\n"+("Please Provide Data Column Name in Main Sheet for test case"+currTestCase);
						//System.out.println("Please Provide Data Column Name in Main Sheet For "+currTestCase);
					}
				}
			}
	} RunTestCases() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public static void mytest() throws Exception {
		// gets the current folder
		varPath = driverScripts.FileReadWrite.getTemplateProjectFolder();
		varResultPath = varPath+"\\test-output";
		logfile = varResultPath+"\\html\\output.html"; 
		// Path to test cases and object repository excelsheets
		keyWordXls = varPath+"src\\project_Files\\TestCases.xls";
		String objRepXls = varPath+"src\\project_Files\\ObjectRepository.xls";
		runTestCases = new RunTestCases(keyWordXls, objRepXls);
		// add test case summary
		totalTc = RunTestCases.runTestCases.tcCount;
		totalFailedTc = RunTestCases.runTestCases.tcFailCount;
		totalNotRunTc = RunTestCases.runTestCases.ncCount;
		totalPassedTc = totalTc - totalFailedTc - totalNotRunTc;
		passRate = ((totalPassedTc/totalTc)*100);
		String currentTime = SaveResult.currentTime();
		long longSuiteEndTimeMilli=System.currentTimeMillis();
		long longTestDuration = longSuiteEndTimeMilli - longTestSuiteStartTimeMilli;
		int hours = (int) (longTestDuration/(1000*60*60));
		int minutes = (int) (longTestDuration/(1000*60))%60;
		int seconds = (int) (longTestDuration/1000)%60;
		SaveResult.writeHeaderTestRunDetails(totalTc, totalPassedTc, passRate, totalFailedTc, totalNotRunTc);
		String [][] arrTestCasesQmetry = ReadExcel.main(keyWordXls,"Qmetry_Details");// contains qmetry project details 
		String qmetryProject = arrTestCasesQmetry[3][1];
		String qmetryRelease = arrTestCasesQmetry[4][1];
		String qmetryBuild = arrTestCasesQmetry[5][1];
		String qmetryTestSuite = arrTestCasesQmetry[6][1];
		String qmetryPlatform = arrTestCasesQmetry[7][1];
		String flagUpdateMail = arrTestCasesQmetry[9][1];
				
		Reporter.log("-------------------------------------------------------------");
		Reporter.log("");
		Reporter.log("#######################"+qmetryTestSuite+ " test run summary#########################");
		Reporter.log("Project                 : "+qmetryProject);
		Reporter.log("Release                 : "+qmetryRelease);
		Reporter.log("Build                   : "+qmetryBuild);
		Reporter.log("Platform                : "+qmetryPlatform);
		Reporter.log("Test suite start time   : "+strTestSuiteStartTime);
		Reporter.log("Test suite end time     : "+currentTime);
		Reporter.log("Test suite duration     : "+hours+" Hours "+minutes+" Minutes "+seconds+" Seconds");
		Reporter.log("Total test cases        : "+(int)totalTc);
		Reporter.log("Passed test cases       : "+(int)totalPassedTc);
		Reporter.log("Failed test cases       : "+(int)totalFailedTc);
		Reporter.log("Not Run test cases      : "+(int)totalNotRunTc);
		Reporter.log("============================================================================");
		Reporter.log("Suite pass rate      :" +passRate+"%");
		Reporter.log("######################################################################################");
		//driverScripts.SaveResult.main();
		if (driverFlag==true){

			RunTestCases.driver.quit();
		}
		if (flagUpdateMail.toUpperCase().contentEquals("YES")){
				String mailbody = qmetryTestSuite.toUpperCase()+ " TEST RUN SUMMARY"+"\n"+"\n"+"Project                         : "+qmetryProject+"\n"+"Release                       : "+qmetryRelease+"\n"+"Build                            : "+qmetryBuild+"\n"+"Platform                      : "+qmetryPlatform+"\n"+"Test suite start time   :  "+strTestSuiteStartTime+"\n"+"Test suite end time    : "+currentTime+"\n"+"Test suite duration     : "+hours+" Hours "+minutes+" Minutes "+seconds+" Seconds"+"\n"+"Total test cases           : "+(int)totalTc+"\n"+"Passed test cases        : "+(int)totalPassedTc+"\n"+"Failed test cases          : "+(int)totalFailedTc+"\n"+"Suite pass rate             :" +passRate+"%";		
				String mailFooter = "\n"+"\n"+"\n"+"This is an auto generated mail. You can reply back using 'Reply' or 'Reply to all' option which will send a mail to the respective owners of this test automation run."+"\n"+"\n"+"This email and any attachments are confidential and may also be privileged. If you are not the addressee, do not disclose, copy, circulate or in any other way use or rely on the information contained in this email or any attachments. If received in error, notify the sender immediately and delete this email and any attachments from your system.";
				FileReadWrite.write(varResultPath+"\\html\\temp.txt",mailbody+mailFooter);
				String [] arrNull = null;
				mailResults.main(arrNull);
	}
	}

	/*@AfterClass
	public static void AfterClassSetup() throws Exception{
		// add test case summary
		totalTc = RunTestCases.runTestCases.tcCount;
		totalFailedTc = RunTestCases.runTestCases.tcFailCount;
		totalNotRunTc = RunTestCases.runTestCases.ncCount;
		totalPassedTc = totalTc - totalFailedTc - totalNotRunTc;
		passRate = ((totalPassedTc/totalTc)*100);
		String currentTime = SaveResult.currentTime();
		long longSuiteEndTimeMilli=System.currentTimeMillis();
		long longTestDuration = longSuiteEndTimeMilli - longTestSuiteStartTimeMilli;
		int hours = (int) (longTestDuration/(1000*60*60));
		int minutes = (int) (longTestDuration/(1000*60))%60;
		int seconds = (int) (longTestDuration/1000)%60;
		//String totalTime = hours+" Hours "+minutes+" Minutes "+seconds+" Seconds";
		SaveResult.writeHeaderTestRunDetails(totalTc, totalPassedTc, passRate, totalFailedTc, totalNotRunTc);
		String [][] arrTestCasesQmetry = ReadExcel.main(keyWordXls,"Qmetry_Details");// contains qmetry project details 
		String qmetryProject = arrTestCasesQmetry[3][1];
		String qmetryRelease = arrTestCasesQmetry[4][1];
		String qmetryBuild = arrTestCasesQmetry[5][1];
		String qmetryTestSuite = arrTestCasesQmetry[6][1];
		String qmetryPlatform = arrTestCasesQmetry[7][1];
		String flagUpdateMail = arrTestCasesQmetry[9][1];
				
		Reporter.log("-------------------------------------------------------------");
		Reporter.log("");
		Reporter.log("#######################"+qmetryTestSuite+ " test run summary#########################");
		Reporter.log("Project                 : "+qmetryProject);
		Reporter.log("Release                 : "+qmetryRelease);
		Reporter.log("Build                   : "+qmetryBuild);
		Reporter.log("Platform                : "+qmetryPlatform);
		Reporter.log("Test suite start time   : "+strTestSuiteStartTime);
		Reporter.log("Test suite end time     : "+currentTime);
		Reporter.log("Test suite duration     : "+hours+" Hours "+minutes+" Minutes "+seconds+" Seconds");
		Reporter.log("Total test cases        : "+(int)totalTc);
		Reporter.log("Passed test cases       : "+(int)totalPassedTc);
		Reporter.log("Failed test cases       : "+(int)totalFailedTc);
		Reporter.log("Not Run test cases      : "+(int)totalNotRunTc);
		Reporter.log("============================================================================");
		Reporter.log("Suite pass rate      :" +passRate+"%");
		Reporter.log("######################################################################################");
		driverScripts.SaveResult.main();
		if (driverFlag==true){
		RunTestCases.driver.quit();
		}
		if (flagUpdateMail.toUpperCase().contentEquals("YES")){
				String mailbody = qmetryTestSuite.toUpperCase()+ " TEST RUN SUMMARY"+"\n"+"\n"+"Project                         : "+qmetryProject+"\n"+"Release                       : "+qmetryRelease+"\n"+"Build                            : "+qmetryBuild+"\n"+"Platform                      : "+qmetryPlatform+"\n"+"Test suite start time   :  "+strTestSuiteStartTime+"\n"+"Test suite end time    : "+currentTime+"\n"+"Test suite duration     : "+hours+" Hours "+minutes+" Minutes "+seconds+" Seconds"+"\n"+"Total test cases           : "+(int)totalTc+"\n"+"Passed test cases        : "+(int)totalPassedTc+"\n"+"Failed test cases          : "+(int)totalFailedTc+"\n"+"Suite pass rate             :" +passRate+"%";		
				String mailFooter = "\n"+"\n"+"\n"+"This is an auto generated mail. You can reply back using 'Reply' or 'Reply to all' option which will send a mail to the respective owners of this test automation run."+"\n"+"\n"+"This email and any attachments are confidential and may also be privileged. If you are not the addressee, do not disclose, copy, circulate or in any other way use or rely on the information contained in this email or any attachments. If received in error, notify the sender immediately and delete this email and any attachments from your system.";
				FileReadWrite.write(varResultPath+"\\html\\temp.txt",mailbody+mailFooter);
				String [] arrNull = null;
				mailResults.main(arrNull);
	}
	}*/
	
	private boolean executeTcStep(String testkeyword, String testParameter, String testObject) throws AWTException, IOException {
		keywordLibrary keyWordLib = new keywordLibrary();
		boolean stepResult = keyWordLib.actions(testkeyword, testParameter, testObject);
		return stepResult;
	}
	
	public int GetColumnIndex(String arrExcelData[][], String columnName) throws IOException
	{
		int columnIndex = -1;
				
		CurrentLoop: for (int iColumn=0; iColumn<arrExcelData[0].length; iColumn++)
		{
			if (arrExcelData[0][iColumn].equals(columnName)) 
				{
					columnIndex = iColumn;
					break CurrentLoop;
				}
		}
		
		return columnIndex;
	}
	
	public Hashtable<String, String> loadObjectRepository(String xlsName, String wrkShtName) throws Exception {
		 
		    Hashtable<String, String> dict= new Hashtable<String, String>();		
		    
		    try {
		    	String [][]objRep = ReadExcel.main(xlsName, "Repository");
		    	for (int iRow=1;iRow<objRep.length;iRow++){
		    		dict.put(objRep[iRow][0], objRep[iRow][1]);
		    	}
					  return dict;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

}

 
