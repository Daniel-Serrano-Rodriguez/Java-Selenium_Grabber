package models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import utils.Profiler;

public class FFDriver {

	private WebDriver webDriver;

	public FFDriver(String path) {
		webDriver = new FirefoxDriver(Profiler.setFirefoxOptions(path));
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

}
