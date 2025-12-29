/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.RoomBookedDAO;
import hotel_management.Models.RoomBooked;
import java.util.List;

public class RoomBookedController {
    private RoomBookedDAO roomBookedDAO;
    
    public RoomBookedController() {
        this.roomBookedDAO = new RoomBookedDAO();
    }
    
    public boolean addRoomBooked(int maDatPhong, int maPhong) {
        return roomBookedDAO.addRoomBooked(maDatPhong, maPhong);
    }
    
    public List<RoomBooked> getRoomsByBookingId(int maDatPhong) {
        return roomBookedDAO.getRoomsByBookingId(maDatPhong);
    }
    
    public boolean deleteRoomBooked(int maPhongDaDat) {
        return roomBookedDAO.deleteRoomBooked(maPhongDaDat);
    }
}
