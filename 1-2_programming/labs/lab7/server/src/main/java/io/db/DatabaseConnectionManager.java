package io.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.flywaydb.core.Flyway;

public class DatabaseConnectionManager {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void migrate() {
        System.out.println("Запуск миграций базы данных...");
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load();

        flyway.migrate();
        System.out.println("Миграции успешно применены!");
    }
}