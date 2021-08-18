package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bdPostgre.Postgre;

public class EstacionMenuPanel extends JPanel {
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;

	public EstacionMenuPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		Map<String, JCheckBox> agregadoCheck = new HashMap<String, JCheckBox>();
		agregarBotones(agregado, agregadoCheck);
		agregarCampText(agregado);
		agregarEtiquetas();
		agregarCheckBox(agregadoCheck);
	}
	
	private void agregarBotones(Map<String, JTextField> agregado, Map<String, JCheckBox> agregadoCheck) {
		JButton boton0 = new JButton("Crear");
		JButton boton1 = new JButton("Atras");
		JButton boton2 = new JButton("Page Rank");
		JButton boton3 = new JButton("Calcular flujo");
		
		boton0.addActionListener( e -> {
			try {
				Integer.parseInt(agregado.get("horarioApertura0").getText());
				Integer.parseInt(agregado.get("horarioCierre0").getText());
				Integer.parseInt(agregado.get("horarioApertura1").getText());
				Integer.parseInt(agregado.get("horarioCierre1").getText());
				if (Postgre.consultaAgregarSQL("INSERT INTO Estacion VALUES ('"+agregado.get("nombre").getText()+"', "+agregado.get("horarioApertura0").getText()+agregado.get("horarioApertura1").getText()+", "+agregado.get("horarioCierre0").getText()+agregado.get("horarioCierre1").getText()+", "+agregadoCheck.get("estado").isSelected()+");", "No se pudo agregar la estación.") == 1)
					JOptionPane.showMessageDialog(null, "Estación agragada con exito.");
				if (!agregadoCheck.get("estado").isSelected()) Postgre.consultaAgregarSQL("INSERT INTO Mantenimiento(nombre_estacion, diaInicio, observaciones) VALUES ('"+agregado.get("nombre").getText()+"', '"+LocalDate.now().toString()+"', 'Creación de la estación.');", "No se empeso el mantenimento de creado.");
					
			}catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Horario invalido");
			}
		});
		boton1.addActionListener( e -> {
			ventana.setContentPane( new MenuPrincipalPanel(ventana) );
			ventana.setVisible(true);
		});
		boton2.addActionListener( e -> {
			ventana.setContentPane( new PageRankPanel(ventana) );
			ventana.setVisible(true);
		});
		boton3.addActionListener( e -> {
			ventana.setContentPane( new FlujoEstacionPanel(ventana) );
			ventana.setVisible(true);
		});

		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.gridy = 4;
		add(boton0, constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		add(boton1, constraints);
		constraints.gridy = 5;
		add(boton2, constraints);
		constraints.gridx = 1;
		add(boton3, constraints);

	}
	
	private void agregarCampText(Map<String, JTextField> agregado) {
		JTextField nombre = new JTextField(20);
		JTextField horarioApertura0 = new JTextField("00",5);
		JTextField horarioApertura1 = new JTextField("00",5);
		JTextField horarioCierre0 = new JTextField("00",5);
		JTextField horarioCierre1 = new JTextField("00",5);
		agregado.put("nombre", nombre);
		agregado.put("horarioApertura0", horarioApertura0);
		agregado.put("horarioApertura1", horarioApertura1);
		agregado.put("horarioCierre0", horarioCierre0);
		agregado.put("horarioCierre1", horarioCierre1);
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(nombre, constraints);
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		add(horarioApertura0, constraints);
		constraints.gridy = 2;
		add(horarioCierre0, constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		add(horarioApertura1, constraints);
		constraints.gridy = 2;
		add(horarioCierre1, constraints);
		constraints.gridwidth = 1;
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Nombre ");
		JLabel etiqueta1 = new JLabel("Horario de apertura: ");
		JLabel etiqueta2 = new JLabel("Horario de cierre: ");
		JLabel etiqueta3 = new JLabel(" : ");
		JLabel etiqueta4 = new JLabel(" : ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridy = 1;
		add(etiqueta1, constraints);
		constraints.gridy = 2;
		add(etiqueta2, constraints);
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(etiqueta3, constraints);
		constraints.gridy = 2;
		add(etiqueta4, constraints);
		constraints.gridwidth = 1;
	}

	private void agregarCheckBox(Map<String, JCheckBox> agregado) {
		JCheckBox estado = new JCheckBox("Habilitada");
		agregado.put("estado", estado);
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 3;
		add(estado, constraints);
		constraints.gridwidth = 1;
		
	}
}
