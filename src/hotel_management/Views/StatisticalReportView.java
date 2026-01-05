package hotel_management.Views;

import hotel_management.DAO.BookingDAO;
import hotel_management.DAO.GuestDAO;
import hotel_management.DAO.ServiceUsedDAO;
import hotel_management.Models.Booking;
import hotel_management.Models.Guest;
import hotel_management.Models.ServiceUsed;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

public class StatisticalReportView extends JFrame {
    private BookingDAO bookingDAO;
    private ServiceUsedDAO serviceUsedDAO;
    private GuestDAO guestDAO;
    
    private JTable tableGuests, tableRooms, tableServices;
    private DefaultTableModel modelGuests, modelRooms, modelServices;
    private JLabel lblTotalRoom, lblTotalService, lblGrandTotal, lblSelectedGuest;
    private JTextField txtSearch;
    
    private int currentGuestId = -1;
    private String currentGuestName = "";

    public StatisticalReportView() {
        this.bookingDAO = new BookingDAO();
        this.serviceUsedDAO = new ServiceUsedDAO();
        this.guestDAO = new GuestDAO();
        initComponents();
        loadGuests();
    }

    private void initComponents() {
        setTitle("Thống kê Khách hàng");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU KHÁCH HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnExport = new JButton("Xuất Excel");
        
        btnSearch.addActionListener(e -> searchGuests());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadGuests();
            clearDetails();
        });
        btnExport.addActionListener(e -> exportToExcel());
        
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnExport);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Guest List Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Danh sách Khách hàng - Click để xem chi tiết"));
        
        String[] guestColumns = {"Mã KH", "Họ", "Tên", "SĐT", "Email", "CMND/CCCD", "Trạng thái"};
        modelGuests = new DefaultTableModel(guestColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableGuests = new JTable(modelGuests);
        tableGuests.setRowHeight(28);
        tableGuests.getTableHeader().setReorderingAllowed(false);
        tableGuests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableGuests.getSelectedRow();
                if (row != -1) {
                    int guestId = (int) tableGuests.getValueAt(row, 0);
                    String ho = (String) tableGuests.getValueAt(row, 1);
                    String ten = (String) tableGuests.getValueAt(row, 2);
                    showGuestDetails(guestId, ho + " " + ten);
                }
            }
        });
        
        topPanel.add(new JScrollPane(tableGuests), BorderLayout.CENTER);

        // Details Panel with Tabs
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblSelectedGuest = new JLabel("CHI TIẾT: Chọn khách hàng để xem");
        lblSelectedGuest.setFont(new Font("Arial", Font.BOLD, 14));
        lblSelectedGuest.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(lblSelectedGuest, BorderLayout.NORTH);

        JTabbedPane tabDetail = new JTabbedPane();
        
        // Tab Rooms
        String[] roomColumns = {"Mã Đặt", "Ngày Nhận", "Ngày Trả", "Số Ngày", "Trạng Thái", "Tiền (VNĐ)"};
        modelRooms = new DefaultTableModel(roomColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRooms = new JTable(modelRooms);
        tableRooms.setRowHeight(28);
        tableRooms.getTableHeader().setReorderingAllowed(false);
        tabDetail.addTab("Lịch sử Phòng", new JScrollPane(tableRooms));

        // Tab Services
        String[] serviceColumns = {"Mã Đặt", "Dịch Vụ", "SL", "Đơn Giá", "Thành Tiền", "Ngày SD"};
        modelServices = new DefaultTableModel(serviceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableServices = new JTable(modelServices);
        tableServices.setRowHeight(28);
        tableServices.getTableHeader().setReorderingAllowed(false);
        tabDetail.addTab("Dịch vụ", new JScrollPane(tableServices));

        bottomPanel.add(tabDetail, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotalRoom = new JLabel("Tổng tiền phòng: 0 VNĐ");
        lblTotalRoom.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblTotalService = new JLabel("Tổng tiền dịch vụ: 0 VNĐ");
        lblTotalService.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblGrandTotal = new JLabel("TỔNG: 0 VNĐ");
        lblGrandTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblGrandTotal.setForeground(Color.RED);
        lblGrandTotal.setHorizontalAlignment(JLabel.RIGHT);
        
        summaryPanel.add(lblTotalRoom);
        summaryPanel.add(lblTotalService);
        summaryPanel.add(lblGrandTotal);
        
        add(summaryPanel, BorderLayout.SOUTH);
    }

    private void loadGuests() {
        modelGuests.setRowCount(0);
        List<Guest> list = guestDAO.getAllGuests();
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có khách hàng nào!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Guest g : list) {
            modelGuests.addRow(new Object[]{
                g.getMaKhachHang(),
                g.getHoKhachHang(),
                g.getTenKhachHang(),
                g.getSoDienThoaiKhachHang(),
                g.getEmailKhachHang(),
                g.getCmndCccdKhachHang(),
                g.getTrangThai()
            });
        }
    }

    private void searchGuests() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadGuests();
            return;
        }
        
        modelGuests.setRowCount(0);
        List<Guest> list = guestDAO.getAllGuests();
        int count = 0;
        
        for (Guest g : list) {
            String fullName = (g.getHoKhachHang() + " " + g.getTenKhachHang()).toLowerCase();
            String phone = g.getSoDienThoaiKhachHang() != null ? g.getSoDienThoaiKhachHang().toLowerCase() : "";
            String email = g.getEmailKhachHang() != null ? g.getEmailKhachHang().toLowerCase() : "";
            
            if (fullName.contains(keyword) || phone.contains(keyword) || email.contains(keyword)) {
                modelGuests.addRow(new Object[]{
                    g.getMaKhachHang(),
                    g.getHoKhachHang(),
                    g.getTenKhachHang(),
                    g.getSoDienThoaiKhachHang(),
                    g.getEmailKhachHang(),
                    g.getCmndCccdKhachHang(),
                    g.getTrangThai()
                });
                count++;
            }
        }
        
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy: " + keyword, 
                "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showGuestDetails(int guestId, String guestName) {
        currentGuestId = guestId;
        currentGuestName = guestName;
        
        lblSelectedGuest.setText("CHI TIẾT: " + guestName.toUpperCase());
        
        modelRooms.setRowCount(0);
        modelServices.setRowCount(0);
        
        long totalRoom = 0;
        long totalService = 0;

        List<Booking> bookings = bookingDAO.getAllBookings()
                .stream()
                .filter(b -> b.getMaKhachHang() == guestId)
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Khách hàng chưa có booking!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        for (Booking b : bookings) {
            long diff = b.getNgayTraPhong().getTime() - b.getNgayNhanPhong().getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24));

            modelRooms.addRow(new Object[]{
                b.getMaDatPhong(),
                b.getNgayNhanPhong(),
                b.getNgayTraPhong(),
                days + " ngày",
                b.getTrangThai(),
                String.format("%,d", b.getSoTienDatPhong())
            });

            if (!"Cancelled".equalsIgnoreCase(b.getTrangThai())) {
                totalRoom += b.getSoTienDatPhong();
            }

            List<ServiceUsed> services = serviceUsedDAO.getServicesByBookingId(b.getMaDatPhong());
            for (ServiceUsed s : services) {
                modelServices.addRow(new Object[]{
                    b.getMaDatPhong(),
                    s.getTenDichVu(),
                    s.getSoLuong(),
                    String.format("%,d", s.getDonGia()),
                    String.format("%,d", s.getThanhTien()),
                    s.getNgaySuDung()
                });
                totalService += s.getThanhTien();
            }
        }

        lblTotalRoom.setText(String.format("Tổng tiền phòng: %,d VNĐ", totalRoom));
        lblTotalService.setText(String.format("Tổng tiền dịch vụ: %,d VNĐ", totalService));
        lblGrandTotal.setText(String.format("TỔNG: %,d VNĐ", totalRoom + totalService));
    }

    private void clearDetails() {
        currentGuestId = -1;
        currentGuestName = "";
        
        lblSelectedGuest.setText("CHI TIẾT: Chọn khách hàng để xem");
        modelRooms.setRowCount(0);
        modelServices.setRowCount(0);
        
        lblTotalRoom.setText("Tổng tiền phòng: 0 VNĐ");
        lblTotalService.setText("Tổng tiền dịch vụ: 0 VNĐ");
        lblGrandTotal.setText("TỔNG: 0 VNĐ");
        
        tableGuests.clearSelection();
    }

    private void exportToExcel() {
        if (currentGuestId == -1) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Chưa chọn khách hàng. Xuất danh sách TẤT CẢ?",
                "Xuất Excel", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                exportAllGuests();
            }
            return;
        }
        exportGuestDetail();
    }
    
    private void exportAllGuests() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu danh sách khách hàng");
        
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fc.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".csv")) filePath += ".csv";

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                    new java.io.OutputStreamWriter(
                    new java.io.FileOutputStream(filePath), "UTF-8"))) {

                bw.write("\ufeff");
                bw.write("DANH SÁCH KHÁCH HÀNG\n");
                bw.write("Ngày xuất:," + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) + "\n\n");

                // Header
                for (int i = 0; i < modelGuests.getColumnCount(); i++) {
                    bw.write(modelGuests.getColumnName(i));
                    if (i < modelGuests.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();

                // Data
                for (int i = 0; i < modelGuests.getRowCount(); i++) {
                    for (int j = 0; j < modelGuests.getColumnCount(); j++) {
                        Object val = modelGuests.getValueAt(i, j);
                        String str = val != null ? val.toString() : "";
                        if (j == 3 || j == 5) str = "'" + str; // SĐT và CMND
                        bw.write(str.replace(",", ";"));
                        if (j < modelGuests.getColumnCount() - 1) bw.write(",");
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(this, "Xuất file thành công!\n" + filePath);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportGuestDetail() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Lưu báo cáo chi tiết");
        fc.setSelectedFile(new java.io.File("BaoCao_" + currentGuestName.replace(" ", "_") + ".csv"));
        
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fc.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".csv")) filePath += ".csv";

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                    new java.io.OutputStreamWriter(
                    new java.io.FileOutputStream(filePath), "UTF-8"))) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                bw.write("\ufeff");
                bw.write("BÁO CÁO CHI TIẾT KHÁCH HÀNG\n");
                bw.write("Tên:," + currentGuestName + "\n");
                bw.write("Mã:," + currentGuestId + "\n");
                
                int row = tableGuests.getSelectedRow();
                if (row != -1) {
                    bw.write("SĐT:,'" + modelGuests.getValueAt(row, 3) + "\n");
                    bw.write("Email:," + modelGuests.getValueAt(row, 4) + "\n");
                }
                
                bw.write("Ngày xuất:," + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) + "\n\n");

                // Phòng
                bw.write("LỊCH SỬ PHÒNG\n");
                writeTableData(bw, modelRooms, sdf);
                
                bw.write("\nDỊCH VỤ\n");
                writeTableData(bw, modelServices, sdf);
                
                // Tổng kết
                bw.write("\n" + lblTotalRoom.getText() + "\n");
                bw.write(lblTotalService.getText() + "\n");
                bw.write(lblGrandTotal.getText() + "\n");

                JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\n" + filePath);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void writeTableData(java.io.BufferedWriter bw, DefaultTableModel model, SimpleDateFormat sdf) throws Exception {
        // Header
        for (int i = 0; i < model.getColumnCount(); i++) {
            bw.write(model.getColumnName(i));
            if (i < model.getColumnCount() - 1) bw.write(",");
        }
        bw.newLine();
        
        // Data
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object val = model.getValueAt(i, j);
                String str = "";
                
                if (val instanceof java.sql.Timestamp || val instanceof java.util.Date) {
                    str = sdf.format(val);
                } else {
                    str = val != null ? val.toString() : "";
                }
                
                bw.write(str.replace(",", ";"));
                if (j < model.getColumnCount() - 1) bw.write(",");
            }
            bw.newLine();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StatisticalReportView().setVisible(true);
        });
    }
}