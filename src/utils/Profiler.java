package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

public class Profiler {

	private static final String profileName = "web/0aqy6coi.selenium-profile";
	private static String profileIniLocation, pilBackup;
	private static int profileCount = 0;

	public static void writeProfile() {
		File profile, profilesIni, pilCopy;
		String os = System.getProperty("os.name"), username = System.getProperty("user.name"), linea;
		profileCount = 0;

		if (os.equals("Linux")) {
			String linuxPath = "/home/" + username + "/.mozilla/firefox/";
			try {
				profileIniLocation = linuxPath + "profiles.ini";
				if (!isWritten()) {
					profile = new File(profileName);
					profilesIni = new File(profileIniLocation);
					pilCopy = new File(profileIniLocation + ".bak");
					pilBackup = pilCopy.getAbsolutePath();
					Files.copy(profilesIni.toPath(), pilCopy.toPath());

					BufferedReader br = new BufferedReader(new FileReader(profilesIni));

					while ((linea = br.readLine()) != null)
						if (linea.matches("\\[Profile[0-9]+\\]"))
							profileCount++;

					br.close();

					BufferedWriter bw = new BufferedWriter(new FileWriter(profilesIni, true));

					bw.write("[Profile" + profileCount + "]\n");
					bw.write("Name=selenium-profile\n");
					bw.write("IsRelative=0\n");
					bw.write("Path=" + profile.getAbsolutePath() + "\n");

					bw.flush();
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static FirefoxOptions setFirefoxOptions(String path) {
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile testprofile = profile.getProfile("selenium-profile");
		testprofile.setPreference("browser.download.dir", path);
		testprofile.setPreference("browser.download.folderList", 2);
		testprofile.addExtension(new File("web/uBlock0_1.46.1b8.firefox.signed.xpi"));
		FirefoxOptions fOptions = new FirefoxOptions();
		fOptions.setProfile(testprofile);
		fOptions.setHeadless(true);
		fOptions.setLogLevel(FirefoxDriverLogLevel.FATAL);
		return fOptions;
	}

	public static void removeProfile() throws IOException {
		File bak = new File(pilBackup), prof = new File(profileIniLocation);
		prof.delete();
		Files.copy(bak.toPath(), prof.toPath());
		bak.delete();
	}

	private static boolean isWritten() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(profileIniLocation)));
		String linea;

		while ((linea = br.readLine()) != null)
			if (linea.matches(profileName)) {
				br.close();
				return true;
			}

		br.close();
		return false;
	}

}
