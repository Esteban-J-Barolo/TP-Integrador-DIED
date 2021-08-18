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
import javax.swing.JTextField;

import bdPostgre.Postgre;	

public class CrearTrayectoMenuPanel extends JPanel {
	
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;

	public CrearTrayectoMenuPanel(JFrame ventana, String linea) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos(linea);
	}

	private void agregarElementos(String linea) {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		Map<String, JComboBox<String>> agregadoCombo = new HashMap<String, JComboBox<String>>();
		Map<String, JCheckBox> agregadoCheck = new HashMap<String, JCheckBox>();
		agregarBotones(agregado, agregadoCombo, agregadoCheck, linea);
		agregarCampText(agregado);
		agregarEtiquetas();
		agregarComboBox(agregadoCombo);
		agregarCheckBox(agregadoCheck);
	}

	private void agregarBotones(Map<String, JTextField> agregado, Map<String, JComboBox<String>> agregadoCombo, Map<String, JCheckBox> agregadoCheck, String linea) {
		JButton boton0 = new JButton("Agregar");
		JButton boton1 = new JButton("Finalizar");
		
		boton0.addActionListener( e -> { 
			if(!agregadoCombo.get("estaciones0").getSelectedItem().toString().equals(agregadoCombo.get("estaciones1").getSelectedItem().toString())) {
				if (Postgre.consultaAgregarSQL(
						"INSERT INTO Ruta VALUES ('"+linea+"', "+agregado.get("distancia").getText()+", "+agregado.get("duracion").getText()+", "+agregado.get("costo").getText()+", "+agregado.get("cantPasajeros").getText()+", "+agregadoCheck.get("estado").isSelected()+", '"+agregadoCombo.get("estaciones0").getSelectedItem()+"', '"+agregadoCombo.get("estaciones1").getSelectedItem()+"');", 
						"Error al agregar la trayectoria.") == 1)
					JOptionPane.showMessageDialog(null, "Trayectoria agragada con exito.");
				
			}else {
				JOptionPane.showMessageDialog(null, "Estaciones iguales: tienen que ser diferente.");
			}
		});
		
		boton1.addActionListener( e -> {
			ventana.setContentPane( new MenuTrayectoriaPanel(ventana) );
			ventana.setVisible(true);
		});
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 2;
		add(boton0, constraints);
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		add(boton1, constraints);
	}

	private void agregarCampText(Map<String, JTextField> agregado) {
		JTextField distancia = new JTextField("0",5);
		JTextField duracion = new JTextField("0",5);
		JTextField cantPasajeros = new JTextField("0",5);
		JTextField costo = new JTextField("0",5);
		agregado.put("distancia", distancia);
		agregado.put("duracion", duracion);
		agregado.put("cantPasajeros", cantPasajeros);
		agregado.put("costo", costo);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		add(distancia, constraints);
		constraints.gridy = 4;
		add(duracion, constraints);
		constraints.gridy = 5;
		add(cantPasajeros, constraints);
		constraints.gridy = 6;
		add(costo, constraints);
		constraints.gridwidth = 1;
	}

	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Seleccionar");
		JLabel etiqueta1 = new JLabel("Distancia: ");
		JLabel etiqueta2 = new JLabel("Duración: ");
		JLabel etiqueta3 = new JLabel("Pasajeros: ");
		JLabel etiqueta4 = new JLabel("Costo: ");
		JLabel etiqueta5 = new JLabel("desde");
		JLabel etiqueta6 = new JLabel("hasta");
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		add(etiqueta0, constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(etiqueta1, constraints);
		constraints.gridy = 4;
		add(etiqueta2, constraints);
		constraints.gridy = 5;
		add(etiqueta3, constraints);
		constraints.gridy = 6;
		add(etiqueta4, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(etiqueta5, constraints);
		constraints.gridx = 2;
		add(etiqueta6, constraints);
	}
	
	private void agregarComboBox(Map<String, JComboBox<String>> agregadoCombo) {
		JComboBox<String> estaciones0 = new JComboBox<String>();
		JComboBox<String> estaciones1 = new JComboBox<String>();
		agregadoCombo.put("estaciones0", estaciones0);
		agregadoCombo.put("estaciones1", estaciones1);
		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT nombre FROM Estacion");
		try {
			while(resultado.next()) {
				estaciones0.addItem( resultado.getString(1) );
				estaciones1.addItem( resultado.getString(1) );
			}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de seleccion "+e1);
		}
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(estaciones0, constraints);
		constraints.gridx = 2;
		add(estaciones1, constraints);
	}

	private void agregarCheckBox(Map<String, JCheckBox> agregadoCheck) {
		JCheckBox estado = new JCheckBox("Activa");
		agregadoCheck.put("estado", estado);
		constraints.gridx = 1;
		constraints.gridy = 7;
		add(estado, constraints);
	}

}
