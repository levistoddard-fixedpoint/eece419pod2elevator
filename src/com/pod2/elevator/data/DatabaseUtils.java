package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtils {

	private static String user;
	private static String password;
	private static String url;

	public static void initialize(Properties properties)
			throws InvalidDatabaseConfigurationException {
		final String USER_KEY = "user";
		final String PASSWORD_KEY = "password";
		final String URL_KEY = "url";

		if (properties.getProperty(USER_KEY) == null) {
			throw new InvalidDatabaseConfigurationException(
					"User name must be specified in database configuration.");
		}
		user = properties.getProperty(USER_KEY);
		if (properties.getProperty(PASSWORD_KEY) == null) {
			throw new InvalidDatabaseConfigurationException(
					"Password must be specified in database configuration.");
		}
		password = properties.getProperty(PASSWORD_KEY);
		if (properties.getProperty(URL_KEY) == null) {
			throw new InvalidDatabaseConfigurationException(
					"Connection must be specified in database configuration.");
		}
		url = properties.getProperty(URL_KEY);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

}
