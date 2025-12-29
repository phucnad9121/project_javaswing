/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class Service {
    private int maDichVu;
    private String tenDichVu;
    private String moTaDichVu;
    private int chiPhiDichVu;

    public Service() {}

    public int getMaDichVu() { return maDichVu; }
    public void setMaDichVu(int maDichVu) { this.maDichVu = maDichVu; }
    
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    
    public String getMoTaDichVu() { return moTaDichVu; }
    public void setMoTaDichVu(String moTaDichVu) { this.moTaDichVu = moTaDichVu; }
    
    public int getChiPhiDichVu() { return chiPhiDichVu; }
    public void setChiPhiDichVu(int chiPhiDichVu) { this.chiPhiDichVu = chiPhiDichVu; }
}