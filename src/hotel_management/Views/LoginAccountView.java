/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.LoginController;
import hotel_management.Controllers.EmployeeController;
import hotel_management.Models.Login;
import hotel_management.Models.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoginAccountView extends JFrame {
    private LoginController loginController;
    private EmployeeController employeeController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTenDangNhap, txtMatKhau;
    private JComboBox<String> cmbNhanVien;
    private List<Employee> employees;
    
    public LoginAccountView() {
        loginController = new LoginController();
        employeeController = new EmployeeController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Tài khoản Đăng nhập");
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Tài khoản"));
        
        inputPanel.add(new JLabel("Nhân viên:"));
        cmbNhanVien = new JComboBox<>();
        loadEmployees();
        inputPanel.add(cmbNhanVien);
        
        inputPanel.add(new JLabel("Tên đăng nhập:"));
        txtTenDangNhap = new JTextField();
        inputPanel.add(txtTenDangNhap);
        
        inputPanel.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JTextField();
        inputPanel.add(txtMatKhau);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Tạo tài khoản");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addLogin());
        btnDelete.addActionListener(e -> deleteLogin());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã đăng nhập", "Tên đăng nhập", "Nhân viên", "Người dùng mới"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadEmployees() {
        employees = employeeController.getAllEmployees();
        cmbNhanVien.removeAllItems();
        for (Employee emp : employees) {
            cmbNhanVien.addItem(emp.getMaNhanVien() + " - " + emp.getHoTenNhanVien());
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Login> list = loginController.getAllLogins();
        for (Login login : list) {
            tableModel.addRow(new Object[]{
                login.getMaDangNhap(), login.getTenDangNhap(),
                login.getEmployee().getHoTenNhanVien(), login.getNguoiDungMoi()
            });
        }
    }
    
    private void addLogin() {
        try {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = txtMatKhau.getText().trim();
            
            if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            
            int maNhanVien = employees.get(cmbNhanVien.getSelectedIndex()).getMaNhanVien();
            
            if (loginController.addLogin(tenDangNhap, matKhau, maNhanVien)) {
                JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công!");
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteLogin() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!");
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa tài khoản?") == JOptionPane.YES_OPTION) {
            int maDangNhap = (int) tableModel.getValueAt(row, 0);
            if (loginController.deleteLogin(maDangNhap)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cmbNhanVien.setSelectedIndex(0);
        table.clearSelection();
    }
}