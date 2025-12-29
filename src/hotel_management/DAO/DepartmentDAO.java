/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    
    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM hotels_departments ORDER BY MaBoPhan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Department dept = new Department();
                dept.setMaBoPhan(rs.getInt("MaBoPhan"));
                dept.setTenBoPhan(rs.getString("TenBoPhan"));
                dept.setMoTaBoPhan(rs.getString("MoTaBoPhan"));
                dept.setLuongKhoiDiem(rs.getInt("LuongKhoiDiem"));
                dept.setChucDanh(rs.getString("ChucDanh"));
                list.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO hotels_departments (TenBoPhan, MoTaBoPhan, LuongKhoiDiem, ChucDanh) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dept.getTenBoPhan());
            pstmt.setString(2, dept.getMoTaBoPhan());
            pstmt.setInt(3, dept.getLuongKhoiDiem());
            pstmt.setString(4, dept.getChucDanh());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE hotels_departments SET TenBoPhan=?, MoTaBoPhan=?, LuongKhoiDiem=?, ChucDanh=? WHERE MaBoPhan=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dept.getTenBoPhan());
            pstmt.setString(2, dept.getMoTaBoPhan());
            pstmt.setInt(3, dept.getLuongKhoiDiem());
            pstmt.setString(4, dept.getChucDanh());
            pstmt.setInt(5, dept.getMaBoPhan());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDepartment(int maBoPhan) {
        String sql = "DELETE FROM hotels_departments WHERE MaBoPhan=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maBoPhan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
