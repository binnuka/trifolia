package trifolia.pkg;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;

import com.google.common.base.Predicate;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait; 
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;


@Ignore
public class C_ImplementationGuideFunctions {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private static Boolean createdImplementationGuide = false;
  public C_ImplementationGuideFunctions() {}
  
  public void setDriver(WebDriver driver){
	  this.driver = driver;
  }
  public void WebDriverWait(WebDriver driver, long timeOutInSeconds) {
	  }

  
  @Before
  public void initialize(){
	  ProfilesIni allProfiles = new ProfilesIni();
	  FirefoxProfile profile = allProfiles.getProfile("default");
	  if(this.driver == null){
		  //initialize a driver since one has not been provided already
		  this.driver = new FirefoxDriver(profile);
	  }
  }
  
  public void waitForPageLoad() 
  {
	    WebDriverWait wait = new WebDriverWait(driver, 60);
	     wait.until(ExpectedConditions.jsReturnsValue("return document.readyState ==\"complete\";"));		
  }
  
  public void waitForBindings(String waitForBinding) 
  {
        JavascriptExecutor js = (JavascriptExecutor)driver;	
	  	WebDriverWait wait = new WebDriverWait(driver, 60);
	  	wait.until(ExpectedConditions.jsReturnsValue("return !!ko.dataFor(document.getElementById('"+waitForBinding+"'))"));  
  }
  
  public void OpenIGBrowser()
  {
		// Confirm Welcome Message is present
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"),"Welcome to Trifolia Workbench!"));
		assertTrue("Unable to confirm Login",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S][\\s\\S]*$"));

	    //Open the IG Browser 
		 WebDriverWait wait1 = new WebDriverWait(driver, 60);
	 	 WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[2]/a")));
		 driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[2]/a")).click();
		 WebDriverWait wait2 = new WebDriverWait(driver, 60);
	     wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[1]/div/div[2]/ul/li[2]/ul/li[1]/a"),"Implementation Guides"));
		 driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[2]/ul/li[1]/a")).click();
		 
		 // Wait for page to fully load
		    waitForPageLoad();
		    
	     // Wait for the bindings to complete
		    waitForBindings("BrowseImplementationGuides");
	    
	    //Confirm the IG Browser appears
	    WebDriverWait wait3 = new WebDriverWait(driver, 60);
	    WebElement element3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BrowseImplementationGuides")));
	    assertTrue("Could not find \"Browse Implementation Guides\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Browse Implementation Guides[\\s\\S]*$"));
	       
	    // Clear existing Search Criteria
	       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/button")).click();
	    
	    // Wait for page to fully load
		    waitForPageLoad();
		    
	     // Wait for the bindings to complete
		    waitForBindings("BrowseImplementationGuides");
		    
	    // Confirm existing Search Criteria is cleared
	       WebDriverWait wait4 = new WebDriverWait(driver, 60);                    
	       wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/table/tbody/tr[2]/td[4]")));    
	    	
	       // Wait for page to fully load
		     waitForPageLoad(); 
		     
	       // Wait for the bindings to complete
	          waitForBindings("BrowseImplementationGuides");
  } 
  
  public void FindImplementationGuide(String implementationGuideName) throws Exception {
	
	  // Wait for page to fully load
	     waitForPageLoad();
	     
	  // Wait for the bindings to complete
	     waitForBindings("BrowseImplementationGuides");
	  
	// Confirm the Search options are available
		 WebDriverWait wait = new WebDriverWait(driver, 60);
	 	 WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[2]/a")));
	 	    
    // Search for the Implementation Guide
       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/input")).sendKeys(implementationGuideName);
    
       // Wait for page to fully load
	     waitForPageLoad();
	     
       // Wait for the bindings to complete
 	      waitForBindings("BrowseImplementationGuides");
 	  
    //Confirm the search is complete
      WebDriverWait wait3 = new WebDriverWait(driver, 60);                    
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/table/tbody/tr[2]/td[4]")));    
    
    //Confirm the correct IG is found
      WebDriverWait wait4 = new WebDriverWait(driver, 120);                    
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[1]"), implementationGuideName));
      assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);
 }
  
  public void ConfirmIGViewer(String implementationGuideName) throws Exception
  {
	  // Wait for page to fully load
	    waitForPageLoad();
	    
	  // Wait for the bindings to complete
	     waitForBindings("ViewImplementationGuide");
	
	// Confirm the IG Viewer appears.
	  WebDriverWait wait = new WebDriverWait(driver, 60);                     
  	  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/ul/li[1]/a"), "Templates/Profiles"));
     
   // Confirm the Search box is available /html/body/div[2]/div/div/div[3]/div[1]/div[1]/div[1]
  	  WebDriverWait wait1 = new WebDriverWait(driver, 60);                     
 	  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[1]/div[1]/div[1]"), "Search"));
  	  
	// Confirm the correct IG appears in the IG Vewer.
       WebDriverWait wait4 = new WebDriverWait(driver, 60);                     
	   wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[2]/div/h2"), implementationGuideName));
       assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);

  }
  public void ConfirmIGEditor(String implementationGuideName) throws Exception
  {
	  // Wait for page to fully load
	     waitForPageLoad();
	    
	  // Wait for the bindings to complete
         waitForBindings("EditImplementationGuide");
      
      // Wait for page to fully load
	     waitForPageLoad();
	   
     // Wait for the bindings to complete
         waitForBindings("EditImplementationGuide");
         
         WebDriverWait wait = new WebDriverWait(driver, 5);		
    	 wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body/div[8]"))); 
    	 
    	 // Wait for the bindings to complete
         waitForBindings("EditImplementationGuide");
	     
      // Confirm the Edit Implementation Guide Editor appears
	    WebDriverWait wait1 = new WebDriverWait(driver, 60);
	    // WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("EditImplementationGuide")));
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("EditImplementationGuide"), "Edit Implementation Guide"));	
	    assertTrue("Could not find \"Edit Implementation Guide\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Edit Implementation Guide[\\s\\S]*$"));
		 
	    // Confirm Text on IG Editor page
	    WebDriverWait wait2 = new WebDriverWait(driver, 60);                     
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[2]/div[1]/label"), "Access Manager"));	
	     
	    // Confirm the Organization option is available.  
	    //driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	    WebDriverWait wait9 = new WebDriverWait(driver, 60);
	    WebElement element9 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[3]/select")));   

     }
  public void SaveImplementationGuide(String implementationGuideName) throws Exception {
		
      // Save the Implementation Guide
      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button[1]")).click();
 
	//Confirm the Alert appears
	WebDriverWait wait = new WebDriverWait(driver, 60);
	wait.until(ExpectedConditions.alertIsPresent());
	
	 // Switch the driver context to the "Successfully saved implementation Guide" alert
	 Alert alertDialog1 = driver.switchTo().alert();
	 // Get the alert text
	 String alertText1 = alertDialog1.getText();
	 // Click the OK button on the alert.
	 alertDialog1.accept();

	 // Wait for page to fully load
        waitForPageLoad();
     
	 // Wait for the bindings to complete
        waitForBindings("EditImplementationGuide");
        
	 WebDriverWait wait1 = new WebDriverWait(driver, 5);		
	 wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body/div[8]"))); 
	 
}
  public void ReturnHome(String welcomeMessage) throws Exception {
	  
	   // Return to the Trifolia Home Page
	    driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[1]/a")).click();
	    WebDriverWait wait = new WebDriverWait(driver, 60);
	    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("appnav")));
	    
	   // Wait for page to fully load
          waitForPageLoad();
         
	   // Wait for Bindings to complete
	      waitForBindings("appnav");
	      
	    //Confirm the Welcome Message appears
		WebDriverWait wait1 = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"), welcomeMessage));
		assertTrue("Could not find \"Welcome To Trifolia\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(welcomeMessage) >= 0);   
}

  @Test
  //Browse an existing Implementation Guide
  public void BrowseImplementationGuide(String implementationGuideName, String Item, String Primitive, String Entries, String fileName, String validationText, String templateType, String cardinality, 
		  String customSchematron, String Permission, String Volume, String Category, String permissionUserName) throws Exception {
	
	  // Open the IG Browser
	     OpenIGBrowser();
	  
	  // Find the Implementation Guide
	  if (permissionUserName == "lcg.admin") 
	     {
		  FindImplementationGuide(implementationGuideName);   
	     }
	  
	  if (permissionUserName == "lcg.user") 
	  	 {
		  FindImplementationGuide(implementationGuideName);
	  	 }
	  
	// Open the IG Viewer  
	    if (permissionUserName == "lcg.admin") 
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, 60);
		 	    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[5]/div/button")));
			    driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[5]/div/button")).click();
			}
	    if (permissionUserName == "lcg.user") 
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, 60);
		 	    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/table/tbody/tr[1]/td[4]/div/button")));
			    driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr[1]/td[4]/div/button")).click();
		    }
    // Confirm the correct IG appears in the viewer 
	    
	    if (permissionUserName == "lcg.admin") 
	    {
	    ConfirmIGViewer(implementationGuideName);
	    }
	    if (permissionUserName == "lcg.user") 
	    {
	    ConfirmIGViewer(implementationGuideName);
	    }
	    
    if (permissionUserName == "lcg.admin") 
	    {
	    // Notes Tab Validation
   	 
    	 // Wait for page to fully load
	        waitForPageLoad();
	     
    	 // Wait for the bindings to complete
		    waitForBindings("ViewImplementationGuide");
		    
		// Open the Notes Tab
    	WebDriverWait wait = new WebDriverWait(driver, 60);
 	    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/ul/li[2]/a")));
    	driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[2]/a")).click();
	    
   	 // Wait for page to fully load
        waitForPageLoad();
     
	 // Wait for the bindings to complete
	    waitForBindings("ViewImplementationGuide");
	    
	 // Confirm page is properly refreshed
		WebDriverWait wait1 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[2]/table/thead/tr/th[1]"), "Type"));
	  
	 // Confirm Notes values appear
    	WebDriverWait waitA = new WebDriverWait(driver, 120);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[2]/table/thead/tr/th[2]"), "Item"));
	    
	    // Open Primitives Tab 
	    WebDriverWait wait2 = new WebDriverWait(driver, 60);
 	    WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/ul/li[3]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[3]/a")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("ViewImplementationGuide");
		  
	    // Confirm information in the Primitives Tab
	    WebDriverWait wait3 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[3]/table/thead/tr/th[3]"), "Primitive"));
        assertTrue("Could not find \"Primitive\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(Primitive) >= 0);
	  
	    // Open the Audit Trail Tab 
	    WebDriverWait wait4 = new WebDriverWait(driver, 60);
 	    WebElement element4 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/ul/li[4]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[4]/a")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("ViewImplementationGuide");
		  
	    // Confirm information in the Audit Trail Tab
	    WebDriverWait wait5 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[4]/table/thead/tr/th[1]"), "Who"));
	    assertTrue("Could not find \"Entries\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(Entries) >= 0);
	  
	    // Open the Files Tab 
	    WebDriverWait wait6 = new WebDriverWait(driver, 60);
 	    WebElement element6 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/ul/li[5]/a")));   	
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[5]/a")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("ViewImplementationGuide");
		  
	    // Confirm the Files Page is fully loaded
	    WebDriverWait wait7 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[5]/table/thead/tr/th[1]"), "Name"));
	    WebDriverWait wait71 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[5]/table/thead/tr/th[2]"), "Type"));
	    
	 // Confirm information in the Files Tab
	    WebDriverWait wait72 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[5]/table/tbody/tr[1]/td[1]/a"), fileName));
	    
	    if (implementationGuideName == "C-CDA on FHIR") 
	    {
	      // Click on the FHIR XML Tab
		    WebDriverWait wait8 = new WebDriverWait(driver, 60);
	 	    WebElement element8 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/ul/li[6]/a")));   	
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[6]/a")).click();
		    
		    // Wait for page to fully load
		       waitForPageLoad();
		     
		    // Wait for the bindings to complete
			   waitForBindings("ViewImplementationGuide");
		       
		    // Confirm information in the FHIR XML Tab
		    WebDriverWait wait9 = new WebDriverWait(driver, 60);    
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[6]/pre"), "<ImplementationGuide"));
		    assertTrue("Could not find \"<ImplementationGuide\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*<ImplementationGuide[\\s\\S]*$"));
	    	
		 // Click on the FHIR JSON Tab
		    WebDriverWait wait10 = new WebDriverWait(driver, 60);
	 	    WebElement element10 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/ul/li[7]/a")));   	
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[7]/a")).click();
		    
		    // Wait for page to fully load
		       waitForPageLoad();
		     
		    // Wait for the bindings to complete
			   waitForBindings("ViewImplementationGuide");
			  
			// Wait for Text to appear
			   WebDriverWait wait19 = new WebDriverWait(driver, 60);
		 	    WebElement element19 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[3]/div[7]/pre")));  
		 	     
			   
		    // Confirm information in the FHIR JSON Tab
		    WebDriverWait wait11 = new WebDriverWait(driver, 60); 
		    assertTrue("Could not find \"resourceType\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*resourceType[\\s\\S]*$")); 
	    }
	    
	    // Click on the Edit top menu option and select Implementation Guide
	    
	    WebDriverWait wait8 = new WebDriverWait(driver, 60);
 	    WebElement element8 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/a")));   	
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/a")).click();         
	    WebDriverWait wait9 = new WebDriverWait(driver, 60);
 	    WebElement element9 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/ul/li[1]/a")));   	
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/ul/li[1]/a")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	    
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		   
	    ConfirmIGEditor(implementationGuideName);    
	    
	    // Open the Template Types Page
	    WebDriverWait wait10 = new WebDriverWait(driver, 60);                              
	    WebElement element10 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[2]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[2]/a")).click(); 
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	    
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		  
		// Confirm the Templates Tab appears
		   WebDriverWait wait11 = new WebDriverWait(driver, 120);
		   wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/div[2]/div[1]/div[1]"), templateType));
		   // assertTrue("Could not find \"document\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*document[\\s\\S]*$"));
		   
		// Validate information in the Templates Types Tab
	    WebDriverWait wait12 = new WebDriverWait(driver, 60);                   
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/div[2]/div[1]/div[1]"), templateType));
		assertTrue("Could not find \"Template Type\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(templateType) >= 0);
		  
	    // Confirm the Cardinality Tab option is available
	    WebDriverWait wait13 = new WebDriverWait(driver, 60);
	    WebElement element13 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[3]/a")));
	    
	    // Click on Cardinality Tab 
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[3]/a")).click();  
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		  
		// Validate information in the Cardinality Tab
	    WebDriverWait wait14 = new WebDriverWait(driver, 60);
	    WebElement element14 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[3]/div[1]/div")));
	    assertTrue("Could not find \"Cardinality\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(cardinality) >= 0);
		
	    // Confirm the Custom Schematron Tab option is available
	    WebDriverWait wait15 = new WebDriverWait(driver, 60);
	    WebElement element15 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[4]/a")));
	    
	    // Click on Custom Schematron Tab 
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[4]/a")).click(); 
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		  
		// Validate information in the Custom Schematron Tab
	    WebDriverWait wait16 = new WebDriverWait(driver, 60);
	    WebElement element16 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[2]/input")));
	    assertTrue("Could not find \"Cardinality\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(customSchematron) >= 0);

	    // Confirm the Permissions Tab option is available
	    WebDriverWait wait17 = new WebDriverWait(driver, 60);
	    WebElement element17 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[5]/a")));
	    
	    // Click on Permissions Tab 
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[5]/a")).click(); 
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		  
		// Validate the information in the Permissions Tab
	    WebDriverWait wait18 = new WebDriverWait(driver, 60);                   
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[1]/div[1]/div"), "View Permission"));
	    assertTrue("Could not find \"View Permissions\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(Permission) >= 0);

	    // Confirm the Volumes Tab option is available
	    WebDriverWait wait19 = new WebDriverWait(driver, 60);
	    WebElement element19 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[6]/a")));
	    
	    // Click on Volumes Tab 
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[6]/a")).click(); 
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		  
		// Validate information in the Volumes Tab 
	    WebDriverWait wait20 = new WebDriverWait(driver, 60);
	    WebElement element20 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[2]/input")));
	    assertTrue("Could not find \"Volume\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(Volume) >= 0);

	    // Confirm the Categories Tab option is available
	    WebDriverWait wait21 = new WebDriverWait(driver, 60);
	    WebElement element21 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[7]/a")));
	    
	    // Click on Categories Tab 
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[7]/a")).click();  
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("EditImplementationGuide");
		  
	    // Validate information in the Categories Tab
	    WebDriverWait wait22 = new WebDriverWait(driver, 60);  
	    WebElement element22 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[7]/div[2]/div")));
		assertTrue("Could not find \"Category\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(Category) >= 0);

	    // Click on Cancel to return to the IG Viewer
		 WebDriverWait wait42 = new WebDriverWait(driver, 60);  
		 WebElement element42 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[3]/button[2]")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button[2]")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("ViewImplementationGuide");
		  
		// Confirm the Implementation Guide Viewer appears
	       ConfirmIGViewer(implementationGuideName);  
	       
	    // Click on the Edit top menu option and select Bookmarks 	    
	    WebDriverWait wait24 = new WebDriverWait(driver, 60);  
	    WebElement element24 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/a")).click();
	    WebDriverWait wait25 = new WebDriverWait(driver, 60);  
	    WebElement element25 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/ul/li[2]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/ul/li[2]/a")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("EditBookmarks");
		  
	    // Confirm the Bookmarks page opens
	    WebDriverWait wait26 = new WebDriverWait(driver, 60);
	    WebElement element26 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("EditBookmarks")));    
	    assertTrue("Could not find \"Edit Bookmarks\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Edit Bookmarks[\\s\\S]*$"));

	    if (implementationGuideName != "C-CDA on FHIR") 
	    {
	    // Click on one entry in the list
	    WebDriverWait wait27 = new WebDriverWait(driver, 60);                             
	    WebElement element27 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[2]/div[2]/div[1]/input")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div[2]/div[1]/input")).click();  
	    }  
	    
	    // Confirm the option to generate bookmarks exists
	     WebDriverWait wait28 = new WebDriverWait(driver, 120);
		 wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/button"), "Regenerate All Bookmarks"));
	    
	    // Scroll down to the bottom of the page
	    ((JavascriptExecutor)driver).executeScript("scroll(0,400)");
	    
	    // Wait for page to fully load
	     waitForPageLoad();
	     
	    // Click on Cancel to return to the IG Viewer
	     WebDriverWait wait0 = new WebDriverWait(driver, 60);                                     
		 WebElement element0 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div/div/button[2]")));    
		  
        WebDriverWait wait29 = new WebDriverWait(driver, 60);
	    WebElement element29 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[3]/div/div/button[2]")));
         driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div/button[2]")).click();
         // driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div/button[2]")).click();
    
	      // Wait for page to fully load
		     waitForPageLoad();
		     
	      // Wait for the bindings to complete
		     waitForBindings("ViewImplementationGuide");
		  
	   // Confirm the Implementation Guide Viewer appears
	       ConfirmIGViewer(implementationGuideName);  
	       
	    // Click on the Edit top menu option and select Files 
	    WebDriverWait wait30 = new WebDriverWait(driver, 60);
	    WebElement element30 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/a")).click();
	    WebDriverWait wait31 = new WebDriverWait(driver, 60);
	    WebElement element31 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/ul/li[3]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[2]/ul/li[3]/a")).click();
	   
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("mainBody");
		  
	    // Confirm the Files Page Opens
	    WebDriverWait wait32 = new WebDriverWait(driver, 60);
	    WebElement element32 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/table/thead/tr/th[1]")));
	    assertTrue("Could not find \"Manage Implementation Guide Files\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Manage Implementation Guide Files[\\s\\S]*$"));
	    
	    // Confirm the correct IG appears in the Files Tab
	    WebDriverWait wait33 = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/p"), implementationGuideName));
	    assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);
	    
	    // Click on Cancel to return to the IG Viewer
	    WebDriverWait wait34 = new WebDriverWait(driver, 60);
	    WebElement element34 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/button[2]")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/button[2]")).click();
	    
	    // Wait for page to fully load
	       waitForPageLoad();
	     
	    // Wait for the bindings to complete
		   waitForBindings("ViewImplementationGuide");
		
	   // Confirm the Implementation Guide Viewer appears
	       ConfirmIGViewer(implementationGuideName);
	       
	    // Click on the Export Menu option
	    WebDriverWait wait35 = new WebDriverWait(driver, 60);
	    WebElement element35 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[3]/a")));
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[3]/a")).click();
	    
	    // Confirm the Export Form appears
	    WebDriverWait wait36 = new WebDriverWait(driver, 60);                     
	  	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"),"Export"));
	  
	    // Return to the Trifolia Home Page    
	  	 ReturnHome("Welcome to Trifolia Workbench");   
	       
	    WebDriverWait wait37 = new WebDriverWait(driver, 60);
	    WebElement element37 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("appnav")));
	    }
    else if (permissionUserName == "hl7.member") 
	    {
	    driver.findElement(By.xpath("//*[@id=\"ViewImplementationGuide\"]/ul/li[2]/a")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//*[@id=\"ViewImplementationGuide\"]/ul/li[3]/a")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//*[@id=\"ViewImplementationGuide\"]/ul/li[4]/a")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[1]/a")).click();
	    
	    //Confirm the user is returned to the Templates Tab
	    WebDriverWait wait = new WebDriverWait(driver, 60);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/div[1]/div[2]/div/div/div[1]"), "document"));
	   
	    // Return to the Trifolia Home Page
	     ReturnHome("Welcome to Trifolia Workbench");   
	     
	    }
    else
	    {
	    
	     // Return to the Trifolia Home Page
	     ReturnHome("Welcome to Trifolia Workbench");    
	     
	    }  
  }  
  
//TEST 2:  Add Permissions to an existing Implementation Guide
  @Test
  public void PermissionImplementationGuide(String implementationGuideName, 
		  String validationText, String permissionUserName, String loginUserName) throws Exception {
	  
	  // Open the IG Browser
	     OpenIGBrowser();
	     
	  // Find the Implementation Guide
	  if (permissionUserName == "lcg.admin") 
	     {
		  FindImplementationGuide("Test IHE PCC");   
	     }
 	      
	  // Launch the Implementation Guide Editor
		if (permissionUserName == "lcg.admin") 
		  {                              
		  	driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[5]/div/a[2]")).click();  	
		  }
		else
		  {
		  	driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[4]/div/a[2]")).click();
		  }
 
		 // Wait for page to fully load
	        waitForPageLoad(); 
	     
		 // Wait for the bindings to complete
            waitForBindings("EditImplementationGuide");
        
             ConfirmIGEditor("Test IHE PCC");
          
		    // Confirm the Permissions tab is visible  
	        WebDriverWait wait = new WebDriverWait(driver, 60);
		  	wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[5]/a"), "Permissions"));
		    
	      // Navigate to the permissions page
	         WebDriverWait wait1 = new WebDriverWait(driver, 60);
	         WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[2]/a")));       
	         driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[2]/a")).click();
	         driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[3]/a")).click();
	         driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[4]/a")).click();
	         driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[5]/a")).click();
	         
	         // Wait for page to fully load
		        waitForPageLoad();
		     
	         // Wait for the bindings to complete
			    waitForBindings("EditImplementationGuide");
	         
	         // Confirm the Permissions page appears
	         WebDriverWait wait2 = new WebDriverWait(driver, 120);
			 wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/div[1]"), "Add Permissions"));
			  assertTrue("Could not find \"Add Permissions\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Add Permissions[\\s\\S]*$"));
	   
			// Search for the Admin User
			  // Confirm the search field appears and enter search criteria
			    WebDriverWait wait3 = new WebDriverWait(driver, 60);
	   	        WebElement element3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/div[2]/form/div/div/div/input")));
		   	     driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/div[2]/form/div/div/div/input")).click();
		   	     driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/div[2]/form/div/div/div/input")).sendKeys(loginUserName);
		   	     driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/div[2]/form/div/div/div/input")).sendKeys(Keys.TAB);
		   	     driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/div[2]/form/div/div/div/div/button[2]")).click();
		   	     
		   	     // Confirm the search returns the correct user
		   	      WebDriverWait wait4 = new WebDriverWait(driver, 60);
				  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/table/tbody/tr[2]/td[1]"), loginUserName));
				  assertTrue("Could not find \"Add Permissions\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Lantana Admin[\\s\\S]*$"));
		   
	         // Click the Add View and Edit option
			    WebDriverWait wait5 = new WebDriverWait(driver, 60);
		   	    WebElement element5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/table/tbody/tr[2]/td[2]/div/div/button[2]")));	  
	            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[2]/div/table/tbody/tr[2]/td[2]/div/div/button[2]")).click();
	          
	         // Confirm the permissions have been applied
	            WebDriverWait wait6 = new WebDriverWait(driver, 60);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[1]/div[1]/table/tbody/tr[2]/td[1]/span[1]"), loginUserName));
				WebDriverWait wait7 = new WebDriverWait(driver, 60);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[5]/div/div[1]/div[2]/table/tbody/tr[2]/td[1]/span[1]"), loginUserName));
					
	   	      // Wait for page to fully load
	   	        waitForPageLoad();
	   	           	  
   	        // Confirm the Notify New Users option is available, and click the option
	           WebDriverWait wait8 = new WebDriverWait(driver, 60);
		       wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[2]"), "Notify new users and groups that they have been granted permissions")); 	                                        
   	           driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/input")).click();
		        
   	        // Save the Edit Permissions Form
   	           WebDriverWait wait9 = new WebDriverWait(driver, 60);
	   	       WebElement element9 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[3]/button[1]")));	  
	   	       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button[1]")).click();
   	           
		   	//Confirm the Alert appears
		   	WebDriverWait wait0 = new WebDriverWait(driver, 60);
		   	wait.until(ExpectedConditions.alertIsPresent());
		   	
		   	 // Switch the driver context to the "Successfully saved implementation Guide" alert
		   	 Alert alertDialog1 = driver.switchTo().alert();
		   	 // Get the alert text
		   	 String alertText1 = alertDialog1.getText();
		   	 // Click the OK button on the alert.
		   	 alertDialog1.accept();
	
		   	 // Wait for page to fully load
		        waitForPageLoad();
		        
		   	 // Wait for the bindings to complete
		        waitForBindings("EditImplementationGuide");
	           
		   	 WebDriverWait wait10 = new WebDriverWait(driver, 5);		
		   	 wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body/div[8]"))); 
  	           	  
	     // Return to the Trifolia Home Page
	        ReturnHome("Welcome to Trifolia Workbench");    
 }
  
//TEST 3:  Create an Implementation Guide
    @Test
    public void CreateImplementationGuide(String implementationGuideName, String implementationGuideIdentifier, String implementationGuideOrganization, String ImplementationGuideDisplayName, 
    		String iGWebDisplayName, String iGWebDescription, String implementationGuideType, String permissionUserName) throws Exception {
    	if (createdImplementationGuide) 
    	{
			return;
		}

    	 // Open the IG Browser
	     OpenIGBrowser();
	     
	    // Search for the Implementation Guide
	    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/input")).sendKeys(implementationGuideName);
	        
//	    {
//	    assertFalse("Found\"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);
//	    createdImplementationGuide = true;
//	    }
//      // Open the IG Editor if the IG does not already exist
//	  	if (createdImplementationGuide = false)
//	  	{
	    
	    // Open the Implementation Guide Editor
		// driver.findElement(By.xpath("/html/body/div[2]/div/div/table/thead/tr/th[5]/div/button")).click();
		
	    if (permissionUserName == "lcg.admin" || permissionUserName == "test.user") 
	    	{                            
	    	driver.findElement(By.xpath("/html/body/div[2]/div/div/table/thead/tr/th[5]/div/button")).click();
	    	}
	    else 
	    	{
	    	driver.findElement(By.xpath("/html/body/div[2]/div/div/table/thead/tr/th[4]/div/button")).click();
		    }

		    // Wait for page to fully load
		     waitForPageLoad();
		     
	        // Wait for the bindings to complete
	           waitForBindings("EditImplementationGuide");
	    
		    //Confirm Correct Form Opens and Enter IG Meta Data
		     WebDriverWait wait = new WebDriverWait(driver, 60);
			 WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("EditImplementationGuide")));
			 assertTrue("Could not find \"Add Implementation Guides\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Add Implementation Guide[\\s\\S]*$"));
			 
			 // Add IG Name
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[1]/input")).sendKeys(implementationGuideName);
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[1]/input")).sendKeys(Keys.TAB);
		    
		  	// Add IG Type
		    
		    if(implementationGuideType == "CDA")
		    {
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(implementationGuideType);
		    WebDriverWait wait5 = new WebDriverWait(driver, 60);
		  	wait5.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select"), implementationGuideType));
		    }
		    
		    if(implementationGuideType == "FHIR STU3")
		    {                               
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).click();
		       //driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys("FHIR");
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.ARROW_DOWN);
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.ARROW_DOWN);
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.ARROW_DOWN);
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.ARROW_DOWN);
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.ARROW_DOWN);
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.ARROW_DOWN);
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).click();
		       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[2]/select")).sendKeys(Keys.TAB);
		    }
		    
		    // Add Organization  
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[3]/select")).sendKeys(implementationGuideOrganization);
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[3]/select")).sendKeys(Keys.TAB);
		    
		    // Add IG Identifier/URL
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[4]/input")).sendKeys(implementationGuideIdentifier);
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[4]/input")).sendKeys(Keys.TAB);	  
	
		    // Add Display Name
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[5]/input")).sendKeys(iGWebDisplayName);
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[5]/input")).sendKeys(Keys.TAB);
		   
		    // Add Web Display Name
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[6]/input")).sendKeys(iGWebDisplayName);
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[6]/input")).sendKeys(Keys.TAB);
		    
		    // Add Consolidated format Option 
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[7]/select")).sendKeys("Yes");
		    driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[1]/div/div[1]/div[7]/select")).sendKeys(Keys.TAB);
		    
		    // Add Web IG Description
		    driver.findElement(By.xpath("/html/body")).sendKeys(iGWebDescription);
		    driver.findElement(By.xpath("/html/body")).sendKeys(Keys.TAB);
		    
		    // Wait until the Save option is available.
		    WebDriverWait wait6 = new WebDriverWait(driver, 60);                               
		    WebElement element6 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[3]/button[1]")));
		    
		    // Find the Implementation Guide
			  if (permissionUserName == "lcg.admin") 
			     {
				  SaveImplementationGuide("1Automation Test IG");   
			     }
		      createdImplementationGuide = true;
		      			  
		     // Return to the Trifolia Home Page
		     ReturnHome("Welcome to Trifolia Workbench");    
} 

//Find and Edit an Implementation Guide
    
@Test
    public void EditImplementationGuide(String implementationGuideName, String permissionUserName, String customSchematron) throws Exception {
	if (createdImplementationGuide)
	{

		 // Open the IG Browser
	     OpenIGBrowser();
	     
	  // Find the Implementation Guide
	  if (permissionUserName == "lcg.admin") 
	     {
		  FindImplementationGuide("1Automation Test IG");   
	     }
		  
	  // Open the IG Editor
      if (permissionUserName == "lcg.admin") 
  	  	{
    	  driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr[1]/td[5]/div/a[2]")).click();
  		   
  	  	}
      else 
  		{                              
    	  driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[4]/div/a")).click();
  		}
        
      // Confirm the IG Editor opens with the correct IG
         ConfirmIGEditor("1Automation Test IG");
          
	  // Add Custom Schematron
	      // Wait for page to load
		  WebDriverWait wait2 = new WebDriverWait(driver, 60);
		  WebElement element2 = wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[4]/a")));
          
		  // Click the "Custom Schematron" tab
		  driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[1]/a")).click();  
		  driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[2]/a")).click();  
		  driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[3]/a")).click();  
		  driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/ul/li[4]/a")).click();  

          // Confirm the Custom Schematron form appears
		  WebDriverWait wait = new WebDriverWait(driver, 60);
		  WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[4]/div[1]/div[4]/div/button")));
		  assertTrue("Could not find \"Pattern ID\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Pattern ID[\\s\\S]*$"));
		  
	      // Add the Custom Schematron 
		  // Open Edit Custom Schematron form
		  driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[4]/div[1]/div[4]/div/button")).click();
		  WebDriverWait wait4 = new WebDriverWait(driver, 60);
		  WebElement element4 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div[4]/div/div/div[2]/div[1]/select")));
		  assertTrue("Could not find \"Edit Custom Schematron\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Edit Custom Schematron[\\s\\S]*$"));
		 
		  
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[4]/div/div/div[2]/div[1]/select")).sendKeys("Error");
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[4]/div/div/div[2]/div[2]/input")).sendKeys("document-errors");
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[4]/div/div/div[2]/div[3]/textarea")).sendKeys(customSchematron);
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[4]/div/div/div[3]/button[1]")).click();
	      Thread.sleep(500);
	      		  
	      //Save the Implementation Guide
	       driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button[1]")).click();

		  if (permissionUserName == "lcg.admin") 
		     {
			  SaveImplementationGuide("1Automation Test IG");   
		     }
		  
		     // Return to the Trifolia Home Page
		     ReturnHome("Welcome to Trifolia Workbench");    
		     
	}
	else
		{
				return;
		}
} 

//TEST 5: Web View of an Existing Implementation Guide
public void WebViewImplementationGuide(String baseURL, String implementationGuideName, String iGDisplayName, String overviewText, String templateText, String valueSetText, String codeSystemText, String permissionUserName) throws Exception {

	 // Open the IG Browser
        OpenIGBrowser();
        
	  // Find the Implementation Guide
	  if (permissionUserName == "lcg.admin") 
	     {
		  FindImplementationGuide("Healthcare Associated Infection Reports Release 2 DSTU 1 V8");   
	     }
	  if (permissionUserName == "hl7.member") 
	  	 {
		  FindImplementationGuide("Public Health Case Report Release 1");
	  	 }
//   String parentHandle = driver.getWindowHandle();
   
	 // Wait for the bindings to complete
        waitForBindings("BrowseImplementationGuides");
    
   //Load the WebViewer page
   if (permissionUserName == "lcg.admin") 
   {
	   WebDriverWait wait = new WebDriverWait(driver, 60);
	   WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[5]/div/a[1]")));
   	   driver.findElement(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[5]/div/a[1]")).click();
   }	       
	   // Wait for the page to fully load
	   	  waitForPageLoad();
	   	
//	   	 // switch focus of WebDriver to WebView page
//	      	for (String winHandle : driver.getWindowHandles()) 
//	    {
			
//			driver.switchTo().window(winHandle);

		    if (baseURL == "http://dev.trifolia.lantanagroup.com/")
		    {
		    	driver.get("https://trifolia-dev.lantanagroup.com/IG/View/3247#/home");
		    }
		    if (baseURL == "https://staging-trifolia.lantanagroup.com") 
		    {
		    	driver.get("https://trifolia-staging.lantanagroup.com/IG/View/3247#/home");
		    }
			
		    // Wait for the page to fully re-load
		    waitForPageLoad();
		    WebDriverWait wait = new WebDriverWait(driver, 120);                    
		    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/html/body/div/div[2]/div")));       
	
		   // Wait for the Web Viewer to be loaded
		    WebDriverWait wait1 = new WebDriverWait(driver, 120);                    
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div[1]/div/div/h2"), "Table of Contents"));
		    assertTrue("WebView Home Page did not appear", driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Overview[\\s\\S]*$"));	    		    
		   	   
		    // Confirm correct IG is loaded  
			    assertTrue("Could not find \"Implementation Guide Display Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(iGDisplayName) >= 0);
		    
		// Overview page validation
		     	    
			// Wait for the page to fully load
		   	  waitForPageLoad();
		   	  
			// Confirm the overview page is loaded 
			WebDriverWait wait5 = new WebDriverWait(driver, 60);
		    WebElement element5 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h1")));
		  
		    //Validate the text within the overview page
		    WebDriverWait wait6 = new WebDriverWait(driver, 60);                    
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div[1]/div/div/div[1]/div[1]/h1/span[2]"), overviewText));
		    assertTrue("Could not find \"Overview Text\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(overviewText) >= 0);
		    
		    //Return to the Web IG Home page and confirm the Home page appears
		    driver.findElement(By.xpath("/html/body/div/div[1]/span[1]/span/a")).click();
		    WebDriverWait wait7 = new WebDriverWait(driver, 60);
		    WebElement element6 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h2")));
		
		 //Template page validation
		    
		    // Open the Template Page 
			driver.findElement(By.xpath("/html/body/div/div[1]/div/h4[2]/a")).click();
		
			// Wait for the page to fully load
		   	  waitForPageLoad();
		   	  
			//Confirm the Template page is loaded
		    WebDriverWait wait8 = new WebDriverWait(driver, 60);
		    WebElement element8 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h1")));
		  
		    //Validate the text within the Template page
		    WebDriverWait wait9 = new WebDriverWait(driver, 60);                   
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div[1]/div/uib-accordion/div/div[1]/div[2]/div/div[2]/ul/li[1]/ul/li[3]/a"), templateText));
		    assertTrue("Could not find \"Template Text\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(templateText) >= 0);
			   
		    //Return to the Web IG Home page and confirm the Home page appears
		    driver.findElement(By.xpath("/html/body/div/div[1]/span[1]/span/a")).click();
		    WebDriverWait wait10 = new WebDriverWait(driver, 60);
		    WebElement element10 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h2")));
		
	     //Value Sets page validation
		    
		    // Wait for the page to fully load
		   	  waitForPageLoad();
		   	  
		    // Open the Value Sets Page 
			driver.findElement(By.xpath("/html/body/div/div[1]/div/h4[3]/a")).click();
		
			//Confirm the Value Sets page is loaded
		    WebDriverWait wait11 = new WebDriverWait(driver, 60);
		    WebElement element11 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h1")));
		  
		    //Validate the text within the Value Sets page
		    WebDriverWait wait12 = new WebDriverWait(driver, 60);                    
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div[1]/div/div[3]/div/div/a/strong"), valueSetText));
		    assertTrue("Could not find \"Value Set Text\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(valueSetText) >= 0);
			   
		    //Return to the Web IG Home page 
		    driver.findElement(By.xpath("/html/body/div/div[1]/span[1]/span/a")).click();
		
		    // Wait for the page to fully load
		   	  waitForPageLoad();
		   	  
		   	// Confirm the Home page appears
		    WebDriverWait wait13 = new WebDriverWait(driver, 60);
		    WebElement element13 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h2")));
		
	        //Code Systems page validation
		    
		    // Open the Code Systems Page 
			driver.findElement(By.xpath("/html/body/div/div[1]/div/h4[4]/a")).click();
		
			// Wait for the page to fully load
		   	  waitForPageLoad();
		   	  
			//Confirm the Code Systems page is loaded
		    WebDriverWait wait14 = new WebDriverWait(driver, 60);
		    WebElement element14 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h1")));
		  
		    //Validate the text within the Code Systems page
		    WebDriverWait wait15 = new WebDriverWait(driver, 60);                    
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div[1]/div/div/table/tbody/tr[5]/td[1]"), codeSystemText));
		    assertTrue("Could not find \"Code System Text\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(codeSystemText) >= 0);
			   
		    //Return to the Web IG Home page 
		    driver.findElement(By.xpath("/html/body/div/div[1]/span[1]/span/a")).click();
		    
				// Wait for the page to fully load
		   	  waitForPageLoad(); 
		  
	// Download the WebIG
		   	  
		      //Code Systems page validation
			    
			    // Click on the Downloads option
				driver.findElement(By.xpath("/html/body/div/nav/div/div[2]/ul/li[2]/a")).click();
			
				// Wait for the page to fully load
			   	  waitForPageLoad();
			   	  
//				//Confirm the Downloads page is loaded
//			    WebDriverWait wait16 = new WebDriverWait(driver, 60);
//			    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[1]/div/h1")));
//			  
//			    //Validate the text within the Code Systems page
//			    WebDriverWait wait17 = new WebDriverWait(driver, 60);                    
//			    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div[1]/div/div/table/tbody/tr[5]/td[1]"), codeSystemText));
//			    assertTrue("Could not find \"Code System Text\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(codeSystemText) >= 0);
//				   
			    //Return to the Web IG Home page 
			    driver.findElement(By.xpath("/html/body/div/div[1]/span[1]/span/a")).click();
			    
					// Wait for the page to fully load
			   	  waitForPageLoad(); 

		   	  
		   	  
//		   	  // Switch focus to Trifolia Implementation Guide listing Page
//		   	 	driver.switchTo().window(parentHandle);
		   	     
			      if (baseURL == "http://dev.trifolia.lantanagroup.com/")
				    {
			    	  driver.navigate().to("http://dev.trifolia.lantanagroup.com/IGManagement/List");
				    }
				    if (baseURL == "https://staging-trifolia.lantanagroup.com") 
				    {
				    	driver.get("https://staging-trifolia.lantanagroup.com/IGManagement/List");
				    }
		
//		}   
//	     // Switch focus to Trifolia Implementation Guide listing Page
//	   	 	driver.switchTo().window(parentHandle);
	   	     
		        if (baseURL == "http://dev.trifolia.lantanagroup.com/")
			    {
		    	  driver.navigate().to("http://dev.trifolia.lantanagroup.com/IGManagement/List");
			    }
			    if (baseURL == "https://staging-trifolia.lantanagroup.com") 
			    {
			    	driver.get("https://staging-trifolia.lantanagroup.com/IGManagement/List");
			    }
			    
	    // Return to the Trifolia Home Page
	     ReturnHome("Welcome to Trifolia Workbench"); 	     	         
}

//Delete the Versioned Implementation Guide and it's Version
    @Test
    public void DeleteVersionedImplementationGuide(String implementationGuideVersioned, String iGType, String permissionUserName) throws Exception {
    	
    	 // Open the IG Browser
	        OpenIGBrowser();
	        
		      if (iGType == "CDA") 
		      {
		          //Delete the Versioned IG
		    	  //Search for the IG to be Deleted
			      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/input")).click();
			      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/input")).sendKeys(implementationGuideVersioned);
			  
			      //Open the IG Viewer
			      if (permissionUserName == "lcg.admin") 
			      {
			      	driver.findElement(By.xpath("//*[@id=\"BrowseImplementationGuides\"]/table/tbody/tr/td[5]/div/button[1]")).click();
			      }
			      else
			      {
			      	driver.findElement(By.xpath("//*[@id=\"BrowseImplementationGuides\"]/table/tbody/tr/td[4]/div/button")).click();
			      }     
			      
			      //Confirm the IG Viewer opens with the correct IG
			      WebDriverWait wait = new WebDriverWait(driver, 60);
			      WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ViewImplementationGuide\"]/ul/li[1]/a")));
			      assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideVersioned) >= 0);
			  
			  // Open the Delete IG Form and confirm the correct form opens.
			      
		      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[6]/a")).click();
		      WebDriverWait wait3 = new WebDriverWait(driver, 180);
		      WebElement element3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DeleteImplementationGuide")));
		      assertTrue("Could not find \"Delete Implementation Guides\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Delete Implementation Guide[\\s\\S]*$"));
	          
		      //Confirm the correct IG appears 
		      WebDriverWait wait4 = new WebDriverWait(driver, 60);                    
			  wait3.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"DeleteImplementationGuide\"]/div/h3"), implementationGuideVersioned));
		      assertTrue("Could not find \"Versioned Implementation Guide\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideVersioned) >= 0);
		      
		      //Click Delete and accept Confirmation
		      driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/button")).click();

		      //Confirm the Alert appears
			   WebDriverWait wait10 = new WebDriverWait(driver, 60);
			   wait10.until(ExpectedConditions.alertIsPresent());
		  
		      // Switch the driver context to the "Successfully Deleted implementation Guide" alert
		      Alert alertDialog11 = driver.switchTo().alert();
		      // Get the alert text
		      String alertText11 = alertDialog11.getText();
		      // assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Are you absolutely sure you want to delete this implementation guide?[\\s\\S]*$"));
		      Thread.sleep(500);
		      // Click the OK button on the alert.
		      alertDialog11.accept();
		      
		      // Return to the Trifolia Home Page
			     ReturnHome("Welcome to Trifolia Workbench");    
		   }  
    }
    
//Delete the Implementation Guide
@Test
		public void DeleteImplementationGuide(String implementationGuideName, String iGType, String permissionUserName) throws Exception {
		  
	// Open the IG Browser
       OpenIGBrowser();
    
	  	  //Search for the IG to be Deleted
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/input")).click();
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/input")).sendKeys(implementationGuideName);
	      
	      //Confirm the correct IG is found
	        WebDriverWait wait = new WebDriverWait(driver, 60);                    
		    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/table/tbody/tr/td[1]"), implementationGuideName));
		    assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);
	      
	      //Open the IG Viewer
	      if (permissionUserName == "lcg.admin") 
	      {
	      	driver.findElement(By.xpath("//*[@id=\"BrowseImplementationGuides\"]/table/tbody/tr/td[5]/div/button[1]")).click();
	      }
	      else
	      {
	      	driver.findElement(By.xpath("//*[@id=\"BrowseImplementationGuides\"]/table/tbody/tr/td[4]/div/button")).click();
	      }
	      
	      //Confirm the IG Viewer opens with the correct IG
	      WebDriverWait wait1 = new WebDriverWait(driver, 60);
	      WebElement element1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ViewImplementationGuide\"]/ul/li[1]/a")));
	      assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);
	      	      
	    //Launch the Delete IG form with the selected IG
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div[2]/ul/li[6]/a")).click();
	      
	      //Confirm the Delete IG form opens
	      WebDriverWait wait6 = new WebDriverWait(driver, 60);
	      WebElement element6 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DeleteImplementationGuide")));
	      assertTrue("Could not find \"Delete Implementation Guides\" on page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Delete Implementation Guide[\\s\\S]*$"));
          
	      //Confirm the correct IG appears in the Delete IG form
	      WebDriverWait wait7 = new WebDriverWait(driver, 60);                    
		  wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"DeleteImplementationGuide\"]/div/h3"), implementationGuideName));
	      assertTrue("Could not find \"Implementation Guide Name\" on page.", driver.findElement(By.cssSelector("BODY")).getText().indexOf(implementationGuideName) >= 0);
	      
	      //Delete IG and accept Confirmation, and return to the Trifolia Home Page
	      driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/button")).click();

	    //Confirm the Alert appears
		   WebDriverWait wait22 = new WebDriverWait(driver, 60);
		   wait.until(ExpectedConditions.alertIsPresent());
	  
	      // 5.1 Switch the driver context to the "Successfully Deleted implementation Guide" alert
	      Alert alertDialog12 = driver.switchTo().alert();
	      // 5.2 Get the alert text
	      String alertText12 = alertDialog12.getText();
	      // assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Are you absolutely sure you want to delete this implementation guide?[\\s\\S]*$"));
	      Thread.sleep(500);
	      // 5.3 Click the OK button on the alert.
	      alertDialog12.accept();

	      // Re-set the IG Created Flag
	      createdImplementationGuide = false;
	      
		     // Return to the Trifolia Home Page
		     ReturnHome("Welcome to Trifolia Workbench");    
	      }
    
}