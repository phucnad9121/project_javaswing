/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.RoomController;
import hotel_management.Controllers.RoomTypeController;
import hotel_management.Models.Room;
import hotel_management.Models.RoomType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomView extends JFrame {
    private RoomController roomController;
    private RoomTypeController roomTypeController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSoPhong;
    private JComboBox<String> cmbLoaiPhong, cmbKhaDung;
    private List<RoomType> roomTypes;
    
    public RoomView() {
        roomController = new RoomController();
        roomTypeController = new RoomTypeController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Phòng");
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Phòng"));
        
        inputPanel.add(new JLabel("Số phòng:"));
        txtSoPhong = new JTextField();
        inputPanel.add(txtSoPhong);
        
        inputPanel.add(new JLabel("Loại phòng:"));
        cmbLoaiPhong = new JComboBox<>();
        loadRoomTypes();
        inputPanel.add(cmbLoaiPhong);
        
        inputPanel.add(new JLabel("Khả dụng:"));
        cmbKhaDung = new JComboBox<>(new String[]{"Yes", "No"});
        inputPanel.add(cmbKhaDung);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addRoom());
        btnUpdate.addActionListener(e -> updateRoom());
        btnDelete.addActionListener(e -> deleteRoom());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã", "Số phòng", "Loại phòng", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtSoPhong.setText(tableModel.getValueAt(row, 1).toString());
                cmbLoaiPhong.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                String status = tableModel.getValueAt(row, 3).toString();
                cmbKhaDung.setSelectedItem(status.contains("Có sẵn") ? "Yes" : "No");
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadRoomTypes() {
        roomTypes = roomTypeController.getAllRoomTypes();
        cmbLoaiPhong.removeAllItems();
        for (RoomType rt : roomTypes) {
            cmbLoaiPhong.addItem(rt.getTenLoaiPhong());
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Room> list = roomController.getAllRooms();
        for (Room room : list) {
            String statusDisplay = room.getKhaDung().equals("Yes") ? "Có sẵn" : "Không có";
            tableModel.addRow(new Object[]{
                room.getMaPhong(), room.getSoPhong(), room.getTenLoaiPhong(), statusDisplay
            });
        }
    }
    
    private void addRoom() {
        try {
            int maLoaiPhong = roomTypes.get(cmbLoaiPhong.getSelectedIndex()).getMaLoaiPhong();
            String khaDung = cmbKhaDung.getSelectedItem().toString();
            
            if (roomController.addRoom(txtSoPhong.getText(), maLoaiPhong, khaDung)) {
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void updateRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng!");
            return;
        }
        try {
            int maPhong = (int) tableModel.getValueAt(row, 0);
            int maLoaiPhong = roomTypes.get(cmbLoaiPhong.getSelectedIndex()).getMaLoaiPhong();
            String khaDung = cmbKhaDung.getSelectedItem().toString();
            
            if (roomController.updateRoom(maPhong, txtSoPhong.getText(), maLoaiPhong, khaDung)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteRoom() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
            int maPhong = (int) tableModel.getValueAt(row, 0);
            if (roomController.deleteRoom(maPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtSoPhong.setText("");
        cmbLoaiPhong.setSelectedIndex(0);
        cmbKhaDung.setSelectedIndex(0);
        table.clearSelection();
    }
}