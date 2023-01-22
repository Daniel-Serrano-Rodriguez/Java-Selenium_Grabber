package models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

public class FFDriver {
	
	private WebDriver webDriver;
	
	public FFDriver(String path) {
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile testprofile = profile.getProfile("selenium");
		testprofile.setPreference("browser.download.dir", path);
		FirefoxOptions fo = new FirefoxOptions();
		fo.setProfile(testprofile);
		fo.setHeadless(true);
		fo.setLogLevel(FirefoxDriverLogLevel.FATAL);
		
		webDriver = new FirefoxDriver(fo);
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}

}
