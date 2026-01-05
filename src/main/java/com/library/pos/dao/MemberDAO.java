package com.library.pos.dao;

import com.library.pos.config.DatabaseConfig;
import com.library.pos.models.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public List<Member> getAll() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member(
                    rs.getInt("id"),
                    rs.getString("member_code"),
                    rs.getString("name"),
                    rs.getString("age"),
                    rs.getString("phone"),
                    rs.getString("address")
                ));
            }
        }
        return list;
    }

    public void insert(Member m) throws SQLException {
        String sql = "INSERT INTO members (name, member_code, age, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, m.getMember_code());
            pst.setString(2, m.getName());
            pst.setString(3, m.getAge());
            pst.setString(4, m.getPhone());
            pst.setString(5, m.getAddress());
            pst.executeUpdate();
        }
    }

    public void update(Member m) throws SQLException {
        String sql = "UPDATE members SET member_code=?, name=?, age=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, m.getMember_code());
            pst.setString(2, m.getName());
            pst.setString(3, m.getAge());
            pst.setString(4, m.getPhone());
            pst.setString(5, m.getAddress());
            pst.setInt(6, m.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM members WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}