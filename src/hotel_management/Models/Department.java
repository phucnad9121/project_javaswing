/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Models;

/**
 *
 * @author phucd
 */
public class Department {
    private int maBoPhan;
    private String tenBoPhan;
    private String moTaBoPhan;
    private int luongKhoiDiem;
    private String chucDanh;

    public Department() {}

    public int getMaBoPhan() { return maBoPhan; }
    public void setMaBoPhan(int maBoPhan) { this.maBoPhan = maBoPhan; }
    
    public String getTenBoPhan() { return tenBoPhan; }
    public void setTenBoPhan(String tenBoPhan) { this.tenBoPhan = tenBoPhan; }
    
    public String getMoTaBoPhan() { return moTaBoPhan; }
    public void setMoTaBoPhan(String moTaBoPhan) { this.moTaBoPhan = moTaBoPhan; }
    
    public int getLuongKhoiDiem() { return luongKhoiDiem; }
    public void setLuongKhoiDiem(int luongKhoiDiem) { this.luongKhoiDiem = luongKhoiDiem; }
    
    public String getChucDanh() { return chucDanh; }
    public void setChucDanh(String chucDanh) { this.chucDanh = chucDanh; }
}

