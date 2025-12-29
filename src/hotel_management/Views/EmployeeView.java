/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.EmployeeController;
import hotel_management.Controllers.DepartmentController;
import hotel_management.Models.Employee;
import hotel_management.Models.Department;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class EmployeeView extends JFrame {
    private EmployeeController employeeController;
    private DepartmentController departmentController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtHo, txtTen, txtChucDanh, txtSoDienThoai, txtEmail, txtDiaChi, txtCMND;
    private JSpinner spinnerNgayVaoLam;
    private JComboBox<String> cmbBoPhan;
    private List<Department> departments;
    
    public EmployeeView() {
        employeeController = new EmployeeController();
        departmentController = new DepartmentController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản lý Nhân viên");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));
        
        inputPanel.add(new JLabel("Họ:"));
        txtHo = new JTextField();
        inputPanel.add(txtHo);
        
        inputPanel.add(new JLabel("Tên:"));
        txtTen = new JTextField();
        inputPanel.add(txtTen);
        
        inputPanel.add(new JLabel("Chức danh:"));
        txtChucDanh = new JTextField();
        inputPanel.add(txtChucDanh);
        
        inputPanel.add(new JLabel("Số điện thoại:"));
        txtSoDienThoai = new JTextField();
        inputPanel.add(txtSoDienThoai);
        
        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);
        
        inputPanel.add(new JLabel("Ngày vào làm:"));
        spinnerNgayVaoLam = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerNgayVaoLam, "dd/MM/yyyy");
        spinnerNgayVaoLam.setEditor(editor);
        inputPanel.add(spinnerNgayVaoLam);
        
        inputPanel.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        inputPanel.add(txtDiaChi);
        
        inputPanel.add(new JLabel("Bộ phận:"));
        cmbBoPhan = new JComboBox<>();
        loadDepartments();
        inputPanel.add(cmbBoPhan);
        
        inputPanel.add(new JLabel("CMND/CCCD:"));
        txtCMND = new JTextField();
        inputPanel.add(txtCMND);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        inputPanel.add(buttonPanel);
        
        String[] columns = {"Mã", "Họ và tên", "Chức danh", "SĐT", "Email", "Bộ phận", "CMND"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFieldsFromTable();
            }
        });
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, new JScrollPane(table));
        splitPane.setDividerLocation(350);
        add(splitPane);
    }
    
    private void loadDepartments() {
        departments = departmentController.getAllDepartments();
        cmbBoPhan.removeAllItems();
        for (Department dept : departments) {
            cmbBoPhan.addItem(dept.getTenBoPhan());
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Employee> list = employeeController.getAllEmployees();
        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                emp.getMaNhanVien(), emp.getHoTenNhanVien(), emp.getChucDanhNV(),
                emp.getSoDienThoaiNV(), emp.getEmailNhanVien(), emp.getTenBoPhan(), emp.getCmndCccd()
            });
        }
    }
    
    private void fillFieldsFromTable() {
        int row = table.getSelectedRow();
        List<Employee> emps = employeeController.getAllEmployees();
        Employee emp = emps.get(row);
        
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
                cmbBoPhan.setSelectedIndex(i);
                break;
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
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void updateEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
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
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void deleteEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?") == JOptionPane.YES_OPTION) {
            int maNhanVien = (int) tableModel.getValueAt(row, 0);
            if (employeeController.deleteEmployee(maNhanVien)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearFields();
            }
        }
    }
    
    private void clearFields() {
        txtHo.setText("");
        txtTen.setText("");
        txtChucDanh.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtCMND.setText("");
        cmbBoPhan.setSelectedIndex(0);
        table.clearSelection();
    }
}