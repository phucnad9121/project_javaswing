/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.RoomTypeController;
import hotel_management.Models.RoomType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomTypeView extends JFrame {
    private RoomTypeController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenLoaiPhong, txtGiaPhong, txtMoTa;
    
    public RoomTypeView() {
        controller = new RoomTypeController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Loại phòng");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Loại phòng"));
        
        inputPanel.add(new JLabel("Tên loại:"));
        txtTenLoaiPhong = new JTextField();
        inputPanel.add(txtTenLoaiPhong);
        
        inputPanel.add(new JLabel("Giá phòng (VNĐ):"));
        txtGiaPhong = new JTextField();
        inputPanel.add(txtGiaPhong);
        
        inputPanel.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        inputPanel.add(txtMoTa);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addRoomType());
        btnUpdate.addActionListener(e -> updateRoomType());
        btnDelete.addActionListener(e -> deleteRoomType());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã", "Tên loại", "Giá phòng", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtTenLoaiPhong.setText(tableModel.getValueAt(row, 1).toString());
                txtGiaPhong.setText(tableModel.getValueAt(row, 2).toString().replaceAll("[^0-9]", ""));
                txtMoTa.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<RoomType> list = controller.getAllRoomTypes();
        for (RoomType rt : list) {
            tableModel.addRow(new Object[]{
                rt.getMaLoaiPhong(), rt.getTenLoaiPhong(), 
                String.format("%,d VNĐ", rt.getGiaPhong()), rt.getMoTaPhong()
            });
        }
    }
    
    private void addRoomType() {
        try {
            if (controller.addRoomType(txtTenLoaiPhong.getText(), 
                Integer.parseInt(txtGiaPhong.getText().replaceAll("[^0-9]", "")), 
                txtMoTa.getText())) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void updateRoomType() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
            return;
        }
        try {
            int maLoaiPhong = (int) tableModel.getValueAt(row, 0);
            if (controller.updateRoomType(maLoaiPhong, txtTenLoaiPhong.getText(),
                Integer.parseInt(txtGiaPhong.getText().replaceAll("[^0-9]", "")),
                txtMoTa.getText())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteRoomType() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
            int maLoaiPhong = (int) tableModel.getValueAt(row, 0);
            if (controller.deleteRoomType(maLoaiPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtTenLoaiPhong.setText("");
        txtGiaPhong.setText("");
        txtMoTa.setText("");
        table.clearSelection();
    }
}