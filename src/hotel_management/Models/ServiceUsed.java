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

public class ServiceUsed {
    private int maDichVuSuDung;
    private int maDichVu;
    private String tenDichVu;
    private int maDatPhong;
    private int soLuong;
    private int donGia;
    private int thanhTien;
    private Timestamp ngaySuDung;

    public ServiceUsed() {}

    public int getMaDichVuSuDung() { return maDichVuSuDung; }
    public void setMaDichVuSuDung(int maDichVuSuDung) { this.maDichVuSuDung = maDichVuSuDung; }
    
    public int getMaDichVu() { return maDichVu; }
    public void setMaDichVu(int maDichVu) { this.maDichVu = maDichVu; }
    
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    
    public int getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(int maDatPhong) { this.maDatPhong = maDatPhong; }
    
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    
    public int getDonGia() { return donGia; }
    public void setDonGia(int donGia) { this.donGia = donGia; }
    
    public int getThanhTien() { return thanhTien; }
    public void setThanhTien(int thanhTien) { this.thanhTien = thanhTien; }
    
    public Timestamp getNgaySuDung() { return ngaySuDung; }
    public void setNgaySuDung(Timestamp ngaySuDung) { this.ngaySuDung = ngaySuDung; }
}