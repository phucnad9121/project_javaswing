/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, d.TenBoPhan FROM hotels_employees e " +
                    "LEFT JOIN hotels_departments d ON e.MaBoPhan = d.MaBoPhan " +
                    "ORDER BY e.MaNhanVien";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setMaNhanVien(rs.getInt("MaNhanVien"));
                emp.setTenNhanVien(rs.getString("TenNhanVien"));
                emp.setHoNhanVien(rs.getString("HoNhanVien"));
                emp.setChucDanhNV(rs.getString("ChucDanhNV"));
                emp.setSoDienThoaiNV(rs.getString("SoDienThoaiNV"));
                emp.setEmailNhanVien(rs.getString("EmailNhanVien"));
                emp.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                emp.setDiaChi(rs.getString("DiaChi"));
                emp.setMaBoPhan(rs.getInt("MaBoPhan"));
                emp.setTenBoPhan(rs.getString("TenBoPhan"));
                emp.setCmndCccd(rs.getString("CMND_CCCD"));
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Employee getEmployeeById(int maNhanVien) {
        String sql = "SELECT e.*, d.TenBoPhan FROM hotels_employees e " +
                    "LEFT JOIN hotels_departments d ON e.MaBoPhan = d.MaBoPhan " +
                    "WHERE e.MaNhanVien = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maNhanVien);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Employee emp = new Employee();
                emp.setMaNhanVien(rs.getInt("MaNhanVien"));
                emp.setTenNhanVien(rs.getString("TenNhanVien"));
                emp.setHoNhanVien(rs.getString("HoNhanVien"));
                emp.setChucDanhNV(rs.getString("ChucDanhNV"));
                emp.setSoDienThoaiNV(rs.getString("SoDienThoaiNV"));
                emp.setEmailNhanVien(rs.getString("EmailNhanVien"));
                emp.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                emp.setDiaChi(rs.getString("DiaChi"));
                emp.setMaBoPhan(rs.getInt("MaBoPhan"));
emp.setTenBoPhan(rs.getString("TenBoPhan"));
                emp.setCmndCccd(rs.getString("CMND_CCCD"));
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO hotels_employees (TenNhanVien, HoNhanVien, ChucDanhNV, SoDienThoaiNV, " +
                    "EmailNhanVien, NgayVaoLam, DiaChi, MaBoPhan, CMND_CCCD) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, emp.getTenNhanVien());
            pstmt.setString(2, emp.getHoNhanVien());
            pstmt.setString(3, emp.getChucDanhNV());
            pstmt.setString(4, emp.getSoDienThoaiNV());
            pstmt.setString(5, emp.getEmailNhanVien());
            pstmt.setDate(6, emp.getNgayVaoLam());
            pstmt.setString(7, emp.getDiaChi());
            pstmt.setInt(8, emp.getMaBoPhan());
            pstmt.setString(9, emp.getCmndCccd());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE hotels_employees SET TenNhanVien=?, HoNhanVien=?, ChucDanhNV=?, " +
                    "SoDienThoaiNV=?, EmailNhanVien=?, NgayVaoLam=?, DiaChi=?, MaBoPhan=?, CMND_CCCD=? " +
                    "WHERE MaNhanVien=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, emp.getTenNhanVien());
            pstmt.setString(2, emp.getHoNhanVien());
            pstmt.setString(3, emp.getChucDanhNV());
            pstmt.setString(4, emp.getSoDienThoaiNV());
            pstmt.setString(5, emp.getEmailNhanVien());
            pstmt.setDate(6, emp.getNgayVaoLam());
            pstmt.setString(7, emp.getDiaChi());
            pstmt.setInt(8, emp.getMaBoPhan());
            pstmt.setString(9, emp.getCmndCccd());
            pstmt.setInt(10, emp.getMaNhanVien());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteEmployee(int maNhanVien) {
        String sql = "DELETE FROM hotels_employees WHERE MaNhanVien=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maNhanVien);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
public List<Employee> searchEmployees(String keyword) {
    List<Employee> list = new ArrayList<>();
    // Tìm kiếm theo Mã (số) hoặc Tên, Họ (chuỗi)
    String sql = "SELECT e.*, d.TenBoPhan FROM hotels_employees e " +
                "LEFT JOIN hotels_departments d ON e.MaBoPhan = d.MaBoPhan " +
                "WHERE e.MaNhanVien LIKE ? OR e.TenNhanVien LIKE ? OR e.HoNhanVien LIKE ? " +
                "ORDER BY e.MaNhanVien";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        String searchKey = "%" + keyword + "%";
        pstmt.setString(1, searchKey);
        pstmt.setString(2, searchKey);
        pstmt.setString(3, searchKey);
        
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Employee emp = new Employee();
            emp.setMaNhanVien(rs.getInt("MaNhanVien"));
            emp.setTenNhanVien(rs.getString("TenNhanVien"));
            emp.setHoNhanVien(rs.getString("HoNhanVien"));
            emp.setChucDanhNV(rs.getString("ChucDanhNV"));
            emp.setSoDienThoaiNV(rs.getString("SoDienThoaiNV"));
            emp.setEmailNhanVien(rs.getString("EmailNhanVien"));
            emp.setNgayVaoLam(rs.getDate("NgayVaoLam"));
            emp.setDiaChi(rs.getString("DiaChi"));
            emp.setMaBoPhan(rs.getInt("MaBoPhan"));
            emp.setTenBoPhan(rs.getString("TenBoPhan"));
            emp.setCmndCccd(rs.getString("CMND_CCCD"));
            list.add(emp);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
}
