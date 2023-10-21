package me.workwolf.skeletondungeon.Utils.Database;

import me.workwolf.skeletondungeon.SkeletonDungeon;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Database {

    public Connection connection;
    private final SkeletonDungeon Sd;

    public Database(SkeletonDungeon Sd, String file) throws ClassNotFoundException, SQLException {
        this.Sd = Sd;
        this.connection = DriverManager.getConnection("jdbc:sqlite:"+file);
        Class.forName("org.sqlite.JDBC");
        createTable();
    }


    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS bossroom(name TEXT NOT NULL, opened BOOLEAN NOT NULL)";
        Statement st = this.connection.createStatement();
        st.execute(sql);
        close(st);
    }

    public void addDungeon(String name, boolean opened) {
        String sql = "INSERT INTO bossroom(name, opened) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setBoolean(2, opened);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }
    }

    public void updateOpenedValue(String name, boolean value) throws SQLException {
        String sql = "UPDATE bossroom set opened = ? WHERE name = ?";
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(sql);
            ps.setBoolean(1, value);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }
    }

    public Boolean getOpened(String name) {
        String sql = "SELECT opened FROM bossroom WHERE name = ?";
        PreparedStatement st = null;
        try {
            st = this.connection.prepareStatement(sql);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("opened");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
        }
        return false;
    }

    public Boolean getName() {
        String sql = "SELECT name FROM bossroom";
        PreparedStatement st = null;
        try {
            st = this.connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
        }
        return false;
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
