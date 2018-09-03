package driverScripts;

import java.io.IOException;



public class mailResults {
	public static void main(String args[])  {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		String varPath;
		try {
			varPath = driverScripts.FileReadWrite.getTemplateProjectFolder();
		
		String varResultPath = varPath+"\\test-output";
		//String logfile = varResultPath+"\\html\\output.html"; 
		String keyWordXls = varPath+"\\src\\project_Files\\TestCases.xls";
		
		String [][] arrTestCasesQmetry = ReadExcel.main(keyWordXls,"Qmetry_Details");// contains qmetry project details 
	
		String flagUpdateMail = arrTestCasesQmetry[9][1];
		String mailRecipents = arrTestCasesQmetry[10][1];
		String mailReplyTo = arrTestCasesQmetry[11][1];
		
		String content = FileReadWrite.read(varResultPath+"\\html\\temp.txt");
		
		// gets the current folder

				if (flagUpdateMail.toUpperCase().contentEquals("YES")){		
					try {
						driverScripts.Mail.sendMail(mailReplyTo,mailRecipents,"Automated test run report",content,SaveResult.resultFilePath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
}
	
}

