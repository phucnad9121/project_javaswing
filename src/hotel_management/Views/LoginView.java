/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
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
    
    public LoginView() {
        adminController = new AdminController();
        loginController = new LoginController();
        guestController = new GuestController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đăng nhập - Hệ thống Quản lý Khách sạn");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ KHÁCH SẠN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Loại tài khoản:"), gbc);
        
        gbc.gridx = 1;
        cmbUserType = new JComboBox<>(new String[]{"Khách hàng", "Nhân viên", "Quản trị viên"});
        cmbUserType.addActionListener(e -> updateLoginLabel());
        mainPanel.add(cmbUserType, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1;
        txtUsername = new JTextField(20);
        mainPanel.add(txtUsername, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setForeground(Color.BLACK);
        
        btnRegister = new JButton("Đăng ký Khách hàng");
        btnRegister.setForeground(Color.BLACK);
        
        btnExit = new JButton("Thoát");
        btnExit.setForeground(Color.BLACK);
        
        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> openRegisterForm());
        btnExit.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnExit);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // Thêm ghi chú cho khách hàng
        JLabel lblNote = new JLabel("<html><i>Khách hàng: Dùng số điện thoại để đăng nhập</i></html>");
        lblNote.setFont(new Font("Arial", Font.PLAIN, 11));
        lblNote.setForeground(Color.GRAY);
        gbc.gridy = 5;
        mainPanel.add(lblNote, gbc);
        
        add(mainPanel);
    }
    
    private void updateLoginLabel() {
        // Có thể thêm hint cho từng loại tài khoản
    }
    
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
            // Admin login
            Admin admin = adminController.adminLogin(username, password);
            if (admin != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập Admin thành công!");
                this.dispose();
                new MainView(null, admin, null).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } else if (userType.equals("Nhân viên")) {
            // Employee login
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
            // Guest login - Dùng số điện thoại
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
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}