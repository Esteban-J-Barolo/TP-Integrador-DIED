package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class LineaMenuPanel extends JPanel {

	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	
	public LineaMenuPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		Map<String, JCheckBox> agregadoCheck = new HashMap<String, JCheckBox>();
		JComboBox colores = agregarComboBox();
		agregarBotones(agregado, colores, agregadoCheck);
		agregarCampText(agregado);
		agregarEtiquetas();
		agregarCheckBox(agregadoCheck);
	}
	
	private void agregarBotones(Map<String, JTextField> agregado, JComboBox colores, Map<String, JCheckBox> agregadoCheck) {
		JButton boton0 = new JButton("Crear");
		JButton boton1 = new JButton("Atras");
		JButton boton2 = new JButton("Trayectorias");
		
		boton0.addActionListener( e -> {
			if (Postgre.consultaAgregarSQL("INSERT INTO LineaTransporte VALUES ('"+agregado.get("nombre").getText()+"', '"+colores.getSelectedItem()+"', "+agregadoCheck.get("estado").isSelected()+");", "No se pudo agregar la linea.") == 1)
				JOptionPane.showMessageDialog(null, "Linea agragada con exito.");
		});
		boton1.addActionListener( e -> {
			ventana.setContentPane( new MenuPrincipalPanel(ventana) );
			ventana.setVisible(true);
		});
		boton2.addActionListener( e -> {
			ventana.setContentPane( new MenuTrayectoriaPanel(ventana) );
			ventana.setVisible(true);
		});

		constraints.gridy = 5;
		add(boton0, constraints);
		constraints.gridx = 0;
		add(boton1, constraints);
		constraints.gridy = 6;
		add(boton2, constraints);
	}
	
	private JComboBox agregarComboBox() {
		JComboBox<String> colores = new JComboBox<String>();
		colores.addItem("Rojo");
		colores.addItem("Verde");
		colores.addItem("Azul");
		colores.addItem("Amarillo");
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		add(colores, constraints);
		constraints.gridwidth = 1;
		return colores;
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Nombre ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
	}
	
	private void agregarCampText(Map<String, JTextField> agregado) {
		JTextField nombre = new JTextField(20);
		agregado.put("nombre", nombre);
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(nombre, constraints);
	}
	
	private void agregarCheckBox(Map<String, JCheckBox> agregar) {
		JCheckBox estado = new JCheckBox("Habilitada");
		agregar.put("estado", estado);
		constraints.gridx = 1;
		constraints.gridy = 4;
		add(estado, constraints);
	}
}