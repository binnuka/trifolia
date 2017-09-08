package trifolia.pkg;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Ignore
public class A_TrifoliaLogin {
	private WebDriver driver;
	private String baseUrl;
	String currentURL = null;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private String basicUserUsername = "user";
	private String basicUserPassword = "password";
	private String adminUserUsername = "admin.user";
	private String adminUserPassword = "admin.password";
	private String stagingUserUsername = "staging.user.username";
	private String stagingUserPassword = "staging.user.password";
	private String stagingAdminUsername = "staging.admin.username";
	private String stagingAdminPassword = "staging.admin.password";
	private String hl7MemberUsername = "hl7.member.username";
	private String hl7MemberPassword = "hl7.member.password";
	private String hl7UserUsername = "hl7.user.username";
	private String hl7UserPassword = "hl7.user.password";

	public A_TrifoliaLogin() {
		if (System.getProperty("basicUserUsername") != null) {
			this.basicUserUsername = System.getProperty("basicUserUsername");
		}
		if (System.getProperty("basicUserPassword") != null) {
			this.basicUserPassword = System.getProperty("basicUserPassword");
		}
		if (System.getProperty("adminUserUsername") != null) {
			this.adminUserUsername = System.getProperty("adminUserUsername");
		}
		if (System.getProperty("adminUserPassword") != null) {
			this.adminUserPassword = System.getProperty("adminUserPassword");
		}
		if (System.getProperty("stagingUserUsername") != null) {
			this.stagingUserUsername = System.getProperty("stagingUserUsername");
		}
		if (System.getProperty("stagingUserPassword") != null) {
			this.stagingUserPassword = System.getProperty("stagingUserPassword");
		}
		if (System.getProperty("stagingAdminUsername") != null) {
			this.stagingAdminUsername = System.getProperty("stagingAdminUsername");
		}
		if (System.getProperty("stagingAdminPassword") != null) {
			this.stagingAdminPassword = System.getProperty("stagingAdminPassword");
		}
		if (System.getProperty("hl7MemberUsername") != null) {
			this.hl7MemberUsername = System.getProperty("hl7MemberUsername");
		}
		if (System.getProperty("hl7MemberPassword") != null) {
			this.hl7MemberPassword = System.getProperty("hl7MemberPassword");
		}
		if (System.getProperty("hl7UserUsername") != null) {
			this.hl7UserUsername = System.getProperty("hl7UserUsername");
		}
		if (System.getProperty("hl7UserPassword") != null) {
			this.hl7UserPassword = System.getProperty("hl7UserPassword");
		}
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	// SPM: This isn't really used since this class has the @ignore property
	// on it and it is only called by other test classes
	@Before
	public void initialize() {
		ProfilesIni allProfiles = new ProfilesIni();
		FirefoxProfile profile = allProfiles.getProfile("default");
		if (this.driver == null) {
			// initialize a driver since one has not been provided already
			this.driver = new FirefoxDriver(profile);
		}
	}

	@Test
	
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
	
	 public void completeNewProfile(String firstName, String lastName, String Phone, String Organization)
	 {
			 WebDriverWait wait = new WebDriverWait(driver, 60);
			 wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/h2"),"New Profile"));
			 assertTrue("Could not find \"New Profile\" on Account page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*New Profile[\\s\\S]*$"));

			// Enter Trifolia User account Firstname
			WebDriverWait wait1 = new WebDriverWait(driver, 60);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[1]/input")));
			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/input")).sendKeys(firstName);

			// Enter Trifolia User account Lastname
			WebDriverWait wait2 = new WebDriverWait(driver, 60);
			WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[2]/input")));
			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/input")).sendKeys(lastName);
			
			// Enter Trifolia User Account Phone 
			WebDriverWait wait3 = new WebDriverWait(driver, 60);
			WebElement element3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[3]/input")));
			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[3]/input")).sendKeys(Phone);
			
			// Enter Trifolia User Account Organization
			WebDriverWait wait4 = new WebDriverWait(driver, 60);
			WebElement element4 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[5]/input")));
			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[5]/input")).sendKeys(Organization);
			
			//Click Save
			WebDriverWait wait5 = new WebDriverWait(driver, 60);
			WebElement element5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div/div/div/div[8]/button")));
			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[8]/button")).click();

	 }
	   public void LCGAdminLogin(String permissionUserName, String baseURL)
			throws Exception {
		// Login to Trifolia Workbench as Lantana Administrator

		// Wait for page to fully load
		   waitForPageLoad(); 
		   
		// Confirm Welcome Page Appears
		   assertTrue("Login Page did not appear",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

			String parentHandle = driver.getWindowHandle();

			// Launch the Auth0 Login Page
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[3]/a/span")).click();
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[3]/ul/li/a")).click();
			
			for (String winHandle : driver.getWindowHandles()) {
				// switch focus of WebDriver to Auth0 login page
				driver.switchTo().window(winHandle);
				
//				Thread.sleep(5000);
				
				if (baseURL == "https://trifolia-staging.lantanagroup.com")
				{
					WebDriverWait wait = new WebDriverWait(driver, 240);                    
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div/div[2]/form/div/div/div[1]/div[2]/div"),"Trifolia"));
					assertTrue("Could not find \"Trifolia\" on Auth0 page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Trifolia[\\s\\S]*$"));
				
					// Enter Trifolia Admin account e-mail and password
					WebDriverWait wait1 = new WebDriverWait(driver, 60);                              
					WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input")));
					driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input")).sendKeys(this.adminUserUsername);
					Thread.sleep(1000);
					
					WebDriverWait wait2 = new WebDriverWait(driver, 60);
					WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div[1]/input")));
					driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div[1]/input")).sendKeys(this.adminUserPassword);
									
					//Click Login
					WebDriverWait wait5 = new WebDriverWait(driver, 60);
					WebElement element5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/button")));
					driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/button")).click();
			}
				if (baseURL == "http://dev.trifolia.lantanagroup.com/")
				{
				WebDriverWait wait = new WebDriverWait(driver, 240);                    
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div/div[2]/form/div/div/div[1]/div[2]/div"),"Trifolia"));
				assertTrue("Could not find \"Trifolia\" on Auth0 page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Trifolia[\\s\\S]*$"));
				
				// Enter Trifolia Admin account e-mail and password
				WebDriverWait wait1 = new WebDriverWait(driver, 60);                              
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input")));
				driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input")).sendKeys(this.adminUserUsername);
				Thread.sleep(1000);
				
				WebDriverWait wait2 = new WebDriverWait(driver, 60);                               
				WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div/input")));
				driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div/input")).sendKeys(this.adminUserPassword);
								
				//Click Login
				WebDriverWait wait5 = new WebDriverWait(driver, 60);                               
				WebElement element5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/button")));
				driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/button")).click();

				}
			}

			// Wait for page to fully load
			   waitForPageLoad(); 
			   
			 // Wait for the bindings to complete
		        waitForBindings("appnav");
		    
		    	if(driver.findElements(By.xpath("/html/body/div[2]/div/div/h2")).size() != 0)
				{
					completeNewProfile("Lantana", "Admin", "111-222-3333", "Lantana Consulting Group");
				}		
		    	
		    // Switch focus to Trifolia Home Page
			driver.switchTo().window(parentHandle);
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"),"Welcome to Trifolia Workbench!"));
			assertTrue("Could not find \"Welcome to Trifolia\" on page.",driver.findElement(By.cssSelector("BODY"))
							.getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

		// Confirm Login is Successful
		WebDriverWait wait1 = new WebDriverWait(driver, 60);
		wait1.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"),"Welcome to Trifolia Workbench!"));
		assertTrue("Unable to confirm Login",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S][\\s\\S]*$"));

//		// Confirm Welcome Page Text appears
//		WebDriverWait wait2 = new WebDriverWait(driver, 60);
//		wait1.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/p/strong"), "Did you know?"));
}

	@Test
	public void LCGUserLogin(String permissionUserName, String baseURL)
			throws Exception {

          // Launch the Auth0 Login Page
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[3]/a/span")).click();
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[3]/ul/li/a")).click();
			
			String parentHandle = driver.getWindowHandle();
			
			for (String winHandle : driver.getWindowHandles()) 
			{
				// switch focus of WebDriver to Auth0 login page
				driver.switchTo().window(winHandle);

				WebDriverWait wait = new WebDriverWait(driver, 240);                   
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div/div/div[2]/form/div/div/div[1]/div[2]/div"),"Trifolia"));
				assertTrue("Could not find \"Trifolia\" on Auth0 page.",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Trifolia[\\s\\S]*$"));

				// Enter Trifolia User account e-mail and password
				WebDriverWait wait1 = new WebDriverWait(driver, 60);
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input")));
				driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input")).sendKeys(this.basicUserUsername);
				Thread.sleep(1000);
				
				WebDriverWait wait2 = new WebDriverWait(driver, 60);
				WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div/input")));
				driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[2]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div/input")).sendKeys(this.basicUserPassword);
								
				//Click Login
				WebDriverWait wait5 = new WebDriverWait(driver, 60);
				WebElement element5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/form/div/div/button")));
				driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/button")).click();
			}

					// Wait for page to fully load
					   waitForPageLoad(); 
					   
					 // Wait for the bindings to complete
				        waitForBindings("appnav");
				
		// If Trifolia New Profile page appears, complete the Profile Page.
			
			if(driver.findElements(By.xpath("/html/body/div[2]/div/div/h2")).size() != 0)
			{
				completeNewProfile("Lantana", "User", "111-222-3333", "Lantana Consulting Group");
			}			        
				        
				    // Switch focus to Trifolia Home Page
					driver.switchTo().window(parentHandle);
					WebDriverWait wait = new WebDriverWait(driver, 120);
					wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"),"Welcome to Trifolia Workbench!"));
					assertTrue("Could not find \"Welcome to Trifolia\" on page.",driver.findElement(By.cssSelector("BODY"))
									.getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));
		
//		// Complete User Profile if the page appears appears
//	    
//		// if (driver.findElement(By.cssSelector("Body")).getText().matches("^[\\s\\S]*New Profile[\\s\\S]*$"))
//		   if (driver.getPageSource().contains("New Profile"))   
//			   {
//			// Wait for page header to appear
//			WebDriverWait wait1 = new WebDriverWait(driver, 60);
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/div/h2"), "New Profile"));
//			
//			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/input")).sendKeys("Lantana");
//			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[2]/input")).sendKeys("User");
//			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[3]/input")).sendKeys("111-222-3333");
//			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[4]/input")).sendKeys("lantana.user@lcg.org");
//			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[5]/input")).sendKeys("Lantana Consulting Group");
//			
//			// Click Save
//			driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[7]/button")).click();
//		} 
//		else 
//		{
  		// Wait for page to re-load
			   waitForPageLoad();
			
			// Confirm Login is Successful
			WebDriverWait wait1 = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/h2"), "Welcome to Trifolia Workbench!"));
			assertTrue("Login was not Successful",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S][\\s\\S]*$"));

//			// Confirm Welcome Page Text appears
//			WebDriverWait wait2 = new WebDriverWait(driver, 60);
//			wait1.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[2]/div/p/strong"), "Did you know?"));
		
	}

	@Test
	public void HL7MemberLogin(String hl7Welcome) throws Exception {
		// Login to Trifolia Workbench Dev environment as HL7 Member via HL7
		// QCommerce site

		// Step 1 - Confirm the Trifolia Home Page Appears
		assertTrue(
				"Trifolia Home Page did not appear",
				driver.findElement(By.cssSelector("BODY"))
						.getText()
						.matches(
								"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));
		Thread.sleep(500);
		// get the current window handle
		String parentHandle = driver.getWindowHandle();

		// Launch the HL7 Login Page
		driver.findElement(By.xpath("/html/body/div[2]/div/h3/a")).click();

		for (String winHandle : driver.getWindowHandles()) {
			// switch focus of WebDriver to HL7 QCommerce login page
			driver.switchTo().window(winHandle);

			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("/html/body/form/div[3]/div[1]/div/div[2]/div[1]"),
					hl7Welcome));
			assertTrue("Could not find \"Health Level Seven\" on page.",
					driver.findElement(By.cssSelector("BODY")).getText()
							.indexOf(hl7Welcome) >= 0);

			// Enter HL7 Member Username and Password
			driver.findElement(
					By.xpath("//*[@id=\"ctl00_MainContent_txtUserNameHL7\"]"))
					.sendKeys(this.hl7MemberUsername);
			driver.findElement(
					By.xpath("//*[@id=\"ctl00_MainContent_txtPasswordHL7\"]"))
					.sendKeys(this.hl7MemberPassword);
			driver.findElement(
					By.xpath("//*[@id=\"ctl00_MainContent_SubmitHL7\"]"))
					.click();
		}

		// Switch focus to Trifolia Home Page
		driver.switchTo().window(parentHandle);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id("appnav")));
		assertTrue(
				"Could not find \"Welcome to Trifolia\" on page.",
				driver.findElement(By.cssSelector("BODY"))
						.getText()
						.matches(
								"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

	}

	@Test
	public void HL7UserLogin(String hl7Welcome) throws Exception {
		// Login to the Trifolia Workbench as HL7 User via HL7 QCommerce site

		// Confirm the Trifolia Home Page Appears
		assertTrue(
				"Trifolia Home Page did not appear",
				driver.findElement(By.cssSelector("BODY"))
						.getText()
						.matches(
								"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));
		// get the current window handle
		String parentHandle = driver.getWindowHandle();

		// Launch the HL7 Login Page
		driver.findElement(By.xpath("/html/body/div[2]/div/h3/a")).click();
		// assertTrue("QCommerce Login Page did not appear",
		// driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Health Level Seven International[\\s\\S]*$"));
		for (String winHandle : driver.getWindowHandles()) {

			// switch focus of WebDriver to HL7 QCommerce login page
			driver.switchTo().window(winHandle);

			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("/html/body/form/div[3]/div[1]/div/div[2]/div[1]"),
					hl7Welcome));
			assertTrue("Could not find \"Health Level Seven\" on page.",
					driver.findElement(By.cssSelector("BODY")).getText()
							.indexOf(hl7Welcome) >= 0);

			// Enter HL7 Username and Password
			driver.findElement(
					By.xpath("//*[@id=\"ctl00_MainContent_txtUserNameHL7\"]"))
					.sendKeys(this.hl7UserUsername);
			Thread.sleep(500);
			driver.findElement(
					By.xpath("//*[@id=\"ctl00_MainContent_txtPasswordHL7\"]"))
					.sendKeys(this.hl7UserPassword);
			driver.findElement(
					By.xpath("//*[@id=\"ctl00_MainContent_SubmitHL7\"]"))
					.click();

			// Switch focus to Trifolia Home Page
			Thread.sleep(20000);
			driver.switchTo().window(parentHandle);

			if (driver.findElement(By.cssSelector("BODY")).getText()
					.matches("^[\\s\\S]*New Profile[\\s\\S]*$")) {
				driver.findElement(
						By.xpath("//*[@id=\"mainBody\"]/div/div[1]/input"))
						.sendKeys("HL7");
				driver.findElement(
						By.xpath("//*[@id=\"mainBody\"]/div/div[2]/input"))
						.sendKeys("User");
				driver.findElement(
						By.xpath("//*[@id=\"mainBody\"]/div/div[3]/input"))
						.sendKeys("111-222-3333");
				driver.findElement(
						By.xpath("//*[@id=\"mainBody\"]/div/div[4]/input"))
						.sendKeys("hl7.user@hl7.org");
				driver.findElement(
						By.xpath("//*[@id=\"mainBody\"]/div/div[5]/input"))
						.sendKeys("HL7 International");
				driver.findElement(
						By.xpath("//*[@id=\"mainBody\"]/div/div[7]/button"))
						.click();
			}

			else {
				// Confirm Login is Successful
				assertTrue(
						"Login was not Successful",
						driver.findElement(By.cssSelector("BODY"))
								.getText()
								.matches(
										"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S][\\s\\S]*$"));
			}
		}
		// Switch focus to Trifolia Home Page
		driver.switchTo().window(parentHandle);
		WebDriverWait wait1 = new WebDriverWait(driver, 60);
		wait1.until(ExpectedConditions.textToBePresentInElementLocated(
				By.xpath("/html/body/div[2]/div/h2"),
				"Welcome to Trifolia Workbench!"));
		assertTrue(
				"Could not find \"Welcome to Trifolia\" on page.",
				driver.findElement(By.cssSelector("BODY"))
						.getText()
						.matches(
								"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

	}

	// Lantana Administrator and Lantana User Logout of Trifolia Workbench
	@Test
	public void LCGLogout(String permissionUserName) throws Exception {

		// Wait for the page to fully load
		  waitForPageLoad();
		  
		// Confirm the User is still logged in
		assertTrue(
				"Welcome to Trifolia Workbench!",
				driver.findElement(By.cssSelector("BODY"))
						.getText()
						.matches(
								"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

		// Logout the Lantana admin or user account
		
		if (permissionUserName == "lcg.admin" && driver.findElements(By.xpath("/html/body/div[2]/div/div/h2")).size() == 0)
		{
			WebDriverWait wait = new WebDriverWait(driver, 60);                                
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/a/b")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/a/b")).click();
			WebDriverWait wait1 = new WebDriverWait(driver, 60);
			WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/ul/li[4]/a")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/ul/li[4]/a")).click();
			
			// Wait for the page to fully load
			  waitForPageLoad();
		}  
		
		if (permissionUserName == "lcg.admin" && driver.findElements(By.xpath("/html/body/div[1]/div/div[2]/ul/li[3]")).size() == 0)
		{
			WebDriverWait wait = new WebDriverWait(driver, 60);                                
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/a/span")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/a/span")).click();
			WebDriverWait wait1 = new WebDriverWait(driver, 60);
			WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/ul/li[4]/a")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[8]/ul/li[4]/a")).click();
			
			// Wait for the page to fully load
			  waitForPageLoad();

		} 
		else if (permissionUserName == "lcg.user") 
		{
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[7]/a/span")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[7]/a/span")).click();
			WebDriverWait wait1 = new WebDriverWait(driver, 60);
			WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/ul/li[7]/ul/li[4]/a")));
			driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/ul/li[7]/ul/li[4]/a")).click();
			
			// Wait for page to re-load
			waitForPageLoad();
			  	
		}
		
		// Confirm Logout is Successful
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"appnav\"]/div/div[2]/ul/li[3]/a/span")));
		assertTrue("Logout was not Successful",driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

		// Confirm Welcome Page text appears.
		WebDriverWait wait1 = new WebDriverWait(driver, 60);
		wait1.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("/html/body/div[1]/div/div[2]/ul/li[3]/a/span"),"Log In"));

	}

	// HL7 Member and HL7 User Logout of Trifolia Workbench
	@Test
	public void HL7Logout(String hl7Welcome, String baseURL,
			String permissionUserName) throws Exception {

		// Wait for page to fully load
		  waitForPageLoad();
					
		// Log the HL7 Member or User out of Trifolia

		if (permissionUserName == "hl7.member") {
			driver.findElement(
					By.xpath("/html/body/div[1]/div/div[2]/ul/li[5]/a/span"))
					.click();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement element = wait
					.until(ExpectedConditions.elementToBeClickable(By
							.xpath("/html/body/div[1]/div/div[2]/ul/li[5]/ul/li[3]/a")));
			driver.findElement(
					By.xpath("/html/body/div[1]/div/div[2]/ul/li[5]/ul/li[3]/a"))
					.click();
		}

		if (permissionUserName == "hl7.user") {
			driver.findElement(
					By.xpath("/html/body/div[1]/div/div[2]/ul/li[5]/a/span"))
					.click();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement element = wait
					.until(ExpectedConditions.elementToBeClickable(By
							.xpath("/html/body/div[1]/div/div[2]/ul/li[5]/ul/li[3]/a")));
			driver.findElement(
					By.xpath("/html/body/div[1]/div/div[2]/ul/li[5]/ul/li[3]/a"))
					.click();
		}

		// Wait for page to fully load
		    waitForPageLoad();
					
		// Confirm Trifolia Logout is Successful
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement element = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("/html/body/div[2]/div/h3/a")));
		assertTrue(
				"Logout was not Successful",
				driver.findElement(By.cssSelector("BODY"))
						.getText()
						.matches(
								"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));

		String parentHandle = driver.getWindowHandle();

		// Navigate to the HL7 Test Server page
		if (baseURL == "http://dev.trifolia.lantanagroup.com/") {
			driver.navigate().to("http://hl7.amg-hq.net/");
		}
		if (baseURL == "http://staging.lantanagroup.com:1234/") {
			driver.navigate().to("http://www.hl7.org/index.cfm");
		}

		for (String winHandle : driver.getWindowHandles())

		{
			// switch focus of WebDriver to HL7 Test Server page
			driver.switchTo().window(winHandle);

			// Confirm the HL7 Welcome Page appears
			WebDriverWait wait2 = new WebDriverWait(driver, 60);
			wait2.until(ExpectedConditions.textToBePresentInElementLocated(
					By.xpath("/html/body/div[1]/div[1]/div[1]/div[1]"),
					hl7Welcome));
			assertTrue("Could not find \"Health Level Seven\" on page.",
					driver.findElement(By.cssSelector("BODY")).getText()
							.indexOf(hl7Welcome) >= 0);

			// Logout the HL7 Member or User from the HL7 Test Server
			driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[3]/a"))
					.click();

			if (baseURL == "http://dev.trifolia.lantanagroup.com/") {
				WebDriverWait wait3 = new WebDriverWait(driver, 60);
				wait3.until(ExpectedConditions.textToBePresentInElementLocated(
						By.xpath("/html/body/form/div[4]/div[3]/div[3]/div[3]/div[2]/div[2]/div/div[1]/div[1]/h3"),
						"Existing Users, Please Log In"));
				// driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[3]/a")).click();
			}

			// Wait for page to fully load
			WebDriverWait wait4 = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By
					.xpath("/html/body/div[1]/div[1]/div[3]/a")));

			// Switch focus back to Trifolia Home Page
			driver.navigate().to("http://dev.trifolia.lantanagroup.com/");
			WebDriverWait wait5 = new WebDriverWait(driver, 60);
			WebElement element5 = wait.until(ExpectedConditions
					.elementToBeClickable(By
							.xpath("/html/body/div[2]/div/h3/a")));
			assertTrue(
					"Logout was not Successful",
					driver.findElement(By.cssSelector("BODY"))
							.getText()
							.matches(
									"^[\\s\\S]*Welcome to Trifolia Workbench![\\s\\S]*$"));
		}
	}

	@After
	public void tearDown() throws Exception {
		// driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
