/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Controllers;

/**
 *
 * @author phucd
 */
import hotel_management.DAO.DiscountDAO;
import hotel_management.Models.Discount;
import java.util.List;

public class DiscountController {
    private DiscountDAO discountDAO;
    
    public DiscountController() {
        this.discountDAO = new DiscountDAO();
    }
    
    public List<Discount> getAllDiscounts() {
        return discountDAO.getAllDiscounts();
    }
    
    public boolean addDiscount(String tenGiamGia, String moTaGiamGia, int tyLeGiamGia, int maNhanVien) {
        Discount discount = new Discount();
        discount.setTenGiamGia(tenGiamGia);
        discount.setMoTaGiamGia(moTaGiamGia);
        discount.setTyLeGiamGia(tyLeGiamGia);
        discount.setMaNhanVien(maNhanVien);
        return discountDAO.addDiscount(discount);
    }
    
    public boolean updateDiscount(int maGiamGia, String tenGiamGia, String moTaGiamGia, 
                                 int tyLeGiamGia, int maNhanVien) {
        Discount discount = new Discount();
        discount.setMaGiamGia(maGiamGia);
        discount.setTenGiamGia(tenGiamGia);
        discount.setMoTaGiamGia(moTaGiamGia);
        discount.setTyLeGiamGia(tyLeGiamGia);
        discount.setMaNhanVien(maNhanVien);
        return discountDAO.updateDiscount(discount);
    }
    
    public boolean deleteDiscount(int maGiamGia) {
        return discountDAO.deleteDiscount(maGiamGia);
    }
}
