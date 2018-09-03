package driverScripts;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

//import java.util.ArrayList;
//import java.util.List;

//import org.testng.TestNG;
//import org.testng.xml.XmlClass;
//import org.testng.xml.XmlSuite;
//import org.testng.xml.XmlTest;

public class RunSuite {
	@Test
	 public static void main(String args[]){
	  @SuppressWarnings("rawtypes")
	  List<Class> listnerClasses = new ArrayList<Class>();
	  listnerClasses.add(org.uncommons.reportng.HTMLReporter.class);
	     listnerClasses.add(org.uncommons.reportng.JUnitXMLReporter.class);
	  XmlSuite suite = new XmlSuite();
	  suite.setName("Automation test suite");
	   
	  XmlTest test = new XmlTest(suite);
	  test.setName("My Test");
	  List<XmlClass> classes = new ArrayList<XmlClass>();
	  classes.add(new XmlClass("driverScripts.RunTestCases"));
	  test.setXmlClasses(classes) ;
	  List<XmlSuite> suites = new ArrayList<XmlSuite>();
	  suites.add(suite);
	  TestNG tng = new TestNG();
	  tng.setListenerClasses(listnerClasses);
	  tng.setXmlSuites(suites);
	  tng.run();
	 }

}

