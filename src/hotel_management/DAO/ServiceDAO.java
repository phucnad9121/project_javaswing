package hotel_management.DAO;

import hotel_management.Models.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM hotelservice_services";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Service service = new Service();
                service.setMaDichVu(rs.getInt("MaDichVu"));
                service.setTenDichVu(rs.getString("TenDichVu"));
                service.setMoTaDichVu(rs.getString("MoTaDichVu"));
                service.setChiPhiDichVu(rs.getInt("ChiPhiDichVu"));
                list.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Service getServiceById(int maDichVu) {
        String sql = "SELECT * FROM hotelservice_services WHERE MaDichVu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDichVu);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Service service = new Service();
                service.setMaDichVu(rs.getInt("MaDichVu"));
                service.setTenDichVu(rs.getString("TenDichVu"));
                service.setMoTaDichVu(rs.getString("MoTaDichVu"));
                service.setChiPhiDichVu(rs.getInt("ChiPhiDichVu"));
                return service;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addService(Service service) {
        String sql = "INSERT INTO hotelservice_services (TenDichVu, MoTaDichVu, ChiPhiDichVu) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, service.getTenDichVu());
            pstmt.setString(2, service.getMoTaDichVu());
            pstmt.setInt(3, service.getChiPhiDichVu());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateService(Service service) {
        String sql = "UPDATE hotelservice_services SET TenDichVu = ?, MoTaDichVu = ?, ChiPhiDichVu = ? WHERE MaDichVu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, service.getTenDichVu());
            pstmt.setString(2, service.getMoTaDichVu());
            pstmt.setInt(3, service.getChiPhiDichVu());
            pstmt.setInt(4, service.getMaDichVu());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteService(int maDichVu) {
        String sql = "DELETE FROM hotelservice_services WHERE MaDichVu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDichVu);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}