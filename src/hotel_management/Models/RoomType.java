/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class RoomType {
    private int maLoaiPhong;
    private String tenLoaiPhong;
    private int giaPhong;
    private String moTaPhong;

    public RoomType() {}

    public int getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(int maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    
    public String getTenLoaiPhong() { return tenLoaiPhong; }
    public void setTenLoaiPhong(String tenLoaiPhong) { this.tenLoaiPhong = tenLoaiPhong; }
    
    public int getGiaPhong() { return giaPhong; }
    public void setGiaPhong(int giaPhong) { this.giaPhong = giaPhong; }
    
    public String getMoTaPhong() { return moTaPhong; }
    public void setMoTaPhong(String moTaPhong) { this.moTaPhong = moTaPhong; }
}