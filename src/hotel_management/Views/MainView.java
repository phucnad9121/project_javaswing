/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Login;
import hotel_management.Models.Admin;
import hotel_management.Models.Guest;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private Login currentLogin;
    private Admin currentAdmin;
    private Guest currentGuest;
    private boolean isAdmin;
    
    public MainView(Login login, Admin admin, Guest guest) {
        this.currentLogin = login;
        this.currentAdmin = admin;
        this.currentGuest = guest;
        this.isAdmin = (admin != null);
        initComponents();
    }
    
    private void initComponents() {
        String title = isAdmin ? "Hệ thống Quản lý - Quản trị viên" : 
                       "Hệ thống Quản lý - " + currentLogin.getEmployee().getHoTenNhanVien();
        setTitle(title);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        String welcomeText = isAdmin ? "Chào mừng: Quản trị viên" : 
                            "Chào mừng: " + currentLogin.getEmployee().getHoTenNhanVien() + 
                            " (" + currentLogin.getEmployee().getChucDanhNV() + ")";
        JLabel lblWelcome = new JLabel(welcomeText);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(lblWelcome, BorderLayout.WEST);
        
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> logout());
        topPanel.add(btnLogout, BorderLayout.EAST);
        
        JPanel menuPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Menu cho Admin
        if (isAdmin) {
            menuPanel.add(createMenuButton("Quản lý Bộ phận", e -> new DepartmentView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Nhân viên", e -> new EmployeeView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Tài khoản", e -> new LoginAccountView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Loại phòng", e -> new RoomTypeView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Phòng", e -> new RoomView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Dịch vụ", e -> new ServiceView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Giảm giá", e -> new DiscountView(getCurrentEmployeeId()).setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Khách hàng", e -> new GuestView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Đặt phòng", e -> new BookingView(getCurrentEmployeeId()).setVisible(true)));
            menuPanel.add(createMenuButton("Thanh toán & Trả phòng", e -> new CheckoutView().setVisible(true)));
            menuPanel.add(createMenuButton("Báo cáo & Thống kê", e -> showComingSoon()));
        } else {
            // Menu cho Employee
            menuPanel.add(createMenuButton("Quản lý Đặt phòng", e -> new BookingView(getCurrentEmployeeId()).setVisible(true)));
            menuPanel.add(createMenuButton("Thanh toán & Trả phòng", e -> new CheckoutView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Khách hàng", e -> new GuestView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Phòng", e -> new RoomView().setVisible(true)));
            menuPanel.add(createMenuButton("Quản lý Dịch vụ", e -> new ServiceView().setVisible(true)));
            menuPanel.add(createMenuButton("Giảm giá", e -> new DiscountView(getCurrentEmployeeId()).setVisible(true)));
        }
        
        add(topPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
    }
    
    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 60));
        button.addActionListener(listener);
        return button;
    }
    
    private void showComingSoon() {
        JOptionPane.showMessageDialog(this, "Chức năng đang được phát triển!");
    }
    
    private int getCurrentEmployeeId() {
        return isAdmin ? 1 : currentLogin.getMaNhanVien();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn đăng xuất?", "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginView().setVisible(true);
        }
    }
}