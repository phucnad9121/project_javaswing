/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.ServiceUsedDAO;
import hotel_management.Models.ServiceUsed;
import java.util.List;

public class ServiceUsedController {
    private ServiceUsedDAO serviceUsedDAO;
    
    public ServiceUsedController() {
        this.serviceUsedDAO = new ServiceUsedDAO();
    }
    
    public boolean addServiceToBooking(int maDatPhong, int maDichVu, int soLuong, int donGia) {
        return serviceUsedDAO.addServiceToBooking(maDatPhong, maDichVu, soLuong, donGia);
    }
    
    public List<ServiceUsed> getServicesByBookingId(int maDatPhong) {
        return serviceUsedDAO.getServicesByBookingId(maDatPhong);
    }
    
    public int getTotalServiceCharge(int maDatPhong) {
        return serviceUsedDAO.getTotalServiceCharge(maDatPhong);
    }
    
    public boolean deleteServiceFromBooking(int maDichVuSuDung) {
        return serviceUsedDAO.deleteServiceFromBooking(maDichVuSuDung);
    }
    
    public boolean updateServiceQuantity(int maDichVuSuDung, int newQuantity) {
        return serviceUsedDAO.updateServiceQuantity(maDichVuSuDung, newQuantity);
    }
}
