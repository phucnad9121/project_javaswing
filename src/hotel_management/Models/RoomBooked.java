/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class RoomBooked {
    private int maPhongDaDat;
    private int maDatPhong;
    private int maPhong;
    private String soPhong;

    public RoomBooked() {}

    public int getMaPhongDaDat() { return maPhongDaDat; }
    public void setMaPhongDaDat(int maPhongDaDat) { this.maPhongDaDat = maPhongDaDat; }
    
    public int getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(int maDatPhong) { this.maDatPhong = maDatPhong; }
    
    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
}
