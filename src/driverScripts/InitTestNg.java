package driverScripts;

import org.testng.annotations.Test;

public class InitTestNg {
	@Test
	public void startTest(){
		try {
			RunTestCases.mytest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
