/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.LoginDAO;
import hotel_management.Models.Login;
import java.util.List;

public class LoginController {
    private LoginDAO loginDAO;
    
    public LoginController() {
        this.loginDAO = new LoginDAO();
    }
    
    public Login employeeLogin(String tenDangNhap, String matKhau) {
        return loginDAO.employeeLogin(tenDangNhap, matKhau);
    }
    
    public List<Login> getAllLogins() {
        return loginDAO.getAllLogins();
    }
    
    public boolean addLogin(String tenDangNhap, String matKhau, int maNhanVien) {
        Login login = new Login();
        login.setTenDangNhap(tenDangNhap);
        login.setMatKhau(matKhau);
        login.setMaNhanVien(maNhanVien);
        login.setNguoiDungMoi("Yes");
        return loginDAO.addLogin(login);
    }
    
    public boolean updatePassword(int maDangNhap, String matKhauMoi) {
        return loginDAO.updatePassword(maDangNhap, matKhauMoi);
    }
    
    public boolean deleteLogin(int maDangNhap) {
        return loginDAO.deleteLogin(maDangNhap);
    }
}