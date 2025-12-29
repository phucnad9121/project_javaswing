/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.Views;

/**
 *
 * @author phucd
 */
import hotel_management.Models.Guest;
import javax.swing.*;
import java.awt.*;

public class GuestHomeView extends JFrame {
    private Guest currentGuest;
    
    public GuestHomeView(Guest guest) {
        this.currentGuest = guest;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Trang chủ - Khách sạn");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblWelcome = new JLabel("Xin chào, " + currentGuest.getHoTenKhachHang() + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.BLACK);
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> logout());
        headerPanel.add(btnLogout, BorderLayout.EAST);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin của bạn"));
        infoPanel.setBackground(Color.WHITE);
        
        infoPanel.add(createInfoLabel("Mã khách hàng:", String.valueOf(currentGuest.getMaKhachHang())));
        infoPanel.add(createInfoLabel("Họ và tên:", currentGuest.getHoTenKhachHang()));
        infoPanel.add(createInfoLabel("Số điện thoại:", currentGuest.getSoDienThoaiKhachHang()));
        infoPanel.add(createInfoLabel("Email:", currentGuest.getEmailKhachHang()));
        infoPanel.add(createInfoLabel("CMND/CCCD:", currentGuest.getCmndCccdKhachHang()));
        infoPanel.add(createInfoLabel("Địa chỉ:", currentGuest.getDiaChi()));
        
        // Menu Panel
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        menuPanel.setBackground(new Color(236, 240, 241));
        
        JButton btnBookRoom = createMenuButton(
            "Đặt phòng mới", 
            "Tạo yêu cầu đặt phòng",
            e -> openBookingForm()
        );
        
        JButton btnMyBookings = createMenuButton(
            "Booking của tôi",
            "Xem các booking đã đặt",
            e -> viewMyBookings()
        );
        
        JButton btnServices = createMenuButton(
            "Dịch vụ khách sạn",
            "Xem các dịch vụ có sẵn",
            e -> viewServices()
        );
        
        JButton btnProfile = createMenuButton(
            "Thông tin cá nhân",
            "Cập nhật thông tin",
            e -> updateProfile()
        );
        
        menuPanel.add(btnBookRoom);
        menuPanel.add(btnMyBookings);
        menuPanel.add(btnServices);
        menuPanel.add(btnProfile);
        
        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblMainTitle = new JLabel("CHÀO MỪNG ĐẾN VỚI KHÁCH SẠN");
        lblMainTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblMainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubTitle = new JLabel("Trải nghiệm dịch vụ tốt nhất cùng chúng tôi");
        lblSubTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubTitle.setForeground(Color.GRAY);
        
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(lblMainTitle);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(lblSubTitle);
        welcomePanel.add(Box.createVerticalGlue());
        
        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(236, 240, 241));
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(welcomePanel, BorderLayout.CENTER);
        centerPanel.add(menuPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(lblLabel);
        panel.add(lblValue);
        
        return panel;
    }
    
    private JButton createMenuButton(String title, String description, 
                                     java.awt.event.ActionListener listener) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.BLACK);
        lblDesc.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblDesc);
        
        button.add(textPanel, BorderLayout.CENTER);
        button.addActionListener(listener);
        
        return button;
    }
    
    private void openBookingForm() {
        new BookingRequestView(currentGuest).setVisible(true);
    }
    
    private void viewMyBookings() {
        new MyBookingsView(currentGuest).setVisible(true);
    }
    
    private void viewServices() {
        JOptionPane.showMessageDialog(this, 
            "Dịch vụ có sẵn:\n\n" +
            "• Giặt ủi: 50,000 VNĐ\n" +
            "• Ăn sáng buffet: 100,000 VNĐ\n" +
            "• Spa massage: 500,000 VNĐ\n" +
            "• Đưa đón sân bay: 300,000 VNĐ\n" +
            "• Minibar: 150,000 VNĐ\n\n" +
            "Các dịch vụ sẽ được thêm vào hóa đơn khi bạn sử dụng.",
            "Dịch vụ khách sạn",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateProfile() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng cập nhật thông tin đang được phát triển!\n" +
            "Vui lòng liên hệ lễ tân để thay đổi thông tin.",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
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