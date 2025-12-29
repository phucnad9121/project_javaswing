/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.RoomDAO;
import hotel_management.Models.Room;
import java.util.List;

public class RoomController {
    private RoomDAO roomDAO;
    
    public RoomController() {
        this.roomDAO = new RoomDAO();
    }
    
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }
    
    public List<Room> getAvailableRoomsByType(int maLoaiPhong) {
        return roomDAO.getAvailableRoomsByType(maLoaiPhong);
    }
    
    public int countAvailableRoomsByType(int maLoaiPhong) {
        return roomDAO.countAvailableRoomsByType(maLoaiPhong);
    }
    
    public boolean addRoom(String soPhong, int maLoaiPhong, String khaDung) {
        Room room = new Room();
        room.setSoPhong(soPhong);
        room.setMaLoaiPhong(maLoaiPhong);
        room.setKhaDung(khaDung);
        return roomDAO.addRoom(room);
    }
    
    public boolean updateRoom(int maPhong, String soPhong, int maLoaiPhong, String khaDung) {
        Room room = new Room();
        room.setMaPhong(maPhong);
        room.setSoPhong(soPhong);
        room.setMaLoaiPhong(maLoaiPhong);
        room.setKhaDung(khaDung);
        return roomDAO.updateRoom(room);
    }
    
    public boolean deleteRoom(int maPhong) {
        return roomDAO.deleteRoom(maPhong);
    }
}