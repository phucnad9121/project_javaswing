/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings_booking (NgayDatPhong, ThoiGianLuuTru, NgayNhanPhong, NgayTraPhong, " +
                    "SoTienDatPhong, MaKhachHang, TrangThai, GhiChu) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setDate(1, booking.getNgayDatPhong());
            pstmt.setInt(2, booking.getThoiGianLuuTru());
            pstmt.setDate(3, booking.getNgayNhanPhong());
            pstmt.setDate(4, booking.getNgayTraPhong());
            pstmt.setInt(5, booking.getSoTienDatPhong());
            pstmt.setInt(6, booking.getMaKhachHang());
            pstmt.setString(7, booking.getTrangThai());
            
            // Lưu loại phòng vào GhiChu theo format: "ROOMTYPE:ID|note"
            String noteWithRoomType = "ROOMTYPE:" + booking.getMaLoaiPhong();
            if (booking.getGhiChu() != null && !booking.getGhiChu().isEmpty()) {
                noteWithRoomType += "|" + booking.getGhiChu();
            }
            pstmt.setString(8, noteWithRoomType);
            
            if (pstmt.executeUpdate() > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    booking.setMaDatPhong(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, " +
                    "CONCAT(g.HoKhachHang, ' ', g.TenKhachHang) as TenKhachHang, " +
                    "CONCAT(e.HoNhanVien, ' ', e.TenNhanVien) as TenNhanVien, " +
                    "d.TenGiamGia " +
                    "FROM bookings_booking b " +
                    "JOIN hotels_guests g ON b.MaKhachHang = g.MaKhachHang " +
                    "LEFT JOIN hotels_employees e ON b.MaNhanVien = e.MaNhanVien " +
                    "LEFT JOIN bookings_discount d ON b.MaGiamGia = d.MaGiamGia " +
                    "ORDER BY b.NgayTao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setMaDatPhong(rs.getInt("MaDatPhong"));
                booking.setNgayDatPhong(rs.getDate("NgayDatPhong"));
                booking.setThoiGianLuuTru(rs.getInt("ThoiGianLuuTru"));
                booking.setNgayNhanPhong(rs.getDate("NgayNhanPhong"));
                booking.setNgayTraPhong(rs.getDate("NgayTraPhong"));
                booking.setSoTienDatPhong(rs.getInt("SoTienDatPhong"));
                booking.setMaNhanVien(rs.getObject("MaNhanVien", Integer.class));
                booking.setTenNhanVien(rs.getString("TenNhanVien"));
                booking.setMaKhachHang(rs.getInt("MaKhachHang"));
                booking.setTenKhachHang(rs.getString("TenKhachHang"));
                booking.setMaGiamGia(rs.getObject("MaGiamGia", Integer.class));
                booking.setTenGiamGia(rs.getString("TenGiamGia"));
                booking.setTrangThai(rs.getString("TrangThai"));
                
                // Parse loại phòng từ GhiChu
                String ghiChu = rs.getString("GhiChu");
                if (ghiChu != null && ghiChu.startsWith("ROOMTYPE:")) {
                    String[] parts = ghiChu.split("\\|", 2);
                    String roomTypePart = parts[0].replace("ROOMTYPE:", "");
                    try {
                        booking.setMaLoaiPhong(Integer.parseInt(roomTypePart));
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                    if (parts.length > 1) {
                        booking.setGhiChu(parts[1]);
                    } else {
                        booking.setGhiChu("");
                    }
                } else {
                    booking.setGhiChu(ghiChu);
                }
                
                booking.setNgayTao(rs.getTimestamp("NgayTao"));
                booking.setNgayCapNhat(rs.getTimestamp("NgayCapNhat"));
                list.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Booking getBookingById(int maDatPhong) {
        String sql = "SELECT b.*, " +
                    "CONCAT(g.HoKhachHang, ' ', g.TenKhachHang) as TenKhachHang, " +
                    "CONCAT(e.HoNhanVien, ' ', e.TenNhanVien) as TenNhanVien " +
                    "FROM bookings_booking b " +
                    "JOIN hotels_guests g ON b.MaKhachHang = g.MaKhachHang " +
                    "LEFT JOIN hotels_employees e ON b.MaNhanVien = e.MaNhanVien " +
                    "WHERE b.MaDatPhong = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, maDatPhong);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setMaDatPhong(rs.getInt("MaDatPhong"));
                booking.setNgayDatPhong(rs.getDate("NgayDatPhong"));
                booking.setThoiGianLuuTru(rs.getInt("ThoiGianLuuTru"));
                booking.setNgayNhanPhong(rs.getDate("NgayNhanPhong"));
                booking.setNgayTraPhong(rs.getDate("NgayTraPhong"));
                booking.setSoTienDatPhong(rs.getInt("SoTienDatPhong"));
                booking.setMaNhanVien(rs.getObject("MaNhanVien", Integer.class));
                booking.setTenNhanVien(rs.getString("TenNhanVien"));
                booking.setMaKhachHang(rs.getInt("MaKhachHang"));
                booking.setTenKhachHang(rs.getString("TenKhachHang"));
                booking.setMaGiamGia(rs.getObject("MaGiamGia", Integer.class));
                booking.setTrangThai(rs.getString("TrangThai"));
                
                // Parse loại phòng từ GhiChu
                String ghiChu = rs.getString("GhiChu");
                if (ghiChu != null && ghiChu.startsWith("ROOMTYPE:")) {
                    String[] parts = ghiChu.split("\\|", 2);
                    String roomTypePart = parts[0].replace("ROOMTYPE:", "");
                    try {
                        booking.setMaLoaiPhong(Integer.parseInt(roomTypePart));
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                    if (parts.length > 1) {
                        booking.setGhiChu(parts[1]);
                    }
                } else {
                    booking.setGhiChu(ghiChu);
                }
                
                return booking;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean confirmBooking(int maDatPhong, int maNhanVien) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Cập nhật trạng thái booking
            String sqlBooking = "UPDATE bookings_booking SET TrangThai='Confirmed', MaNhanVien=? WHERE MaDatPhong=?";
            PreparedStatement pstmt1 = conn.prepareStatement(sqlBooking);
            pstmt1.setInt(1, maNhanVien);
            pstmt1.setInt(2, maDatPhong);
            pstmt1.executeUpdate();

            // 2. ✅ CẬP NHẬT TRẠNG THÁI PHÒNG NGAY KHI XÁC NHẬN
            // Phòng đã được gán cho khách, không thể gán cho người khác
            String sqlRoom = "UPDATE rooms_room SET KhaDung='No' WHERE MaPhong IN " +
                           "(SELECT MaPhong FROM rooms_roombooked WHERE MaDatPhong=?)";
            PreparedStatement pstmt2 = conn.prepareStatement(sqlRoom);
            pstmt2.setInt(1, maDatPhong);
            pstmt2.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {}
            }
        }
    }
    
    public boolean checkIn(int maDatPhong) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Cập nhật trạng thái booking
            String sqlBooking = "UPDATE bookings_booking SET TrangThai='Checkin' WHERE MaDatPhong=?";
            PreparedStatement pstmt1 = conn.prepareStatement(sqlBooking);
            pstmt1.setInt(1, maDatPhong);
            pstmt1.executeUpdate();

            // 2. Cập nhật trạng thái khách hàng
            String sqlGuest = "UPDATE hotels_guests SET TrangThai='Reserved' WHERE MaKhachHang = " +
                            "(SELECT MaKhachHang FROM bookings_booking WHERE MaDatPhong=?)";
            PreparedStatement pstmt2 = conn.prepareStatement(sqlGuest);
            pstmt2.setInt(1, maDatPhong);
            pstmt2.executeUpdate();

            // ✅ KHÔNG CẬP NHẬT PHÒNG NỮA (đã cập nhật từ lúc confirm)
            // Phòng đã bị khóa từ khi nhân viên xác nhận và gán phòng

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {}
            }
        }
    }
    
    public boolean cancelBooking(int maDatPhong) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Kiểm tra trạng thái booking trước khi hủy
            String sqlCheck = "SELECT TrangThai FROM bookings_booking WHERE MaDatPhong=?";
            PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
            pstmtCheck.setInt(1, maDatPhong);
            ResultSet rs = pstmtCheck.executeQuery();

            String trangThai = "";
            if (rs.next()) {
                trangThai = rs.getString("TrangThai");
            }

            // 2. Cập nhật trạng thái booking sang Cancelled
            String sqlBooking = "UPDATE bookings_booking SET TrangThai='Cancelled' WHERE MaDatPhong=?";
            PreparedStatement pstmt1 = conn.prepareStatement(sqlBooking);
            pstmt1.setInt(1, maDatPhong);
            pstmt1.executeUpdate();

            // 3. ✅ NẾU BOOKING ĐÃ CONFIRMED, TRẢ PHÒNG VỀ AVAILABLE
            // Chỉ trả phòng nếu booking đã được xác nhận và gán phòng
            if ("Confirmed".equals(trangThai)) {
                String sqlRoom = "UPDATE rooms_room SET KhaDung='Yes' WHERE MaPhong IN " +
                               "(SELECT MaPhong FROM rooms_roombooked WHERE MaDatPhong=?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sqlRoom);
                pstmt2.setInt(1, maDatPhong);
                pstmt2.executeUpdate();
            }

            // 4. ✅ NẾU BOOKING ĐÃ CHECK-IN, CŨNG PHẢI TRẢ PHÒNG
            // (Trường hợp khách hủy sau khi đã check-in)
            if ("Checkin".equals(trangThai)) {
                String sqlRoom = "UPDATE rooms_room SET KhaDung='Yes' WHERE MaPhong IN " +
                               "(SELECT MaPhong FROM rooms_roombooked WHERE MaDatPhong=?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sqlRoom);
                pstmt2.setInt(1, maDatPhong);
                pstmt2.executeUpdate();

                // Cập nhật lại trạng thái khách hàng
                String sqlGuest = "UPDATE hotels_guests SET TrangThai='Not Reserved' WHERE MaKhachHang = " +
                                "(SELECT MaKhachHang FROM bookings_booking WHERE MaDatPhong=?)";
                PreparedStatement pstmt3 = conn.prepareStatement(sqlGuest);
                pstmt3.setInt(1, maDatPhong);
                pstmt3.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {}
            }
        }
    }
    
    public boolean deleteBooking(int maDatPhong) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Xóa các dịch vụ đã sử dụng
            String sqlServices = "DELETE FROM hotelservice_servicesused WHERE MaDatPhong=?";
            PreparedStatement pstmt1 = conn.prepareStatement(sqlServices);
            pstmt1.setInt(1, maDatPhong);
            pstmt1.executeUpdate();

            // Xóa các phòng đã đặt
            String sqlRooms = "DELETE FROM rooms_roombooked WHERE MaDatPhong=?";
            PreparedStatement pstmt2 = conn.prepareStatement(sqlRooms);
            pstmt2.setInt(1, maDatPhong);
            pstmt2.executeUpdate();

            // Xóa booking
            String sqlBooking = "DELETE FROM bookings_booking WHERE MaDatPhong=?";
            PreparedStatement pstmt3 = conn.prepareStatement(sqlBooking);
            pstmt3.setInt(1, maDatPhong);
            pstmt3.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {}
            }
        }
    }
    
    public boolean checkout(int maDatPhong, int soTienThanhToan) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Cập nhật trạng thái booking và tổng tiền
            String sqlBooking = "UPDATE bookings_booking SET TrangThai='Checkout', SoTienDatPhong=? WHERE MaDatPhong=?";
            PreparedStatement pstmt1 = conn.prepareStatement(sqlBooking);
            pstmt1.setInt(1, soTienThanhToan);
            pstmt1.setInt(2, maDatPhong);
            pstmt1.executeUpdate();
            
            // Cập nhật trạng thái phòng về Available
            String sqlRoom = "UPDATE rooms_room SET KhaDung='Yes' WHERE MaPhong IN " +
                           "(SELECT MaPhong FROM rooms_roombooked WHERE MaDatPhong=?)";
            PreparedStatement pstmt2 = conn.prepareStatement(sqlRoom);
            pstmt2.setInt(1, maDatPhong);
            pstmt2.executeUpdate();
            
            // Cập nhật trạng thái khách hàng
            String sqlGuest = "UPDATE hotels_guests SET TrangThai='Not Reserved' WHERE MaKhachHang = " +
                            "(SELECT MaKhachHang FROM bookings_booking WHERE MaDatPhong=?)";
            PreparedStatement pstmt3 = conn.prepareStatement(sqlGuest);
            pstmt3.setInt(1, maDatPhong);
            pstmt3.executeUpdate();
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {}
            }
        }
    }
    
    
}
