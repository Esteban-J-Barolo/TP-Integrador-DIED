package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bdPostgre.Postgre;	

public class FlujoEstacionPanel extends JPanel {

	private Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
	private ArrayList<String> estacionesNombre = new ArrayList<String>();
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	private ArrayList<LineaTransporte> lineas = new ArrayList<LineaTransporte>();

	public FlujoEstacionPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		Postgre.extraerLineas(lineas, estaciones, estacionesNombre);
		agregarElementos();
	}
	
	private void agregarElementos() {
		JLabel etiqueta3 = agregarEtiquetas();
		agregarBotones();
		agregarComboBox(etiqueta3);
	}
	
	private void agregarBotones() {
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener( e -> {
			ventana.setContentPane( new EstacionMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		constraints.gridx = 0;
		constraints.gridy = 4;
		add(boton0, constraints);
	}
	
	private void agregarComboBox( JLabel etiqueta3) {
		JComboBox<String> estaciones0 = new JComboBox<String>();
		JComboBox<String> estaciones1 = new JComboBox<String>();
		
		for(String str : estacionesNombre) {
			estaciones0.addItem(str);
			estaciones1.addItem(str);
		}
		int flujo=0;
		for(LineaTransporte linea : lineas) {
			flujo+=linea.flujoMax(estaciones.get(estaciones0.getSelectedItem().toString()), estaciones.get(estaciones1.getSelectedItem().toString()));	
		}
		etiqueta3.setText(String.valueOf(flujo));
		
		estaciones0.addActionListener( e -> {
			int flu=0;
			for(LineaTransporte linea : lineas) {
				flu+=linea.flujoMax(estaciones.get(estaciones0.getSelectedItem().toString()), estaciones.get(estaciones1.getSelectedItem().toString()));	
			}
			etiqueta3.setText(String.valueOf(flu));
		});
		
		estaciones1.addActionListener( e -> {
			int flu=0;
			for(LineaTransporte linea : lineas) {
				flu+=linea.flujoMax(estaciones.get(estaciones0.getSelectedItem().toString()), estaciones.get(estaciones1.getSelectedItem().toString()));	
			}
			etiqueta3.setText(String.valueOf(flu));
		});
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(estaciones0, constraints);
		constraints.gridx = 1;
		add(estaciones1, constraints);
	}
	
	private JLabel agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Origen");
		JLabel etiqueta1= new JLabel("Destino");
		JLabel etiqueta2 = new JLabel("Flujo Maximo");
		JLabel etiqueta3 = new JLabel();
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridx = 1;
		add(etiqueta1, constraints);
		constraints.gridx = 2;
		add(etiqueta2, constraints);
		constraints.gridy = 1;
		add(etiqueta3, constraints);
		return etiqueta3;
	}
}
