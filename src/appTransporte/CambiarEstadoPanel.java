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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bdPostgre.Postgre;

public class CambiarEstadoPanel extends JPanel {
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;

	public CambiarEstadoPanel(JFrame ventana, String linea) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos(linea);
	}
	
	private void agregarElementos(String linea) {
		Map<String, JComboBox<String>> agregado = new HashMap<String, JComboBox<String>>();
		JLabel etiqueta4 = agregarEtiquetas(linea);
		agregarBotones(agregado, linea, etiqueta4);
		agregarCombo(agregado, linea, etiqueta4);
	}
	
	private void agregarBotones(Map<String, JComboBox<String>> agregado, String nombreLinea, JLabel etiqueta4) {
		JButton boton0 = new JButton("Atras");
		JButton boton1 = new JButton("Cambiar");
		boton0.addActionListener( e -> {
			ventana.setContentPane( new MenuTrayectoriaPanel(ventana) );
			ventana.setVisible(true);
		});
		boton1.addActionListener( e -> {
			ResultSet resultado = null;
			String[] result = agregado.get("trayectos").getSelectedItem().toString().split(" - ");
			resultado = Postgre.consultaSQL("SELECT activa FROM Ruta WHERE nombre_linea ='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'");
			try {
				resultado.next();
				if(resultado.getBoolean(1)) {
					if (Postgre.consultaAgregarSQL("UPDATE Ruta SET activa=false  WHERE nombre_linea='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'", "ERROR al actualizar estado de la trayectoria (false).") == 1 )
						JOptionPane.showMessageDialog(null, "Se pudo realizar el cambio de estado");
				}else {
					if (Postgre.consultaAgregarSQL("UPDATE Ruta SET activa=true  WHERE nombre_linea='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'", "ERROR al actualizar estado de la trayectoria (true).") == 1 )
						JOptionPane.showMessageDialog(null, "Se pudo realizar el cambio de estado");
				}
				etiqueta4.setText( String.valueOf(!resultado.getBoolean(1)) );
				repaint();
			} catch (SQLException e1) {
				System.out.println("Error consulta. "+e1);
			}
		});
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(boton1, constraints);
		constraints.gridy = 3;
		add(boton0, constraints);
		
		
	}
	
	private void agregarCombo(Map<String, JComboBox<String>> agregado, String nombreLinea, JLabel etiqueta4) {
		JComboBox<String> trayectos = new JComboBox<String>();
		agregado.put("trayectos", trayectos);
		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT nombre_origen, nombre_destino FROM Ruta WHERE nombre_linea ='"+nombreLinea+"'");
		try {
			while(resultado.next()) {
				trayectos.addItem( (resultado.getString(1)+" - "+resultado.getString(2)) );
			}
		} catch (SQLException e1) {
			System.out.println("Error en agregar a la lista de seleccion de trayectos. "+e1);
		}
		trayectos.addActionListener( e -> {
			String[] result = trayectos.getSelectedItem().toString().split(" - ");
			ResultSet resultado2 = null;
			resultado2 = Postgre.consultaSQL("SELECT activa FROM Ruta WHERE nombre_linea ='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'");
			try {
				resultado2.next();
				etiqueta4.setText( String.valueOf(resultado2.getBoolean(1)) );
				repaint();
			} catch (SQLException e1) {
				System.out.println("Error consulta. "+e1);
			}
		});
		String[] result = trayectos.getSelectedItem().toString().split(" - ");
		ResultSet resultado2 = null;
		resultado2 = Postgre.consultaSQL("SELECT activa FROM Ruta WHERE nombre_linea ='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'");
		try {
			resultado2.next();
			etiqueta4.setText( String.valueOf(resultado2.getBoolean(1)) );
		} catch (SQLException e1) {
			System.out.println("Error consulta. "+e1);
		}
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(trayectos, constraints);
	}
	
	private JLabel agregarEtiquetas(String nombreLinea) {
		JLabel etiqueta0 = new JLabel("Trayectos");
		JLabel etiqueta1 = new JLabel("Linea");
		JLabel etiqueta2 = new JLabel("Estado");
		JLabel etiqueta3 = new JLabel(nombreLinea);
		JLabel etiqueta4 = new JLabel();
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridx = 0;
		add(etiqueta1, constraints);
		constraints.gridy = 1;
		add(etiqueta3, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(etiqueta2, constraints);
		constraints.gridy = 1;
		add(etiqueta4, constraints);
		return etiqueta4;
	}
}
