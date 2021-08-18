package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bdPostgre.Postgre;	

public class BoletoMenuPanel extends JPanel {
	
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	
	public BoletoMenuPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		Map<String, JComboBox<String>> agregado = new HashMap<String, JComboBox<String>>();
		JCheckBox cri = agregarCheck();
		agregarBotones(agregado, cri);
		agregarCombo(agregado);
		agregarEtiquetas();
	}
	
	private void agregarBotones(Map<String, JComboBox<String>> agregado, JCheckBox criterio) {
		JButton boton0 = new JButton("Atras");
		JButton boton1 = new JButton("Siguiente");
		boton0.addActionListener(e -> {
			ventana.setContentPane( new MenuPrincipalPanel(ventana) );
			ventana.setVisible(true);
		});
		boton1.addActionListener(e -> {
			if(!agregado.get("estaciones0").getSelectedItem().equals(agregado.get("estaciones1").getSelectedItem())) {
				ventana.setContentPane( new MostrarCaminosPanel(ventana, agregado.get("estaciones0").getSelectedItem().toString(), agregado.get("estaciones1").getSelectedItem().toString(), criterio.isSelected() ) );
				ventana.setVisible(true);
			}
			else JOptionPane.showMessageDialog(null, "Por favor seleccione estaciones diferentes.");
		});
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(boton0, constraints);
		constraints.gridx = 1;
		add(boton1, constraints);
	}
	
	private void agregarCombo(Map<String, JComboBox<String>> agregado) {
		JComboBox<String> estaciones0 = new JComboBox<String>();
		JComboBox<String> estaciones1 = new JComboBox<String>();
		agregado.put("estaciones0", estaciones0);
		agregado.put("estaciones1", estaciones1);
		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT nombre, horarioApertura, horarioCierre FROM Estacion WHERE abierta");
		try {
			while(resultado.next()) {
				estaciones0.addItem( resultado.getString(1) );
				estaciones1.addItem( resultado.getString(1) );
				}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de seleccion "+e1);
		}
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(estaciones0, constraints);
		constraints.gridx = 1;
		add(estaciones1, constraints);
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Desde");
		JLabel etiqueta1 = new JLabel("Hasta");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridx = 1;
		add(etiqueta1, constraints);
	}
	
	private JCheckBox agregarCheck() {
		JCheckBox criterio = new JCheckBox("Solo lineas");
		constraints.gridy = 1;
		constraints.gridx = 2;
		add(criterio, constraints);
		return criterio;
	}

}
