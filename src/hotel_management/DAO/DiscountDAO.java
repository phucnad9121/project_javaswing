/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Discount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {
    
    public List<Discount> getAllDiscounts() {
        List<Discount> list = new ArrayList<>();
        String sql = "SELECT d.*, CONCAT(e.HoNhanVien, ' ', e.TenNhanVien) as TenNhanVien " +
                    "FROM bookings_discount d " +
                    "JOIN hotels_employees e ON d.MaNhanVien = e.MaNhanVien " +
                    "ORDER BY d.MaGiamGia";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Discount discount = new Discount();
                discount.setMaGiamGia(rs.getInt("MaGiamGia"));
                discount.setTenGiamGia(rs.getString("TenGiamGia"));
                discount.setMoTaGiamGia(rs.getString("MoTaGiamGia"));
                discount.setTyLeGiamGia(rs.getInt("TyLeGiamGia"));
                discount.setMaNhanVien(rs.getInt("MaNhanVien"));
                discount.setTenNhanVien(rs.getString("TenNhanVien"));
                list.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean addDiscount(Discount discount) {
        String sql = "INSERT INTO bookings_discount (TenGiamGia, MoTaGiamGia, TyLeGiamGia, MaNhanVien) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, discount.getTenGiamGia());
            pstmt.setString(2, discount.getMoTaGiamGia());
            pstmt.setInt(3, discount.getTyLeGiamGia());
            pstmt.setInt(4, discount.getMaNhanVien());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDiscount(Discount discount) {
        String sql = "UPDATE bookings_discount SET TenGiamGia=?, MoTaGiamGia=?, TyLeGiamGia=?, MaNhanVien=? " +
                    "WHERE MaGiamGia=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, discount.getTenGiamGia());
            pstmt.setString(2, discount.getMoTaGiamGia());
            pstmt.setInt(3, discount.getTyLeGiamGia());
            pstmt.setInt(4, discount.getMaNhanVien());
            pstmt.setInt(5, discount.getMaGiamGia());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDiscount(int maGiamGia) {
        String sql = "DELETE FROM bookings_discount WHERE MaGiamGia=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maGiamGia);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
