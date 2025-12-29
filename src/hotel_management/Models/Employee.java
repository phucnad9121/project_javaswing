/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
import java.sql.Date;

public class Employee {
    private int maNhanVien;
    private String tenNhanVien;
    private String hoNhanVien;
    private String chucDanhNV;
    private String soDienThoaiNV;
    private String emailNhanVien;
    private Date ngayVaoLam;
    private String diaChi;
    private int maBoPhan;
    private String tenBoPhan;
    private String cmndCccd;

    public Employee() {}

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }
    
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
    
    public String getHoNhanVien() { return hoNhanVien; }
    public void setHoNhanVien(String hoNhanVien) { this.hoNhanVien = hoNhanVien; }
    
    public String getHoTenNhanVien() { return hoNhanVien + " " + tenNhanVien; }
    
    public String getChucDanhNV() { return chucDanhNV; }
    public void setChucDanhNV(String chucDanhNV) { this.chucDanhNV = chucDanhNV; }
    
    public String getSoDienThoaiNV() { return soDienThoaiNV; }
    public void setSoDienThoaiNV(String soDienThoaiNV) { this.soDienThoaiNV = soDienThoaiNV; }
    
    public String getEmailNhanVien() { return emailNhanVien; }
    public void setEmailNhanVien(String emailNhanVien) { this.emailNhanVien = emailNhanVien; }
    
    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public int getMaBoPhan() { return maBoPhan; }
    public void setMaBoPhan(int maBoPhan) { this.maBoPhan = maBoPhan; }
    
    public String getTenBoPhan() { return tenBoPhan; }
    public void setTenBoPhan(String tenBoPhan) { this.tenBoPhan = tenBoPhan; }
    
    public String getCmndCccd() { return cmndCccd; }
    public void setCmndCccd(String cmndCccd) { this.cmndCccd = cmndCccd; }
}
