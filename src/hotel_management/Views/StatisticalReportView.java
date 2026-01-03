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


public class StatisticalReportView extends JFrame {

    private BookingDAO bookingDAO;
    private ServiceUsedDAO serviceUsedDAO;
    private GuestDAO guestDAO;

    private JTable tableGuests, tableRooms, tableServices;
    private DefaultTableModel modelGuests, modelRooms, modelServices;
    private JLabel lblTotalRoom, lblTotalService, lblGrandTotal, lblSelectedGuest;
    private JTextField txtSearch;
    private JButton btnRefresh, btnExport;

    public StatisticalReportView() {
        this.bookingDAO = new BookingDAO();
        this.serviceUsedDAO = new ServiceUsedDAO();
        this.guestDAO = new GuestDAO();

        initComponents();
        loadGuests();
    }

    private void initComponents() {
        setTitle("QUẢN LÝ THỐNG KÊ KHÁCH HÀNG");
        setSize(1200, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("HỆ THỐNG TRUY XUẤT DOANH THU CHI TIẾT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLACK);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setForeground(Color.BLACK);
        lblSearch.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(lblSearch);
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Tìm");
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchGuests());
        searchPanel.add(btnSearch);
        
        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadGuests();
            clearDetails();
        });
        searchPanel.add(btnRefresh);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content ---
        // Top Panel: Guest List
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 5, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                "DANH SÁCH KHÁCH HÀNG - Click vào khách hàng để xem chi tiết",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
            )
        ));
        
        String[] guestColumns = {"Mã KH", "Họ", "Tên", "Số Điện Thoại", "Email", "CMND/CCCD", "Trạng Thái"};
        modelGuests = new DefaultTableModel(guestColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableGuests = createStyledTable(modelGuests);
        tableGuests.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableGuests.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableGuests.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableGuests.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableGuests.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableGuests.getColumnModel().getColumn(4).setPreferredWidth(180);
        tableGuests.getColumnModel().getColumn(5).setPreferredWidth(120);
        tableGuests.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Highlight selected row
        tableGuests.setSelectionBackground(new Color(52, 152, 219));
        tableGuests.setSelectionForeground(Color.BLACK);
        
        tableGuests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableGuests.getSelectedRow();
                if (row != -1) {
                    int guestId = (int) tableGuests.getValueAt(row, 0);
                    String ho = (String) tableGuests.getValueAt(row, 1);
                    String ten = (String) tableGuests.getValueAt(row, 2);
                    String name = ho + " " + ten;
                    showGuestDetails(guestId, name);
                }
            }
        });

        JScrollPane scrollGuests = new JScrollPane(tableGuests);
        scrollGuests.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        topPanel.add(scrollGuests, BorderLayout.CENTER);

        // Bottom Panel: Details with Tabs
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        lblSelectedGuest = new JLabel("📋 CHI TIẾT: (Vui lòng chọn khách hàng từ danh sách bên trên)");
        lblSelectedGuest.setFont(new Font("Arial", Font.BOLD, 15));
        lblSelectedGuest.setForeground(new Color(52, 73, 94));
        lblSelectedGuest.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        lblSelectedGuest.setOpaque(true);
        lblSelectedGuest.setBackground(new Color(236, 240, 241));
        
        bottomPanel.add(lblSelectedGuest, BorderLayout.NORTH);

        JTabbedPane tabDetail = new JTabbedPane();
        tabDetail.setFont(new Font("Arial", Font.BOLD, 13));
        
        // Tab 1: Room Bookings
        String[] roomColumns = {"Mã Đặt Phòng", "Ngày Nhận", "Ngày Trả", "Số Ngày", "Trạng Thái", "Tiền Phòng (VNĐ)"};
        modelRooms = new DefaultTableModel(roomColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRooms = createStyledTable(modelRooms);
        tableRooms.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableRooms.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableRooms.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableRooms.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableRooms.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableRooms.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        JScrollPane scrollRooms = new JScrollPane(tableRooms);
        tabDetail.addTab("🏨 Lịch Sử Thuê Phòng", scrollRooms);

        // Tab 2: Services Used
        String[] serviceColumns = {"Mã Đặt Phòng", "Tên Dịch Vụ", "Số Lượng", "Đơn Giá (VNĐ)", "Thành Tiền (VNĐ)", "Ngày Sử Dụng"};
        modelServices = new DefaultTableModel(serviceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableServices = createStyledTable(modelServices);
        tableServices.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableServices.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableServices.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableServices.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableServices.getColumnModel().getColumn(4).setPreferredWidth(130);
        tableServices.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        JScrollPane scrollServices = new JScrollPane(tableServices);
        tabDetail.addTab("🛎️ Dịch Vụ Đã Sử Dụng", scrollServices);

        bottomPanel.add(tabDetail, BorderLayout.CENTER);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(280);
        splitPane.setResizeWeight(0.35);
        add(splitPane, BorderLayout.CENTER);

        // --- Summary Panel ---
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(new Color(236, 240, 241));
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(41, 128, 185)),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));

        JPanel leftSummary = new JPanel(new GridLayout(2, 1, 5, 5));
        leftSummary.setOpaque(false);
        
        lblTotalRoom = new JLabel("💰 Tổng tiền phòng: 0 VNĐ");
        lblTotalRoom.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalRoom.setForeground(new Color(41, 128, 185));
        
        lblTotalService = new JLabel("🛎️ Tổng tiền dịch vụ: 0 VNĐ");
        lblTotalService.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalService.setForeground(new Color(155, 89, 182));
        
        leftSummary.add(lblTotalRoom);
        leftSummary.add(lblTotalService);

        lblGrandTotal = new JLabel("TỔNG CỘNG: 0 VNĐ");
        lblGrandTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblGrandTotal.setForeground(new Color(192, 57, 43));
        lblGrandTotal.setHorizontalAlignment(JLabel.RIGHT);

        summaryPanel.add(leftSummary, BorderLayout.WEST);
        summaryPanel.add(lblGrandTotal, BorderLayout.EAST);

        add(summaryPanel, BorderLayout.SOUTH);
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.setGridColor(new Color(189, 195, 199));
        table.setShowGrid(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        
        return table;
    }

    private void loadGuests() {
        modelGuests.setRowCount(0);
        List<Guest> list = guestDAO.getAllGuests();
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không có khách hàng nào trong hệ thống!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
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
        
        System.out.println("DEBUG: Loaded " + list.size() + " guests");
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
            String cmnd = g.getCmndCccdKhachHang() != null ? g.getCmndCccdKhachHang().toLowerCase() : "";
            
            if (fullName.contains(keyword) || phone.contains(keyword) || 
                email.contains(keyword) || cmnd.contains(keyword)) {
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
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy khách hàng phù hợp với từ khóa: " + keyword,
                "Kết quả tìm kiếm",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showGuestDetails(int guestId, String guestName) {
        System.out.println("DEBUG: Loading details for guest ID: " + guestId);

        lblSelectedGuest.setText("📋 CHI TIẾT KHÁCH HÀNG: " + guestName.toUpperCase());
        lblSelectedGuest.setBackground(new Color(46, 204, 113));
        lblSelectedGuest.setForeground(Color.WHITE);

        modelRooms.setRowCount(0);
        modelServices.setRowCount(0);

        long totalRoom = 0;
        long totalService = 0;
        int bookingCount = 0;
        int serviceCount = 0;

        // Lấy tất cả booking của khách
        List<Booking> bookings = bookingDAO.getAllBookings()
                .stream()
                .filter(b -> b.getMaKhachHang() == guestId)
                .collect(Collectors.toList());

        System.out.println("DEBUG: Found " + bookings.size() + " bookings for guest " + guestId);

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Khách hàng này chưa có booking nào!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }

        for (Booking b : bookings) {
            bookingCount++;

            // Tính số ngày
            long diff = b.getNgayTraPhong().getTime() - b.getNgayNhanPhong().getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24));

            // Thêm vào bảng phòng
            modelRooms.addRow(new Object[]{
                b.getMaDatPhong(),
                b.getNgayNhanPhong(),
                b.getNgayTraPhong(),
                days + " ngày",
                b.getTrangThai(),
                String.format("%,d", b.getSoTienDatPhong())
            });

            // Chỉ tính tiền nếu không bị hủy
            if (!"Cancelled".equalsIgnoreCase(b.getTrangThai())) {
                totalRoom += b.getSoTienDatPhong();
            }

            // Lấy dịch vụ của booking này
            List<ServiceUsed> services = serviceUsedDAO.getServicesByBookingId(b.getMaDatPhong());
            System.out.println("DEBUG: Found " + services.size() + " services for booking " + b.getMaDatPhong());

            for (ServiceUsed s : services) {
                serviceCount++;
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

        // Cập nhật tổng tiền
        lblTotalRoom.setText(String.format("💰 Tổng tiền phòng: %,d VNĐ (%d booking)", totalRoom, bookingCount));
        lblTotalService.setText(String.format("🛎️ Tổng tiền dịch vụ: %,d VNĐ (%d dịch vụ)", totalService, serviceCount));
        lblGrandTotal.setText(String.format("TỔNG CỘNG: %,d VNĐ", totalRoom + totalService));

        System.out.println("DEBUG: Total room = " + totalRoom + ", Total service = " + totalService);
    }

    private void clearDetails() {
        lblSelectedGuest.setText("📋 CHI TIẾT: (Vui lòng chọn khách hàng từ danh sách bên trên)");
        lblSelectedGuest.setBackground(new Color(236, 240, 241));
        lblSelectedGuest.setForeground(new Color(52, 73, 94));
        
        modelRooms.setRowCount(0);
        modelServices.setRowCount(0);
        
        lblTotalRoom.setText("💰 Tổng tiền phòng: 0 VNĐ");
        lblTotalService.setText("🛎️ Tổng tiền dịch vụ: 0 VNĐ");
        lblGrandTotal.setText("TỔNG CỘNG: 0 VNĐ");
        
        tableGuests.clearSelection();
    }

    // Method để test
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