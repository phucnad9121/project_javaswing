/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.GuestDAO;
import hotel_management.Models.Guest;
import java.util.List;

public class GuestController {
    private GuestDAO guestDAO;
    
    public GuestController() {
        this.guestDAO = new GuestDAO();
    }
    
    public Guest register(String tenKhachHang, String hoKhachHang, String emailKhachHang,
                         String soDienThoai, String cmndCccd, String diaChi, String matKhau) {
        Guest guest = new Guest();
        guest.setTenKhachHang(tenKhachHang);
        guest.setHoKhachHang(hoKhachHang);
        guest.setEmailKhachHang(emailKhachHang);
        guest.setSoDienThoaiKhachHang(soDienThoai);
        guest.setCmndCccdKhachHang(cmndCccd);
        guest.setDiaChi(diaChi);
        guest.setMatKhau(matKhau);
        return guestDAO.register(guest);
    }
    
    public Guest login(String soDienThoai, String matKhau) {
        return guestDAO.login(soDienThoai, matKhau);
    }
    
    public List<Guest> getAllGuests() {
        return guestDAO.getAllGuests();
    }
    
    public Guest getGuestById(int maKhachHang) {
        return guestDAO.getGuestById(maKhachHang);
    }
}