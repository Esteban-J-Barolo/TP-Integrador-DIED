package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MenuPrincipalPanel extends JPanel {
	
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	
	public MenuPrincipalPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		agregarBotones();
		agregarEtiquetas();
	}
	
	private void agregarBotones() {
		JButton boton0 = new JButton("Estaciones");
		JButton boton1 = new JButton("Lineas");
		JButton boton2 = new JButton("Mantenimiento");
		JButton boton3 = new JButton("Vender Boleto");
		JButton boton4 = new JButton("Cerrar Sesión");
		boton0.addActionListener(e -> {
			ventana.setContentPane( new EstacionMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		boton1.addActionListener(e -> {
			ventana.setContentPane( new LineaMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		boton2.addActionListener(e -> {
			ventana.setContentPane( new MantenimientoMenuPanel(ventana) );
			ventana.setVisible(true);
			
		});
		boton3.addActionListener(e -> {
			ventana.setContentPane( new BoletoMenuPanel(ventana) );
			ventana.setVisible(true);
			
		});
		boton4.addActionListener(e -> {
			ventana.setContentPane( new InicioPanel(ventana) );
			ventana.setVisible(true);
		});
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(boton0, constraints);
		constraints.gridy = 2;
		add(boton1, constraints);
		constraints.gridy = 3;
		add(boton2, constraints);
		constraints.gridy = 4;
		add(boton3, constraints);
		constraints.gridy = 5;
		add(boton4, constraints);
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta = new JLabel("Menu Principal");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta, constraints);
	}

}
