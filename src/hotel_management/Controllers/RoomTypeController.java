/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.RoomTypeDAO;
import hotel_management.Models.RoomType;
import java.util.List;

public class RoomTypeController {
    private RoomTypeDAO roomTypeDAO;
    
    public RoomTypeController() {
        this.roomTypeDAO = new RoomTypeDAO();
    }
    
    public List<RoomType> getAllRoomTypes() {
        return roomTypeDAO.getAllRoomTypes();
    }
    
    public boolean addRoomType(String tenLoaiPhong, int giaPhong, String moTaPhong) {
        RoomType roomType = new RoomType();
        roomType.setTenLoaiPhong(tenLoaiPhong);
        roomType.setGiaPhong(giaPhong);
        roomType.setMoTaPhong(moTaPhong);
        return roomTypeDAO.addRoomType(roomType);
    }
    
    public boolean updateRoomType(int maLoaiPhong, String tenLoaiPhong, int giaPhong, String moTaPhong) {
        RoomType roomType = new RoomType();
        roomType.setMaLoaiPhong(maLoaiPhong);
        roomType.setTenLoaiPhong(tenLoaiPhong);
        roomType.setGiaPhong(giaPhong);
        roomType.setMoTaPhong(moTaPhong);
        return roomTypeDAO.updateRoomType(roomType);
    }
    
    public boolean deleteRoomType(int maLoaiPhong) {
        return roomTypeDAO.deleteRoomType(maLoaiPhong);
    }
}