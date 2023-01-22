package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDAO {

	private Connection conn;

	private SQLiteDAO() {
	}

	public Connection getConn() throws SQLException {
		String url = "db/ddbb.db";

		if (conn == null)
			conn = DriverManager.getConnection(url);

		return conn;
	}

	public void closeConn() throws SQLException {
		conn.close();
	}
}
