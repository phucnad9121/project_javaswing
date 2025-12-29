/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.EmployeeDAO;
import hotel_management.Models.Employee;
import java.sql.Date;
import java.util.List;

public class EmployeeController {
    private EmployeeDAO employeeDAO;
    
    public EmployeeController() {
        this.employeeDAO = new EmployeeDAO();
    }
    
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    public boolean addEmployee(String tenNhanVien, String hoNhanVien, String chucDanhNV,
                              String soDienThoaiNV, String emailNhanVien, Date ngayVaoLam,
                              String diaChi, int maBoPhan, String cmndCccd) {
        Employee emp = new Employee();
        emp.setTenNhanVien(tenNhanVien);
        emp.setHoNhanVien(hoNhanVien);
        emp.setChucDanhNV(chucDanhNV);
        emp.setSoDienThoaiNV(soDienThoaiNV);
        emp.setEmailNhanVien(emailNhanVien);
        emp.setNgayVaoLam(ngayVaoLam);
        emp.setDiaChi(diaChi);
        emp.setMaBoPhan(maBoPhan);
        emp.setCmndCccd(cmndCccd);
        return employeeDAO.addEmployee(emp);
    }
    
    public boolean updateEmployee(int maNhanVien, String tenNhanVien, String hoNhanVien,
                                 String chucDanhNV, String soDienThoaiNV, String emailNhanVien,
                                 Date ngayVaoLam, String diaChi, int maBoPhan, String cmndCccd) {
        Employee emp = new Employee();
        emp.setMaNhanVien(maNhanVien);
        emp.setTenNhanVien(tenNhanVien);
        emp.setHoNhanVien(hoNhanVien);
        emp.setChucDanhNV(chucDanhNV);
        emp.setSoDienThoaiNV(soDienThoaiNV);
        emp.setEmailNhanVien(emailNhanVien);
        emp.setNgayVaoLam(ngayVaoLam);
        emp.setDiaChi(diaChi);
        emp.setMaBoPhan(maBoPhan);
        emp.setCmndCccd(cmndCccd);
        return employeeDAO.updateEmployee(emp);
    }
    
    public boolean deleteEmployee(int maNhanVien) {
        return employeeDAO.deleteEmployee(maNhanVien);
    }
}
