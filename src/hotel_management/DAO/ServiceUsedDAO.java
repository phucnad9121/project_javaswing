package hotel_management.DAO;

import hotel_management.Models.ServiceUsed;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUsedDAO {
    
    public boolean addServiceToBooking(int maDatPhong, int maDichVu, int soLuong, int donGia) {
        String sql = "INSERT INTO hotelservice_servicesused (MaDichVu, MaDatPhong, SoLuong, DonGia, ThanhTien) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int thanhTien = soLuong * donGia;
            
            pstmt.setInt(1, maDichVu);
            pstmt.setInt(2, maDatPhong);
            pstmt.setInt(3, soLuong);
            pstmt.setInt(4, donGia);
            pstmt.setInt(5, thanhTien);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ServiceUsed> getServicesByBookingId(int maDatPhong) {
        List<ServiceUsed> list = new ArrayList<>();
        String sql = "SELECT su.*, s.TenDichVu FROM hotelservice_servicesused su " +
                    "JOIN hotelservice_services s ON su.MaDichVu = s.MaDichVu " +
                    "WHERE su.MaDatPhong = ? ORDER BY su.NgaySuDung DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDatPhong);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ServiceUsed su = new ServiceUsed();
                su.setMaDichVuSuDung(rs.getInt("MaDichVuSuDung"));
                su.setMaDichVu(rs.getInt("MaDichVu"));
                su.setTenDichVu(rs.getString("TenDichVu"));
                su.setMaDatPhong(rs.getInt("MaDatPhong"));
                su.setSoLuong(rs.getInt("SoLuong"));
                su.setDonGia(rs.getInt("DonGia"));
                su.setThanhTien(rs.getInt("ThanhTien"));
                su.setNgaySuDung(rs.getTimestamp("NgaySuDung"));
                list.add(su);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int getTotalServiceCharge(int maDatPhong) {
        String sql = "SELECT COALESCE(SUM(ThanhTien), 0) as total FROM hotelservice_servicesused WHERE MaDatPhong = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDatPhong);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean deleteServiceFromBooking(int maDichVuSuDung) {
        String sql = "DELETE FROM hotelservice_servicesused WHERE MaDichVuSuDung = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDichVuSuDung);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateServiceQuantity(int maDichVuSuDung, int newQuantity) {
        String sql = "UPDATE hotelservice_servicesused SET SoLuong = ?, ThanhTien = DonGia * ? WHERE MaDichVuSuDung = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, newQuantity);
            pstmt.setInt(3, maDichVuSuDung);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}