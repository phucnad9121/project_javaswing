/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Login;
import hotel_management.Models.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    
    public Login employeeLogin(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM authentication_login WHERE TenDangNhap=? AND MatKhau=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Login login = new Login();
                login.setMaDangNhap(rs.getInt("MaDangNhap"));
                login.setTenDangNhap(rs.getString("TenDangNhap"));
                login.setMatKhau(rs.getString("MatKhau"));
                login.setMaNhanVien(rs.getInt("MaNhanVien"));
                login.setNguoiDungMoi(rs.getString("NguoiDungMoi"));
                
                // Lấy thông tin nhân viên
                Employee emp = employeeDAO.getEmployeeById(login.getMaNhanVien());
                login.setEmployee(emp);
                
                return login;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Login> getAllLogins() {
        List<Login> list = new ArrayList<>();
        String sql = "SELECT l.*, e.HoNhanVien, e.TenNhanVien FROM authentication_login l " +
                    "JOIN hotels_employees e ON l.MaNhanVien = e.MaNhanVien " +
                    "ORDER BY l.MaDangNhap";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Login login = new Login();
                login.setMaDangNhap(rs.getInt("MaDangNhap"));
                login.setTenDangNhap(rs.getString("TenDangNhap"));
                login.setMaNhanVien(rs.getInt("MaNhanVien"));
                login.setNguoiDungMoi(rs.getString("NguoiDungMoi"));
                
                Employee emp = new Employee();
                emp.setMaNhanVien(rs.getInt("MaNhanVien"));
                emp.setHoNhanVien(rs.getString("HoNhanVien"));
                emp.setTenNhanVien(rs.getString("TenNhanVien"));
                login.setEmployee(emp);
                
                list.add(login);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean addLogin(Login login) {
        String sql = "INSERT INTO authentication_login (TenDangNhap, MatKhau, MaNhanVien, NguoiDungMoi) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login.getTenDangNhap());
            pstmt.setString(2, login.getMatKhau());
            pstmt.setInt(3, login.getMaNhanVien());
            pstmt.setString(4, login.getNguoiDungMoi());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePassword(int maDangNhap, String matKhauMoi) {
        String sql = "UPDATE authentication_login SET MatKhau=?, NguoiDungMoi='No' WHERE MaDangNhap=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, matKhauMoi);
            pstmt.setInt(2, maDangNhap);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteLogin(int maDangNhap) {
        String sql = "DELETE FROM authentication_login WHERE MaDangNhap=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDangNhap);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}