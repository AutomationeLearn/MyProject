/*This class loads the testcases from the Testcases.xls file*
 * it also forms an n-dimensional array stored as the value in the HashMap,
 *  which is then passed to the engine for the actual execution 
 */
package Framework_Scripts;
import java.util.ArrayList;
import java.util.HashMap;

import Framework_Scripts.ReadExcel;

/**
 * @author sujay
 *
 */
public class LoadTestCases{
	LoadTestCases ltc = new LoadTestCases();
	
	
	private HashMap<String , Object[][]> LoadTestcasesForExecution(){
		HashMap<String, Object[][]> testCaseStructure = new HashMap<>();
		
		ArrayList<String> TestCaseNumbers =  ReadExcel.getMainExcelSheetData();
		
		for (int i = 0; i < TestCaseNumbers.size(); i++) {
			
			testCaseStructure.put(TestCaseNumbers.get(i), ReadExcel.getExcelSheetData(TestCaseNumbers.get(i)));		
		
		}
		return testCaseStructure;
		
	}
	
	public HashMap<String , Object[][]> getTestcasesForExecution (){
		
		HashMap<String, Object[][]> gettestCaseStructure = ltc.LoadTestcasesForExecution();
		return gettestCaseStructure;
	}
	
}
