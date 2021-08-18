package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bdPostgre.Postgre;	

public class MenuTrayectoriaPanel extends JPanel {
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;

	public MenuTrayectoriaPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		JComboBox lineas=agregarComboBox();
		agregarBotones(lineas);
		agregarEtiquetas();
	}
	
	private void agregarBotones(JComboBox lineas) {

		JButton boton0 = new JButton("Atras");
		JButton boton1 = new JButton("Crear");
		JButton boton2 = new JButton("Cambiar estado");
		boton0.addActionListener( e -> {
			ventana.setContentPane( new LineaMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		boton1.addActionListener( e -> {
			ventana.setContentPane( new CrearTrayectoMenuPanel(ventana, (String) lineas.getSelectedItem()) );
			ventana.setVisible(true);
		});
		boton2.addActionListener( e -> {
			ventana.setContentPane( new CambiarEstadoPanel(ventana, (String) lineas.getSelectedItem()) );
			ventana.setVisible(true);
		});
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(boton1, constraints);
		constraints.gridx = 1;
		add(boton2, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		add(boton0, constraints);
	}
	
	private JComboBox agregarComboBox() {
		JComboBox<String> lineas = new JComboBox<String>();

		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT nombre FROM LineaTransporte");
		try {
			while(resultado.next()) {
				lineas.addItem( resultado.getString(1) );
			}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de seleccion "+e1);
		}
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		add(lineas, constraints);
		constraints.gridwidth = 1;
		return lineas;
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Seleccionar linea");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		add(etiqueta0, constraints);
		constraints.gridwidth = 1;
	}
}
