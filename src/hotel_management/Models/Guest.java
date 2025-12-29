/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
import java.sql.Timestamp;

public class Guest {
    private int maKhachHang;
    private String tenKhachHang;
    private String hoKhachHang;
    private String emailKhachHang;
    private String soDienThoaiKhachHang;
    private String cmndCccdKhachHang;
    private String diaChi;
    private String matKhau;
    private String trangThai;
    private Timestamp ngayTao;

    public Guest() {}

    public int getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }
    
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    
    public String getHoKhachHang() { return hoKhachHang; }
    public void setHoKhachHang(String hoKhachHang) { this.hoKhachHang = hoKhachHang; }
    
    public String getHoTenKhachHang() { return hoKhachHang + " " + tenKhachHang; }
    
    public String getEmailKhachHang() { return emailKhachHang; }
    public void setEmailKhachHang(String emailKhachHang) { this.emailKhachHang = emailKhachHang; }
    
    public String getSoDienThoaiKhachHang() { return soDienThoaiKhachHang; }
    public void setSoDienThoaiKhachHang(String soDienThoaiKhachHang) { this.soDienThoaiKhachHang = soDienThoaiKhachHang; }
    
    public String getCmndCccdKhachHang() { return cmndCccdKhachHang; }
    public void setCmndCccdKhachHang(String cmndCccdKhachHang) { this.cmndCccdKhachHang = cmndCccdKhachHang; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }
}

