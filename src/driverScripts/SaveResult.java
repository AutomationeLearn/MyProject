package driverScripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class SaveResult {
	public static String Result;
	public static String resultFolderPath,resultFilePath;
	
	public static void main() throws IOException
	{
		File directory = new File (".");
		String currPath = directory.getCanonicalPath();
		String[] subDirs = currPath.split(Pattern.quote(File.separator));
		resultFolderPath = subDirs[0]+"\\Results";
		resultFilePath = resultFolderPath+"\\"+RunTestCases.resultFileName+".html";
		copyTemplate(RunTestCases.resultFileName);
		writeHeaderProjectDetails();
	}

	public static void writeResultRow(String currTestCase,String currTestStep,String currTestStepDescription,String testkeyword,String testParameter,String testStatus){
		String [] arrResultRow;
		arrResultRow = new String[6];
		arrResultRow[0]= "<td style=\"text-align: center;\">"+"\n"+currTestStep+"</td>"+"\n";
		arrResultRow[1]= "<td>"+"\n"+currTestStepDescription+"</td>"+"\n";
		arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+testkeyword+"</td>"+"\n";
		arrResultRow[3]= "<td style=\"text-align: center;\">"+"\n"+testParameter+"</td>"+"\n";
		arrResultRow[4]= "<td style=\"text-align: center;\">"+"\n"+testStatus+"</td>"+"\n";
		arrResultRow[5]= "<td style=\"text-align: center;\">"+"\n"+keywordLibrary.testStepError+"</td>"+"\n";
		
		//Check for null or empty in any value and replace with -
		
	    if (currTestStep == "" || currTestStep== null)
	    	arrResultRow[0]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (currTestStepDescription == "" || currTestStepDescription== null)
	    	arrResultRow[1]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (testkeyword == "" || testkeyword== null)
	    	arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (testParameter == "" || testParameter== null)
	    	arrResultRow[3]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (testStatus == "" || testStatus== null)
	    	arrResultRow[4]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (keywordLibrary.testStepError == "" || keywordLibrary.testStepError== null)
	    	arrResultRow[5]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";

		if (testStatus=="Fail")
			arrResultRow[4]= "<td style=\"text-align: center; color: rgb(251,0,0) \">"+"\n"+"<strong>Fail</strong>"+"\n"+"</td>"+"\n";	
		
		if (testStatus=="Pass")
			arrResultRow[4]= "<td style=\"text-align: center; color: rgb(34,177,76) \">"+"\n"+"<strong>Pass</strong>"+"\n"+"</td>"+"\n";
		
		String newResultRow = "\n"+"<tr>"+"\n"+arrResultRow[0]+"\n"+arrResultRow[1]+"\n"+arrResultRow[2]+"\n"+arrResultRow[3]+"\n"+arrResultRow[4]+"\n"+arrResultRow[5]+"\n"+"</tr>"+"\n";
			
		String Result = FileReadWrite.read(resultFilePath);
		
		String newResult = Result + newResultRow;
				
		FileReadWrite.write(resultFilePath, newResult);
	}
	
	public static void writeTestCaseNameRow(String testName){
		String testCaseNameRow = FileReadWrite.read(resultFilePath);
		String testCaseName = "<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+testName+"\n"+"</tr>";
		testCaseNameRow = testCaseNameRow + testCaseName;
		FileReadWrite.write(resultFilePath, testCaseNameRow);
	}
	
	public static void copyTemplate(String fileName) throws IOException{
		String varPath = driverScripts.FileReadWrite.getTemplateProjectFolder();
		String resultPath = varPath+"\\Test_Report_Template.html";
		
		Result = FileReadWrite.read(resultPath);
		
		FileReadWrite.write(resultFilePath, Result);
	}
	
	public static void writeHeaderProjectDetails(){
		String header = FileReadWrite.read(resultFilePath);
		header = header.replace("ProjectNameVariable", RunTestCases.arrTestCaseQmetry[3][1]);
		header = header.replace("ReleaseNameVariable",RunTestCases.arrTestCaseQmetry[4][1]);
		header = header.replace("BuildVariable",RunTestCases.arrTestCaseQmetry[5][1]);
		header = header.replace("PlatformVariable", RunTestCases.arrTestCaseQmetry[7][1]);
		header = header.replace("ExecutedByVariable", RunTestCases.arrTestCaseQmetry[1][1]);
		header = header.replace("StartTimeVariable", currentDate()+", "+currentTime());
		FileReadWrite.write(resultFilePath, header);
		
	}
	
	public static void writeHeaderTestRunDetails(Double total,Double passed, Double passRate, Double failed, Double notRun) throws IOException{
		writeNotRunTests();
		String resultRow = FileReadWrite.read(resultFilePath);
		String myTotal = ""+total.shortValue();
		String myPassed = ""+passed.shortValue();
		String myPassRate = ""+passRate.shortValue();
		String myFailed = ""+failed.shortValue();
		String InvalidVariable = ""+notRun.shortValue();
		resultRow = resultRow.replace("TotalTestCasesVariable", myTotal);
		resultRow = resultRow.replace("PassedVariable", myPassed);
		resultRow = resultRow.replace("PassRateVariable", myPassRate+"%");
		resultRow = resultRow.replace("FailedVariable", myFailed);
		resultRow = resultRow.replace("InvalidVariable", InvalidVariable);
		resultRow = resultRow.replace("EndTimeVariable",  currentDate()+", "+currentTime());
		
		//variable for graph generation
		resultRow = resultRow.replace("PassCount", myPassed);
		resultRow = resultRow.replace("FailCount", myFailed);
		resultRow = resultRow.replace("NotRunCount", InvalidVariable);
		String varProjPath = driverScripts.FileReadWrite.getTemplateProjectFolder();
		String resultPath = varProjPath+"\\Footer.html";
		String footer = FileReadWrite.read(resultPath);
		resultRow = resultRow + footer;
		FileReadWrite.write(resultFilePath, resultRow);
	}
	
	public static void writeNotRunTests(){
		String notRunTests="",NATests="",blockedTests="";
		for (int i=1; i<RunTestCases.arrTestCasesMain.length; i++){
			if (RunTestCases.arrTestCasesMain[i][2].equalsIgnoreCase("FALSE"))
				notRunTests= notRunTests+"\n"+"<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: left;background-color: #FFFFFF;\">"+"\n"+RunTestCases.arrTestCasesMain[i][0]+" - "+RunTestCases.arrTestCasesMain[i][1]+"\n"+"</tr>";
			if (RunTestCases.arrTestCasesMain[i][2].equalsIgnoreCase("NA"))
				NATests= NATests+"\n"+"<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: left;background-color: #FFFFFF;\">"+"\n"+RunTestCases.arrTestCasesMain[i][0]+" - "+RunTestCases.arrTestCasesMain[i][1]+"\n"+"</tr>";
			if (RunTestCases.arrTestCasesMain[i][2].equalsIgnoreCase("BLOCKED"))
				blockedTests= blockedTests+"\n"+"<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: left;\">"+"\n"+RunTestCases.arrTestCasesMain[i][0]+" - "+RunTestCases.arrTestCasesMain[i][1]+"\n"+"</tr>";
		}
		String testResult = FileReadWrite.read(resultFilePath);
		testResult= testResult+"\n"+"<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\"><strong>"+"\n"+"Tests Not Run"+"\n"+"</strong></tr>"+notRunTests;
		testResult= testResult+"\n"+"<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\"><strong>"+"\n"+"Tests Not Applicable"+"\n"+"</strong></tr>"+NATests;
		testResult= testResult+"\n"+"<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\"><strong>"+"\n"+"Blocked Tests"+"\n"+"</strong></tr>"+blockedTests;
		FileReadWrite.write(resultFilePath, testResult);
	}
	
	public static String dateTimeStamp()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("dd_MMM_yyyy_hh_mm");  
	      String currentDate = sdf.format(date);
	      return currentDate;
	}
	
	public static String currentDate()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("dd MMM yyyy");  
	      String currentDate = sdf.format(date);
	      return currentDate;
	}
	
	
	public static String currentTime()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("hh:mm:ss");  
	      String currentTimeStamp = sdf.format(date);
	      return currentTimeStamp;
	}
	
	public static String currentTimeZone()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("zzz");  
	      String currentDate = sdf.format(date);
	      return currentDate;
	}
	
	public static void copyFile(){
		  // Date dNow = new Date(0);
				String myTimeStamp = dateTimeStamp();
			      try {
			    	  SaveResult.copyDirectory(new File(driverScripts.RunTestCases.varResultPath),new File(resultFolderPath+myTimeStamp));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public static void copyDirectory(File sourceLocation , File targetLocation)
		    throws IOException {
		        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
               // targetLocation.mkdir();
            
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
	
}
	    


