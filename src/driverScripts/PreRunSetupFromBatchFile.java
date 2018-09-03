package driverScripts;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import org.apache.poi.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class PreRunSetupFromBatchFile {
	
public static void main(String args[]) {
// ******************************************Update browser variable**********************************************
	
	
	try {
		if (args.length >= 1){
			String value = args[0];
			File directory = new File (".");
			String testCasePath = directory.getCanonicalPath()+"\\Selenium_Automation\\Selenium_Automation\\Automation_Workspace\\Template_Project\\src\\project_Files\\ObjectRepository.xls";
			//System.out.println(testCasePath);
			String sheetName = "Repository";
			int sRow = 1;
			int sCol = 1;
			//writeDataToExcelFile(testCasePath, sheetName, sRow, sCol, value);
			FileInputStream fi = new FileInputStream (testCasePath);
			HSSFWorkbook myWorkBook = new HSSFWorkbook(fi);
		    HSSFSheet mySheet = (HSSFSheet) myWorkBook.getSheet(sheetName);
		    HSSFRow myRow;
		    HSSFCell myCell;
		    
		    myRow = (HSSFRow) mySheet.getRow(sRow);
		    myCell = (HSSFCell) myRow.createCell(sCol);
		    myCell.setCellValue(new HSSFRichTextString(value));
		
		    FileOutputStream out = new FileOutputStream(testCasePath);
		    myWorkBook.write(out);
		    out.flush();
		    out.close();
		}
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
//**********************************Copy test suite excel files to working directory*********************************
	
	try {
		if (args.length >= 2){
				File directory = new File (".");
				String source = directory.getCanonicalPath()+"\\"+args[1];
				String dest = directory.getCanonicalPath()+"\\Selenium_Automation\\Selenium_Automation\\Automation_Workspace\\Template_Project\\src\\project_Files";
				System.out.println(source);
				System.out.println(dest);
			    FileUtils.copyDirectory(new File (source), new File (dest));
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}

