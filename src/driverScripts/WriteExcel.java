package driverScripts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class WriteExcel {
	
	public static void writeDataToExcelFile(String fileName,String sheetName, int sheetRow, int SheetCell, String Content) {
		
        try {
        	
        	FileInputStream fi = new FileInputStream (fileName);
        	HSSFWorkbook myWorkBook = new HSSFWorkbook(fi);
            HSSFSheet mySheet = (HSSFSheet) myWorkBook.getSheet(sheetName);
            HSSFRow myRow;
            HSSFCell myCell;
            
            myRow = (HSSFRow) mySheet.getRow(sheetRow);
            myCell = (HSSFCell) myRow.createCell(SheetCell);
            myCell.setCellValue(new HSSFRichTextString(Content));

            FileOutputStream out = new FileOutputStream(fileName);
            myWorkBook.write(out);
            out.flush();
            out.close();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
