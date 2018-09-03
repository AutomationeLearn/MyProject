package driverScripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RESTRequests {
	public static String main() throws IOException 
	{
		//testParameter=testParameter.replace("\n","");// use this to remove line breaks
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
			 try{
				 
					httpCon.setDoOutput(true);
					httpCon.setRequestMethod("POST");
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
