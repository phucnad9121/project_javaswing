/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.DepartmentDAO;
import hotel_management.Models.Department;
import java.util.List;

public class DepartmentController {
    private DepartmentDAO departmentDAO;
    
    public DepartmentController() {
        this.departmentDAO = new DepartmentDAO();
    }
    
    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }
    
    public boolean addDepartment(String tenBoPhan, String moTaBoPhan, int luongKhoiDiem, String chucDanh) {
        Department dept = new Department();
        dept.setTenBoPhan(tenBoPhan);
        dept.setMoTaBoPhan(moTaBoPhan);
        dept.setLuongKhoiDiem(luongKhoiDiem);
        dept.setChucDanh(chucDanh);
        return departmentDAO.addDepartment(dept);
    }
    
    public boolean updateDepartment(int maBoPhan, String tenBoPhan, String moTaBoPhan, 
                                   int luongKhoiDiem, String chucDanh) {
        Department dept = new Department();
        dept.setMaBoPhan(maBoPhan);
        dept.setTenBoPhan(tenBoPhan);
        dept.setMoTaBoPhan(moTaBoPhan);
        dept.setLuongKhoiDiem(luongKhoiDiem);
        dept.setChucDanh(chucDanh);
        return departmentDAO.updateDepartment(dept);
    }
    
    public boolean deleteDepartment(int maBoPhan) {
        return departmentDAO.deleteDepartment(maBoPhan);
    }
}
