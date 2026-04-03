import view.MainFrame;

import javax.swing.*;

/**
 * Application Entry Point
 * Launches the Student Management System with Swing EDT safety.
 */
public class Main {
    public static void main(String[] args) {
        // Set a modern Look & Feel
        try {
            // Try Nimbus first (nicer than Metal)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    // Nimbus color customization
                    UIManager.put("nimbusBase",            new java.awt.Color(30, 58, 138));
                    UIManager.put("nimbusBlueGrey",        new java.awt.Color(100, 116, 139));
                    UIManager.put("control",               new java.awt.Color(248, 250, 252));
                    UIManager.put("Table.alternateRowColor", new java.awt.Color(248, 250, 252));
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not set Nimbus L&F, using default: " + e.getMessage());
        }

        // Run on the Event Dispatch Thread (EDT) — Swing thread safety
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
