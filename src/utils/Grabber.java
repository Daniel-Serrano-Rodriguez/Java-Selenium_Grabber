package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

import com.google.common.io.Files;

public class Grabber extends Thread {
	private String url, outputFolder;
	private int threadNumber;
	private List<String> errors;

	public Grabber(String url, String outputFolder, int number) {
		this.url = url;
		this.outputFolder = outputFolder;
		threadNumber = number;
		errors = new ArrayList<>();
	}

	@Override
	public void run() {
		boolean prepared = false, cached = false, downloaded = false;
		File outFolder = new File(outputFolder), fileFolder = new File(outputFolder + "/" + threadNumber);
		String[] files;

		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile testprofile = profile.getProfile("selenium");
		testprofile.setPreference("browser.download.dir", fileFolder.getAbsolutePath());
		FirefoxOptions fo = new FirefoxOptions();
		fo.setProfile(testprofile);
//		fo.setHeadless(true);
		fo.setLogLevel(FirefoxDriverLogLevel.FATAL);

		WebDriver driver = new FirefoxDriver(fo);
		WebElement txfUrl, btnUrl, btnDownload, txtDownloadProcess;
		String selectorCssButtonUrl = "#gatsby-focus-wrapper > main > section:nth-child(1) > div > div.sm\\:text-center.md\\:max-w-2xl.md\\:mx-auto.lg\\:mx-0.lg\\:col-span-8.lg\\:text-left > div.mt-8.sm\\:mx-auto.sm\\:text-center.lg\\:mx-0.lg\\:text-left > form > button";

		if (!outFolder.exists())
			outFolder.mkdirs();

		driver.get("https://www.savethevideo.com/es/home");
		System.out.println("Enlace: " + url);

		try {

			txfUrl = findElement(driver, By.cssSelector("#url"));

			for (int i = 0, l = url.length(); i < l; i++) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					errors.add("Adding letters to url textfield");
				}

				txfUrl.sendKeys(url.charAt(i) + "");
			}

			btnUrl = findElement(driver, By.cssSelector(selectorCssButtonUrl));
			btnUrl.click();

			while (!prepared)
				try {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						errors.add("Waiting for download availability");
					}

					btnUrl.findElement(By.name("svg"));
				} catch (Exception e) {
					prepared = true;
				}

			btnDownload = findElement(driver, By.cssSelector("a.flex:nth-child(3)"));
			btnDownload.click();
			txtDownloadProcess = driver.findElement(By.cssSelector("p.text-sm:nth-child(4)"));

			while (!cached) {
				try {
					if (!txtDownloadProcess.getText().matches(".*(A partir|%).*"))
						cached = true;
				} catch (Exception e) {
					cached = true;
				}
			}

			if (!fileFolder.exists())
				fileFolder.mkdir();

			btnDownload.click();

			files = fileFolder.list();

			while (!downloaded) {
				boolean finished = true;

				for (String f : files) {
					if (f.matches(".*part$"))
						finished = false;
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					errors.add("Waiting for partial files");
				}

				downloaded = finished;

				files = fileFolder.list();
			}

			File file = new File(fileFolder.getAbsoluteFile() + "/" + files[0]);
			Files.move(file, new File(outputFolder + "/" + file.getName()));

			fileFolder.delete();
		} catch (Exception e) {
			e.printStackTrace();
			errors.add("Don't know fam");
		}

		driver.quit();

		try {
			if (errors.size() > 0)
				writeErrorsLog();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private WebElement findElement(WebDriver driver, By by) {
		WebElement element = null;

		try {
			element = driver.findElement(by);
		} catch (Exception e) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.getStackTrace();
				errors.add("Error waiting for element");
			}
			element = findElement(driver, by);
		}

		return element;
	}

	private void writeErrorsLog() throws IOException {
		File errorLog = new File("./" + url.split("=")[1] + "-error_log.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(errorLog));

		for (String line : errors) {
			bw.write(line + "\n");
		}

		bw.close();
	}
}
