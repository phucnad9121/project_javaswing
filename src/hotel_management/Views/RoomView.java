package hotel_management.Views;

import hotel_management.Controllers.RoomController;
import hotel_management.Controllers.RoomTypeController;
import hotel_management.Models.Room;
import hotel_management.Models.RoomType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors; // Import thư viện để dùng Filter

public class RoomView extends JFrame {
    private RoomController roomController;
    private RoomTypeController roomTypeController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSoPhong;
    
    // 1. Thêm biến cho chức năng Tìm kiếm
    private JTextField txtSearch; 
    
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
        setSize(900, 650); // Tăng chiều cao một chút để chứa thanh tìm kiếm
        setLocationRelativeTo(null);
        
        // --- PANEL NHẬP LIỆU (Giữ nguyên) ---
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
        
        // --- PANEL NÚT BẤM (Cập nhật nút Làm mới để reset tìm kiếm) ---
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addRoom());
        btnUpdate.addActionListener(e -> updateRoom());
        btnDelete.addActionListener(e -> deleteRoom());
        // Khi làm mới: Xóa trắng ô tìm kiếm và tải lại toàn bộ dữ liệu
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
        searchPanel.add(new JLabel("Tìm kiếm (Số phòng/Loại): "));
        
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> searchRoom()); // Gọi hàm tìm kiếm
        searchPanel.add(btnSearch);

        // --- BẢNG DỮ LIỆU ---
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
        
        // --- BỐ CỤC TỔNG THỂ ---
        // Tạo một Panel chứa cả Thanh tìm kiếm và Bảng để đặt vào vùng CENTER
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH); // Tìm kiếm nằm trên
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER); // Bảng nằm dưới

        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER); // Thay thế dòng add(scrollPane) cũ
    }
    
    // --- CÁC HÀM XỬ LÝ (LOGIC) ---

    // 3. Hàm hiển thị danh sách (Tách ra để dùng chung cho loadData và searchRoom)
    private void displayData(List<Room> list) {
        tableModel.setRowCount(0);
        for (Room room : list) {
            String statusDisplay = room.getKhaDung().equals("Yes") ? "Có sẵn" : "Không có";
            tableModel.addRow(new Object[]{
                room.getMaPhong(), room.getSoPhong(), room.getTenLoaiPhong(), statusDisplay
            });
        }
    }

    // 4. Hàm Tìm kiếm (Giống hệt EmployeeView)
    private void searchRoom() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData(); // Nếu ô tìm kiếm trống thì hiện tất cả
            return;
        }

        List<Room> allRooms = roomController.getAllRooms();
        // Lọc dữ liệu bằng Stream
        List<Room> filteredList = allRooms.stream()
            .filter(r -> r.getSoPhong().toLowerCase().contains(keyword) || 
                         r.getTenLoaiPhong().toLowerCase().contains(keyword))
            .collect(Collectors.toList());

        displayData(filteredList);
        
        if (filteredList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng nào phù hợp!");
        }
    }

    private void loadRoomTypes() {
        roomTypes = roomTypeController.getAllRoomTypes();
        cmbLoaiPhong.removeAllItems();
        for (RoomType rt : roomTypes) {
            cmbLoaiPhong.addItem(rt.getTenLoaiPhong());
        }
    }
    
    // Cập nhật hàm loadData để gọi displayData
    private void loadData() {
        List<Room> list = roomController.getAllRooms();
        displayData(list);
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