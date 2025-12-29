/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.AdminDAO;
import hotel_management.Models.Admin;

public class AdminController {
    private AdminDAO adminDAO;
    
    public AdminController() {
        this.adminDAO = new AdminDAO();
    }
    
    public Admin adminLogin(String tenDangNhap, String matKhau) {
        return adminDAO.adminLogin(tenDangNhap, matKhau);
    }
}