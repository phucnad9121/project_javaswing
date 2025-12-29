/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.RoomType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {
    
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms_roomtype ORDER BY MaLoaiPhong";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                RoomType rt = new RoomType();
                rt.setMaLoaiPhong(rs.getInt("MaLoaiPhong"));
                rt.setTenLoaiPhong(rs.getString("TenLoaiPhong"));
                rt.setGiaPhong(rs.getInt("GiaPhong"));
                rt.setMoTaPhong(rs.getString("MoTaPhong"));
                list.add(rt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public RoomType getRoomTypeById(int maLoaiPhong) {
        String sql = "SELECT * FROM rooms_roomtype WHERE MaLoaiPhong=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maLoaiPhong);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                RoomType rt = new RoomType();
                rt.setMaLoaiPhong(rs.getInt("MaLoaiPhong"));
                rt.setTenLoaiPhong(rs.getString("TenLoaiPhong"));
                rt.setGiaPhong(rs.getInt("GiaPhong"));
                rt.setMoTaPhong(rs.getString("MoTaPhong"));
                return rt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addRoomType(RoomType roomType) {
        String sql = "INSERT INTO rooms_roomtype (TenLoaiPhong, GiaPhong, MoTaPhong) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomType.getTenLoaiPhong());
            pstmt.setInt(2, roomType.getGiaPhong());
            pstmt.setString(3, roomType.getMoTaPhong());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoomType(RoomType roomType) {
        String sql = "UPDATE rooms_roomtype SET TenLoaiPhong=?, GiaPhong=?, MoTaPhong=? WHERE MaLoaiPhong=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomType.getTenLoaiPhong());
            pstmt.setInt(2, roomType.getGiaPhong());
            pstmt.setString(3, roomType.getMoTaPhong());
            pstmt.setInt(4, roomType.getMaLoaiPhong());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRoomType(int maLoaiPhong) {
        String sql = "DELETE FROM rooms_roomtype WHERE MaLoaiPhong=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maLoaiPhong);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
