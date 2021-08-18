package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bdPostgre.Postgre;

public class InicioPanel extends JPanel {

	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	
	public InicioPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos();
	}
	
	private void agregarElementos() {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		agregarBotones(agregado);
		agregarCampText(agregado);
		agregarEtiquetas();
	}
	
	private void agregarBotones(Map<String, JTextField> agregado) {
		JButton boton1 = new JButton("Crear usuario");
		JButton boton2 = new JButton("Iniciar sesión");
		JButton boton3 = new JButton("Salir");
		
		boton1.addActionListener(e -> {
			ventana.setContentPane( new CrearUsuarioPanel(ventana) );
			ventana.setVisible(true);
		});
		
		boton2.addActionListener(e -> iniciarSesion( agregado.get("usuario").getText() , agregado.get("contrasenia").getText() ) );

		boton3.addActionListener( e -> {
			Postgre.desc();
			ventana.dispose();
		});
		
		constraints.gridx = 2;
		constraints.gridy = 2;
		add(boton2, constraints);
		constraints.ipadx = 0;
		constraints.ipady = 2;
		constraints.gridy = 3;
		add(boton1, constraints);
		constraints.weighty = 0;
		constraints.gridy = 4;
		add(boton3, constraints);
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta1 = new JLabel("Usuario ");
		JLabel etiqueta2 = new JLabel("Contraseña ");

		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta1, constraints);
		constraints.gridy = 1;
		add(etiqueta2, constraints);
	}

	private void agregarCampText(Map<String, JTextField> agregado) {

		JTextField text1 = new JTextField(20);
		JTextField text2 = new JTextField(20);
		agregado.put("usuario", text1);
		agregado.put("contrasenia", text2);
		constraints.gridy = 0;
		constraints.gridx = 2;
		add(text1, constraints);
		constraints.gridy = 1;
		add(text2, constraints);
	}
	
	private void iniciarSesion(String nombre, String contrasenia) {
		ResultSet resultado = null;
		boolean encontrado = false;
		resultado = Postgre.consultaSQL("SELECT nombre, contrasenia FROM Usuario");
		try {
			while(resultado.next() && !encontrado){
				if (resultado.getString(1).equalsIgnoreCase(nombre) && resultado.getString(2).equals(contrasenia)) encontrado=true; 
			}
			if (encontrado) {
				ventana.setContentPane(new MenuPrincipalPanel(ventana));
				ventana.setVisible(true);
			}
			else JOptionPane.showMessageDialog(null, "Usuario no encontrado");
			resultado.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al tratar de iniciar sesión: " + e);
		}
	}

}
