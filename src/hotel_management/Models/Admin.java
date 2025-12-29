/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class Admin {
    private int maAdmin;
    private String tenDangNhap;
    private String matKhau;

    public Admin() {}

    public Admin(int maAdmin, String tenDangNhap, String matKhau) {
        this.maAdmin = maAdmin;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public int getMaAdmin() { return maAdmin; }
    public void setMaAdmin(int maAdmin) { this.maAdmin = maAdmin; }
    
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
}