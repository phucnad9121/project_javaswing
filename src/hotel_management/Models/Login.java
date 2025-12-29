/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class Login {
    private int maDangNhap;
    private String tenDangNhap;
    private String matKhau;
    private int maNhanVien;
    private String nguoiDungMoi;
    private Employee employee;

    public Login() {}

    public int getMaDangNhap() { return maDangNhap; }
    public void setMaDangNhap(int maDangNhap) { this.maDangNhap = maDangNhap; }
    
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    
    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }
    
    public String getNguoiDungMoi() { return nguoiDungMoi; }
    public void setNguoiDungMoi(String nguoiDungMoi) { this.nguoiDungMoi = nguoiDungMoi; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}
