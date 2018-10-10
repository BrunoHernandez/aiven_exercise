package com.aiven.consumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ScopedPostgreSqlConnection implements AutoCloseable {

    private static String PROPERTIES_PATH = "postgresql.properties";
    private static String OPEN_RETRIES = "connection.open.retries";
    private static String CLOSE_RETRIES = "connection.close.retries";
    private static String CONNECTION_TIMEOUT =
        "connection.timeout.milliseconds";
    private Connection connection = null;
    private Properties properties = new Properties();

    public ScopedPostgreSqlConnection(DataAccessObject dataAccessObject)
            throws SQLException{
        try {
            properties.load(new java.io.FileInputStream(PROPERTIES_PATH));
        } catch (java.io.IOException exception) {
            System.err.println("Unable to load properties file: "
                               + exception);
        }
        openConnection();
        dataAccessObject = new DataAccessObject(connection);
    }

    private void openConnection() {
        final int RETRIES = Integer.parseInt(
            properties.getProperty(OPEN_RETRIES));
        for (int i = 0; i < RETRIES; i++) {
            try {
                connection = DriverManager.getConnection(
                    properties.getProperty("jdbc.url"), properties);
                System.out.println("Database connection OK.");
                return;
            } catch (SQLException exception) {
                System.err.println("Error getting database connection: "
                                   + exception.getMessage());
                sleep();
            }
        }
        terminate();
    }

    private void terminate() {
        close();
        System.err.println("[PostgreSQL] Giving up...");
        return;
    }

    public void close() {
        if (connection == null) {
            return;
        }
        final int RETRIES = Integer.parseInt(
            properties.getProperty(CLOSE_RETRIES));
        for (int i = 0; i < RETRIES; i++) {
            try {
                connection.close();
                return;
            } catch (SQLException exception) {
                System.err.println("Error closing connection: "
                                   + exception.getMessage());
                sleep();
            }
        }
    }

    private void sleep() {
        final long TIMEOUT = Integer.parseInt(
            properties.getProperty(CONNECTION_TIMEOUT));
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException exception) {
            System.err.println("Thread interrupted.");
        }
    }

    public boolean isOpen() {
        return connection != null;
    }
}
