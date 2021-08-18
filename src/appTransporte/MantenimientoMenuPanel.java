package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

public class MantenimientoMenuPanel extends JPanel {
	
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	
	public MantenimientoMenuPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		JComboBox<String> estaciones =agregarCombo();
		agregarBotones(agregado, estaciones);
		agregarCampText(agregado);
		agregarEtiquetas();
	}
	
	private void agregarBotones(Map<String, JTextField> agregado, JComboBox<String> estaciones) {
		JButton boton1 = new JButton("Atras");
		JButton boton2 = new JButton("Comenzar");
		JButton boton0 = new JButton("Finalizar");
		boton1.addActionListener( e -> {
			ventana.setContentPane( new MenuPrincipalPanel(ventana) );
			ventana.setVisible(true);
		});
		boton2.addActionListener( e -> {
			if (Postgre.consultaAgregarSQL("INSERT INTO Mantenimiento VALUES ('"+estaciones.getSelectedItem()+"', '"+LocalDate.now().toString()+"');", "ERROR, no se empezo el mantenimento.") == 1 && 
					Postgre.consultaAgregarSQL("UPDATE Estacion SET abierta=false  WHERE nombre='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la estación (false).") == 1 && 
							Postgre.consultaAgregarSQL("UPDATE Ruta SET activa=false  WHERE nombre_origen='"+estaciones.getSelectedItem()+"' OR nombre_destino='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la ruta (false).") == 1)
				JOptionPane.showMessageDialog(null, "Se pudo realizar el mantenimento");
			ventana.setContentPane( new MenuPrincipalPanel(ventana) );
			ventana.setVisible(true);
		});
		boton0.addActionListener( e -> {
			ResultSet resultado = null;
			//Busca mantenimiento que no este finalizado
			resultado = Postgre.consultaSQL("SELECT diaInicio, diaFin FROM Mantenimiento WHERE nombre_estacion='"+estaciones.getSelectedItem()+"' AND diaFin isnull;");
			try {
				resultado.next();
				do {
					if (Postgre.consultaAgregarSQL("UPDATE Mantenimiento SET diaFin='"+LocalDate.now()+"'  WHERE nombre_estacion='"+estaciones.getSelectedItem()+"' AND diaInicio='"+resultado.getString(1)+"'", "ERROR, no se finalizo el mantenimento.") == 1 && 
							Postgre.consultaAgregarSQL("UPDATE Estacion SET abierta=true  WHERE nombre='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la estación (true).") == 1 && 
									Postgre.consultaAgregarSQL("UPDATE Ruta SET activa=true  WHERE nombre_origen='"+estaciones.getSelectedItem()+"' OR nombre_destino='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la ruta (true).") == 1)
						JOptionPane.showMessageDialog(null, "Se pudo finalizar el mantenimento.");
				}while(resultado.next() && !resultado.getString(2).isEmpty());
			} catch (SQLException e1) {
				System.out.println("error en: mantenimento (finalizar)"+e1);
			}
			ventana.setContentPane( new MenuPrincipalPanel(ventana) );
			ventana.setVisible(true);	
		});
		estaciones.addActionListener( e -> {
			ResultSet resultado = null;
			constraints.gridy = 3;
			constraints.gridx = 2;
			resultado = Postgre.consultaSQL("SELECT abierta FROM Estacion WHERE nombre='"+estaciones.getSelectedItem()+"'");
			try {
				while(resultado.next()) {
					if (!resultado.getBoolean(1)) {//esta en mantenimento
						remove(boton2);
						add(boton0, constraints);
						ventana.setVisible(true);
						ventana.repaint();
					}else {
						remove(boton0);
						add(boton2, constraints);
						ventana.setVisible(true);
						ventana.repaint();
					}
				}
			} catch (SQLException e1) {
				System.out.println("error en: lista desplegable");
			}
		});
		ResultSet resultado = null;
		constraints.gridy = 3;
		constraints.gridx = 2;
		resultado = Postgre.consultaSQL("SELECT abierta FROM Estacion WHERE nombre='"+estaciones.getSelectedItem()+"'");
		try {
			while(resultado.next()) {
				if (!resultado.getBoolean(1)) {//esta en mantenimento
					remove(boton2);
					add(boton0, constraints);
					ventana.setVisible(true);
					ventana.repaint();
				}else {
					remove(boton0);
					add(boton2, constraints);
					ventana.setVisible(true);
					ventana.repaint();
				}
			}
		} catch (SQLException e1) {
			System.out.println("error en: lista desplegable");
		}
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(boton1, constraints);
	}
	
	private void agregarCampText(Map<String, JTextField> agregado) {
		JTextField comentario = new JTextField(20);
		agregado.put("comentario", comentario);
		constraints.gridx = 1;
		constraints.gridy = 2;
		add(comentario, constraints);
	}
	
	private JComboBox<String> agregarCombo() {
		JComboBox<String> estaciones = new JComboBox<String>();
		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT nombre FROM Estacion");
		try {
			while(resultado.next()) {
				estaciones.addItem( resultado.getString(1) );
			}
		} catch (SQLException e1) {
			System.out.println("Error en agregar a la lista de seleccion "+e1);
		}
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		add(estaciones, constraints);
		return estaciones;
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Seleccionar Estación");
		JLabel etiqueta3 = new JLabel("Comentario");
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(etiqueta3, constraints);
	}

}
