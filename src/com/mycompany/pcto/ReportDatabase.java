package com.mycompany.pcto;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportDatabase {

    private static final String DB_DIR = System.getProperty("user.home") + "/.galileo";
    private static final String DB_PATH = DB_DIR + "/reports.db";

    static {
        try {
            new File(DB_DIR).mkdirs();
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = connect();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS reports (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        created_at TEXT NOT NULL,
                        image_name TEXT NOT NULL,
                        fabric_type TEXT NOT NULL,
                        content TEXT NOT NULL
                    )
                """);
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS cache (
                        image_path TEXT NOT NULL,
                        fabric_type TEXT NOT NULL,
                        content TEXT NOT NULL,
                        cached_at TEXT NOT NULL,
                        PRIMARY KEY (image_path, fabric_type)
                    )
                """);
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_created_at ON reports(created_at DESC)");
            }
        } catch (Exception e) {
            System.err.println("Errore init DB: " + e.getMessage());
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    public static void save(String imageName, String fabricType, String content) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO reports (created_at, image_name, fabric_type, content) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            ps.setString(2, imageName);
            ps.setString(3, fabricType);
            ps.setString(4, content);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Errore salvataggio report: " + e.getMessage());
        }
    }

    public static List<String[]> getAll() {
        List<String[]> list = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, created_at, image_name, fabric_type FROM reports ORDER BY created_at DESC")) {
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("created_at"),
                    rs.getString("image_name"),
                    rs.getString("fabric_type")
                });
            }
        } catch (Exception e) {
            System.err.println("Errore lettura report: " + e.getMessage());
        }
        return list;
    }

    public static String getById(int id) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("SELECT content FROM reports WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("content");
        } catch (Exception e) {
            System.err.println("Errore lettura report: " + e.getMessage());
        }
        return null;
    }

    public static String getCached(String imagePath, String fabricType) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT content FROM cache WHERE image_path = ? AND fabric_type = ?")) {
            ps.setString(1, imagePath);
            ps.setString(2, fabricType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("content");
        } catch (Exception e) {
            System.err.println("Errore cache: " + e.getMessage());
        }
        return null;
    }

    public static void saveCache(String imagePath, String fabricType, String content) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT OR REPLACE INTO cache (image_path, fabric_type, content, cached_at) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, imagePath);
            ps.setString(2, fabricType);
            ps.setString(3, content);
            ps.setString(4, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Errore salvataggio cache: " + e.getMessage());
        }
    }
}
