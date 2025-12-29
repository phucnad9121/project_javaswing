/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel_management.DAO;

/**
 *
 * @author phucd
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_mngt";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Thay đổi password của bạn
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Kết nối database thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối database!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}