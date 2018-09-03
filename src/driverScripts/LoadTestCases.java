package driverScripts;

import java.io.*;

public class LoadTestCases {
	
	public static void main(String args[]) throws IOException{
		String strProject = args[0]; 
		File directory = new File (".");
		String currPath = directory.getCanonicalPath();
		//System.out.println(currPath);
		String [] arrPathSplit = currPath.split("qa-automation");
		File sourceLocation = new File(arrPathSplit[0]+"qa-automation\\"+strProject);
		//System.out.println(testCasePath);
		File targetLocation = new File (arrPathSplit[0]+"\\qa-automation\\Selenium_Automation\\Selenium_Automation\\Automation_Workspace\\Template_Project\\src\\project_Files");
		
		copyTestCaseToSrcFolder(sourceLocation,targetLocation);
		
		
	}
	public static void copyTestCaseToSrcFolder(File sourceLocation , File targetLocation) throws IOException{
	        if (sourceLocation.isDirectory()) {
	         
	            String[] children = sourceLocation.list();
	            for (int i=0; i<children.length; i++) {
	            	copyTestCaseToSrcFolder(new File(sourceLocation, children[i]),
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
	//}


