package hotel_management.Views;

import hotel_management.Controllers.BookingController;
import hotel_management.Controllers.ServiceUsedController;
import hotel_management.Controllers.DiscountController;
import hotel_management.Models.Booking;
import hotel_management.Models.Discount;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

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
        getContentPane().setBackground(new Color(245, 247, 250));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /* ================= TABLE ================= */
        String[] columns = {
                "Mã", "Khách hàng", "Phòng",
                "Ngày nhận", "Ngày trả",
                "Số đêm", "Trạng thái"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(6).setCellRenderer(center);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                calculateAmount();
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(1200, 320));

        /* ================= PAYMENT PANEL ================= */
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                "CHI TIẾT THANH TOÁN",
                0, 0,
                new Font("Arial", Font.BOLD, 15),
                new Color(52, 73, 94)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font moneyFont = new Font("Consolas", Font.BOLD, 15);

        // Tiền phòng
        gbc.gridx = 0; gbc.gridy = 0;
        paymentPanel.add(new JLabel("Tiền phòng:"), gbc);
        gbc.gridx = 1;
        txtTienPhong = createMoneyField(moneyFont);
        paymentPanel.add(txtTienPhong, gbc);

        // Tiền dịch vụ
        gbc.gridx = 0; gbc.gridy = 1;
        paymentPanel.add(new JLabel("Tiền dịch vụ:"), gbc);
        gbc.gridx = 1;
        txtTienDichVu = createMoneyField(moneyFont);
        paymentPanel.add(txtTienDichVu, gbc);

        // Mã giảm giá
        gbc.gridx = 0; gbc.gridy = 2;
        paymentPanel.add(new JLabel("Mã giảm giá:"), gbc);
        gbc.gridx = 1;
        cmbDiscount = new JComboBox<>();
        cmbDiscount.setFont(new Font("Arial", Font.BOLD, 13));
        cmbDiscount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadDiscounts();
        cmbDiscount.addActionListener(e -> applyDiscount());
        paymentPanel.add(cmbDiscount, gbc);

        // Số tiền giảm
        gbc.gridx = 0; gbc.gridy = 3;
        paymentPanel.add(new JLabel("Số tiền giảm:"), gbc);
        gbc.gridx = 1;
        txtGiamGia = createMoneyField(moneyFont);
        txtGiamGia.setForeground(new Color(46, 204, 113));
        paymentPanel.add(txtGiamGia, gbc);

        // Tổng cộng
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblTong = new JLabel("TỔNG CỘNG:");
        lblTong.setFont(new Font("Arial", Font.BOLD, 18));
        lblTong.setForeground(new Color(231, 76, 60));
        paymentPanel.add(lblTong, gbc);

        gbc.gridx = 1;
        txtTongCong = createMoneyField(new Font("Consolas", Font.BOLD, 18));
        txtTongCong.setForeground(new Color(231, 76, 60));
        txtTongCong.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 2));
        paymentPanel.add(txtTongCong, gbc);

        /* ================= BUTTONS ================= */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnView = new JButton("Xem dịch vụ");
        JButton btnCalc = new JButton("Tính lại");
        JButton btnCheckout = new JButton("💳 THANH TOÁN & TRẢ PHÒNG");
        JButton btnRefresh = new JButton("Làm mới");

        styleButton(btnView, new Color(155, 89, 182));
        styleButton(btnCalc, new Color(52, 152, 219));
        styleButton(btnCheckout, new Color(46, 204, 113));
        styleButton(btnRefresh, new Color(149, 165, 166));

        btnView.addActionListener(e -> viewServiceDetails());
        btnCalc.addActionListener(e -> calculateAmount());
        btnCheckout.addActionListener(e -> checkout());
        btnRefresh.addActionListener(e -> loadData());

        buttonPanel.add(btnView);
        buttonPanel.add(btnCalc);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        paymentPanel.add(buttonPanel, gbc);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, paymentPanel);
        split.setDividerLocation(330);
        split.setBorder(null);

        add(split);
    }

    /* ================= HELPER UI ================= */

    private JTextField createMoneyField(Font f) {
        JTextField txt = new JTextField();
        txt.setEditable(false);
        txt.setFont(f);
        txt.setBackground(Color.WHITE);
        return txt;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /* ================= DATA ================= */

    private void loadDiscounts() {
        discounts = discountController.getAllDiscounts();
        cmbDiscount.addItem("Không giảm giá");
        for (Discount d : discounts) {
            cmbDiscount.addItem(d.getTenGiamGia() + " - " + d.getTyLeGiamGia() + "%");
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Booking> list = bookingController.getAllBookings()
                .stream()
                .filter(b -> b.getTrangThai().equals("Checkin"))
                .toList();

        for (Booking b : list) {
            tableModel.addRow(new Object[]{
                    b.getMaDatPhong(),
                    b.getTenKhachHang(),
                    "Phòng",
                    b.getNgayNhanPhong(),
                    b.getNgayTraPhong(),
                    b.getThoiGianLuuTru() + " đêm",
                    "Đã nhận phòng"
            });
        }
        clearFields();
    }

    private void calculateAmount() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int maDatPhong = (int) tableModel.getValueAt(row, 0);
        Booking booking = bookingController.getBookingById(maDatPhong);
        if (booking == null) return;

        int tienPhong = booking.getSoTienDatPhong();
        int tienDichVu = serviceUsedController.getTotalServiceCharge(maDatPhong);

        txtTienPhong.setText(String.format("%,d VNĐ", tienPhong));
        txtTienDichVu.setText(String.format("%,d VNĐ", tienDichVu));

        applyDiscount();
    }

    private void applyDiscount() {
        try {
            int tienPhong = Integer.parseInt(txtTienPhong.getText().replaceAll("[^0-9]", ""));
            int tienDichVu = Integer.parseInt(txtTienDichVu.getText().replaceAll("[^0-9]", ""));
            int tong = tienPhong + tienDichVu;

            int giam = 0;
            if (cmbDiscount.getSelectedIndex() > 0) {
                Discount d = discounts.get(cmbDiscount.getSelectedIndex() - 1);
                giam = tong * d.getTyLeGiamGia() / 100;
            }

            txtGiamGia.setText(String.format("-%,d VNĐ", giam));
            txtTongCong.setText(String.format("%,d VNĐ", tong - giam));

        } catch (Exception e) {
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
        new ServiceUsedView(maDatPhong,
                tableModel.getValueAt(row, 1).toString(),
                tableModel.getValueAt(row, 2).toString()).setVisible(true);
    }

    private void checkout() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int tong = Integer.parseInt(txtTongCong.getText().replaceAll("[^0-9]", ""));
        int maDatPhong = (int) tableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán và trả phòng?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.checkout(maDatPhong, tong)) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
            }
        }
    }

    private void clearFields() {
        txtTienPhong.setText("");
        txtTienDichVu.setText("0 VNĐ");
        txtGiamGia.setText("0 VNĐ");
        txtTongCong.setText("");
        cmbDiscount.setSelectedIndex(0);
    }
}
