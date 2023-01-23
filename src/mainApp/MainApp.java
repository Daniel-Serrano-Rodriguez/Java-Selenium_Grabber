package mainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Grabber;

public class MainApp {

	/*
	 * TODO Add selenium entry in profiles.ini. Folders:
	 * 
	 * %APPDATA%/Roaming/Mozilla/Firefox for windows
	 * 
	 * $HOME/.mozilla/firefox for linux
	 * 
	 * [Profile69]
	 * 
	 * Name=selenium
	 * 
	 * IsRelative=0
	 * 
	 * Path=6wr009zz.selenium
	 */
	public static void main(String[] args) {
		Thread[] hilos;
		BufferedReader br;
		String linea;
		List<String> urls = new ArrayList<>();
		int urlCount = 0, urlsPassed = 0, safeThreadCount = Runtime.getRuntime().availableProcessors(), threadsUsed = 0;
		File source = new File(args[0]);
		boolean complete = false;

		if (safeThreadCount > 4) {
			if (safeThreadCount % 4 == 0)
				safeThreadCount = safeThreadCount / 4;
			else if (safeThreadCount % 3 == 0)
				safeThreadCount = safeThreadCount / 3;
		} else
			safeThreadCount = 1;

		try {
			br = new BufferedReader(new FileReader(source));

			while ((linea = br.readLine()) != null)
				if (!linea.isBlank()) {
					urls.add(linea);
					urlCount++;
				}
			br.close();

			hilos = new Thread[urlCount];

			for (int i = 0; i < urlCount; i++) {
				hilos[i] = new Grabber(urls.get(i), args[1], i);
			}

			while (!complete) {
				if (threadsUsed < safeThreadCount) {
					for (int i = 0; i < urlsPassed; i++)
						if (hilos[i] != null)
							if (!hilos[i].isAlive()) {
								threadsUsed--;
								hilos[i] = null;
							}

					hilos[urlsPassed].start();
					urlsPassed++;
					threadsUsed++;

					System.out.println((safeThreadCount - threadsUsed) + " hilos disponibles");
					System.out.println("Enlace " + urlsPassed + ": " + urls.get(urlsPassed - 1));

					Thread.sleep(5000);
				} else {
					for (int i = 0; i < urlsPassed; i++)
						if (hilos[i] != null)
							if (!hilos[i].isAlive()) {
								threadsUsed--;
								hilos[i] = null;
							}
					if (threadsUsed < safeThreadCount)
						System.out.println((safeThreadCount - threadsUsed) + " hilos disponibles");
					else {
						System.out.println("Hilos ocupados: " + threadsUsed);
						Thread.sleep(10000);
					}
				}

				if (urlsPassed == urlCount - 1)
					complete = true;
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
