package driverScripts;

import java.awt.AWTException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;


public class keywordLibrary {
	//public static WebDriver driver;
	public static String testStepError;
	public String replacedText, getData1;
	Boolean flagAction;
	String strFlag;
	String[] getData;
	WebDriver driver;
	WebDriverWait wait;
	
	public boolean actions(String testkeyword, String testParameter, String testObject) throws AWTException, FailingHttpStatusCodeException, MalformedURLException, IOException{
			
		testStepError = "";
		// Trim the keyword
			if (testkeyword!=null)
			{
				testkeyword = testkeyword.toUpperCase().trim();
			}
		//Trim the parameter		
			if (testParameter!=null)
			{
				testParameter = testParameter.trim();
			}
	    //Trim the test object value		
			if (testObject!=null)
			{
				testObject = testObject.trim();
			}

			try {
				if (testObject!="" || testObject!=null) {
					if (!testkeyword.equals("LAUNCH")&& !testkeyword.equals("OPEN_URL")&& !testkeyword.equals("CLOSE_BROWSER")){ 
						Wait<WebDriver> wait = new WebDriverWait(RunTestCases.driver,60);
						wait.until(visibilityOfElementLocated(By.xpath(testObject)));
					}
				}
			}
			catch (Exception e){
				testStepError = e.getMessage(); 
				strFlag = "false";
			}
			//System.out.println(driverScripts.RunTestCases.currTestCase);
			//System.out.println(driverScripts.RunTestCases.currTestStep);
		// Select action	
			switch (testkeyword){
			case "SENDKEY_ESCAPE":
				try{
					Actions builder_Enter = new Actions(RunTestCases.driver);
					builder_Enter.sendKeys(Keys.ESCAPE).build().perform();
					strFlag = "true";
					break;
				}
				
				catch(Exception e){
					//Reporter.log("Unable to click enter");
					//e.printStackTrace();
					strFlag = "false";
					break;  
				}
				
			case "SENDKEY_ENTER":
				try{
					Actions builder_Enter = new Actions(RunTestCases.driver);
					builder_Enter.sendKeys(Keys.ENTER).build().perform();
					strFlag = "true";
					break;
				}
				catch(Exception e){
					//Reporter.log("Unable to click enter");
					//e.printStackTrace();
					strFlag = "false";
					break;  
				}
				
			case "SENDKEY_TAB":
				try{
					Actions builder_Tab = new Actions(RunTestCases.driver);
					builder_Tab.sendKeys(Keys.TAB).build().perform();
					strFlag = "true";
					break;
				}
				catch(Exception e){
						//Reporter.log("Unable to click enter");
						//e.printStackTrace();
						strFlag = "false";
						break;  
					}
				
			case "SENDKEY_SHIFT_TAB":
				try{
					Actions builder_Shift_Tab = new Actions(RunTestCases.driver);
					builder_Shift_Tab.sendKeys(Keys.SHIFT, Keys.TAB).build().perform(); 
					strFlag = "true";
					break;
				}
				catch(Exception e){
						//Reporter.log("Unable to click enter");
						e.printStackTrace();
						Reporter.log(e.getMessage());
						strFlag = "false";
						break;  
					}
				
			case "SENDKEYS":
				try{
					Actions builder_keys = new Actions(RunTestCases.driver);
					builder_keys.sendKeys(testParameter).build().perform();
					//System.out.println(testParameter);
					strFlag = "true";
					break;
				}
				catch(Exception e){
						//Reporter.log("Unable to click enter");
						//e.printStackTrace();
						strFlag = "false";
						break;  
					}
			case "COMPARE_INNER_HTML":
				try{
				WebElement mygraph = RunTestCases.driver.findElement(By.xpath(testObject));
				String innerhtml = (String)((JavascriptExecutor)RunTestCases.driver).executeScript("return arguments[0].innerHTML;", mygraph); 
				if (innerhtml.contains(testParameter)==true)
					{
						strFlag = "true";
						break;
					}
				else
					{
						strFlag = "false";
						break;
					}
				}
				catch(NoSuchElementException e) {
				 testStepError = e.getMessage(); 
				 Reporter.log(e.getMessage());
				 e.printStackTrace();
				 strFlag = "false";
				 break;
				}	
				
				
			case "INPUT_TEXT" :
				try{
				WebElement textbox = RunTestCases.driver.findElement(By.xpath(testObject));
				textbox.clear();
				textbox.sendKeys(testParameter);
				Reporter.log("InputText "+testParameter+" on object"+testObject);
				strFlag = "true";
				break;
				}
				 catch (NoSuchElementException e) {
					 testStepError = e.getMessage();     
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
				
			case "MOUSE_HOVER" :
				try{
				     WebElement theTestObject = RunTestCases.driver.findElement(By.xpath(testObject));
				     Action hover = new Actions(RunTestCases.driver).moveToElement(theTestObject).build();
				     hover.perform();
				     strFlag = "true";
				     break;
				    }
				     catch (Exception e) {
				      testStepError = e.getMessage();       
				      Reporter.log(e.getMessage());
				      e.printStackTrace();
				      strFlag = "false";
				      break;  
				     } 
				
			case "CLICK" :
				try{
					WebElement link = RunTestCases.driver.findElement(By.xpath(testObject));
					link.click();
					//Reporter.log("Click "+testObject);
					strFlag = "true";
					break;
				}
				 catch (NoSuchElementException e) {
					 testStepError = e.getMessage();    
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
			
			case "DOUBLE_CLICK" :
	               try{
	                      WebElement object = RunTestCases.driver.findElement(By.xpath(testObject));
	                      Actions doubleclk = new Actions(driver);
	                      doubleclk.doubleClick(object);
	                      doubleclk.perform();
	                      Reporter.log("Double Click "+testObject);
	                      strFlag = "true";
	               }
	               catch (NoSuchElementException e) {
	            	      testStepError = e.getMessage();  
	            	      Reporter.log(e.getMessage());
	                      e.printStackTrace();
	                       strFlag = "false";
	                      break;  
	                }     
	               break;
	               
			case "LAUNCH":
				try{
				if (testObject!=null)
				{
					testObject = testObject.toUpperCase();
				}
				//RunTestCases.driver = new FirefoxDriver(); 
				
				switch (testObject){
				
		            case "FIREFOX" :
		            	FirefoxProfile profile = new FirefoxProfile();
		            	profile.setEnableNativeEvents(true);
		                RunTestCases.driver=new FirefoxDriver(profile);
		                RunTestCases.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		                RunTestCases.driver.manage().window().maximize();
		                RunTestCases.driver.get(testParameter);
		                break;
		
		            case "IE" :
		
		                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		                ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		                try {
		                RunTestCases.driver = new InternetExplorerDriver(ieCapabilities);
		                }
		                catch(Exception e){
		                		e.printStackTrace();
		                }
		                RunTestCases.driver.manage().window().maximize();
		                RunTestCases.driver.get(testParameter);
		                wait = new WebDriverWait(RunTestCases.driver, 60);
		                break;
		                
		            case "SAFARI" :
		            	
        				RunTestCases.driver = new SafariDriver();
        				wait = new WebDriverWait(RunTestCases.driver, 60);
		                //RunTestCases.driver.manage().window().maximize();
		                RunTestCases.driver.get(testParameter);
        				break;
		
		            case "CHROME" :
		
		                DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
		                String chromeBinary = System.getProperty(" ");
		                if (chromeBinary == null || chromeBinary.equals("")) {
		
		                String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
		
		                chromeBinary = "libs/chromedriver-" + os + (os.equals("win") ? ".exe" : "");
		
		                System.setProperty("webdriver.chrome.driver", chromeBinary);
		
		             }
		
		                RunTestCases.driver = new ChromeDriver(chromeCapabilities);
		                wait = new WebDriverWait(RunTestCases.driver, 60);
		               // RunTestCases.driver.manage().window().maximize();
		                RunTestCases.driver.get(testParameter);
		                break;
				}
				
				strFlag = "true";
				break;
				}
				catch (Exception e) {
					testStepError = e.getMessage(); 
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
				
			case "OPEN_URL":
				try{
				RunTestCases.driver.get(testParameter);
				strFlag = "true";
				break;
				}
				 catch (Exception e) {
					
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
								
			case "ASSERT_TITLE":
				try{
				String strCurrentPageTitle= RunTestCases.driver.getTitle();
				//Reporter.log(strCurrentPageTitle);
					if (testParameter.equals(strCurrentPageTitle)) {
					Reporter.log("Page title is as expected."+strCurrentPageTitle);
					}
				strFlag = "true";
				break;
				}
				catch (NoSuchElementException e) {
					testStepError = e.getMessage();    
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
			
			case "VERIFY_TEXT" :
				try{
				    WebElement text = RunTestCases.driver.findElement(By.xpath(testObject));
				    if (testParameter.equals(text.getText()))
				    {
				    strFlag = "true";
				    break;
				    }
				    else
				    {
				    	Reporter.log("Text value does not match. Text displayed is " +text.getText() +".");
				    	strFlag = "false";
				    	break;
				    }
				}
				 catch (NoSuchElementException e) {
					 testStepError = e.getMessage(); 
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
				
			case "VERIFY_TEXT_CONTAINS" :
				try{
				    WebElement text = RunTestCases.driver.findElement(By.xpath(testObject));
				    if ((text.getText().contains(testParameter)))
				    {
				    strFlag = "true";
				    break;
				    }
				    else
				    {
				    	Reporter.log("Text value does not contain expected string. Text displayed is " +text.getText() +".");
				    	strFlag = "false";
				    	break;
				    }
				}
				 catch (NoSuchElementException e) {
					 testStepError = e.getMessage(); 
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
				    
			case "VERIFY_OBJECT":
				try{
				WebElement myobj = RunTestCases.driver.findElement(By.xpath(testObject));
				if (myobj.isDisplayed()==true)
				{
					Reporter.log("Object with property "+testObject+" is displayed");
				}
				strFlag = "true";
				break;
				}
				catch (NoSuchElementException e) {
					testStepError = e.getMessage(); 
					 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
				
		    case "CLOSE_BROWSER" :
		    	try {
                   RunTestCases.driver.close();
                   strFlag = "true";
		    	}
		    	catch (Exception e){
		    		testStepError = e.getMessage(); 
		    		Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
		    	}
                   break;
                   
            case "BROWSER_BACK" :
            	try {
                   RunTestCases.driver.navigate().back();
                   strFlag = "true";
            	}
            	catch (Exception e) {
            		testStepError = e.getMessage(); 
            		Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
            	}
                   break;
                   
            case "BROWSER_FORWARD" :
            	try {
                   RunTestCases.driver.navigate().forward();
                   strFlag = "true";
            	}
            	catch (Exception e){
            		testStepError = e.getMessage(); 
            		Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
            	}
                   break;
                   
            case "SELECT_CHILD_WINDOW" :
            	try{
                   for(String winHandle : RunTestCases.driver.getWindowHandles()){
                	   RunTestCases.driver.switchTo().window(winHandle);
                   }                   
                   strFlag = "true";
                   break;
            	}
            	 catch (NoSuchElementException e) {
            		 testStepError = e.getMessage(); 
            		 Reporter.log(e.getMessage());
					 e.printStackTrace();
					 strFlag = "false";
					 break;  
				 }	
            	
            case "SELECT_DROPDOWN" :
            	try{
            	   strFlag = "true";
                   WebElement dropDownListBox = RunTestCases.driver.findElement(By.xpath(testObject));
                   Select clickThis = new Select(dropDownListBox);
                   clickThis.selectByVisibleText(testParameter);
                   break;
            	}
            	catch (Exception e) {
            		testStepError = e.getMessage(); 
					Reporter.log(e.getMessage());
					//System.out.println(e.getMessage());
					strFlag = "false";
   				    break;  
				 }	
            	
            case "SELECT_SPAN_DROPDOWN" :
                try{
                  String dropdownValue = "//ul[@role=\"listbox\" and @aria-hidden=\"false\"]/li/a[text()=\""+testParameter+"\"]";
                       WebElement dropDownListBox = RunTestCases.driver.findElement(By.xpath(testObject));
                       dropDownListBox.click();
                       WebElement dropDownValue = RunTestCases.driver.findElement(By.xpath(dropdownValue));
                       dropDownValue.click();
                       strFlag = "true";
                       break;
                 }
                catch (Exception e) {
                		testStepError = e.getMessage(); 
			            Reporter.log(e.getMessage());
			            //System.out.println(e.getMessage());
			            strFlag = "false";
			            break;  
                }
                
            case "REPLACE_TEXT" :
            	try{
            	String arrTestParameter[]; // define array for splitting parameter
            	arrTestParameter = testParameter.split(","); // split the test parameter and get 3 values , original string, string to be replaced, and new string to be placed
            	replacedText = arrTestParameter[0].replace(arrTestParameter[1], arrTestParameter[2]); // contains the replaced text as a global variable
            	strFlag = "true";
            	}
            	catch (Exception e){
            		testStepError = e.getMessage(); 
            		Reporter.log(e.getMessage());
					//System.out.println(e.getMessage());
					strFlag = "false";
            	}
            	break;
            	
			case "CLICK_CHILD_OBJECT" :
				try{
					String arrTestObjects []; 
					arrTestObjects	= testObject.split(","); 

					RunTestCases.driver.switchTo().activeElement();
					WebElement parent_Object = RunTestCases.driver.findElement(By.xpath(arrTestObjects[0]));
					WebElement childObject = parent_Object.findElement(By.xpath(arrTestObjects[1]));
					
					childObject.click();
					strFlag = "true";
					break;
				}
				catch(Exception e){
					testStepError = e.getMessage(); 
					Reporter.log(e.getMessage());
					strFlag = "false";
					break;
				}
				
			case "CHECK_CHECKBOX_RADIO_BTN":
                try{
                       WebElement chkbox = RunTestCases.driver.findElement(By.xpath(testObject));
                       
                       if (chkbox.isSelected()== true)
                       {
                              Reporter.log("Checkbox is already selected" +testObject);
                       }
                       else
                       {
                              chkbox.click();
                              if (chkbox.isSelected()== true)
                              {
                                     Reporter.log("Check box is selected"+testObject);
                              }
                       }
                       strFlag = "true";
                       }
                       catch (NoSuchElementException e) {
                    	   	  testStepError = e.getMessage();  
                    	      Reporter.log(e.getMessage());
                              e.printStackTrace();
                              strFlag = "false";
                              break;  
                        }     
                       break;
                       
          case "UNCHECK_CHECKBOX":
                try{
                       WebElement chkbox = RunTestCases.driver.findElement(By.xpath(testObject));
                       
                       if (chkbox.isSelected()!= true)
                       {
                              Reporter.log("Checkbox is already Unchecked" +testObject);
                       }
                       else
                       {
                              chkbox.click();
                              if (chkbox.isSelected()!= true)
                              {
                                     Reporter.log("Check box is Unchecked"+testObject);
                              }
                       }
                       strFlag = "true";
                       }
                       catch (NoSuchElementException e) {
                    	      testStepError = e.getMessage();   
                    	      Reporter.log(e.getMessage());
                              e.printStackTrace();
                              strFlag = "false";
                              break;  
                        }     
                       break;
                       
          case "VERIFY_IS_SELECTED_CHECKBOX_RADIO_BTN":
                try{
                       WebElement chkbox = RunTestCases.driver.findElement(By.xpath(testObject));
                              if (chkbox.isSelected()== true)
                              {
                                     Reporter.log("Checkbox is selected" +testObject);
                                     strFlag = "true";
                              }
                              else
                              {
                                     Reporter.log("Check box is Not selected"+testObject);
                                     strFlag = "false";
                              }
                       }
                       catch (NoSuchElementException e) {
                    	   	  testStepError = e.getMessage(); 
                    	      Reporter.log(e.getMessage());
                              e.printStackTrace();
                              strFlag = "false";
                              break;  
                        }     
                       break;
                       
          case "VERIFY_IS_NOT_SELECTED_CHECKBOX_RADIO_BTN":
                try{
                       WebElement chkbox = RunTestCases.driver.findElement(By.xpath(testObject));
                       
                       if (chkbox.isSelected()== false)
	                       {
	                              Reporter.log("Checkbox is not  selected" +testObject);
	                              strFlag = "true";
	                       }
                       else if (chkbox.isSelected()== true)
	                       {
	                              Reporter.log("Check box is selected"+testObject);
	                              strFlag = "false";
	                       }
                   }
               catch (NoSuchElementException e) {
            	      testStepError = e.getMessage(); 
            	   	  Reporter.log(e.getMessage());
                      e.printStackTrace();
                      strFlag = "false";
                      break;  
                }     
                       break;

			case "GET_TEXT" :
				try{
					WebElement get_text = RunTestCases.driver.findElement(By.xpath(testObject));
					getData1 = get_text.getText();
					WriteExcel.writeDataToExcelFile(RunTestCases.keyWordXls,RunTestCases.currTestCase,RunTestCases.tcRow,RunTestCases.iColObjectValue,getData1);
					strFlag = "true";
					}
				catch (NoSuchElementException e){
					testStepError = e.getMessage(); 
					strFlag = "false";
				}
				
				break;
                       
                     
			case "GET_SPLITTED_TEXT" :
				try{
					WebElement get_text = RunTestCases.driver.findElement(By.xpath(testObject));
					String [] mytxt = testParameter.split(",");
					getData = get_text.getText().split(mytxt[0]);
					int myVal = Integer.parseInt(mytxt[1]);
					getData1 = getData[myVal].trim();
					WriteExcel.writeDataToExcelFile(RunTestCases.keyWordXls,RunTestCases.currTestCase,RunTestCases.tcRow,RunTestCases.iColObjectValue,getData1);
					strFlag = "true";
					}
				catch (NoSuchElementException e){
					testStepError = e.getMessage(); 
					strFlag = "false";
				}
				
				break;
				
		//*************Mobile Voucher Req Keywords**************//

			case "USE_TEXT" :
				try{
					WebElement get_text = RunTestCases.driver.findElement(By.xpath(testObject));
					get_text.clear();
					get_text.sendKeys(getData[1].trim());
					strFlag = "true";
				}
				catch (Exception e){
					testStepError = e.getMessage(); 
					strFlag = "false";
				}
				break;
			
		//*************Mobile Voucher Req Keywords**************//         
                       
				   //*************Bpress Req Keywords**************//    
				      case "GET_SPLITTED_TEXT_BPRESS" :
				        try{
				          WebElement get_text = RunTestCases.driver.findElement(By.xpath(testObject));
				          //String [] mytxt = testParameter.split(" ");
				          getData = get_text.getText().split(" ");
				          System.out.println("Value in array is :" +getData[8]);
				          System.out.println("Value in array is :" +getData[10]);
				          String [] tempval = getData[10].split("");
				          System.out.println("Value in array is :" +tempval[1]);
				          int val1=Integer.parseInt(getData[8]);
				          int val2=Integer.parseInt(tempval[1]);
				          int sum= val1+val2;
				          System.out.println("Sum Is : " +sum);
				          //int myVal = Integer.parseInt(mytxt[1]);
				          getData1 = Integer.toString(sum);
				          WriteExcel.writeDataToExcelFile(RunTestCases.keyWordXls,RunTestCases.currTestCase,RunTestCases.tcRow,RunTestCases.iColObjectValue,getData1);
				          strFlag = "true";
				          }
				        catch (NoSuchElementException e){
				          testStepError = e.getMessage(); 
				          strFlag = "false";
				        }
				        
				        break;
				        
				        //*************Bpress Req Keywords**************//
				
				      case "USE_TEXT_BPRESS" :
				        try{
				          WebElement get_text = RunTestCases.driver.findElement(By.xpath(testObject));
				          get_text.clear();
				          get_text.sendKeys(getData1.trim());
				          strFlag = "true";
				        }
				        catch (Exception e){
				          testStepError = e.getMessage(); 
				          strFlag = "false";
				        }
				        break; 
	               
//############################################ IFRAME RELATED KEYWORDS############################################
			
			case "IFRAME_CLICK_OBJECT":
                try{
	                RunTestCases.driver.switchTo().defaultContent();
	                RunTestCases.driver.switchTo().frame(RunTestCases.driver.findElements(By.tagName("iframe")).get(0));
	                WebElement objLink = RunTestCases.driver.findElement(By.xpath(testObject));
	                objLink.click();
	                Reporter.log("Clicked iframe object.");
	                strFlag = "true";
	                
	                break;
                }
                catch(NoSuchElementException e) {
                	testStepError = e.getMessage(); 
                	Reporter.log(e.getMessage());
	                e.printStackTrace();
	                strFlag = "false";
	               
	                break;  
                }      
                
          case "IFRAME_INPUT_TEXT":
                try{
                	RunTestCases.driver.switchTo().defaultContent();
	                RunTestCases.driver.switchTo().frame(RunTestCases.driver.findElements(By.tagName("iframe")).get(0));
	                WebElement objTxtBox = RunTestCases.driver.findElement(By.xpath(testObject));
	                objTxtBox.sendKeys(testParameter);
	                strFlag = "true";
	                break;
                }
                catch(NoSuchElementException e) {
                	testStepError = e.getMessage();    
                	Reporter.log(e.getMessage());
	                e.printStackTrace();
	                strFlag = "false";
	                break;  
                }      

          case "IFRAME_VERIFY_OBJECT":
                try{
                	RunTestCases.driver.switchTo().defaultContent();
	                RunTestCases.driver.switchTo().frame(RunTestCases.driver.findElements(By.tagName("iframe")).get(0));
	                WebElement myObj = RunTestCases.driver.findElement(By.xpath(testObject));
	                if (myObj.isDisplayed()==true)
	                       {
	                              strFlag = "true";
	                              break;
	                       }
	                else
	                       {
	                              strFlag = "false";
	                              break;
	                       }
                }
                catch(NoSuchElementException e) {
                	testStepError = e.getMessage(); 
                	Reporter.log(e.getMessage());
	                e.printStackTrace();
	                strFlag = "false";
	                
	                break;  
                }  
                
          case "IFRAME_COMPARE_INNER_HTML":
				try{
					RunTestCases.driver.switchTo().defaultContent();
					RunTestCases.driver.switchTo().frame(RunTestCases.driver.findElements(By.tagName("iframe")).get(0));
					WebElement myObj = RunTestCases.driver.findElement(By.xpath(testObject));
					String innerhtml = (String)((JavascriptExecutor)RunTestCases.driver).executeScript("return arguments[0].innerHTML;", myObj); 
					if (innerhtml.contains(testParameter)==true)
						{
							strFlag = "true";
						}
					else
						{
							strFlag = "false";
						}
					}
				catch(NoSuchElementException e) {
					testStepError = e.getMessage(); 
					Reporter.log(e.getMessage());
					e.printStackTrace();
					strFlag = "false";
					break;  
				}	
				break;
				
          case "IFRAME_VERIFY_TEXT":
                try{
	                RunTestCases.driver.switchTo().defaultContent();
	                RunTestCases.driver.switchTo().frame(RunTestCases.driver.findElements(By.tagName("iframe")).get(0));
	                WebElement myObj = RunTestCases.driver.findElement(By.xpath(testObject));
	                if (testParameter.equals(myObj.getText()))
	                       {
	                              strFlag = "true";
	                              break;
	                       }
	                else
	                       {
	                              strFlag = "false";
	                              break;
	                       }
                }
                catch(NoSuchElementException e) {
                	testStepError = e.getMessage(); 
                	Reporter.log(e.getMessage());
	                e.printStackTrace();
	                strFlag = "false";
	                break;  
                }      
         
          case "SELECT_DROPDOWN_CUSTOM" :
	          try{
	        	  WebElement dropdown = RunTestCases.driver.findElement(By.xpath(testObject));
	        	  dropdown.click();
	        	  dropdown.sendKeys(testParameter);
	        	  strFlag="true";
	          	}
	          catch (Exception e){
	        	  testStepError = e.getMessage(); 
	        	  Reporter.log(e.getMessage());
	              e.printStackTrace();
	        	  strFlag = "false";
	          }
	          break;
	          
			default :
			  Reporter.log("No Such Keyword found " +testkeyword);
              break;
			
			case "VERIFY_EMAIL" :
	        	  
	        	  try {
	        		 boolean returnVal = Mail.readMail(testParameter);
	        		 if (returnVal == true){
	        			 strFlag = "true";
	        		 }
	        		 else
	        		 {
	        			 strFlag = "false";
	        		 }
	        	  }
	        	  catch (Exception e){
	        		  testStepError = e.getMessage(); 
	        		  Reporter.log(e.getMessage());
		              e.printStackTrace();
	        		  strFlag = "false";
	        	  }
	        	  break;	
	        	  
			case "GET" :
			case "PUT" :
			case "POST":
			case "DELETE":
			case "TEST_AREA":
				/*WebClient webClient = new WebClient();
				int code = webClient.getPage("http://www.google.com").getWebResponse().getStatusCode();
				System.out.println(code);*/
				
				/*String headerImagePath = "//a[@class=\"site-name\"]//img[1]";
				String headerTargetPath = "//a[@class=\"site-name\"]";
				WebElement headerLink = RunTestCases.driver.findElement(By.xpath(headerImagePath));
				headerLink.click();
				WebElement headerLinkLocation = RunTestCases.driver.findElement(By.xpath(headerTargetPath));
				String targetURL = headerLinkLocation.getAttribute("href").trim();
				String currentURL = RunTestCases.driver.getCurrentUrl().trim();
				if (targetURL.equals(currentURL))
				{
					System.out.println("expected url is "+targetURL+" and actual url is "+ currentURL);
				}
				System.out.println("expected url is "+targetURL+" and actual url is "+ currentURL);*/
			case "MOBILE_SITE_SANITY_RESPOSE_CODE":
				testStepError="";
				int returnCode=0;
				String strCopyrightFlag="";
				RunTestCases.driverFlag=false;
				if (testParameter.contains("http://")==false){
				testParameter="http://"+testParameter;
				}
				for(int i=0;i<3;i++){
					if (i==1){
						testParameter=testParameter+"/content/current";
					}
					if (i==2){
						testParameter=testParameter.replace("/content/current", "/content/by/year");
					}
						URL u = new URL(testParameter.trim()); 
						HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
						try{
						huc.setRequestMethod("GET"); 
						huc.setConnectTimeout(60000);
						huc.setReadTimeout(60000);
						huc.connect(); 
						//System.out.println(huc.getConnectTimeout());
						returnCode = huc.getResponseCode();
						}
						catch(IOException e){
							strFlag="false";
							testStepError = "URL "+testParameter+" returned error "+e+SystemUtils.LINE_SEPARATOR;
							break;
						}
						if (returnCode==200){
							strFlag="true";
							System.out.println(testParameter+" returned code "+ returnCode);
						}
						else{
							strFlag="false";
							testStepError = testStepError+". "+testParameter+" returned code "+returnCode+SystemUtils.LINE_SEPARATOR;
							System.out.println(testParameter+" returned code "+ returnCode);
							break;
						}
						BufferedInputStream  in = new BufferedInputStream(huc.getInputStream(), 8192);
						
				        byte [] buffer = new byte[8192];
				        int read;
				        while ((read = in.read(buffer)) != -1)
				            {
					            String tempstr = new String(buffer, 0, read, "ISO-8859-1");
					            if (tempstr.contains("Copyright"))
					            {
					            	strCopyrightFlag = "true";
					            	strFlag="true";
					            	break;
					            }
					            else
					            {
					            	strCopyrightFlag = "false";
					            	strFlag="false";
					            }
				            }
						in.close();
		
		
						if (returnCode == 200)
						{
							strFlag="true";
							if (strCopyrightFlag == "false"){
								strFlag="false";
								testStepError = testStepError+("Copyright is missing in "+testParameter+SystemUtils.LINE_SEPARATOR);
							}
						}
						else if (returnCode!=200)
						{
							strFlag="false";
							testStepError = testStepError+". "+testParameter+" returned code "+returnCode+SystemUtils.LINE_SEPARATOR;
						}
				}
				case "CHECK_RESPOSE_CODE":
					testStepError="";
					int retCode=0;
					RunTestCases.driverFlag=false;
					if (testParameter.contains("http://")==false){
					testParameter="http://"+testParameter;
					}
					
					URL u = new URL(testParameter.trim()); 
					HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
					try{
					huc.setRequestMethod("GET"); 
					huc.setConnectTimeout(60000);
					huc.setReadTimeout(60000);
					huc.connect(); 
					//System.out.println(huc.getConnectTimeout());
					retCode = huc.getResponseCode();
					}
					catch(IOException e){
						strFlag="false"; 
						testStepError = "URL "+testParameter+" returned error "+e+SystemUtils.LINE_SEPARATOR;
						break;
					}
					if (retCode==200){
						strFlag="true";
						System.out.println(testParameter+" returned code "+ retCode);
					}
					else{
						strFlag="false";
						testStepError = testStepError+". "+testParameter+" returned code "+retCode+SystemUtils.LINE_SEPARATOR;
						System.out.println(testParameter+" returned code "+ retCode);
						break;
					}
	
					if (retCode == 200)
					{
						strFlag="true";

					}
					else if (retCode!=200)
					{
						strFlag="false";
						testStepError = testStepError+". "+testParameter+" returned code "+retCode+SystemUtils.LINE_SEPARATOR;
					}
				}		
				
			
			if (strFlag=="true")
			{
				flagAction=true;
				//System.out.println(driverScripts.RunTestCases.currTestStep+ " - Passed");
			}
			else if (strFlag=="false" & RunTestCases.driverFlag==true)
			{
				try {
					takeScreenshot();
				} catch (Exception e) {
					// TODO Auto-generated catch block*/
					testStepError = testStepError + e.getMessage();
					System.out.println(driverScripts.RunTestCases.currTestStep+ " - Failed!");
					Reporter.log(e.getMessage());
				}
				flagAction=false;
			}
			else if (strFlag=="false" & RunTestCases.driverFlag==false){
				flagAction=false;
				System.out.println("failed - "+testStepError);
			}
			testkeyword = ""; 
			testParameter = "";
			testObject = "";
			return flagAction;
			}
	
	
		
	
		public void takeScreenshot() throws WebDriverException, IOException
			{
				String myTimeStamp = SaveResult.dateTimeStamp();
				
				new File("D:\\Results\\Screenshots\\").mkdirs();
				FileOutputStream file = new FileOutputStream("D:\\Results\\Screenshots\\" +RunTestCases.currTestCase+"_"+RunTestCases.currTestStep+"_"+myTimeStamp + ".png");
				file.write(((TakesScreenshot) RunTestCases.driver).getScreenshotAs(OutputType.BYTES));
				file.close();
			}

		public ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) {
	        return new ExpectedCondition<WebElement>() {
	          public WebElement apply(WebDriver driver) {
	            WebElement element = driver.findElement(by);
	            return element.isDisplayed() ? element : null;
	          }
	        };
	      }


		}
