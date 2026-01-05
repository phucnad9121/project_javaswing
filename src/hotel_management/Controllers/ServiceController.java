package hotel_management.Controllers;

import hotel_management.DAO.ServiceDAO;
import hotel_management.DAO.ServiceUsedDAO;
import hotel_management.Models.Service;
import java.util.List;

public class ServiceController {
    private ServiceDAO serviceDAO;
    private ServiceUsedDAO serviceUsedDAO;
    
    public ServiceController() {
        this.serviceDAO = new ServiceDAO();
        this.serviceUsedDAO = new ServiceUsedDAO();
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
    
    public Service getServiceById(int maDichVu) {
        return serviceDAO.getServiceById(maDichVu);
    }
    
    // Phương thức đặt một dịch vụ
    public boolean orderService(int maDatPhong, int maDichVu, int soLuong) {

        Service service = serviceDAO.getServiceById(maDichVu);
        if (service == null) {
             return false;
        }
                
        boolean result = serviceUsedDAO.addServiceToBooking(
            maDatPhong, 
            maDichVu, 
            soLuong, 
            service.getChiPhiDichVu()
        );
        
        return result;
    }
    
    // Phương thức đặt nhiều dịch vụ cùng lúc
    public boolean orderMultipleServices(int maDatPhong, List<Integer> serviceIds) {
               if (maDatPhong <= 0) {
            return false;
        }
        
        if (serviceIds == null || serviceIds.isEmpty()) {
            return false;
        }
        
        boolean allSuccess = true;
        int successCount = 0;
        
        for (Integer maDichVu : serviceIds) {
            if (orderService(maDatPhong, maDichVu, 1)) {
                successCount++;
            } else {
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
}