/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Guest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    
    public Guest register(Guest guest) {
        String sql = "INSERT INTO hotels_guests (TenKhachHang, HoKhachHang, EmailKhachHang, " +
                    "SoDienThoaiKhachHang, CMND_CCCDKhachHang, DiaChi, MatKhau) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, guest.getTenKhachHang());
            pstmt.setString(2, guest.getHoKhachHang());
            pstmt.setString(3, guest.getEmailKhachHang());
            pstmt.setString(4, guest.getSoDienThoaiKhachHang());
            pstmt.setString(5, guest.getCmndCccdKhachHang());
            pstmt.setString(6, guest.getDiaChi());
            pstmt.setString(7, guest.getMatKhau());
            
            if (pstmt.executeUpdate() > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    guest.setMaKhachHang(rs.getInt(1));
                    return guest;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Guest login(String soDienThoai, String matKhau) {
        String sql = "SELECT * FROM hotels_guests WHERE SoDienThoaiKhachHang=? AND MatKhau=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, soDienThoai);
            pstmt.setString(2, matKhau);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Guest guest = new Guest();
                guest.setMaKhachHang(rs.getInt("MaKhachHang"));
                guest.setTenKhachHang(rs.getString("TenKhachHang"));
                guest.setHoKhachHang(rs.getString("HoKhachHang"));
                guest.setEmailKhachHang(rs.getString("EmailKhachHang"));
                guest.setSoDienThoaiKhachHang(rs.getString("SoDienThoaiKhachHang"));
                guest.setCmndCccdKhachHang(rs.getString("CMND_CCCDKhachHang"));
                guest.setDiaChi(rs.getString("DiaChi"));
                guest.setTrangThai(rs.getString("TrangThai"));
                return guest;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Guest> getAllGuests() {
        List<Guest> list = new ArrayList<>();
        String sql = "SELECT * FROM hotels_guests ORDER BY MaKhachHang DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Guest guest = new Guest();
                guest.setMaKhachHang(rs.getInt("MaKhachHang"));
                guest.setTenKhachHang(rs.getString("TenKhachHang"));
                guest.setHoKhachHang(rs.getString("HoKhachHang"));
                guest.setEmailKhachHang(rs.getString("EmailKhachHang"));
                guest.setSoDienThoaiKhachHang(rs.getString("SoDienThoaiKhachHang"));
                guest.setCmndCccdKhachHang(rs.getString("CMND_CCCDKhachHang"));
                guest.setDiaChi(rs.getString("DiaChi"));
                guest.setTrangThai(rs.getString("TrangThai"));
                guest.setNgayTao(rs.getTimestamp("NgayTao"));
                list.add(guest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Guest getGuestById(int maKhachHang) {
        String sql = "SELECT * FROM hotels_guests WHERE MaKhachHang=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maKhachHang);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Guest guest = new Guest();
                guest.setMaKhachHang(rs.getInt("MaKhachHang"));
                guest.setTenKhachHang(rs.getString("TenKhachHang"));
                guest.setHoKhachHang(rs.getString("HoKhachHang"));
                guest.setEmailKhachHang(rs.getString("EmailKhachHang"));
                guest.setSoDienThoaiKhachHang(rs.getString("SoDienThoaiKhachHang"));
                guest.setCmndCccdKhachHang(rs.getString("CMND_CCCDKhachHang"));
                guest.setDiaChi(rs.getString("DiaChi"));
                guest.setTrangThai(rs.getString("TrangThai"));
                return guest;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateGuestStatus(int maKhachHang, String trangThai) {
        String sql = "UPDATE hotels_guests SET TrangThai=? WHERE MaKhachHang=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, trangThai);
            pstmt.setInt(2, maKhachHang);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateGuest(Guest guest) {
        // Cập nhật cả HoKhachHang và TenKhachHang
        String sql = "UPDATE hotels_guests SET HoKhachHang=?, TenKhachHang=?, EmailKhachHang=?, " +
                    "SoDienThoaiKhachHang=?, CMND_CCCDKhachHang=?, DiaChi=? " +
                    "WHERE MaKhachHang=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guest.getHoKhachHang());  // Cột Họ
            pstmt.setString(2, guest.getTenKhachHang()); // Cột Tên
            pstmt.setString(3, guest.getEmailKhachHang());
            pstmt.setString(4, guest.getSoDienThoaiKhachHang());
            pstmt.setString(5, guest.getCmndCccdKhachHang());
            pstmt.setString(6, guest.getDiaChi());
            pstmt.setInt(7, guest.getMaKhachHang());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGuest(int maKhachHang) {
        String sql = "DELETE FROM hotels_guests WHERE MaKhachHang=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maKhachHang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lỗi này thường do khách hàng đã có booking nên không xóa được
            System.err.println("Không thể xóa khách hàng ID " + maKhachHang + ": " + e.getMessage());
            return false;
        }
    }
}