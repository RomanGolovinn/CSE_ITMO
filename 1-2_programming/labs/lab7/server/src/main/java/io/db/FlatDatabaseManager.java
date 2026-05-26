package io.db;

import models.Coordinates;
import models.Flat;
import models.House;
import models.enums.Furnish;
import models.enums.Transport;
import models.enums.View;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FlatDatabaseManager {
    private final DatabaseConnectionManager connectionManager;

    public FlatDatabaseManager(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Long addFlat(Flat flat, int ownerId) {
        String sql = "INSERT INTO flats (name, x, y, creation_date, area, number_of_rooms, furnish, view, transport, house_name, house_year, house_floors, owner_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, flat.getName());
            pstmt.setLong(2, flat.getCoordinates().getX());
            pstmt.setDouble(3, flat.getCoordinates().getY());
            pstmt.setTimestamp(4, Timestamp.valueOf(flat.getCreationDate() != null ? flat.getCreationDate() : java.time.LocalDateTime.now()));
            pstmt.setLong(5, flat.getArea());
            pstmt.setLong(6, flat.getNumberOfRooms());

            if (flat.getFurnish() != null) {
                pstmt.setString(7, flat.getFurnish().name());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
            }

            if (flat.getView() != null) {
                pstmt.setString(8, flat.getView().name());
            } else {
                pstmt.setNull(8, Types.VARCHAR);
            }

            if (flat.getTransport() != null) {
                pstmt.setString(9, flat.getTransport().name());
            } else {
                pstmt.setNull(9, Types.VARCHAR);
            }

            if (flat.getHouse() != null) {
                pstmt.setString(10, flat.getHouse().getName());
                pstmt.setLong(11, flat.getHouse().getYear());
                pstmt.setLong(12, flat.getHouse().getNumberOfFloors());
            } else {
                pstmt.setNull(10, Types.VARCHAR);
                pstmt.setNull(11, Types.BIGINT);
                pstmt.setNull(12, Types.BIGINT);
            }

            pstmt.setInt(13, ownerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public boolean removeById(Long flatId, int ownerId) {
        String sql = "DELETE FROM flats WHERE id = ? AND owner_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, flatId);
            pstmt.setInt(2, ownerId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ConcurrentLinkedDeque<Flat> loadCollection() {
        ConcurrentLinkedDeque<Flat> collection = new ConcurrentLinkedDeque<>();
        String sql = "SELECT * FROM flats";

        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coordinates coordinates = new Coordinates(
                        rs.getLong("x"),
                        rs.getFloat("y")
                );

                House house = null;
                if (rs.getString("house_name") != null) {
                    house = new House(
                            rs.getString("house_name"),
                            rs.getInt("house_year"),
                            rs.getLong("house_floors")
                    );
                }

                Furnish furnish = rs.getString("furnish") != null ? Furnish.valueOf(rs.getString("furnish")) : null;
                View view = rs.getString("view") != null ? View.valueOf(rs.getString("view")) : null;
                Transport transport = rs.getString("transport") != null ? Transport.valueOf(rs.getString("transport")) : null;

                Flat flat = new Flat(
                        rs.getString("name"),
                        coordinates,
                        rs.getLong("area"),
                        rs.getLong("number_of_rooms"),
                        furnish,
                        view,
                        transport,
                        house
                );

                flat.setId(rs.getLong("id"));
                flat.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                flat.setOwnerId(rs.getInt("owner_id"));

                collection.add(flat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collection;
    }

    public boolean updateFlat(Long id, Flat flat) {
        int ownerId = io.auth.UserContext.getId();

        String sql = "UPDATE flats SET name = ?, x = ?, y = ?, area = ?, " +
                "number_of_rooms = ?, furnish = ?, \"view\" = ?, transport = ?, " +
                "house_name = ?, house_year = ?, house_floors = ? " +
                "WHERE id = ? AND owner_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flat.getName());
            ps.setLong(2, flat.getCoordinates().getX());
            ps.setFloat(3, flat.getCoordinates().getY());

            if (flat.getArea() == null) {
                ps.setNull(4, java.sql.Types.BIGINT);
            } else {
                ps.setLong(4, flat.getArea());
            }

            if (flat.getNumberOfRooms() == null) {
                ps.setNull(5, java.sql.Types.BIGINT);
            } else {
                ps.setLong(5, flat.getNumberOfRooms());
            }

            ps.setString(6, flat.getFurnish().name());
            ps.setString(7, flat.getView().name());
            ps.setString(8, flat.getTransport().name());
            ps.setString(9, flat.getHouse().getName());

            if (flat.getHouse().getYear() == null) {
                ps.setNull(10, java.sql.Types.INTEGER);
            } else {
                ps.setInt(10, flat.getHouse().getYear());
            }

            ps.setLong(11, flat.getHouse().getNumberOfFloors());

            ps.setLong(12, id);
            ps.setInt(13, ownerId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            return false;
        }
    }
}