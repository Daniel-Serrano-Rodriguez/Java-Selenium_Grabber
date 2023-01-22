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
		int urlCount = 0, urlsPassed = 0, safeThreadCount = Runtime.getRuntime().availableProcessors(), threadsUsed = 0;
		File source = new File(args[0]);
		boolean complete = false;

		if (safeThreadCount % 4 == 0)
			safeThreadCount = safeThreadCount / 4;
		else if (safeThreadCount % 3 == 0)
			safeThreadCount = safeThreadCount / 3;

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
//					System.out.println("Hilos usados: " + threadsUsed + " de " + safeThreadCount);

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

//			for (int i = 0; i < urlCount; i++) {
//				if (threadsUsed < safeThreadCount - 1) {
//					System.out.println("Enlace: " + (i + 1));
//					hilos[i].start();
//					threadsUsed++;
//					System.out.println("Hilos usados: " + threadsUsed + " de " + safeThreadCount);
//
//					for (int j = 0; j < i; j++) {
//						if (hilos[j] != null) {
//							if (!hilos[j].isAlive()) {
//								System.out.println("Hilo disponible!");
//								threadsUsed--;
//								hilos[j] = null;
//							}
//						}
//					}
//
//					Thread.sleep(5000);
//				} else {
//					System.out.println("heeeooo");
//					for (int j = 0 + i; j <= threadsUsed + i; j++) {
//						if (!hilos[j].isAlive()) {
//							System.out.println("Hilo disponible!");
//							threadsUsed--;
//						}
//					}
//
//					if (threadsUsed >= safeThreadCount - 1) {
//						System.out.println("Esperando 10 segundos para liberar hilo.");
//						Thread.sleep(10000);
//					}
//
//					urlCount--;
//				}
//			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
