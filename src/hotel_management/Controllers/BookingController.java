/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.BookingDAO;
import hotel_management.Models.Booking;
import java.sql.Date;
import java.util.List;

public class BookingController {
    private BookingDAO bookingDAO;
    
    public BookingController() {
        this.bookingDAO = new BookingDAO();
    }
    
    public boolean createBooking(Date ngayDatPhong, int thoiGianLuuTru, Date ngayNhanPhong,
                                Date ngayTraPhong, int soTienDatPhong, int maKhachHang, 
                                int maLoaiPhong, String ghiChu) {
        Booking booking = new Booking();
        booking.setNgayDatPhong(ngayDatPhong);
        booking.setThoiGianLuuTru(thoiGianLuuTru);
        booking.setNgayNhanPhong(ngayNhanPhong);
        booking.setNgayTraPhong(ngayTraPhong);
        booking.setSoTienDatPhong(soTienDatPhong);
        booking.setMaKhachHang(maKhachHang);
        booking.setMaLoaiPhong(maLoaiPhong);
        booking.setTrangThai("Pending");
        booking.setGhiChu(ghiChu);
        return bookingDAO.createBooking(booking);
    }
    
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
    
    public Booking getBookingById(int maDatPhong) {
        return bookingDAO.getBookingById(maDatPhong);
    }
    
    public boolean confirmBooking(int maDatPhong, int maNhanVien) {
        return bookingDAO.confirmBooking(maDatPhong, maNhanVien);
    }
    
    public boolean checkIn(int maDatPhong) {
        return bookingDAO.checkIn(maDatPhong);
    }
    
    public boolean cancelBooking(int maDatPhong) {
        return bookingDAO.cancelBooking(maDatPhong);
    }
    

    
    public boolean checkout(int maDatPhong, int soTienThanhToan) {
        return bookingDAO.checkout(maDatPhong, soTienThanhToan);
    }
}