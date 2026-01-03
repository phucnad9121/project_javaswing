package hotel_management.Views;

import hotel_management.Controllers.AdminController;
import hotel_management.Controllers.LoginController;
import hotel_management.Controllers.GuestController;
import hotel_management.Models.Admin;
import hotel_management.Models.Login;
import hotel_management.Models.Guest;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private AdminController adminController;
    private LoginController loginController;
    private GuestController guestController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbUserType;
    private JButton btnLogin, btnRegister, btnExit;
    
    // Khai báo các Font đã tăng thêm 1 đơn vị
    private Font fontPlain = new Font("Arial", Font.PLAIN, 14); // Gốc thường là 13
    private Font fontBold = new Font("Arial", Font.BOLD, 14);
    private Font fontTitle = new Font("Arial", Font.BOLD, 21); // Gốc là 20
    
    public LoginView() {
        adminController = new AdminController();
        loginController = new LoginController();
        guestController = new GuestController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đăng nhập - Hệ thống Quản lý Khách sạn");
        setSize(600, 550); // Tăng nhẹ kích thước cửa sổ để chữ to không bị chật
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Màu nền xám nhạt cho sang trọng
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ KHÁCH SẠN");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(new Color(0, 51, 102)); // Màu xanh đậm
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0); // Khoảng cách dưới tiêu đề
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblTitle, gbc);
        
        // --- Thiết lập chung cho các Label và Input ---
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Loại tài khoản
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblType = new JLabel("Loại tài khoản:");
        lblType.setFont(fontBold);
        mainPanel.add(lblType, gbc);
        
        gbc.gridx = 1;
        cmbUserType = new JComboBox<>(new String[]{"Khách hàng", "Nhân viên", "Quản trị viên"});
        cmbUserType.setFont(fontPlain);
        cmbUserType.setPreferredSize(new Dimension(200, 35)); // Tăng chiều cao ô nhập
        mainPanel.add(cmbUserType, gbc);
        
        // Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(fontBold);
        mainPanel.add(lblUser, gbc);
        
        gbc.gridx = 1;
        txtUsername = new JTextField(20);
        txtUsername.setFont(fontPlain);
        txtUsername.setPreferredSize(new Dimension(200, 35));
        mainPanel.add(txtUsername, gbc);
        
        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(fontBold);
        mainPanel.add(lblPass, gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(fontPlain);
        txtPassword.setPreferredSize(new Dimension(200, 35));
        mainPanel.add(txtPassword, gbc);
        
        // --- Panel Nút bấm ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setOpaque(false); // Để lộ màu nền mainPanel
        
        btnLogin = new JButton("Đăng nhập");
        btnRegister = new JButton("Đăng ký");
        btnExit = new JButton("Thoát");
        
        // Áp dụng font và kích thước cho nút
        JButton[] buttons = {btnLogin, btnRegister, btnExit};
        for (JButton btn : buttons) {
            btn.setFont(fontBold);
            btn.setPreferredSize(new Dimension(130, 40));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(btn);
        }
        
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.BLACK); 
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        // Sự kiện
        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> openRegisterForm());
        btnExit.addActionListener(e -> System.exit(0));
        
        add(mainPanel);
    }
    
    // ... (Các hàm login(), openRegisterForm() giữ nguyên logic cũ của bạn)
    
    private void openRegisterForm() {
        new GuestRegisterView().setVisible(true);
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String userType = cmbUserType.getSelectedItem().toString();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        if (userType.equals("Quản trị viên")) {
            Admin admin = adminController.adminLogin(username, password);
            if (admin != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập Admin thành công!");
                this.dispose();
                new MainView(null, admin, null).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } else if (userType.equals("Nhân viên")) {
            Login login = loginController.employeeLogin(username, password);
            if (login != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nChào mừng " + 
                    login.getEmployee().getHoTenNhanVien());
                this.dispose();
                new MainView(login, null, null).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } else {
            Guest guest = guestController.login(username, password);
            if (guest != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nChào mừng " + 
                    guest.getHoTenKhachHang());
                this.dispose();
                new GuestHomeView(guest).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai số điện thoại hoặc mật khẩu!");
            }
        }
    }

    public static void main(String[] args) {
        // Cài đặt giao diện giống hệ điều hành (Look and Feel)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}