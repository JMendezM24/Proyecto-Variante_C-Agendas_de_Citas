package edu.umg.programacion2.proyecto.ui.vista;

import edu.umg.programacion2.proyecto.ui.util.EstiloUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BienvenidaFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;


    public BienvenidaFrame() {
        setTitle("Sistema de Citas Médicas");
        setMinimumSize(new Dimension(520, 420));
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstiloUtil.COLOR_FONDO);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setOpaque(false);
        panelPrincipal.setBorder(new EmptyBorder(15, 20, 25, 20));

        // --- BARRITA SUPERIOR: BOTÓN SALIR EN LA ESQUINA ---
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        JButton btnSalirTop = EstiloUtil.crearBotonRedondeado("X Salir", EstiloUtil.COLOR_PELIGRO, Color.WHITE);
        btnSalirTop.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSalirTop.setBorder(new EmptyBorder(5, 12, 5, 12));
        btnSalirTop.addActionListener(e -> System.exit(0));

        panelTop.add(btnSalirTop, BorderLayout.EAST);
        panelPrincipal.add(panelTop, BorderLayout.NORTH);

        // --- TARJETA CENTRAL ENVOLVENTE ---
        JPanel cardCentral = EstiloUtil.crearPanelTarjeta();
        cardCentral.setLayout(new BoxLayout(cardCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Bienvenido al Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(EstiloUtil.COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Seleccione una opción para continuar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(EstiloUtil.COLOR_SECUNDARIO_TEXTO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnGestionar = EstiloUtil.crearBotonRedondeado("Agendar ", EstiloUtil.COLOR_PRIMARIO, Color.WHITE);
        JButton btnVerConsultas = EstiloUtil.crearBotonRedondeado("Ver/Modificar Citas Agendadas", EstiloUtil.COLOR_EXITO, Color.WHITE);

        btnGestionar.setMaximumSize(new Dimension(320, 45));
        btnVerConsultas.setMaximumSize(new Dimension(320, 45));
        btnGestionar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerConsultas.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardCentral.add(Box.createVerticalStrut(10));
        cardCentral.add(lblTitulo);
        cardCentral.add(Box.createVerticalStrut(5));
        cardCentral.add(lblSubtitulo);
        cardCentral.add(Box.createVerticalStrut(30));
        cardCentral.add(btnGestionar);
        cardCentral.add(Box.createVerticalStrut(15));
        cardCentral.add(btnVerConsultas);

        panelPrincipal.add(cardCentral, BorderLayout.CENTER);

        // Eventos de navegación
        btnGestionar.addActionListener(e -> {
            new FormularioCitaFrame().setVisible(true);
            dispose();
        });

        btnVerConsultas.addActionListener(e -> {
            new TablaConsultasFrame().setVisible(true);
            dispose();
        });

        add(panelPrincipal);
    }
}