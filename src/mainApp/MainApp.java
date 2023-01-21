package mainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Grabber;

public class MainApp {
	public static void main(String[] args) {
		Thread[] hilos;
		BufferedReader br;
		String linea;
		List<String> urls = new ArrayList<>();
		int urlCount = 0;
		File source = new File(args[0]);

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

			for (int i = 0; i < urlCount; i++) {
				hilos[i].start();
				Thread.sleep(10000);
				if (i % 5 == 0 && i != 0)
					Thread.sleep(30000);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
