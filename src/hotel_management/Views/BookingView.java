/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.BookingController;
import hotel_management.Controllers.RoomController;
import hotel_management.Controllers.RoomBookedController;
import hotel_management.Controllers.RoomTypeController;
import hotel_management.Models.Booking;
import hotel_management.Models.Room;
import hotel_management.Models.RoomBooked;
import hotel_management.Models.RoomType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingView extends JFrame {
    private BookingController bookingController;
    private RoomController roomController;
    private RoomBookedController roomBookedController;
    private RoomTypeController roomTypeController;
    private int currentEmployeeId;
    private JTable table;
    private DefaultTableModel tableModel;
    private JCheckBox chkHideCompleted;
    private JTextField txtSearch;
    
    public BookingView(int employeeId) {
        this.currentEmployeeId = employeeId;
        bookingController = new BookingController();
        roomController = new RoomController();
        roomBookedController = new RoomBookedController();
        roomTypeController = new RoomTypeController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Đặt phòng");
        setSize(1400, 700);
        setLocationRelativeTo(null);
        
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JButton btnConfirm = new JButton("Xác nhận & Gán phòng");
        btnConfirm.setForeground(Color.BLACK);
        btnConfirm.setFont(buttonFont); 
        
        JButton btnCheckIn = new JButton("Check-in");
        btnCheckIn.setForeground(Color.BLACK);
        btnCheckIn.setFont(buttonFont); 
        
        JButton btnManageServices = new JButton("Quản lý Dịch vụ");
        btnManageServices.setForeground(Color.BLACK);
        btnManageServices.setFont(buttonFont); 
        
        JButton btnCancel = new JButton("Hủy booking");
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFont(buttonFont); 
        
        JButton btnExport = new JButton("Xuất Excel");
        btnExport.setForeground(Color.BLACK);
        btnExport.setFont(buttonFont);
        
        
//        JButton btnDelete = new JButton("Xóa vĩnh viễn");
//        btnDelete.setForeground(Color.BLACK);
//        btnDelete.setFont(buttonFont); 
        
        // Thêm ô tìm kiếm
        txtSearch = new JTextField(20);
        txtSearch.setToolTipText("Tìm theo tên khách hàng, số phòng, hoặc mã booking");
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        
//        JButton btnRefresh = new JButton("Làm mới");
//        btnRefresh.setForeground(Color.BLACK);
//        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Checkbox ẩn booking đã hoàn thành/hủy
        chkHideCompleted = new JCheckBox("Ẩn booking đã hoàn thành/hủy");
        chkHideCompleted.setSelected(false);
        chkHideCompleted.addActionListener(e -> loadData());
        
        btnConfirm.addActionListener(e -> confirmBooking());
        btnCheckIn.addActionListener(e -> checkIn());
        btnManageServices.addActionListener(e -> manageServices());
        btnCancel.addActionListener(e -> cancelBooking());
        btnExport.addActionListener(e -> exportToExcel());
//        btnDelete.addActionListener(e -> deleteBooking());
        btnSearch.addActionListener(e -> searchBooking());
//        btnRefresh.addActionListener(e -> {
//            txtSearch.setText("");
//            loadData();
//        });
        
        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCheckIn);
        buttonPanel.add(btnManageServices);
        buttonPanel.add(btnCancel);
//        buttonPanel.add(btnDelete);
        buttonPanel.add(btnExport);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(new JLabel("Tìm kiếm:"));
        buttonPanel.add(txtSearch);
        buttonPanel.add(btnSearch);
//        buttonPanel.add(btnRefresh);
        buttonPanel.add(chkHideCompleted);
        
        String[] columns = {"Mã", "Khách hàng", "Loại phòng", "Phòng đã gán", 
                           "Ngày nhận", "Ngày trả", "Số đêm", "Tổng tiền", "Trạng thái", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14)); 
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        // Lấy map loại phòng
        List<RoomType> roomTypes = roomTypeController.getAllRoomTypes();
        Map<Integer, String> roomTypeMap = roomTypes.stream()
            .collect(Collectors.toMap(RoomType::getMaLoaiPhong, RoomType::getTenLoaiPhong));
        
        List<Booking> list = bookingController.getAllBookings();
        
        for (Booking b : list) {
            // Nếu checkbox được chọn, bỏ qua booking đã checkout hoặc cancelled
            if (chkHideCompleted.isSelected() && 
                (b.getTrangThai().equals("Checkout") || b.getTrangThai().equals("Cancelled"))) {
                continue;
            }
            
            // Lấy tên loại phòng
            String tenLoaiPhong = roomTypeMap.getOrDefault(b.getMaLoaiPhong(), "Không rõ");
            
            // Lấy thông tin phòng đã gán
            String roomInfo = "Chưa gán";
            if (b.getTrangThai().equals("Confirmed") || 
                b.getTrangThai().equals("Checkin") || 
                b.getTrangThai().equals("Checkout")) {
                List<RoomBooked> rooms = roomBookedController.getRoomsByBookingId(b.getMaDatPhong());
                if (!rooms.isEmpty()) {
                    roomInfo = rooms.stream()
                        .map(RoomBooked::getSoPhong)
                        .collect(Collectors.joining(", "));
                }
            }
            
            String statusDisplay = getStatusDisplay(b.getTrangThai());
            
            tableModel.addRow(new Object[]{
                b.getMaDatPhong(), 
                b.getTenKhachHang(),
                tenLoaiPhong,
                roomInfo,
                b.getNgayNhanPhong(), 
                b.getNgayTraPhong(),
                b.getThoiGianLuuTru() + " đêm",
                String.format("%,d VNĐ", b.getSoTienDatPhong()),
                statusDisplay, 
                b.getGhiChu()
            });
        }
    }
    
    private void searchBooking() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData(); 
            return;     
        }
        
        tableModel.setRowCount(0);
        
        List<RoomType> roomTypes = roomTypeController.getAllRoomTypes();
        Map<Integer, String> roomTypeMap = roomTypes.stream()
            .collect(Collectors.toMap(RoomType::getMaLoaiPhong, RoomType::getTenLoaiPhong));
        
        List<Booking> list = bookingController.getAllBookings();
        boolean found = false;
        
        for (Booking b : list) {
            String tenLoaiPhong = roomTypeMap.getOrDefault(b.getMaLoaiPhong(), "Không rõ");
            
            String roomInfo = "Chưa gán";
            if (b.getTrangThai().equals("Confirmed") || 
                b.getTrangThai().equals("Checkin") || 
                b.getTrangThai().equals("Checkout")) {
                List<RoomBooked> rooms = roomBookedController.getRoomsByBookingId(b.getMaDatPhong());
                if (!rooms.isEmpty()) {
                    roomInfo = rooms.stream()
                        .map(RoomBooked::getSoPhong)
                        .collect(Collectors.joining(", "));
                }
            }
            
            // Tìm kiếm theo mã booking, tên khách hàng, hoặc số phòng
            if (String.valueOf(b.getMaDatPhong()).contains(keyword) ||
                b.getTenKhachHang().toLowerCase().contains(keyword) ||
                roomInfo.toLowerCase().contains(keyword) ||
                tenLoaiPhong.toLowerCase().contains(keyword)) {
                
                String statusDisplay = getStatusDisplay(b.getTrangThai());
                
                tableModel.addRow(new Object[]{
                    b.getMaDatPhong(), 
                    b.getTenKhachHang(),
                    tenLoaiPhong,
                    roomInfo,
                    b.getNgayNhanPhong(), 
                    b.getNgayTraPhong(),
                    b.getThoiGianLuuTru() + " đêm",
                    String.format("%,d VNĐ", b.getSoTienDatPhong()),
                    statusDisplay, 
                    b.getGhiChu()
                });
                found = true;
            }
        }
        
        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy booking nào phù hợp!");
        }
    }
    
    private String getStatusDisplay(String status) {
        switch (status) {
            case "Pending": return "⏳ Chờ xác nhận";
            case "Confirmed": return "✓ Đã xác nhận";
            case "Checkin": return "🏠 Đã nhận phòng";
            case "Checkout": return "✓ Đã trả phòng";
            case "Cancelled": return "✗ Đã hủy";
            default: return status;
        }
    }
    
    private void confirmBooking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking cần xác nhận!");
            return;
        }
        
        String status = table.getValueAt(row, 8).toString();
        if (!status.contains("Chờ xác nhận")) {
            JOptionPane.showMessageDialog(this, "Chỉ xác nhận được booking đang Pending!");
            return;
        }
        
        int maDatPhong = (int) tableModel.getValueAt(row, 0); 
        Booking booking = bookingController.getBookingById(maDatPhong); 

        // 1. Lấy danh sách phòng trống thực tế
        List<Room> availableRooms = roomController.getAvailableRoomsByType(booking.getMaLoaiPhong()); 
        if (availableRooms.isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Không còn phòng trống loại: " + tableModel.getValueAt(row, 2)); 
            return;
        }

        // 2. Hiển thị hộp thoại chọn phòng
        String[] roomOptions = availableRooms.stream()
            .map(r -> r.getSoPhong() + " - " + r.getTenLoaiPhong())
            .toArray(String[]::new); 

        String selectedRoom = (String) JOptionPane.showInputDialog(this,
            "Chọn phòng cụ thể để gán cho khách:", "Gán phòng",
            JOptionPane.QUESTION_MESSAGE, null, roomOptions, roomOptions[0]); 

        if (selectedRoom != null) {
            String soPhong = selectedRoom.split(" - ")[0]; 
            int maPhong = availableRooms.stream()
                .filter(r -> r.getSoPhong().equals(soPhong))
                .findFirst().get().getMaPhong();

            // --- ĐẢO NGƯỢC LOGIC ĐỂ HỢP LÝ VỚI DATABASE ---
            // BƯỚC A: Gán phòng vào bảng trung gian trước (RoomBooked)
            if (roomBookedController.addRoomBooked(maDatPhong, maPhong)) {

                // BƯỚC B: Sau đó mới gọi xác nhận để khóa trạng thái phòng trong table Room
                if (bookingController.confirmBooking(maDatPhong, currentEmployeeId)) { 
                    JOptionPane.showMessageDialog(this, 
                        "Xác nhận thành công!\nPhòng " + soPhong + " đã chuyển sang trạng thái 'Không có'."); 
                    loadData(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái đặt phòng!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi gán phòng!");
            }
        }
}
    
    private void checkIn() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking cần check-in!");
            return;
        }
        
        String status = table.getValueAt(row, 8).toString();
        if (!status.contains("Đã xác nhận")) {
            JOptionPane.showMessageDialog(this, "Chỉ check-in được booking đã Confirmed!");
            return;
        }
        
        int maDatPhong = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận check-in cho khách?", "Check-in", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.checkIn(maDatPhong)) {
                JOptionPane.showMessageDialog(this, "Check-in thành công!");
                loadData();
            }
        }
    }
    
    private void manageServices() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking!");
            return;
        }
        
        String status = table.getValueAt(row, 8).toString();
        if (!status.contains("Đã nhận phòng")) {
            JOptionPane.showMessageDialog(this, 
                "Chỉ thêm dịch vụ cho booking đã Check-in!\nTrạng thái hiện tại: " + status);
            return;
        }
        
        int maDatPhong = (int) tableModel.getValueAt(row, 0);
        String guestName = tableModel.getValueAt(row, 1).toString();
        String roomNumber = tableModel.getValueAt(row, 3).toString();
        
        new ServiceUsedView(maDatPhong, guestName, roomNumber).setVisible(true);
    }
    
    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking cần hủy!");
            return;
        }
        
        String status = table.getValueAt(row, 8).toString();
        if (status.contains("Đã hủy") || status.contains("Đã trả phòng")) {
            JOptionPane.showMessageDialog(this, "Booking này đã hoàn thành hoặc đã hủy!");
            return;
        }
        
        int maDatPhong = (int) tableModel.getValueAt(row, 0);
        String guestName = tableModel.getValueAt(row, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận hủy booking #" + maDatPhong + " của khách " + guestName + "?\n" +
            "Booking sẽ được đánh dấu là 'Đã hủy'.",
            "Hủy booking", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.cancelBooking(maDatPhong)) {
                JOptionPane.showMessageDialog(this, 
                    "Đã hủy booking thành công!\n" +
                    "Booking vẫn được lưu trong hệ thống với trạng thái 'Đã hủy'.\n" +
                    "Bạn có thể ẩn các booking đã hủy bằng checkbox phía trên.");
                loadData();
            }
        }
    }
    
    private void deleteBooking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking cần xóa!");
            return;
        }
        
        String status = table.getValueAt(row, 8).toString();
        if (!status.contains("Đã hủy")) {
            JOptionPane.showMessageDialog(this, 
                "Chỉ có thể xóa vĩnh viễn các booking đã hủy!\n" +
                "Vui lòng hủy booking trước khi xóa.");
            return;
        }
        
        int maDatPhong = (int) tableModel.getValueAt(row, 0);
        String guestName = tableModel.getValueAt(row, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "️CẢNH BÁO: Xóa vĩnh viễn booking!\n\n" +
            "Booking #" + maDatPhong + " của khách " + guestName + "\n" +
            "sẽ bị XÓA HOÀN TOÀN khỏi hệ thống.\n\n" +
            "Hành động này KHÔNG THỂ HOÀN TÁC!\n" +
            "Bạn có chắc chắn muốn tiếp tục?",
            "Xác nhận xóa vĩnh viễn", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.deleteBooking(maDatPhong)) {
                JOptionPane.showMessageDialog(this, 
                    "Đã xóa booking hoàn toàn khỏi hệ thống!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi xóa booking!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToExcel() {
        // 1. Cho phép người dùng chọn nơi lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            // Tự động thêm đuôi .csv nếu người dùng quên
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                    new java.io.OutputStreamWriter(
                    new java.io.FileOutputStream(filePath), "UTF-8"))) { // Dùng UTF-8 để không lỗi font tiếng Việt

                // Thêm BOM để Excel nhận diện đúng tiếng Việt (quan trọng)
                bw.write("\ufeff");

                // 2. Viết tiêu đề cột
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    bw.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        bw.write(","); // Ngăn cách bằng dấu phẩy
                    }
                }
                bw.newLine(); // Xuống dòng

                // 3. Viết dữ liệu từng dòng
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        String value = tableModel.getValueAt(i, j).toString();

                        // Xử lý nếu dữ liệu có dấu phẩy (thay bằng chấm phẩy hoặc bao quanh bằng ngoặc kép)
                        // Ví dụ: "Ghi chú, có dấu phẩy" -> "Ghi chú; có dấu phẩy"
                        value = value.replace(",", ";"); 
                        value = value.replace("\n", " "); // Xóa xuống dòng nếu có

                        bw.write(value);
                        if (j < tableModel.getColumnCount() - 1) {
                            bw.write(",");
                        }
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(this, "Xuất file thành công!\n" + filePath);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}