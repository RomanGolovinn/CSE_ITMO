package io.db;

import io.crypto.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private final DatabaseConnectionManager connectionManager;

    public UserManager(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public boolean register(String username, String password) {
        String hash = PasswordHasher.hashPassword(password);
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hash);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public int authenticate(String username, String password) {
        String hash = PasswordHasher.hashPassword(password);
        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hash);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}