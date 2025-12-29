/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.*, rt.TenLoaiPhong FROM rooms_room r " +
                    "JOIN rooms_roomtype rt ON r.MaLoaiPhong = rt.MaLoaiPhong " +
                    "ORDER BY r.SoPhong";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room();
                room.setMaPhong(rs.getInt("MaPhong"));
                room.setSoPhong(rs.getString("SoPhong"));
                room.setMaLoaiPhong(rs.getInt("MaLoaiPhong"));
                room.setTenLoaiPhong(rs.getString("TenLoaiPhong"));
                room.setKhaDung(rs.getString("KhaDung"));
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Room> getAvailableRoomsByType(int maLoaiPhong) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.*, rt.TenLoaiPhong FROM rooms_room r " +
                    "JOIN rooms_roomtype rt ON r.MaLoaiPhong = rt.MaLoaiPhong " +
                    "WHERE r.KhaDung = 'Yes' AND r.MaLoaiPhong = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maLoaiPhong);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Room room = new Room();
                room.setMaPhong(rs.getInt("MaPhong"));
                room.setSoPhong(rs.getString("SoPhong"));
                room.setMaLoaiPhong(rs.getInt("MaLoaiPhong"));
                room.setTenLoaiPhong(rs.getString("TenLoaiPhong"));
                room.setKhaDung(rs.getString("KhaDung"));
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int countAvailableRoomsByType(int maLoaiPhong) {
        String sql = "SELECT COUNT(*) as total FROM rooms_room " +
                    "WHERE MaLoaiPhong = ? AND KhaDung = 'Yes'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maLoaiPhong);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms_room (SoPhong, MaLoaiPhong, KhaDung) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getSoPhong());
            pstmt.setInt(2, room.getMaLoaiPhong());
            pstmt.setString(3, room.getKhaDung());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms_room SET SoPhong=?, MaLoaiPhong=?, KhaDung=? WHERE MaPhong=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getSoPhong());
            pstmt.setInt(2, room.getMaLoaiPhong());
            pstmt.setString(3, room.getKhaDung());
            pstmt.setInt(4, room.getMaPhong());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRoom(int maPhong) {
        String sql = "DELETE FROM rooms_room WHERE MaPhong=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maPhong);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

