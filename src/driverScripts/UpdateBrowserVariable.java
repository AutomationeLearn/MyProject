package driverScripts;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class UpdateBrowserVariable {
	
public static void main(String args[]) {

	String value = args[0];
	
	File directory = new File (".");
	try{
		String testCasePath = directory.getCanonicalPath()+"\\Selenium_Automation\\Selenium_Automation\\Automation_Workspace\\Template_Project\\src\\project_Files\\ObjectRepository.xls";
		System.out.println(testCasePath);
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
	catch (Exception e)
	{
		e.printStackTrace();
	}
}
}
