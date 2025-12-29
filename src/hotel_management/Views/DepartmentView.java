/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.DepartmentController;
import hotel_management.Models.Department;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DepartmentView extends JFrame {
    private DepartmentController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenBoPhan, txtMoTa, txtLuong, txtChucDanh;
    
    public DepartmentView() {
        controller = new DepartmentController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Bộ phận");
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Bộ phận"));
        
        inputPanel.add(new JLabel("Tên bộ phận:"));
        txtTenBoPhan = new JTextField();
        inputPanel.add(txtTenBoPhan);
        
        inputPanel.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        inputPanel.add(txtMoTa);
        
        inputPanel.add(new JLabel("Lương khởi điểm:"));
        txtLuong = new JTextField();
        inputPanel.add(txtLuong);
        
        inputPanel.add(new JLabel("Chức danh:"));
        txtChucDanh = new JTextField();
        inputPanel.add(txtChucDanh);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addDepartment());
        btnUpdate.addActionListener(e -> updateDepartment());
        btnDelete.addActionListener(e -> deleteDepartment());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã", "Tên bộ phận", "Mô tả", "Lương khởi điểm", "Chức danh"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtTenBoPhan.setText(tableModel.getValueAt(row, 1).toString());
                txtMoTa.setText(tableModel.getValueAt(row, 2).toString());
                txtLuong.setText(tableModel.getValueAt(row, 3).toString());
                txtChucDanh.setText(tableModel.getValueAt(row, 4).toString());
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Department> list = controller.getAllDepartments();
        for (Department dept : list) {
            tableModel.addRow(new Object[]{
                dept.getMaBoPhan(), dept.getTenBoPhan(), dept.getMoTaBoPhan(),
                String.format("%,d VNĐ", dept.getLuongKhoiDiem()), dept.getChucDanh()
            });
        }
    }
    
    private void addDepartment() {
        try {
            if (controller.addDepartment(txtTenBoPhan.getText(), txtMoTa.getText(),
                Integer.parseInt(txtLuong.getText().replaceAll("[^0-9]", "")), 
                txtChucDanh.getText())) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void updateDepartment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bộ phận cần sửa!");
            return;
        }
        try {
            int maBoPhan = (int) tableModel.getValueAt(row, 0);
            if (controller.updateDepartment(maBoPhan, txtTenBoPhan.getText(), txtMoTa.getText(),
                Integer.parseInt(txtLuong.getText().replaceAll("[^0-9]", "")), 
                txtChucDanh.getText())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteDepartment() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
            int maBoPhan = (int) tableModel.getValueAt(row, 0);
            if (controller.deleteDepartment(maBoPhan)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtTenBoPhan.setText("");
        txtMoTa.setText("");
        txtLuong.setText("");
        txtChucDanh.setText("");
        table.clearSelection();
    }
}
