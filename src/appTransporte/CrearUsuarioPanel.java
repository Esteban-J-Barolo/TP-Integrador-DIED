package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bdPostgre.Postgre;

public class CrearUsuarioPanel extends JPanel {
	
	GridBagConstraints constraints = new GridBagConstraints();

	public CrearUsuarioPanel(JFrame ventana) {
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos(ventana);
	}
	
	private void agregarElementos(JFrame ventana) {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		agregarBotones(ventana, agregado);
		agregarCampText(agregado);
		agregarEtiquetas();
	}
	
	private void agregarBotones(JFrame ventana, Map<String, JTextField> agregado) {
		JButton boton2 = new JButton("Crear");
		JButton boton3 = new JButton("Atras");
		
		boton2.addActionListener(e1 -> {
			if( agregado.get("contr").getText().equals(agregado.get("contrRep").getText()) ) { //coinciden contraseñas
				if (Postgre.consultaAgregarSQL("INSERT INTO usuario VALUES ('"+agregado.get("nombre").getText()+"', '"+agregado.get("contr").getText()+"');", "Usuario ya existente") == 1) { //insertar el usuario, 1 salio todo bien, 0 hay un error
					JOptionPane.showMessageDialog(null, "Usuario creado.");
					ventana.setContentPane( new InicioPanel(ventana) );
					ventana.setVisible(true);
				}
			}else JOptionPane.showMessageDialog(null, "La contraseña no coincide.");
		});
		
		boton3.addActionListener(e1 -> {
			ventana.setContentPane( new InicioPanel(ventana) );
			ventana.setVisible(true);
		});

		constraints.gridx = 2;
		constraints.gridy = 3;
		add(boton2, constraints);
		constraints.gridy = 4;
		add(boton3, constraints);
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Nombre de usuario ");
		JLabel etiqueta2 = new JLabel("Contraseña ");
		JLabel etiqueta3 = new JLabel("Confirmar contraseña ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridy = 1;
		add(etiqueta2, constraints);
		constraints.gridy = 2;
		add(etiqueta3, constraints);
	}
	
	private void agregarCampText(Map<String, JTextField> agregado) {
		JTextField text0 = new JTextField(20);
		JTextField text2 = new JTextField(20);
		JTextField text3 = new JTextField(20);
		agregado.put("nombre", text0);
		agregado.put("contr", text2);
		agregado.put("contrRep", text3);
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(text0, constraints);
		constraints.gridy = 1;
		add(text2, constraints);
		constraints.gridy = 2;
		add(text3, constraints);
		
	}

}
