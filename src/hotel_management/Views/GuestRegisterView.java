/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Controllers.GuestController;
import hotel_management.Models.Guest;
import javax.swing.*;
import java.awt.*;

public class GuestRegisterView extends JFrame {
    private GuestController controller;
    private JTextField txtHo, txtTen, txtSoDienThoai, txtCMND, txtEmail, txtDiaChi;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister, btnCancel;
    
    public GuestRegisterView() {
        controller = new GuestController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Đăng ký Khách hàng");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitle = new JLabel("ĐĂNG KÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Họ:"), gbc);
        gbc.gridx = 1;
        txtHo = new JTextField(20);
        mainPanel.add(txtHo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        txtTen = new JTextField(20);
        mainPanel.add(txtTen, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField(20);
        mainPanel.add(txtSoDienThoai, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("CMND/CCCD:"), gbc);
        gbc.gridx = 1;
        txtCMND = new JTextField(20);
        mainPanel.add(txtCMND, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        mainPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        txtDiaChi = new JTextField(20);
        mainPanel.add(txtDiaChi, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        mainPanel.add(new JLabel("Xác nhận MK:"), gbc);
        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(20);
        mainPanel.add(txtConfirmPassword, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegister = new JButton("Đăng ký");
        btnCancel = new JButton("Hủy");
        
        btnRegister.addActionListener(e -> register());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    private void register() {
        String ho = txtHo.getText().trim();
        String ten = txtTen.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String cmnd = txtCMND.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (ho.isEmpty() || ten.isEmpty() || soDienThoai.isEmpty() || 
            cmnd.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
            return;
        }
        
        Guest guest = controller.register(ten, ho, email, soDienThoai, cmnd, diaChi, password);
        
        if (guest != null) {
            JOptionPane.showMessageDialog(this, 
                "Đăng ký thành công!\nMã KH: " + guest.getMaKhachHang() + 
                "\nChào mừng bạn đến với khách sạn!");
            dispose();
            new GuestHomeView(guest).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại! SĐT hoặc CMND đã tồn tại.");
        }
    }
}