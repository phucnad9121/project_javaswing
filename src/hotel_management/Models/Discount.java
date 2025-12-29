/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class Discount {
    private int maGiamGia;
    private String tenGiamGia;
    private String moTaGiamGia;
    private int tyLeGiamGia;
    private int maNhanVien;
    private String tenNhanVien;

    public Discount() {}

    public int getMaGiamGia() { return maGiamGia; }
    public void setMaGiamGia(int maGiamGia) { this.maGiamGia = maGiamGia; }
    
    public String getTenGiamGia() { return tenGiamGia; }
    public void setTenGiamGia(String tenGiamGia) { this.tenGiamGia = tenGiamGia; }
    
    public String getMoTaGiamGia() { return moTaGiamGia; }
    public void setMoTaGiamGia(String moTaGiamGia) { this.moTaGiamGia = moTaGiamGia; }
    
    public int getTyLeGiamGia() { return tyLeGiamGia; }
    public void setTyLeGiamGia(int tyLeGiamGia) { this.tyLeGiamGia = tyLeGiamGia; }
    
    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }
    
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
}