package com.library.pos.dao;

import com.library.pos.config.DatabaseConfig;
import com.library.pos.models.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public List<Member> getAll() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM anggota";
        
        // Menggunakan Singleton: getInstance().getConnection()
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Member m = new Member();
                m.setId(rs.getInt("id"));
                m.setNama(rs.getString("nama"));
                m.setTelepon(rs.getString("telepon"));
                m.setAlamat(rs.getString("alamat"));
                list.add(m);
            }
        }
        return list;
    }

    public void insert(Member m) throws SQLException {
        String sql = "INSERT INTO anggota (nama, telepon, alamat) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, m.getNama());
            pst.setString(2, m.getTelepon());
            pst.setString(3, m.getAlamat());
            pst.executeUpdate();
        }
    }

    public void update(Member m) throws SQLException {
        String sql = "UPDATE anggota SET nama=?, telepon=?, alamat=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, m.getNama());
            pst.setString(2, m.getTelepon());
            pst.setString(3, m.getAlamat());
            pst.setInt(4, m.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM anggota WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}