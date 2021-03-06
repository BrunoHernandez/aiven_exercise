package com.aiven.consumer;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public final class DataAccessObject {

    private Connection connection;
    private boolean autoCommit = true;

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS Records "
        + "( record_offset BIGINT  PRIMARY KEY  NOT NULL,"
        + " record_value  CHAR(20) )";
    private static final String[] SQL_RESERVED_CHARACTERS =
    { ",", "&", "?", "{", "}", "\\", "(", ")", "[", "]", "-", ";", "~", "|",
      "$", "!", ">", "*", "%", "_"/*wildcard*/};

    public DataAccessObject(Connection connection) throws SQLException {
        if (connection == null) {
            System.err.println("Unable to create DataAccessObject");
            return;
        }
        this.connection = connection;
        createTable();
    }

    private void createTable() throws SQLException {
        final String[] updates = {SQL_CREATE_TABLE};
        executeUpdates(updates, !autoCommit);
    }

    private void executeUpdates(String[] updates, boolean needsCommit)
            throws SQLException {
        String currentUpdate = "none";
        try {
            Statement statement = connection.createStatement();
            for (String update : updates) {
                currentUpdate = update;
                statement.executeUpdate(update);
            }
            statement.close();
            if (needsCommit) {
                connection.commit();
            }
        } catch (SQLException exception) {
            throw new SQLException("Error updating table: "
                + "\nupdate: " + currentUpdate
                + "\nexception: " + exception.getMessage(), exception);
        }
    }

    public void insertRecord(long offset, String value) throws SQLException {
        checkSqlReservedCharacters(value);
        final String SQL_INSERT_RECORD =
            "INSERT INTO Records (record_offset, record_value) "
            + "VALUES (\'" + offset + "\', \'" + value + "\');";
        final String[] updates = {SQL_INSERT_RECORD};
        executeUpdates(updates, !autoCommit);
    }

    private void checkSqlReservedCharacters(String word) throws SQLException {
        for (String character : SQL_RESERVED_CHARACTERS) {
            if (word.contains(character)) {
                throw new SQLException("The word: \"" + word
                    + "\" contains the SQL reserved character \""
                    + character + "\".");
            }
        }
    }
}
