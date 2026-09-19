package edu.umg.programacion2.proyecto.ui;

import edu.umg.programacion2.proyecto.ui.vista.BienvenidaFrame;

import javax.swing.*;

public class MainUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new BienvenidaFrame().setVisible(true);
        });
    }
}