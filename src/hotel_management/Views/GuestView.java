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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuestView extends JFrame {
    private GuestController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public GuestView() {
        controller = new GuestController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Danh sách Khách hàng");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("DANH SÁCH KHÁCH HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");
        
        btnRefresh.addActionListener(e -> loadData());
        btnSearch.addActionListener(e -> searchGuest());
        
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnRefresh);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        String[] columns = {"Mã KH", "Họ và tên", "SĐT", "CMND/CCCD", "Email", "Địa chỉ", "Trạng thái", "Ngày đăng ký"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Guest> list = controller.getAllGuests();
        for (Guest guest : list) {
            String trangThaiDisplay = guest.getTrangThai().equals("Reserved") ? 
                                     "Đang đặt phòng" : "Không đặt phòng";
            tableModel.addRow(new Object[]{
                guest.getMaKhachHang(), 
                guest.getHoTenKhachHang(),
                guest.getSoDienThoaiKhachHang(),
                guest.getCmndCccdKhachHang(),
                guest.getEmailKhachHang(),
                guest.getDiaChi(),
                trangThaiDisplay,
                guest.getNgayTao()
            });
        }
    }
    
    private void searchGuest() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập SĐT hoặc CMND để tìm:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            tableModel.setRowCount(0);
            List<Guest> list = controller.getAllGuests();
            for (Guest guest : list) {
                if (guest.getSoDienThoaiKhachHang().contains(keyword) || 
                    guest.getCmndCccdKhachHang().contains(keyword)) {
                    String trangThaiDisplay = guest.getTrangThai().equals("Reserved") ? 
                                             "Đang đặt phòng" : "Không đặt phòng";
                    tableModel.addRow(new Object[]{
                        guest.getMaKhachHang(), 
                        guest.getHoTenKhachHang(),
                        guest.getSoDienThoaiKhachHang(),
                        guest.getCmndCccdKhachHang(),
                        guest.getEmailKhachHang(),
                        guest.getDiaChi(),
                        trangThaiDisplay,
                        guest.getNgayTao()
                    });
                }
            }
        }
    }
}