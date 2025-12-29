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
import hotel_management.Controllers.ServiceUsedController;
import hotel_management.Controllers.DiscountController;
import hotel_management.Models.Booking;
import hotel_management.Models.Discount;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CheckoutView extends JFrame {
    private BookingController bookingController;
    private ServiceUsedController serviceUsedController;
    private DiscountController discountController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTienPhong, txtTienDichVu, txtGiamGia, txtTongCong;
    private JComboBox<String> cmbDiscount;
    private List<Discount> discounts;
    
    public CheckoutView() {
        bookingController = new BookingController();
        serviceUsedController = new ServiceUsedController();
        discountController = new DiscountController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Thanh toán & Trả phòng");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        
        // Table Panel
        String[] columns = {"Mã", "Khách hàng", "Phòng", "Ngày nhận", 
                           "Ngày trả", "Số đêm", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                calculateAmount();
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1200, 300));
        
        // Payment Panel
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết thanh toán"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tiền phòng
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTienPhong = new JLabel("Tiền phòng:");
        lblTienPhong.setFont(new Font("Arial", Font.BOLD, 14));
        paymentPanel.add(lblTienPhong, gbc);
        
        gbc.gridx = 1;
        txtTienPhong = new JTextField(20);
        txtTienPhong.setEditable(false);
        txtTienPhong.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentPanel.add(txtTienPhong, gbc);
        
        // Tiền dịch vụ
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTienDichVu = new JLabel("Tiền dịch vụ (tự động):");
        lblTienDichVu.setFont(new Font("Arial", Font.BOLD, 14));
        paymentPanel.add(lblTienDichVu, gbc);
        
        gbc.gridx = 1;
        txtTienDichVu = new JTextField(20);
        txtTienDichVu.setEditable(false);
        txtTienDichVu.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentPanel.add(txtTienDichVu, gbc);
        
        // Giảm giá
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblGiamGia = new JLabel("Mã giảm giá:");
        lblGiamGia.setFont(new Font("Arial", Font.BOLD, 14));
        paymentPanel.add(lblGiamGia, gbc);
        
        gbc.gridx = 1;
        cmbDiscount = new JComboBox<>();
        loadDiscounts();
        cmbDiscount.addActionListener(e -> applyDiscount());
        paymentPanel.add(cmbDiscount, gbc);
        
        // Số tiền giảm
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblSoTienGiam = new JLabel("Số tiền giảm:");
        lblSoTienGiam.setFont(new Font("Arial", Font.BOLD, 14));
        paymentPanel.add(lblSoTienGiam, gbc);
        
        gbc.gridx = 1;
        txtGiamGia = new JTextField(20);
        txtGiamGia.setEditable(false);
        txtGiamGia.setFont(new Font("Arial", Font.PLAIN, 14));
        txtGiamGia.setForeground(new Color(46, 204, 113));
        paymentPanel.add(txtGiamGia, gbc);
        
        // Tổng cộng
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblTongCong = new JLabel("TỔNG CỘNG:");
        lblTongCong.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongCong.setForeground(new Color(231, 76, 60));
        paymentPanel.add(lblTongCong, gbc);
        
        gbc.gridx = 1;
        txtTongCong = new JTextField(20);
        txtTongCong.setEditable(false);
        txtTongCong.setFont(new Font("Arial", Font.BOLD, 18));
        txtTongCong.setForeground(new Color(231, 76, 60));
        paymentPanel.add(txtTongCong, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnViewServices = new JButton("Xem chi tiết dịch vụ");
        btnViewServices.setBackground(new Color(155, 89, 182));
        btnViewServices.setForeground(Color.WHITE);
        btnViewServices.setFont(new Font("Arial", Font.BOLD, 13));
        
        JButton btnCalculate = new JButton("Tính toán lại");
        btnCalculate.setBackground(new Color(52, 152, 219));
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setFont(new Font("Arial", Font.BOLD, 13));
        
        JButton btnCheckout = new JButton("💳 THANH TOÁN & TRẢ PHÒNG");
        btnCheckout.setBackground(new Color(46, 204, 113));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckout.setPreferredSize(new Dimension(250, 40));
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setBackground(new Color(149, 165, 166));
        btnRefresh.setForeground(Color.WHITE);
        
        btnViewServices.addActionListener(e -> viewServiceDetails());
        btnCalculate.addActionListener(e -> calculateAmount());
        btnCheckout.addActionListener(e -> checkout());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnViewServices);
        buttonPanel.add(btnCalculate);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnRefresh);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        paymentPanel.add(buttonPanel, gbc);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
            tableScrollPane, paymentPanel);
        splitPane.setDividerLocation(350);
        
        add(splitPane);
    }
    
    private void loadDiscounts() {
        discounts = discountController.getAllDiscounts();
        cmbDiscount.addItem("Không giảm giá");
        for (Discount d : discounts) {
            cmbDiscount.addItem(d.getTenGiamGia() + " - " + d.getTyLeGiamGia() + "%");
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Booking> list = bookingController.getAllBookings().stream()
            .filter(b -> b.getTrangThai().equals("Checkin"))
            .toList();
        
        for (Booking b : list) {
            tableModel.addRow(new Object[]{
                b.getMaDatPhong(), 
                b.getTenKhachHang(), 
                "Phòng", // Cần thêm info
                b.getNgayNhanPhong(), 
                b.getNgayTraPhong(),
                b.getThoiGianLuuTru() + " đêm",
                "Đã nhận phòng"
            });
        }
    }
    
    private void calculateAmount() {
        int row = table.getSelectedRow();
        if (row == -1) {
            clearFields();
            return;
        }
        
        try {
            int maDatPhong = (int) tableModel.getValueAt(row, 0);
            
            // Lấy thông tin booking
            Booking booking = bookingController.getBookingById(maDatPhong);
            if (booking == null) return;
            
            // Tính tiền phòng
            java.sql.Date checkin = booking.getNgayNhanPhong();
            java.sql.Date checkout = booking.getNgayTraPhong();
            
            long days = ChronoUnit.DAYS.between(checkin.toLocalDate(), checkout.toLocalDate());
            if (days <= 0) days = 1;
            
            int tienPhong = booking.getSoTienDatPhong();
            txtTienPhong.setText(String.format("%,d VNĐ", tienPhong));
            
            // Tự động lấy tiền dịch vụ từ database
            int tienDichVu = serviceUsedController.getTotalServiceCharge(maDatPhong);
            txtTienDichVu.setText(String.format("%,d VNĐ", tienDichVu));
            
            // Tính giảm giá
            applyDiscount();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tính toán: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void applyDiscount() {
        try {
            int tienPhong = Integer.parseInt(txtTienPhong.getText().replaceAll("[^0-9]", ""));
            int tienDichVu = Integer.parseInt(txtTienDichVu.getText().replaceAll("[^0-9]", ""));
            int tongTruocGiam = tienPhong + tienDichVu;
            
            int soTienGiam = 0;
            int selectedIndex = cmbDiscount.getSelectedIndex();
            
            if (selectedIndex > 0) { // Có chọn mã giảm giá
                Discount discount = discounts.get(selectedIndex - 1);
                soTienGiam = tongTruocGiam * discount.getTyLeGiamGia() / 100;
            }
            
            txtGiamGia.setText(String.format("-%,d VNĐ", soTienGiam));
            
            int tongCong = tongTruocGiam - soTienGiam;
            txtTongCong.setText(String.format("%,d VNĐ", tongCong));
            
        } catch (Exception e) {
            txtGiamGia.setText("0 VNĐ");
            txtTongCong.setText("0 VNĐ");
        }
    }
    
    private void viewServiceDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking!");
            return;
        }
        
        int maDatPhong = (int) tableModel.getValueAt(row, 0);
        String guestName = tableModel.getValueAt(row, 1).toString();
        String roomNumber = tableModel.getValueAt(row, 2).toString();
        
        new ServiceUsedView(maDatPhong, guestName, roomNumber).setVisible(true);
    }
    
    private void checkout() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn booking cần thanh toán!");
            return;
        }
        
        try {
            int tongCong = Integer.parseInt(txtTongCong.getText().replaceAll("[^0-9]", ""));
            
            // Hiển thị hóa đơn chi tiết
            String invoice = "═══════════════════════════════════\n" +
                           "        HÓA ĐƠN THANH TOÁN        \n" +
                           "═══════════════════════════════════\n" +
                           "Khách hàng: " + tableModel.getValueAt(row, 1) + "\n" +
                           "Phòng: " + tableModel.getValueAt(row, 2) + "\n" +
                           "Check-in: " + tableModel.getValueAt(row, 3) + "\n" +
                           "Check-out: " + tableModel.getValueAt(row, 4) + "\n" +
                           "Số đêm: " + tableModel.getValueAt(row, 5) + "\n" +
                           "───────────────────────────────────\n" +
                           "Tiền phòng: " + txtTienPhong.getText() + "\n" +
                           "Tiền dịch vụ: " + txtTienDichVu.getText() + "\n" +
                           "Giảm giá: " + txtGiamGia.getText() + "\n" +
                           "═══════════════════════════════════\n" +
                           "TỔNG CỘNG: " + txtTongCong.getText() + "\n" +
                           "═══════════════════════════════════\n\n" +
                           "Xác nhận thanh toán và trả phòng?";
            
            int confirm = JOptionPane.showConfirmDialog(this,
                invoice,
                "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                int maDatPhong = (int) tableModel.getValueAt(row, 0);
                
                if (bookingController.checkout(maDatPhong, tongCong)) {
                    JOptionPane.showMessageDialog(this,
                        "✓ Thanh toán thành công!\n" +
                        "✓ Đã trả phòng.\n" +
                        "✓ Phòng đã được cập nhật về trạng thái có sẵn.\n\n" +
                        "Cảm ơn quý khách!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    loadData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearFields() {
        txtTienPhong.setText("");
        txtTienDichVu.setText("0");
        txtGiamGia.setText("0");
        txtTongCong.setText("");
        cmbDiscount.setSelectedIndex(0);
    }
}