package hotel_management.Views;

import hotel_management.Controllers.EmployeeController;
import hotel_management.Controllers.DepartmentController;
import hotel_management.Models.Employee;
import hotel_management.Models.Department;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeView extends JFrame {
    private EmployeeController employeeController;
    private DepartmentController departmentController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtHo, txtTen, txtChucDanh, txtSoDienThoai, txtEmail, txtDiaChi, txtCMND;
    private JTextField txtSearch; // Trường tìm kiếm mới
    private JSpinner spinnerNgayVaoLam;
    private JComboBox<String> cmbBoPhan;
    private List<Department> departments;
    
    // Màu sắc
    private final Color COLOR_BG = new Color(240, 242, 245);
    private final Color COLOR_BTN_ADD = new Color(184, 255, 206); 
    private final Color COLOR_BTN_EDIT = new Color(184, 229, 255); 
    private final Color COLOR_BTN_DELETE = new Color(255, 184, 184); 
    private final Color COLOR_BTN_REFRESH = new Color(230, 230, 230);
    private final Color COLOR_BTN_SEARCH = new Color(255, 243, 191); // Màu vàng nhạt cho nút tìm kiếm

    public EmployeeView() {
        employeeController = new EmployeeController();
        departmentController = new DepartmentController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("QUẢN LÝ NHÂN VIÊN");
        setSize(1280, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_BG);

        JLabel lblHeader = new JLabel("DANH SÁCH NHÂN VIÊN CHI TIẾT", JLabel.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(44, 62, 80));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // --- PANEL TRÁI: NHẬP LIỆU ---
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setPreferredSize(new Dimension(380, 0));
        leftPanel.setOpaque(false);

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 15));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)), "Thông tin nhân viên"),
            new EmptyBorder(15, 10, 15, 10)
        ));

        Font labelFont = new Font("Tahoma", Font.BOLD, 12);
        
        inputPanel.add(new JLabel("Họ:")).setFont(labelFont);
        txtHo = new JTextField(); inputPanel.add(txtHo);
        inputPanel.add(new JLabel("Tên:")).setFont(labelFont);
        txtTen = new JTextField(); inputPanel.add(txtTen);
        inputPanel.add(new JLabel("Chức danh:")).setFont(labelFont);
        txtChucDanh = new JTextField(); inputPanel.add(txtChucDanh);
        inputPanel.add(new JLabel("Số điện thoại:")).setFont(labelFont);
        txtSoDienThoai = new JTextField(); inputPanel.add(txtSoDienThoai);
        inputPanel.add(new JLabel("Email:")).setFont(labelFont);
        txtEmail = new JTextField(); inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Ngày vào làm:")).setFont(labelFont);
        spinnerNgayVaoLam = new JSpinner(new SpinnerDateModel());
        spinnerNgayVaoLam.setEditor(new JSpinner.DateEditor(spinnerNgayVaoLam, "dd/MM/yyyy"));
        inputPanel.add(spinnerNgayVaoLam);
        inputPanel.add(new JLabel("Địa chỉ:")).setFont(labelFont);
        txtDiaChi = new JTextField(); inputPanel.add(txtDiaChi);
        inputPanel.add(new JLabel("Bộ phận:")).setFont(labelFont);
        cmbBoPhan = new JComboBox<>();
        loadDepartments();
        inputPanel.add(cmbBoPhan);
        inputPanel.add(new JLabel("CMND/CCCD:")).setFont(labelFont);
        txtCMND = new JTextField(); inputPanel.add(txtCMND);

        leftPanel.add(inputPanel, BorderLayout.CENTER);

        // --- PANEL NÚT BẤM TÁC VỤ ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        
        JButton btnAdd = createStyledButton("Thêm", COLOR_BTN_ADD);
        JButton btnUpdate = createStyledButton("Sửa", COLOR_BTN_EDIT);
        JButton btnDelete = createStyledButton("Xóa", COLOR_BTN_DELETE);
        JButton btnRefresh = createStyledButton("Làm mới", COLOR_BTN_REFRESH);
        
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadData(); clearFields(); });
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- PANEL PHẢI: TÌM KIẾM VÀ BẢNG ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setOpaque(false);

        // Thanh tìm kiếm (Search Bar)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        searchPanel.add(new JLabel("Tìm kiếm (Mã/Tên):")).setFont(labelFont);
        txtSearch = new JTextField(25);
        searchPanel.add(txtSearch);
        
        JButton btnSearch = createStyledButton("Tìm kiếm", COLOR_BTN_SEARCH);
        btnSearch.setPreferredSize(new Dimension(100, 25));
        btnSearch.addActionListener(e -> searchEmployee()); // Gọi hàm tìm kiếm
        searchPanel.add(btnSearch);

        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columns = {"Mã", "Họ", "Tên", "Chức danh", "SĐT", "Email", "Ngày vào làm", "Địa chỉ", "Bộ phận", "CMND"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.setForeground(Color.BLACK); 
        table.setGridColor(new Color(220, 220, 220));
        table.setRowHeight(30);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(200, 230, 255));
        table.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setForeground(Color.BLACK); 
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 12));
        header.setBackground(new Color(230, 230, 230));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(390);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFieldsFromTable();
            }
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- CÁC HÀM LOGIC ---

    // Hàm tìm kiếm theo Mã hoặc Tên
    private void searchEmployee() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        List<Employee> allEmps = employeeController.getAllEmployees();
        List<Employee> filteredList = allEmps.stream()
            .filter(e -> String.valueOf(e.getMaNhanVien()).contains(keyword) || 
                         e.getTenNhanVien().toLowerCase().contains(keyword) ||
                         e.getHoNhanVien().toLowerCase().contains(keyword) ||
                         e.getHoTenNhanVien().toLowerCase().contains(keyword))
            .collect(Collectors.toList());

        displayData(filteredList);
        
        if (filteredList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào phù hợp!");
        }
    }

    private void loadData() {
        List<Employee> list = employeeController.getAllEmployees();
        displayData(list);
    }

    // Hàm phụ để hiển thị dữ liệu lên bảng từ một List bất kỳ
    private void displayData(List<Employee> list) {
        tableModel.setRowCount(0);
        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                emp.getMaNhanVien(), emp.getHoNhanVien(), emp.getTenNhanVien(),
                emp.getChucDanhNV(), emp.getSoDienThoaiNV(), emp.getEmailNhanVien(),
                emp.getNgayVaoLam(), emp.getDiaChi(), emp.getTenBoPhan(), emp.getCmndCccd()
            });
        }
    }

    private void loadDepartments() {
        departments = departmentController.getAllDepartments();
        cmbBoPhan.removeAllItems();
        if (departments != null) {
            for (Department d : departments) cmbBoPhan.addItem(d.getTenBoPhan());
        }
    }

    private void fillFieldsFromTable() {
        int row = table.getSelectedRow();
        int id = (int) tableModel.getValueAt(row, 0);
        List<Employee> emps = employeeController.getAllEmployees();
        Employee emp = emps.stream().filter(e -> e.getMaNhanVien() == id).findFirst().orElse(null);
        if (emp != null) {
            txtHo.setText(emp.getHoNhanVien());
            txtTen.setText(emp.getTenNhanVien());
            txtChucDanh.setText(emp.getChucDanhNV());
            txtSoDienThoai.setText(emp.getSoDienThoaiNV());
            txtEmail.setText(emp.getEmailNhanVien());
            txtDiaChi.setText(emp.getDiaChi());
            txtCMND.setText(emp.getCmndCccd());
            spinnerNgayVaoLam.setValue(new java.util.Date(emp.getNgayVaoLam().getTime()));
            for (int i = 0; i < departments.size(); i++) {
                if (departments.get(i).getMaBoPhan() == emp.getMaBoPhan()) {
                    cmbBoPhan.setSelectedIndex(i); break;
                }
            }
        }
    }

    private void addEmployee() {
        try {
            int maBoPhan = departments.get(cmbBoPhan.getSelectedIndex()).getMaBoPhan();
            java.util.Date utilDate = (java.util.Date) spinnerNgayVaoLam.getValue();
            Date ngayVaoLam = new Date(utilDate.getTime());
            
            if (employeeController.addEmployee(txtTen.getText(), txtHo.getText(), 
                txtChucDanh.getText(), txtSoDienThoai.getText(), txtEmail.getText(),
                ngayVaoLam, txtDiaChi.getText(), maBoPhan, txtCMND.getText())) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên từ bảng để sửa!");
            return;
        }
        try {
            int maNhanVien = (int) tableModel.getValueAt(row, 0);
            int maBoPhan = departments.get(cmbBoPhan.getSelectedIndex()).getMaBoPhan();
            java.util.Date utilDate = (java.util.Date) spinnerNgayVaoLam.getValue();
            Date ngayVaoLam = new Date(utilDate.getTime());
            
            if (employeeController.updateEmployee(maNhanVien, txtTen.getText(), txtHo.getText(),
                txtChucDanh.getText(), txtSoDienThoai.getText(), txtEmail.getText(),
                ngayVaoLam, txtDiaChi.getText(), maBoPhan, txtCMND.getText())) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int maNhanVien = (int) tableModel.getValueAt(row, 0);
            if (employeeController.deleteEmployee(maNhanVien)) {
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtHo.setText(""); txtTen.setText(""); txtChucDanh.setText("");
        txtSoDienThoai.setText(""); txtEmail.setText(""); txtDiaChi.setText("");
        txtCMND.setText(""); cmbBoPhan.setSelectedIndex(0);
        table.clearSelection();
    }
}