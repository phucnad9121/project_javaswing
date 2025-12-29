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
import java.sql.Timestamp;

public class Booking {
    private int maDatPhong;
    private int maLoaiPhong;
    private Date ngayDatPhong;
    private int thoiGianLuuTru;
    private Date ngayNhanPhong;
    private Date ngayTraPhong;
    private int soTienDatPhong;
    private Integer maNhanVien;
    private String tenNhanVien;
    private int maKhachHang;
    private String tenKhachHang;
    private Integer maGiamGia;
    private String tenGiamGia;
    private String trangThai;
    private String ghiChu;
    private Timestamp ngayTao;
    private Timestamp ngayCapNhat;

    public Booking() {}

    public int getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(int maDatPhong) { this.maDatPhong = maDatPhong; }
    
    public int getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(int maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    
    public Date getNgayDatPhong() { return ngayDatPhong; }
    public void setNgayDatPhong(Date ngayDatPhong) { this.ngayDatPhong = ngayDatPhong; }
    
    public int getThoiGianLuuTru() { return thoiGianLuuTru; }
    public void setThoiGianLuuTru(int thoiGianLuuTru) { this.thoiGianLuuTru = thoiGianLuuTru; }
    
    public Date getNgayNhanPhong() { return ngayNhanPhong; }
    public void setNgayNhanPhong(Date ngayNhanPhong) { this.ngayNhanPhong = ngayNhanPhong; }
    
    public Date getNgayTraPhong() { return ngayTraPhong; }
    public void setNgayTraPhong(Date ngayTraPhong) { this.ngayTraPhong = ngayTraPhong; }
    
    public int getSoTienDatPhong() { return soTienDatPhong; }
    public void setSoTienDatPhong(int soTienDatPhong) { this.soTienDatPhong = soTienDatPhong; }
    
    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }
    
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
    
    public int getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }
    
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    
    public Integer getMaGiamGia() { return maGiamGia; }
    public void setMaGiamGia(Integer maGiamGia) { this.maGiamGia = maGiamGia; }
    
    public String getTenGiamGia() { return tenGiamGia; }
    public void setTenGiamGia(String tenGiamGia) { this.tenGiamGia = tenGiamGia; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }
    
    public Timestamp getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(Timestamp ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
}
