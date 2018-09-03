package driverScripts;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;


class FileReadWrite

{
 public static void write(String path, String content)
  {
  try{
  // Create file 
  FileWriter fstream = new FileWriter(path);
  BufferedWriter out = new BufferedWriter(fstream);
  out.write(content);
  //Close the output stream
  out.close();
  }catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
  }
  }

 public static String read(String path) {
	    File file = new File(path);
	    int ch;
	    StringBuffer strContent = new StringBuffer("");
	    FileInputStream fin = null;
	    try {
	      fin = new FileInputStream(file);
	      while ((ch = fin.read()) != -1)
	        strContent.append((char) ch);
	      fin.close();
	    } catch (Exception e) {
	      System.out.println(e);
	    }
	    return strContent.toString();
	  }
 
 public static void request(){
	 try{
		 
	 //String response = "";
	 String urlParameters = "";// "param1=a&param2=b&param3=c";
	 String request = "http://atom-collections-qa.highwire.org/testArunav/test2/atom";
	 URL url = new URL(request); 
	 HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
	 connection.setDoOutput(true);
	 connection.setDoInput(true);
	 connection.setInstanceFollowRedirects(false); 
	 connection.setRequestMethod("GET"); 
	 connection.setRequestProperty("Content-Type", "application/atom+xml"); 
	 connection.setRequestProperty("charset", "utf-8");
	 connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	 connection.setUseCaches (false);

	 DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	 wr.writeBytes(urlParameters);
	 wr.flush(); 	

	/* // Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line;
	    while ((line = rd.readLine()) != null) {
	    	response = response + line;
	    }
	    System.out.println(response);*/
	 wr.close();
	 connection.disconnect();
	 }
	 catch(Exception e){
		 System.out.println(e);
	 }

	 
 }
 public static String getTemplateProjectFolder() throws IOException{
	 boolean folderFound=false;
	 boolean systemFolder = false;
	 File directory = new File (".");
		String currPath = directory.getCanonicalPath();
		String[] subDirs = currPath.split(Pattern.quote(File.separator));
		String varPath="";
				
		for(int i =0;i < subDirs.length;i++) {
			varPath = varPath+subDirs[i]+"\\";
			if(subDirs[i].equals("Template_Project")){
				folderFound = true;
				break;
			}
			if(subDirs[i].equals("System32")){
				systemFolder = true;
				break;
			}
		}			
		
		if (folderFound==false){
			varPath = currPath+"\\Selenium_Automation\\Selenium_Automation\\Automation_Workspace\\Template_Project\\";
		}
		if (systemFolder==true){
			varPath = "C:\\Users\\highwiredogs\\Documents\\GitHub\\qa-automation\\Selenium_Automation\\Selenium_Automation\\Automation_Workspace\\Template_Project\\";
		}
System.out.println(varPath);
		return varPath;
 }
 public static String sendGetRequest() throws IOException
 	{
	String result="";
	String methodName="GET";
	String requestParameters="";
	String targetURL = "http://atom-collections-qa.highwire.org/testArunav/test2/atom";
	if (requestParameters != null && requestParameters.length () > 0)
	 	{
			targetURL += "?" + requestParameters;
	 	}
	
		URL url = new URL(targetURL);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	

	switch(methodName){
	
		case "GET":
		 	
		 	if (targetURL.startsWith("http://"))
		 	{
			 	// Send a GET request to the servlet
			 	try
			 	{
			 	// Construct data
			    //	StringBuffer data = new StringBuffer();
			 	 
			 	// Send data
			 	//String urlStr = targetURL;
			 	/*if (requestParameters != null && requestParameters.length () > 0)
				 	{
			 			targetURL += "?" + requestParameters;
				 	}
			 	URL url = new URL(targetURL);
			 	URLConnection conn = url.openConnection ();*/
			 	 
			 	// Get the response
			 	BufferedReader rd = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			 	StringBuffer sb = new StringBuffer();
			 	String line;
			 	while ((line = rd.readLine()) != null)
				 	{
				 		sb.append(line);
				 	}
			 	rd.close();
			 	result = sb.toString();
			 	System.out.println(result);
			 	} 
			 	catch (Exception e)
				 	{
				 		e.printStackTrace();
				 	}
			 	}
			break;
			
	 case "PUT":
		 try{
				 
				httpCon.setDoOutput(true);
				httpCon.setRequestMethod("PUT");
				OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
				out.write("Resource content");
				out.close();
				 
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			 	StringBuffer sb = new StringBuffer();
			 	String line;
			 	while ((line = rd.readLine()) != null)
				 	{
				 		sb.append(line);
				 	}
			 	rd.close();
			 	result = sb.toString();
			 	System.out.println(result);
			} 
	
		catch(Exception e){
			e.printStackTrace();
		}
		 
		break;
		
	 case "POST":
	 case "DELETE":
		 try{
			    httpCon.setDoOutput(true);
			    httpCon.setRequestProperty("Content-Type", "application/atom+xml" );
			    httpCon.setRequestMethod("DELETE");
			    httpCon.connect();
			 
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			 	StringBuffer sb = new StringBuffer();
			 	String line;
			 	while ((line = rd.readLine()) != null)
				 	{
				 		sb.append(line);
				 	}
			 	rd.close();
			 	result = sb.toString();
			 	System.out.println(result);
		} 

	catch(Exception e){
		e.printStackTrace();
	}
	 
	}
	return result;
  }
}

