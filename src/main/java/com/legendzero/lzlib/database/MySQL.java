package com.legendzero.lzlib.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database {
	private final String hostname;
	private final String port;
	private final String database;
	private final String user;
	private final String password;

	public MySQL(String hostname, String port, String database, String username, String password) {
		super("MySQL", "com.mysql.jdbc.Driver");
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
	}

	@Override
	protected Connection openConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + this.hostname +
						":" + this.port + "/" + this.database +
						"?autoReconnect=true",
				this.user, this.password);
	}
}
