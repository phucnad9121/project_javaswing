package hotel_management.Views;

import hotel_management.Controllers.ServiceController;
import hotel_management.Controllers.BookingController;
import hotel_management.Models.Guest;
import hotel_management.Models.Service;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author nguye
 */
public class ServiceHotelView extends JFrame {
    private ServiceController controller;
    private BookingController bookingController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblTotalAmount;
    private List<Integer> selectedServiceIds;
    private Guest currentGuest;
    private int maDatPhong;
    
    public ServiceHotelView(Guest guest, int maDatPhong) {
        this.currentGuest = guest;
        this.maDatPhong = maDatPhong;
        this.selectedServiceIds = new ArrayList<>();
        controller = new ServiceController();
        bookingController = new BookingController();
        
        // Auto-fetch booking ID if not provided
        if (maDatPhong <= 0 && guest != null) {
            this.maDatPhong = bookingController.getLatestBookingId(guest.getMaKhachHang());
            System.out.println("Auto-fetched maDatPhong: " + this.maDatPhong);
        }
        
        initComponents();
        loadData();
    }
    
    public ServiceHotelView(Guest guest) {
        this(guest, -1);
    }
    
    private void initComponents() {
        setTitle("Dịch vụ Khách sạn - Đặt dịch vụ");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("DỊCH VỤ KHÁCH SẠN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> searchService());
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        String guestInfo = currentGuest != null ? 
            "Khách hàng: " + currentGuest.getTenKhachHang() + " (Mã: " + currentGuest.getMaKhachHang() + ")" : 
            "Khách hàng: Chưa xác định";
        String bookingInfo = maDatPhong > 0 ? 
            " | <font color='green'>✓ Đặt phòng: " + maDatPhong + "</font>" : 
            " | <font color='red'>✗ Chưa đặt phòng</font>";
        
        JLabel lblInfo = new JLabel("<html><b>Hướng dẫn:</b> Chọn dịch vụ và nhấn 'Đặt dịch vụ'.<br/>" +
                                    "<b>" + guestInfo + bookingInfo + "</b></html>");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblInfo, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Chọn", "Mã DV", "Tên dịch vụ", "Chi phí", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(400);
        
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 0) calculateTotal();
        });
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        infoPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setOpaque(false);
        JLabel lblTotalText = new JLabel("Tổng chi phí dự kiến:");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(lblTotalText);
        
        lblTotalAmount = new JLabel("0 VNĐ");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalAmount.setForeground(new Color(231, 76, 60));
        totalPanel.add(lblTotalAmount);
        bottomPanel.add(totalPanel, BorderLayout.WEST);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton btnCalculate = new JButton("Tính tổng");
        btnCalculate.setPreferredSize(new Dimension(120, 35));
        btnCalculate.setBackground(new Color(52, 152, 219));
        btnCalculate.setForeground(Color.BLACK);
        btnCalculate.setFocusPainted(false);
        btnCalculate.addActionListener(e -> calculateTotal());
        buttonPanel.add(btnCalculate);
        
        JButton btnOrder = new JButton("Đặt dịch vụ");
        btnOrder.setPreferredSize(new Dimension(140, 35));
        btnOrder.setBackground(new Color(46, 204, 113));
        btnOrder.setForeground(Color.BLACK);
        btnOrder.setFont(new Font("Arial", Font.BOLD, 14));
        btnOrder.setFocusPainted(false);
        btnOrder.addActionListener(e -> orderServices());
        buttonPanel.add(btnOrder);
        
        JButton btnClear = new JButton("Xóa chọn");
        btnClear.setPreferredSize(new Dimension(120, 35));
        btnClear.setBackground(new Color(149, 165, 166));
        btnClear.setForeground(Color.BLACK);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> clearSelection());
        buttonPanel.add(btnClear);
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setPreferredSize(new Dimension(120, 35));
        btnRefresh.setBackground(new Color(52, 73, 94));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadData());
        buttonPanel.add(btnRefresh);
        
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Service> list = controller.getAllServices();
        for (Service service : list) {
            tableModel.addRow(new Object[]{
                Boolean.FALSE,
                service.getMaDichVu(),
                service.getTenDichVu(),
                String.format("%,d VNĐ", service.getChiPhiDichVu()),
                service.getMoTaDichVu()
            });
        }
        calculateTotal();
    }
    
    private void searchService() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Service> list = controller.getAllServices();
        for (Service service : list) {
            if (service.getTenDichVu().toLowerCase().contains(keyword) ||
                service.getMoTaDichVu().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    Boolean.FALSE,
                    service.getMaDichVu(),
                    service.getTenDichVu(),
                    String.format("%,d VNĐ", service.getChiPhiDichVu()),
                    service.getMoTaDichVu()
                });
            }
        }
    }
    
    private void calculateTotal() {
        int total = 0;
        List<Service> allServices = controller.getAllServices();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                int maDichVu = (int) tableModel.getValueAt(i, 1);
                for (Service service : allServices) {
                    if (service.getMaDichVu() == maDichVu) {
                        total += service.getChiPhiDichVu();
                        break;
                    }
                }
            }
        }
        
        lblTotalAmount.setText(String.format("%,d VNĐ", total));
    }
    
    private void orderServices() {
        // Auto-retry fetching booking ID if invalid
        if (maDatPhong <= 0 && currentGuest != null) {
            maDatPhong = bookingController.getLatestBookingId(currentGuest.getMaKhachHang());
        }
        
        if (maDatPhong <= 0) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy mã đặt phòng!\n" +
                "Vui lòng đặt phòng trước khi sử dụng dịch vụ.",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        selectedServiceIds.clear();
        StringBuilder selectedServices = new StringBuilder();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                int maDichVu = (int) tableModel.getValueAt(i, 1);
                String tenDichVu = (String) tableModel.getValueAt(i, 2);
                selectedServiceIds.add(maDichVu);
                selectedServices.append("- ").append(tenDichVu).append("\n");
            }
        }
        
        if (selectedServiceIds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn ít nhất một dịch vụ!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn đã chọn các dịch vụ sau:\n\n" + selectedServices.toString() + 
            "\nTổng chi phí: " + lblTotalAmount.getText() + 
            "\nMã đặt phòng: " + maDatPhong +
            "\n\nXác nhận đặt dịch vụ?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = controller.orderMultipleServices(maDatPhong, selectedServiceIds);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Đặt dịch vụ thành công!\n" +
                        "Mã đặt phòng: " + maDatPhong + "\n" +
                        "Số dịch vụ: " + selectedServiceIds.size() + "\n" +
                        "Tổng chi phí: " + lblTotalAmount.getText(),
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Có lỗi xảy ra khi đặt dịch vụ!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearSelection() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(Boolean.FALSE, i, 0);
        }
        table.clearSelection();
        calculateTotal();
    }
    
    public List<Integer> getSelectedServiceIds() {
        return selectedServiceIds;
    }
    
    public void setMaDatPhong(int maDatPhong) {
        this.maDatPhong = maDatPhong;
    }
    
    public int getMaDatPhong() {
        return this.maDatPhong;
    }
}