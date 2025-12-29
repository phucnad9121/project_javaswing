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
import hotel_management.Controllers.RoomTypeController;
import hotel_management.Controllers.RoomBookedController;
import hotel_management.Models.Booking;
import hotel_management.Models.Guest;
import hotel_management.Models.RoomType;
import hotel_management.Models.RoomBooked;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyBookingsView extends JFrame {
    private BookingController controller;
    private RoomTypeController roomTypeController;
    private RoomBookedController roomBookedController;
    private Guest currentGuest;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public MyBookingsView(Guest guest) {
        this.currentGuest = guest;
        controller = new BookingController();
        roomTypeController = new RoomTypeController();
        roomBookedController = new RoomBookedController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Booking của tôi");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("BOOKING CỦA BẠN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh, BorderLayout.EAST);
        
        String[] columns = {"Mã booking", "Loại phòng", "Phòng", "Ngày nhận", "Ngày trả", 
                           "Số ngày", "Tổng tiền", "Trạng thái", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        
        // Lấy tất cả loại phòng để map
        List<RoomType> roomTypes = roomTypeController.getAllRoomTypes();
        Map<Integer, String> roomTypeMap = roomTypes.stream()
            .collect(Collectors.toMap(RoomType::getMaLoaiPhong, RoomType::getTenLoaiPhong));
        
        List<Booking> allBookings = controller.getAllBookings();
        
        for (Booking booking : allBookings) {
            if (booking.getMaKhachHang() == currentGuest.getMaKhachHang()) {
                // Lấy tên loại phòng
                String tenLoaiPhong = roomTypeMap.getOrDefault(booking.getMaLoaiPhong(), "Chưa xác định");
                
                // Lấy số phòng đã gán (nếu có)
                String soPhong = "Chưa gán";
                if (!booking.getTrangThai().equals("Pending")) {
                    List<RoomBooked> rooms = roomBookedController.getRoomsByBookingId(booking.getMaDatPhong());
                    if (!rooms.isEmpty()) {
                        soPhong = rooms.stream()
                            .map(RoomBooked::getSoPhong)
                            .collect(Collectors.joining(", "));
                    }
                }
                
                String statusDisplay = getStatusDisplay(booking.getTrangThai());
                tableModel.addRow(new Object[]{
                    booking.getMaDatPhong(),
                    tenLoaiPhong,
                    soPhong,
                    booking.getNgayNhanPhong(),
                    booking.getNgayTraPhong(),
                    booking.getThoiGianLuuTru() + " đêm",
                    String.format("%,d VNĐ", booking.getSoTienDatPhong()),
                    statusDisplay,
                    booking.getGhiChu()
                });
            }
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
}