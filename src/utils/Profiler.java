package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Profiler {

	private static final String profileName = "0aqy6coi.selenium-profile";
	private static String finalProfile, profileIniLocation, pilBackup;
	private static int profileCount = 0;

	public static void writeProfile() {
		File profile = new File(profileName), profilesIni, pilCopy;
		String os = System.getProperty("os.name"), username = System.getProperty("user.name"), linea;
		profileCount = 0;

		if (os.equals("Linux")) {
			String linuxPath = "/home/" + username + "/.mozilla/firefox";
			try {
				profileIniLocation = "/home/" + username + "/.mozilla/firefox/profiles.ini";
				if (!isWritten()) {
					profilesIni = new File(profileIniLocation);
					pilCopy = new File(profileIniLocation + ".bak");
					pilBackup = pilCopy.getAbsolutePath();
					Files.copy(profilesIni.toPath(), pilCopy.toPath());
					finalProfile = linuxPath + profileName;
					new File(finalProfile).mkdir();
					
					System.out.println(profile.mkdir());

					Files.walk(Paths.get(profileName)).forEach(source -> {
						Path destination = Paths.get(finalProfile, source.toString().substring(profileName.length()));
						try {
							Files.copy(source, destination);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});

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

	public static void removeProfile() throws IOException {
		Files.copy(new File(profileIniLocation).toPath(), new File(pilBackup).toPath());
		new File(finalProfile).delete();
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
