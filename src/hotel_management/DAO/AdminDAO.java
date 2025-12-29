/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Admin;
import java.sql.*;

public class AdminDAO {
    
    public Admin adminLogin(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM authentication_admin WHERE TenDangNhap=? AND MatKhau=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setMaAdmin(rs.getInt("MaAdmin"));
                admin.setTenDangNhap(rs.getString("TenDangNhap"));
                admin.setMatKhau(rs.getString("MatKhau"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}