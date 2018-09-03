package driverScripts;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * A simple POI example of opening an Excel spreadsheet
 * and writing its contents to the command line.
 * @author  Tony Sintes
 */
public class POIExample {

    public static void main(String[] args) {
        String fileName = "C:\\testPOIWrite.xls";
        writeDataToExcelFile(fileName);
        readDataToExcelFile(fileName);
    }

    public static void readDataToExcelFile(String fileName){
        try{
            FileInputStream fis = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);

            for (int rowNum = 0; rowNum < 10; rowNum++) {
                for (int cellNum = 0; cellNum < 5; cellNum++) {
                    HSSFCell cell = (HSSFCell) sheet.getRow(rowNum).getCell(cellNum);
                    System.out.println(rowNum+":"+cellNum+" = " + cell.getStringCellValue());
                }
            }
            
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }


    }
    public static void writeDataToExcelFile(String fileName) {
        try {

            HSSFWorkbook myWorkBook = new HSSFWorkbook();
            HSSFSheet mySheet = (HSSFSheet) myWorkBook.createSheet();
            HSSFRow myRow;
            HSSFCell myCell;

            for (int rowNum = 0; rowNum < 10; rowNum++) {
                myRow = (HSSFRow) mySheet.createRow(rowNum);
                for (int cellNum = 0; cellNum < 5; cellNum++) {
                    myCell = (HSSFCell) myRow.createCell(cellNum);
                    myCell.setCellValue(new HSSFRichTextString(rowNum + "," + cellNum));
                }
            }


            FileOutputStream out = new FileOutputStream(fileName);
            myWorkBook.write(out);
            out.flush();
            out.close();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
