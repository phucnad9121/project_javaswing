/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.ServiceController;
import hotel_management.Models.Service;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServiceView extends JFrame {
    private ServiceController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenDichVu, txtChiPhi, txtMoTa;
    
    public ServiceView() {
        controller = new ServiceController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Dịch vụ");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Dịch vụ"));
        
        inputPanel.add(new JLabel("Tên dịch vụ:"));
        txtTenDichVu = new JTextField();
        inputPanel.add(txtTenDichVu);
        
        inputPanel.add(new JLabel("Chi phí (VNĐ):"));
        txtChiPhi = new JTextField();
        inputPanel.add(txtChiPhi);
        
        inputPanel.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        inputPanel.add(txtMoTa);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addService());
        btnUpdate.addActionListener(e -> updateService());
        btnDelete.addActionListener(e -> deleteService());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã", "Tên dịch vụ", "Chi phí", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtTenDichVu.setText(tableModel.getValueAt(row, 1).toString());
                txtChiPhi.setText(tableModel.getValueAt(row, 2).toString().replaceAll("[^0-9]", ""));
                txtMoTa.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Service> list = controller.getAllServices();
        for (Service service : list) {
            tableModel.addRow(new Object[]{
                service.getMaDichVu(), service.getTenDichVu(),
                String.format("%,d VNĐ", service.getChiPhiDichVu()), service.getMoTaDichVu()
            });
        }
    }
    
    private void addService() {
        try {
            if (controller.addService(txtTenDichVu.getText(), txtMoTa.getText(),
                Integer.parseInt(txtChiPhi.getText().replaceAll("[^0-9]", "")))) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void updateService() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ!");
            return;
        }
        try {
            int maDichVu = (int) tableModel.getValueAt(row, 0);
            if (controller.updateService(maDichVu, txtTenDichVu.getText(), txtMoTa.getText(),
                Integer.parseInt(txtChiPhi.getText().replaceAll("[^0-9]", "")))) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteService() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
            int maDichVu = (int) tableModel.getValueAt(row, 0);
            if (controller.deleteService(maDichVu)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtTenDichVu.setText("");
        txtChiPhi.setText("");
        txtMoTa.setText("");
        table.clearSelection();
    }
}