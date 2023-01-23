package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Generales;

public class GGenerales implements Gestor<Generales> {

	private Connection conn;

	public GGenerales() {
		try {
			conn = new SQLiteDAO().getConn();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Generales getById(String id) {
		Generales general = null;

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select url, downloaded from general where id = " + Integer.parseInt(id));
			rs.next();

			general = new Generales(rs.getString(1), rs.getInt(2));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return general;
	}

	@Override
	public Generales getByUrl(String url) {
		Generales general = null;

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select url, downloaded from general where url = " + url);
			rs.next();

			general = new Generales(rs.getString(1), rs.getInt(2));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return general;
	}

	@Override
	public List<Generales> getAll() {
		List<Generales> generales = new ArrayList<>();

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select url, downloaded from general");

			while (rs.next())
				generales.add(new Generales(rs.getString(1), rs.getInt(2)));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generales;
	}

	@Override
	public List<Generales> getByNotDownloaded() {
		List<Generales> generales = new ArrayList<>();

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select url from general where downloaded = 0");

			while (rs.next())
				generales.add(new Generales(rs.getString(1)));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generales;
	}

	@Override
	public boolean insert(Generales object) {
		boolean ok = false;

		try {
			Statement stmt = conn.createStatement();
			ok = stmt.execute("insert into general(url) values ('" + object.getUrl() + "');");
		} catch (SQLException e) {
			System.err.println("URL already exists");
		}

		return ok;
	}

}
