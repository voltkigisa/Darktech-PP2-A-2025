package com.library.pos.controllers.admin;

import com.library.pos.dao.MemberDAO;
import com.library.pos.models.Member;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MemberController {
    private final MemberDAO memberDAO = new MemberDAO();

    public void loadDataToTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Member> list = memberDAO.getAll();
            for (Member m : list) {
                model.addRow(new Object[]{m.getId(), m.getNama(), m.getTelepon(), m.getAlamat()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean save(String nama, String telp, String alamat) {
        try {
            memberDAO.insert(new Member(0, nama, telp, alamat));
            return true;
        } catch (SQLException e) { 
            return false; 
        }
    }

    public boolean update(int id, String nama, String telp, String alamat) {
        try {
            memberDAO.update(new Member(id, nama, telp, alamat));
            return true;
        } catch (SQLException e) { 
            return false; 
        }
    }

    public boolean delete(int id) {
        try {
            memberDAO.delete(id);
            return true;
        } catch (SQLException e) { 
            return false; 
        }
    }
}