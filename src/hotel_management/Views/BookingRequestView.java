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
import hotel_management.Controllers.RoomController;
import hotel_management.Models.Guest;
import hotel_management.Models.RoomType;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingRequestView extends JFrame {
    private BookingController bookingController;
    private RoomTypeController roomTypeController;
    private RoomController roomController;
    private Guest currentGuest;
    private JComboBox<String> cmbRoomType;
    private JSpinner spinnerCheckin, spinnerCheckout;
    private JTextArea txtNotes;
    private JLabel lblEstimatedPrice;
    private JLabel lblAvailableRooms;
    private List<RoomType> roomTypes;
    
    public BookingRequestView(Guest guest) {
        this.currentGuest = guest;
        bookingController = new BookingController();
        roomTypeController = new RoomTypeController();
        roomController = new RoomController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đặt phòng - " + currentGuest.getHoTenKhachHang());
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitle = new JLabel("TẠO YÊU CẦU ĐẶT PHÒNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Khách hàng:"), gbc);
        gbc.gridx = 1;
        JLabel lblGuest = new JLabel(currentGuest.getHoTenKhachHang() + " - " + 
                                     currentGuest.getSoDienThoaiKhachHang());
        lblGuest.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(lblGuest, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 1;
        cmbRoomType = new JComboBox<>();
        cmbRoomType.setPreferredSize(new Dimension(300, 30));
        loadRoomTypes();
        cmbRoomType.addActionListener(e -> {
            calculateEstimatedPrice();
            updateAvailableRoomsCount();
        });
        mainPanel.add(cmbRoomType, gbc);
        
        // Thêm label hiển thị số phòng trống
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Số phòng trống:"), gbc);
        gbc.gridx = 1;
        lblAvailableRooms = new JLabel("0 phòng");
        lblAvailableRooms.setFont(new Font("Arial", Font.BOLD, 14));
        lblAvailableRooms.setForeground(new Color(39, 174, 96));
        mainPanel.add(lblAvailableRooms, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Ngày nhận phòng:"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel modelCheckin = new SpinnerDateModel();
        spinnerCheckin = new JSpinner(modelCheckin);
        JSpinner.DateEditor editorCheckin = new JSpinner.DateEditor(spinnerCheckin, "dd/MM/yyyy");
        spinnerCheckin.setEditor(editorCheckin);
        spinnerCheckin.addChangeListener(e -> calculateEstimatedPrice());
        mainPanel.add(spinnerCheckin, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Ngày trả phòng:"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel modelCheckout = new SpinnerDateModel();
        spinnerCheckout = new JSpinner(modelCheckout);
        JSpinner.DateEditor editorCheckout = new JSpinner.DateEditor(spinnerCheckout, "dd/MM/yyyy");
        spinnerCheckout.setEditor(editorCheckout);
        spinnerCheckout.addChangeListener(e -> calculateEstimatedPrice());
        mainPanel.add(spinnerCheckout, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Giá ước tính:"), gbc);
        gbc.gridx = 1;
        lblEstimatedPrice = new JLabel("0 VNĐ");
        lblEstimatedPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblEstimatedPrice.setForeground(new Color(231, 76, 60));
        mainPanel.add(lblEstimatedPrice, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        txtNotes = new JTextArea(4, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtNotes);
        mainPanel.add(scrollPane, gbc);
        
        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.setBorder(BorderFactory.createTitledBorder("Lưu ý"));
        JTextArea txtInfo = new JTextArea(
            "• Yêu cầu đặt phòng sẽ được gửi đến lễ tân.\n" +
            "• Lễ tân sẽ liên hệ xác nhận và gán phòng cụ thể.\n" +
            "• Vui lòng chú ý điện thoại để nhận thông báo.\n" +
            "• Nếu hết phòng, yêu cầu sẽ bị từ chối.");
        txtInfo.setEditable(false);
        txtInfo.setBackground(new Color(236, 240, 241));
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        notePanel.add(txtInfo);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        mainPanel.add(notePanel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnSubmit = new JButton("Gửi yêu cầu đặt phòng");
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setFocusPainted(false);
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFocusPainted(false);
        
        btnSubmit.addActionListener(e -> submitBooking());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);
        
        gbc.gridy = 9;
        mainPanel.add(buttonPanel, gbc);
        
        add(new JScrollPane(mainPanel));
        calculateEstimatedPrice();
        updateAvailableRoomsCount();
    }
    
    private void loadRoomTypes() {
        roomTypes = roomTypeController.getAllRoomTypes();
        for (RoomType rt : roomTypes) {
            cmbRoomType.addItem(rt.getTenLoaiPhong() + " - " + 
                              String.format("%,d VNĐ/đêm", rt.getGiaPhong()));
        }
    }
    
    private void updateAvailableRoomsCount() {
        try {
            if (cmbRoomType.getSelectedIndex() == -1) {
                lblAvailableRooms.setText("0 phòng");
                lblAvailableRooms.setForeground(Color.RED);
                return;
            }
            
            int selectedIndex = cmbRoomType.getSelectedIndex();
            int roomTypeId = roomTypes.get(selectedIndex).getMaLoaiPhong();
            
            // Đếm số phòng trống
            int availableCount = roomController.countAvailableRoomsByType(roomTypeId);
            
            lblAvailableRooms.setText(availableCount + " phòng");
            
            // Đổi màu theo số lượng
            if (availableCount == 0) {
                lblAvailableRooms.setForeground(Color.RED);
            } else if (availableCount <= 2) {
                lblAvailableRooms.setForeground(new Color(243, 156, 18)); // Vàng cam
            } else {
                lblAvailableRooms.setForeground(new Color(39, 174, 96)); // Xanh lá
            }
            
        } catch (Exception e) {
            lblAvailableRooms.setText("Lỗi");
            lblAvailableRooms.setForeground(Color.RED);
        }
    }
    
    private void calculateEstimatedPrice() {
        try {
            if (cmbRoomType.getSelectedIndex() == -1) return;
            
            java.util.Date checkinUtil = (java.util.Date) spinnerCheckin.getValue();
            java.util.Date checkoutUtil = (java.util.Date) spinnerCheckout.getValue();
            
            LocalDate checkinDate = new java.sql.Date(checkinUtil.getTime()).toLocalDate();
            LocalDate checkoutDate = new java.sql.Date(checkoutUtil.getTime()).toLocalDate();
            
            long days = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            if (days <= 0) {
                lblEstimatedPrice.setText("Ngày không hợp lệ");
                return;
            }
            
            RoomType selectedType = roomTypes.get(cmbRoomType.getSelectedIndex());
            int totalPrice = (int)(days * selectedType.getGiaPhong());
            
            lblEstimatedPrice.setText(String.format("%,d VNĐ (%d đêm)", totalPrice, days));
        } catch (Exception e) {
            lblEstimatedPrice.setText("0 VNĐ");
        }
    }
    
    private void submitBooking() {
        try {
            int selectedIndex = cmbRoomType.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
                return;
            }
            
            int roomTypeId = roomTypes.get(selectedIndex).getMaLoaiPhong();
            
            // ✅ KIỂM TRA SỐ LƯỢNG PHÒNG TRỐNG
            int availableRooms = roomController.countAvailableRoomsByType(roomTypeId);
            
            if (availableRooms <= 0) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ RẤT TIẾC!\n\n" +
                    "Hiện tại đã HẾT PHÒNG loại: " + roomTypes.get(selectedIndex).getTenLoaiPhong() + "\n\n" +
                    "Vui lòng:\n" +
                    "• Chọn loại phòng khác\n" +
                    "• Hoặc liên hệ lễ tân để được tư vấn thêm",
                    "Hết phòng",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date checkinUtil = (java.util.Date) spinnerCheckin.getValue();
            java.util.Date checkoutUtil = (java.util.Date) spinnerCheckout.getValue();

            Date ngayDatPhong = new Date(System.currentTimeMillis());
            Date checkinDate = new Date(checkinUtil.getTime());
            Date checkoutDate = new Date(checkoutUtil.getTime());

            if (checkoutDate.before(checkinDate) || checkoutDate.equals(checkinDate)) {
                JOptionPane.showMessageDialog(this, "Ngày trả phòng phải sau ngày nhận phòng!");
                return;
            }

            LocalDate checkin = checkinDate.toLocalDate();
            LocalDate checkout = checkoutDate.toLocalDate();
            int days = (int) ChronoUnit.DAYS.between(checkin, checkout);

            RoomType selectedType = roomTypes.get(selectedIndex);
            int soTienDatPhong = days * selectedType.getGiaPhong();

            String notes = txtNotes.getText().trim();

            // Gửi yêu cầu đặt phòng
            if (bookingController.createBooking(ngayDatPhong, days, checkinDate, checkoutDate,
                soTienDatPhong, currentGuest.getMaKhachHang(), roomTypeId, notes)) {

                JOptionPane.showMessageDialog(this,
                    "GỬI YÊU CẦU ĐẶT PHÒNG THÀNH CÔNG!\n\n" +
                    "Thông tin booking:\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "Loại phòng: " + selectedType.getTenLoaiPhong() + "\n" +
                    "Số phòng trống hiện tại: " + availableRooms + " phòng\n" +
                    "Thời gian: " + days + " đêm\n" +
                    "Giá ước tính: " + String.format("%,d VNĐ", soTienDatPhong) + "\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                    "Lễ tân sẽ liên hệ với bạn để xác nhận.\n" +
                    "Vui lòng chú ý điện thoại!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gửi yêu cầu thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}