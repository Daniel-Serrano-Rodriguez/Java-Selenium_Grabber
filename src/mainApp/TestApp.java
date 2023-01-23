package mainApp;

import java.util.List;

import dao.GGenerales;
import dao.Gestor;
import models.FFDriver;
import models.Generales;

public class TestApp {
	public static void main(String[] args) {
//		Gestor<Generales> gestor = new GGenerales();
//		Generales general = new Generales("https://es.pornhub.com/view_video.php?viewkey=ph628b6da0c2ec8");
//		List<Generales> generales;
//
//		gestor.insert(general);
//		generales = gestor.getByNotDownloaded();
//
//		for (Generales generales2 : generales) {
//			System.out.println(generales2.getUrl());
//		}
		new FFDriver("test");
	}
}
