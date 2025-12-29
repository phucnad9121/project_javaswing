/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.DiscountController;
import hotel_management.Models.Discount;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DiscountView extends JFrame {
    private DiscountController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenGiamGia, txtMoTa, txtTyLe;
    private int currentEmployeeId;
    
    public DiscountView(int employeeId) {
        this.currentEmployeeId = employeeId;
        controller = new DiscountController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Giảm giá");
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Giảm giá"));
        
        inputPanel.add(new JLabel("Tên giảm giá:"));
        txtTenGiamGia = new JTextField();
        inputPanel.add(txtTenGiamGia);
        
        inputPanel.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        inputPanel.add(txtMoTa);
        
        inputPanel.add(new JLabel("Tỷ lệ giảm (%):"));
        txtTyLe = new JTextField();
        inputPanel.add(txtTyLe);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addDiscount());
        btnUpdate.addActionListener(e -> updateDiscount());
        btnDelete.addActionListener(e -> deleteDiscount());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã", "Tên giảm giá", "Mô tả", "Tỷ lệ (%)", "Người tạo"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtTenGiamGia.setText(tableModel.getValueAt(row, 1).toString());
                txtMoTa.setText(tableModel.getValueAt(row, 2).toString());
                txtTyLe.setText(tableModel.getValueAt(row, 3).toString().replaceAll("[^0-9]", ""));
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Discount> list = controller.getAllDiscounts();
        for (Discount discount : list) {
            tableModel.addRow(new Object[]{
                discount.getMaGiamGia(), discount.getTenGiamGia(), discount.getMoTaGiamGia(),
                discount.getTyLeGiamGia() + "%", discount.getTenNhanVien()
            });
        }
    }
    
    private void addDiscount() {
        try {
            if (controller.addDiscount(txtTenGiamGia.getText(), txtMoTa.getText(),
                Integer.parseInt(txtTyLe.getText().replaceAll("[^0-9]", "")), currentEmployeeId)) {
                JOptionPane.showMessageDialog(this, "Thêm giảm giá thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void updateDiscount() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giảm giá!");
            return;
        }
        try {
            int maGiamGia = (int) tableModel.getValueAt(row, 0);
            if (controller.updateDiscount(maGiamGia, txtTenGiamGia.getText(), txtMoTa.getText(),
                Integer.parseInt(txtTyLe.getText().replaceAll("[^0-9]", "")), currentEmployeeId)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteDiscount() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
            int maGiamGia = (int) tableModel.getValueAt(row, 0);
            if (controller.deleteDiscount(maGiamGia)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtTenGiamGia.setText("");
        txtMoTa.setText("");
        txtTyLe.setText("");
        table.clearSelection();
    }
}
