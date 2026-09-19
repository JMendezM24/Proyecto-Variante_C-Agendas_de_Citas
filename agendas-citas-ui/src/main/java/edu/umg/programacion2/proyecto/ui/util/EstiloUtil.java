package edu.umg.programacion2.proyecto.ui.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;

public class EstiloUtil {

    // Paleta de colores moderna (Modo Claro / Material)
    public static final Color COLOR_FONDO = new Color(245, 247, 250);
    public static final Color COLOR_TARJETA = Color.WHITE;
    public static final Color COLOR_TEXTO = new Color(33, 37, 41);
    public static final Color COLOR_SECUNDARIO_TEXTO = new Color(108, 117, 125);

    // Botones
    public static final Color COLOR_PRIMARIO = new Color(79, 70, 229);    // Índigo / Púrpura suave
    public static final Color COLOR_EXITO = new Color(16, 185, 129);       // Verde esmeralda
    public static final Color COLOR_PELIGRO = new Color(239, 68, 68);      // Rojo coral
    public static final Color COLOR_NEUTRO = new Color(100, 116, 139);      // Gris pizarra

    public static final DateTimeFormatter FORMATO_CORTO = DateTimeFormatter.ofPattern("dd-MM-yy");

    // Botón redondeado con antialiasing
    public static JButton crearBotonRedondeado(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();

                super.paintComponent(g);
            }
        };

        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));

        return btn;
    }

    // Contenedor tipo "Card" flotante con bordes suaves
    public static JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_TARJETA);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    // Nombres de meses en español
    public static final String[] MESES = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public static String getNombreMes(int numeroMes) {
        return MESES[numeroMes - 1];
    }
}