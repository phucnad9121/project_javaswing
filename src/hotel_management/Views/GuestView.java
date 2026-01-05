package hotel_management.Views;

import hotel_management.Controllers.GuestController;
import hotel_management.Models.Guest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class GuestView extends JFrame {
    private GuestController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Vẫn giữ 1 ô nhập liệu duy nhất cho Họ Tên
    private JTextField txtHoTen, txtSDT, txtCMND, txtEmail, txtDiaChi;
    private JTextField txtSearch;

    public GuestView() {
        controller = new GuestController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Khách hàng");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10)); 
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));
        
        inputPanel.add(new JLabel("Họ và Tên:"));
        txtHoTen = new JTextField(); inputPanel.add(txtHoTen);
        
        inputPanel.add(new JLabel("SĐT:"));
        txtSDT = new JTextField(); inputPanel.add(txtSDT);
        
        inputPanel.add(new JLabel("CMND/CCCD:"));
        txtCMND = new JTextField(); inputPanel.add(txtCMND);
        
        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(); inputPanel.add(txtEmail);
        
        inputPanel.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField(); inputPanel.add(txtDiaChi);
        
        inputPanel.add(new JLabel("")); inputPanel.add(new JLabel("")); 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        btnUpdate.addActionListener(e -> updateGuest());
        btnDelete.addActionListener(e -> deleteGuest());
        
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(inputPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolsPanel.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        toolsPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Tìm");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnExport = new JButton("Xuất Excel");
        
        btnSearch.addActionListener(e -> searchGuest());   
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
            clearFields();
        });
        btnExport.addActionListener(e -> exportToExcel());
        
        toolsPanel.add(btnSearch);
        toolsPanel.add(btnRefresh);
        toolsPanel.add(btnExport);

        // 3. BẢNG DỮ LIỆU 
        String[] columns = {"Mã KH", "Họ và Tên", "SĐT", "CMND/CCCD", "Email", "Địa chỉ", "Trạng thái", "Ngày đăng ký"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // gõ vào bảng hiển thi dlieu
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtHoTen.setText(tableModel.getValueAt(row, 1).toString()); // Lấy cả họ tên
                txtSDT.setText(tableModel.getValueAt(row, 2).toString());
                txtCMND.setText(tableModel.getValueAt(row, 3).toString());
                
                Object email = tableModel.getValueAt(row, 4);
                txtEmail.setText(email != null ? email.toString() : "");
                
                Object diaChi = tableModel.getValueAt(row, 5);
                txtDiaChi.setText(diaChi != null ? diaChi.toString() : "");
            }
        });
        


        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(toolsPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    // CÁC HÀM LOGIC

    private void loadData() {
        tableModel.setRowCount(0);
        List<Guest> list = controller.getAllGuests();
        for (Guest g : list) {
            String trangThaiDisplay = g.getTrangThai().equals("Reserved") ? "Đang đặt phòng" : "Không đặt phòng";
            
            String fullName = g.getHoKhachHang() + " " + g.getTenKhachHang();

            tableModel.addRow(new Object[]{
                g.getMaKhachHang(), 
                fullName, // Hiển thị chuỗi đã gộp
                g.getSoDienThoaiKhachHang(), 
                g.getCmndCccdKhachHang(),
                g.getEmailKhachHang(), 
                g.getDiaChi(),
                trangThaiDisplay, 
                g.getNgayTao()
            });
        }
    }
    
    private void updateGuest() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        
        try {
            int maKH = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String fullInput = txtHoTen.getText().trim();
            
            // --- LOGIC TÁCH HỌ TÊN ---
            String ho = "";
            String ten = "";
            
            // Tìm khoảng trắng cuối cùng để tách tên
            int lastSpaceIndex = fullInput.lastIndexOf(" ");
            if (lastSpaceIndex != -1) {
                ho = fullInput.substring(0, lastSpaceIndex); // Phần trước khoảng trắng cuối là Họ đệm
                ten = fullInput.substring(lastSpaceIndex + 1); // Phần sau là Tên
            } else {
                ten = fullInput; // Nếu không có khoảng trắng, coi như chỉ có Tên
            }

            if (controller.updateGuest(maKH, ho, ten, // Truyền 2 tham số riêng biệt
                                      txtEmail.getText(), txtSDT.getText(), 
                                      txtCMND.getText(), txtDiaChi.getText())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteGuest() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        
        int maKH = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String hoTen = txtHoTen.getText();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xác nhận XÓA khách hàng: " + hoTen + "?", 
            "Cảnh báo xóa", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteGuest(maKH)) {
                JOptionPane.showMessageDialog(this, "Đã xóa khách hàng!");
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa (Do ràng buộc dữ liệu Booking)!");
            }
        }
    }
    
    private void searchGuest() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        List<Guest> list = controller.getAllGuests();
        for (Guest g : list) {
            // Gộp để tìm kiếm chính xác
            String fullName = (g.getHoKhachHang() + " " + g.getTenKhachHang()).toLowerCase();
            
            if (g.getSoDienThoaiKhachHang().contains(keyword) || 
                g.getCmndCccdKhachHang().contains(keyword) ||
                fullName.contains(keyword)) { 
                
                String trangThaiDisplay = g.getTrangThai().equals("Reserved") ? "Đang đặt phòng" : "Không đặt phòng";
                tableModel.addRow(new Object[]{
                    g.getMaKhachHang(), 
                    g.getHoKhachHang() + " " + g.getTenKhachHang(), // Hiển thị gộp
                    g.getSoDienThoaiKhachHang(), g.getCmndCccdKhachHang(),
                    g.getEmailKhachHang(), g.getDiaChi(),
                    trangThaiDisplay, g.getNgayTao()
                });
            }
        }
    }
    
    private void clearFields() {
        txtHoTen.setText(""); 
        txtSDT.setText(""); txtCMND.setText("");
        txtEmail.setText(""); txtDiaChi.setText("");
        table.clearSelection();
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu danh sách");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) filePath += ".csv";

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                    new java.io.OutputStreamWriter(new java.io.FileOutputStream(filePath), "UTF-8"))) {

                bw.write("\ufeff"); 
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    bw.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object cellValue = tableModel.getValueAt(i, j);
                        String value = "";

                        if (cellValue != null) {
                            if (j == 7 && (cellValue instanceof java.sql.Timestamp || cellValue instanceof java.util.Date)) {
                                try { value = sdf.format(cellValue); } catch (Exception e) { value = cellValue.toString(); }
                            } else {
                                value = cellValue.toString();
                            }
                            if (j == 2 || j == 3) {
                                value = "'" + value;
                            }
                        }
                        value = value.replace(",", ";").replace("\n", " ");
                        bw.write(value);
                        if (j < tableModel.getColumnCount() - 1) bw.write(",");
                    }
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(this, "Xuất file thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }
}