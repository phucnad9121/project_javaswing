/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotel_management;

/**
 *
 * @author phucd
 */
import hotel_management.Views.LoginView;
import hotel_management.Views.GuestRegisterView;
import javax.swing.*;

public class Hotel_Management {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}