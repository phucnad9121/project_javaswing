/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.ServiceDAO;
import hotel_management.Models.Service;
import java.util.List;

public class ServiceController {
    private ServiceDAO serviceDAO;
    
    public ServiceController() {
        this.serviceDAO = new ServiceDAO();
    }
    
    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }
    
    public boolean addService(String tenDichVu, String moTaDichVu, int chiPhiDichVu) {
        Service service = new Service();
        service.setTenDichVu(tenDichVu);
        service.setMoTaDichVu(moTaDichVu);
        service.setChiPhiDichVu(chiPhiDichVu);
        return serviceDAO.addService(service);
    }
    
    public boolean updateService(int maDichVu, String tenDichVu, String moTaDichVu, int chiPhiDichVu) {
        Service service = new Service();
        service.setMaDichVu(maDichVu);
        service.setTenDichVu(tenDichVu);
        service.setMoTaDichVu(moTaDichVu);
        service.setChiPhiDichVu(chiPhiDichVu);
        return serviceDAO.updateService(service);
    }
    
    public boolean deleteService(int maDichVu) {
        return serviceDAO.deleteService(maDichVu);
    }
}
