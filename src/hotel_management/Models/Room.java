/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class Room {
    private int maPhong;
    private String soPhong;
    private int maLoaiPhong;
    private String tenLoaiPhong;
    private String khaDung;

    public Room() {}

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
    
    public int getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(int maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    
    public String getTenLoaiPhong() { return tenLoaiPhong; }
    public void setTenLoaiPhong(String tenLoaiPhong) { this.tenLoaiPhong = tenLoaiPhong; }
    
    public String getKhaDung() { return khaDung; }
    public void setKhaDung(String khaDung) { this.khaDung = khaDung; }
}