/**This class reads the excel and sends data in the form of a 2 dimensional object array
 *  to the Load testcases to form the hashmap which is then fed as data for test execution.
 * 
 */
package Framework_Scripts;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author sujay
 *
 */


public class ReadExcel {
	
	private HSSFWorkbook wB;


	public static void main(String[] args) {
		
		
	}
	
	
	private Object[][] ReadSheetData(String sheetName){
		try {
			String Testcases = "/home/sujay/eclipse-workspace/Test_Automation/bin/Data_Files/Testcases.xls";
			FileInputStream fis = new FileInputStream(Testcases);
			wB = new HSSFWorkbook(fis);
			HSSFSheet es = wB.getSheet(sheetName);
			HSSFRow rw = es.createRow(0);
			
			
			int rows = es.getPhysicalNumberOfRows();
			int column = rw.getLastCellNum();
			
			Object [][] excelData = new Object [rows-1][column];
			
			
			for (int i = 1; i < rows; i++) {
				rw = es.getRow(i);
				for (int j = 0; j < column; j++) {
					HSSFCell ec = rw.getCell(j);
					excelData[i][j]= ec.getStringCellValue();
				}
				
			}
			
			fis.close();
			return excelData;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null; 
		}
		
		
	}
	
	
	public static Object[][] getExcelSheetData(String Sheet) {
		
		ReadExcel RE = new ReadExcel();
		
		Object[][] Sheetdata = Arrays.copyOf(RE.ReadSheetData(Sheet), RE.ReadSheetData(Sheet).length) ;
		return Sheetdata;
	}
	
	
	
	public static ArrayList<String> getMainExcelSheetData(){
		
		ReadExcel RE = new ReadExcel();
		Object[][] Sheetdata = Arrays.copyOf(RE.ReadSheetData("Main"), RE.ReadSheetData("Main").length) ;
		
		ArrayList<String> mainSheetdata = new ArrayList<String>();

		for (int i = 0; i < Sheetdata.length; i++) {
			if (Sheetdata[i][3].toString().equalsIgnoreCase("Y")) {
				
				mainSheetdata.add(Sheetdata[i][1].toString());
			}
		}
		
		return mainSheetdata;
	}
	
}
