package hotel_management.Views;

import hotel_management.Controllers.DepartmentController;
import hotel_management.Models.Department;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class DepartmentView extends JFrame {
    private DepartmentController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenBoPhan, txtMoTa, txtLuong, txtChucDanh;
    
    // Khai báo bảng màu Pastel chuyên nghiệp
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_BTN_ADD = new Color(184, 255, 206); // Xanh lá nhạt
    private final Color COLOR_BTN_EDIT = new Color(184, 229, 255); // Xanh dương nhạt
    private final Color COLOR_BTN_DELETE = new Color(255, 184, 184); // Đỏ nhạt
    private final Color COLOR_BTN_REFRESH = new Color(230, 230, 230); // Xám nhạt

    public DepartmentView() {
        controller = new DepartmentController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("HỆ THỐNG QUẢN LÝ BỘ PHẬN");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BG);

        // --- TIÊU ĐỀ ---
        JLabel lblHeader = new JLabel("QUẢN LÝ DANH MỤC BỘ PHẬN & PHÒNG BAN", JLabel.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(44, 62, 80));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // --- PANEL TRÁI: FORM NHẬP LIỆU ---
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Thông tin chi tiết"),
            new EmptyBorder(20, 15, 20, 15)
        ));

        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        
        // Các trường nhập liệu được đóng gói vào các Panel nhỏ để có Label bên trên
        txtTenBoPhan = createInputGroup(formPanel, "Tên bộ phận:", labelFont);
        txtMoTa = createInputGroup(formPanel, "Mô tả công việc:", labelFont);
        txtLuong = createInputGroup(formPanel, "Lương khởi điểm (VNĐ):", labelFont);
        txtChucDanh = createInputGroup(formPanel, "Chức danh mặc định:", labelFont);

        leftPanel.add(formPanel, BorderLayout.CENTER);

        // --- PANEL NÚT BẤM TÁC VỤ ---
        JPanel buttonGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonGrid.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Thêm mới", COLOR_BTN_ADD);
        JButton btnUpdate = createStyledButton("Cập nhật", COLOR_BTN_EDIT);
        JButton btnDelete = createStyledButton("Xóa bỏ", COLOR_BTN_DELETE);
        JButton btnRefresh = createStyledButton("Làm mới", COLOR_BTN_REFRESH);
        
        btnAdd.addActionListener(e -> addDepartment());
        btnUpdate.addActionListener(e -> updateDepartment());
        btnDelete.addActionListener(e -> deleteDepartment());
        btnRefresh.addActionListener(e -> { clearFields(); loadData(); });
        
        buttonGrid.add(btnAdd);
        buttonGrid.add(btnUpdate);
        buttonGrid.add(btnDelete);
        buttonGrid.add(btnRefresh);
        leftPanel.add(buttonGrid, BorderLayout.SOUTH);

        // --- PANEL PHẢI: BẢNG DANH SÁCH ---
        String[] columns = {"Mã", "Tên bộ phận", "Mô tả", "Lương khởi điểm", "Chức danh"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        
        // Định dạng bảng
        table.setForeground(Color.BLACK); // Chữ trong bảng màu đen
        table.setRowHeight(35);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(212, 230, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        // Renderer để đảm bảo chữ luôn màu đen
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setForeground(Color.BLACK);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 13));
        header.setBackground(new Color(230, 230, 230));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách bộ phận"));
        
        // Sử dụng SplitPane để người dùng có thể kéo giãn vùng xem bảng
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setDividerLocation(360);
        splitPane.setBorder(null);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);

        // Sự kiện chọn dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtTenBoPhan.setText(tableModel.getValueAt(row, 1).toString());
                txtMoTa.setText(tableModel.getValueAt(row, 2).toString());
                txtLuong.setText(tableModel.getValueAt(row, 3).toString().replaceAll("[^0-9]", ""));
                txtChucDanh.setText(tableModel.getValueAt(row, 4).toString());
            }
        });
    }

    // Hàm phụ tạo Label + TextField
    private JTextField createInputGroup(JPanel parent, String labelText, Font font) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(font);
        JTextField txt = new JTextField();
        p.add(lbl, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        parent.add(p);
        return txt;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK); // CHỮ MÀU ĐEN
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- CÁC HÀM XỬ LÝ DỮ LIỆU (Giữ nguyên logic của bạn) ---

    private void loadData() {
        tableModel.setRowCount(0);
        List<Department> list = controller.getAllDepartments();
        for (Department dept : list) {
            tableModel.addRow(new Object[]{
                dept.getMaBoPhan(), dept.getTenBoPhan(), dept.getMoTaBoPhan(),
                String.format("%,d VNĐ", dept.getLuongKhoiDiem()), dept.getChucDanh()
            });
        }
    }

    private void addDepartment() {
        try {
            if (txtTenBoPhan.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên bộ phận!");
                return;
            }
            if (controller.addDepartment(txtTenBoPhan.getText(), txtMoTa.getText(),
                Integer.parseInt(txtLuong.getText().replaceAll("[^0-9]", "")), 
                txtChucDanh.getText())) {
                JOptionPane.showMessageDialog(this, "Thêm bộ phận mới thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void updateDepartment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
            return;
        }
        try {
            int maBoPhan = (int) tableModel.getValueAt(row, 0);
            if (controller.updateDepartment(maBoPhan, txtTenBoPhan.getText(), txtMoTa.getText(),
                Integer.parseInt(txtLuong.getText().replaceAll("[^0-9]", "")), 
                txtChucDanh.getText())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void deleteDepartment() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bộ phận này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int maBoPhan = (int) tableModel.getValueAt(row, 0);
            if (controller.deleteDepartment(maBoPhan)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }

    private void clearFields() {
        txtTenBoPhan.setText("");
        txtMoTa.setText("");
        txtLuong.setText("");
        txtChucDanh.setText("");
        table.clearSelection();
    }
}