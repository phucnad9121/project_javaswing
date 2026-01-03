package hotel_management.Views;

import hotel_management.Controllers.RoomTypeController;
import hotel_management.Models.RoomType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors; // Import thư viện lọc dữ liệu

public class RoomTypeView extends JFrame {
    private RoomTypeController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenLoaiPhong, txtGiaPhong, txtMoTa;
    
    // 1. Thêm biến cho ô tìm kiếm
    private JTextField txtSearch;
    
    public RoomTypeView() {
        controller = new RoomTypeController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Loại phòng");
        setSize(850, 600); // Tăng chiều rộng chút cho thoải mái
        setLocationRelativeTo(null);
        
        // --- PANEL NHẬP LIỆU (Giữ nguyên) ---
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
        
        // --- PANEL NÚT BẤM ---
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addRoomType());
        btnUpdate.addActionListener(e -> updateRoomType());
        btnDelete.addActionListener(e -> deleteRoomType());
        
        // Nút làm mới: Xóa ô tìm kiếm và tải lại toàn bộ
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
            clearFields();
        });
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        // --- 2. PANEL TÌM KIẾM (Mới thêm) ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        searchPanel.add(new JLabel("Tìm kiếm (Tên/Mô tả): "));
        
        txtSearch = new JTextField(25);
        searchPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> searchRoomType()); // Gọi hàm tìm kiếm
        searchPanel.add(btnSearch);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"Mã", "Tên loại", "Giá phòng", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtTenLoaiPhong.setText(tableModel.getValueAt(row, 1).toString());
                // Xử lý chuỗi tiền tệ để lấy lại số nguyên khi hiển thị lên ô nhập
                txtGiaPhong.setText(tableModel.getValueAt(row, 2).toString().replaceAll("[^0-9]", ""));
                txtMoTa.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
        
        // --- BỐ CỤC TỔNG THỂ ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH); // Tìm kiếm nằm trên
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER); // Bảng nằm dưới
        
        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    // --- 3. Tách hàm hiển thị dữ liệu để dùng chung ---
    private void displayData(List<RoomType> list) {
        tableModel.setRowCount(0);
        for (RoomType rt : list) {
            tableModel.addRow(new Object[]{
                rt.getMaLoaiPhong(), 
                rt.getTenLoaiPhong(), 
                String.format("%,d VNĐ", rt.getGiaPhong()), 
                rt.getMoTaPhong()
            });
        }
    }

    // --- 4. Hàm Tìm kiếm (Logic chính) ---
    private void searchRoomType() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        List<RoomType> allTypes = controller.getAllRoomTypes();
        // Lọc theo Tên loại phòng HOẶC Mô tả
        List<RoomType> filteredList = allTypes.stream()
            .filter(rt -> rt.getTenLoaiPhong().toLowerCase().contains(keyword) || 
                          rt.getMoTaPhong().toLowerCase().contains(keyword))
            .collect(Collectors.toList());

        displayData(filteredList);
        
        if (filteredList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng nào phù hợp!");
        }
    }
    
    private void loadData() {
        List<RoomType> list = controller.getAllRoomTypes();
        displayData(list); // Gọi hàm displayData
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