/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.ServiceUsedController;
import hotel_management.Controllers.ServiceController;
import hotel_management.Models.ServiceUsed;
import hotel_management.Models.Service;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServiceUsedView extends JFrame {
    private ServiceUsedController serviceUsedController;
    private ServiceController serviceController;
    private int maDatPhong;
    private String guestName;
    private String roomNumber;
    
    private JTable serviceTable;
    private DefaultTableModel serviceTableModel;
    private JTable usedServiceTable;
    private DefaultTableModel usedServiceTableModel;
    
    private JComboBox<String> cmbService;
    private JSpinner spinnerQuantity;
    private JLabel lblTotalServiceCharge;
    
    private List<Service> availableServices;
    
    public ServiceUsedView(int maDatPhong, String guestName, String roomNumber) {
        this.maDatPhong = maDatPhong;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.serviceUsedController = new ServiceUsedController();
        this.serviceController = new ServiceController();
        
        initComponents();
        loadAvailableServices();
        loadUsedServices();
    }
    
    private void initComponents() {
        setTitle("Quản lý Dịch vụ - Booking #" + maDatPhong);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Booking"));
        infoPanel.add(new JLabel("Khách hàng: " + guestName));
        infoPanel.add(new JLabel("Phòng: " + roomNumber));
        lblTotalServiceCharge = new JLabel("Tổng tiền dịch vụ: 0 VNĐ");
        lblTotalServiceCharge.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalServiceCharge.setForeground(new Color(231, 76, 60));
        infoPanel.add(lblTotalServiceCharge);
        
        // Available Services Panel
        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách Dịch vụ"));
        
        String[] serviceColumns = {"Mã", "Tên dịch vụ", "Đơn giá", "Mô tả"};
        serviceTableModel = new DefaultTableModel(serviceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        serviceTable = new JTable(serviceTableModel);
        availablePanel.add(new JScrollPane(serviceTable), BorderLayout.CENTER);
        
        // Add Service Panel
        JPanel addServicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addServicePanel.add(new JLabel("Chọn dịch vụ:"));
        cmbService = new JComboBox<>();
        addServicePanel.add(cmbService);
        
        addServicePanel.add(new JLabel("Số lượng:"));
        spinnerQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerQuantity.setPreferredSize(new Dimension(60, 25));
        addServicePanel.add(spinnerQuantity);
        
        JButton btnAdd = new JButton("Thêm dịch vụ");
        
        btnAdd.setForeground(Color.BLACK);
        btnAdd.addActionListener(e -> addService());
        addServicePanel.add(btnAdd);
        
        availablePanel.add(addServicePanel, BorderLayout.SOUTH);
        
        // Used Services Panel
        JPanel usedPanel = new JPanel(new BorderLayout());
        usedPanel.setBorder(BorderFactory.createTitledBorder("Dịch vụ đã sử dụng"));
        
        String[] usedColumns = {"Mã", "Tên dịch vụ", "Số lượng", "Đơn giá", "Thành tiền", "Thời gian"};
        usedServiceTableModel = new DefaultTableModel(usedColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usedServiceTable = new JTable(usedServiceTableModel);
        usedPanel.add(new JScrollPane(usedServiceTable), BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnUpdateQuantity = new JButton("Cập nhật số lượng");
        JButton btnDelete = new JButton("Xóa dịch vụ");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnClose = new JButton("Đóng");
        
        btnUpdateQuantity.addActionListener(e -> updateQuantity());
        btnDelete.addActionListener(e -> deleteService());
        btnRefresh.addActionListener(e -> {
            loadUsedServices();
            updateTotalCharge();
        });
        btnClose.addActionListener(e -> dispose());
        
        buttonPanel.add(btnUpdateQuantity);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);
        
        usedPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
            availablePanel, usedPanel);
        splitPane.setDividerLocation(250);
        
        // Layout
        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void loadAvailableServices() {
        serviceTableModel.setRowCount(0);
        availableServices = serviceController.getAllServices();
        
        cmbService.removeAllItems();
        for (Service service : availableServices) {
            serviceTableModel.addRow(new Object[]{
                service.getMaDichVu(),
                service.getTenDichVu(),
                String.format("%,d VNĐ", service.getChiPhiDichVu()),
                service.getMoTaDichVu()
            });
            
            cmbService.addItem(service.getTenDichVu() + " - " + 
                             String.format("%,d VNĐ", service.getChiPhiDichVu()));
        }
    }
    
    private void loadUsedServices() {
        usedServiceTableModel.setRowCount(0);
        List<ServiceUsed> usedServices = serviceUsedController.getServicesByBookingId(maDatPhong);
        
        for (ServiceUsed su : usedServices) {
            usedServiceTableModel.addRow(new Object[]{
                su.getMaDichVuSuDung(),
                su.getTenDichVu(),
                su.getSoLuong(),
                String.format("%,d", su.getDonGia()),
                String.format("%,d", su.getThanhTien()),
                su.getNgaySuDung()
            });
        }
        
        updateTotalCharge();
    }
    
    private void updateTotalCharge() {
        int total = serviceUsedController.getTotalServiceCharge(maDatPhong);
        lblTotalServiceCharge.setText(String.format("Tổng tiền dịch vụ: %,d VNĐ", total));
    }
    
    private void addService() {
        int selectedIndex = cmbService.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ!");
            return;
        }
        
        Service selectedService = availableServices.get(selectedIndex);
        int quantity = (int) spinnerQuantity.getValue();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Thêm dịch vụ: " + selectedService.getTenDichVu() + "\n" +
            "Số lượng: " + quantity + "\n" +
            "Đơn giá: " + String.format("%,d VNĐ", selectedService.getChiPhiDichVu()) + "\n" +
            "Thành tiền: " + String.format("%,d VNĐ", selectedService.getChiPhiDichVu() * quantity),
            "Xác nhận thêm dịch vụ",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (serviceUsedController.addServiceToBooking(
                maDatPhong, 
                selectedService.getMaDichVu(), 
                quantity, 
                selectedService.getChiPhiDichVu())) {
                
                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
                loadUsedServices();
                spinnerQuantity.setValue(1);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thất bại!");
            }
        }
    }
    
    private void updateQuantity() {
        int row = usedServiceTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần cập nhật!");
            return;
        }
        
        int maDichVuSuDung = (int) usedServiceTableModel.getValueAt(row, 0);
        String serviceName = usedServiceTableModel.getValueAt(row, 1).toString();
        int currentQuantity = (int) usedServiceTableModel.getValueAt(row, 2);
        
        String input = JOptionPane.showInputDialog(this, 
            "Nhập số lượng mới cho dịch vụ: " + serviceName,
            currentQuantity);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(input.trim());
                if (newQuantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                    return;
                }
                
                if (serviceUsedController.updateServiceQuantity(maDichVuSuDung, newQuantity)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadUsedServices();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            }
        }
    }
    
    private void deleteService() {
        int row = usedServiceTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa!");
            return;
        }
        
        int maDichVuSuDung = (int) usedServiceTableModel.getValueAt(row, 0);
        String serviceName = usedServiceTableModel.getValueAt(row, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa dịch vụ: " + serviceName + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (serviceUsedController.deleteServiceFromBooking(maDichVuSuDung)) {
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
                loadUsedServices();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thất bại!");
            }
        }
    }
}