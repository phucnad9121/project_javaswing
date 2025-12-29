/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.RoomBooked;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomBookedDAO {
    
    public boolean addRoomBooked(int maDatPhong, int maPhong) {
        String sql = "INSERT INTO rooms_roombooked (MaDatPhong, MaPhong) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDatPhong);
            pstmt.setInt(2, maPhong);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<RoomBooked> getRoomsByBookingId(int maDatPhong) {
        List<RoomBooked> list = new ArrayList<>();
        String sql = "SELECT rb.*, r.SoPhong FROM rooms_roombooked rb " +
                    "JOIN rooms_room r ON rb.MaPhong = r.MaPhong " +
                    "WHERE rb.MaDatPhong = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDatPhong);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RoomBooked rb = new RoomBooked();
                rb.setMaPhongDaDat(rs.getInt("MaPhongDaDat"));
                rb.setMaDatPhong(rs.getInt("MaDatPhong"));
                rb.setMaPhong(rs.getInt("MaPhong"));
                rb.setSoPhong(rs.getString("SoPhong"));
                list.add(rb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean deleteRoomBooked(int maPhongDaDat) {
        String sql = "DELETE FROM rooms_roombooked WHERE MaPhongDaDat=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maPhongDaDat);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
