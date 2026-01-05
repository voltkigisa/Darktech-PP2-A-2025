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
                // Menyesuaikan dengan getter baru di model Member
                model.addRow(new Object[]{
                    m.getId(),
                    m.getMember_code(),
                    m.getName(), 
                    m.getAge(), 
                    m.getPhone(), 
                    m.getAddress()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menambah parameter 'age' agar sesuai dengan database & model
    public boolean save(String member_code, String name, String age, String phone, String address) {
        try {
            memberDAO.insert(new Member(0, member_code, name, age, phone, address));
            return true;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    // Menambah parameter 'age' dan menyesuaikan constructor Member
    public boolean update(int id, String member_code, String name, String age, String phone, String address) {
        try {
            memberDAO.update(new Member(id, member_code, name, age, phone, address));
            return true;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    public boolean delete(int id) {
        try {
            memberDAO.delete(id);
            return true;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }
}