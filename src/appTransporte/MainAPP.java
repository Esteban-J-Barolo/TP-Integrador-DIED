package appTransporte;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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

public class MainAPP{
	
	private JFrame ventanaPrincipal = new JFrame("Transporte Multimodal");
	private static Postgre bdPostre = new Postgre();
	private ArrayList<LineaTransporte> lineas = new ArrayList<LineaTransporte>(); 
	
	public static void main(String[] args) {
		
		MainAPP a = new MainAPP();
		bdPostre.start();
		a.inicio();
	}
	
	public void inicio() {
		ventanaPrincipal.setMinimumSize(new Dimension(400, 300));
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel etiqueta1 = new JLabel("Usuario ");
		JLabel etiqueta2 = new JLabel("Contraseña ");
		JTextField text1 = new JTextField(20);
		JTextField text2 = new JTextField(20);
		JButton boton1 = new JButton("Crear usuario");
		boton1.addActionListener(e -> crearUsuario());
		
		JButton boton2 = new JButton("Iniciar sesión");
		boton2.addActionListener(e -> iniciarSesion(text1.getText(), text2.getText()));

		JButton boton3 = new JButton("Salir");
		boton3.addActionListener( e -> {
			bdPostre.desconectar();
			ventanaPrincipal.dispose();
		});
		
		constraints.gridx = 0; // Columna 0
		constraints.gridy = 0; // Fila 0
		constraints.gridwidth = 1; //  ocupa una columna
		constraints.gridheight = 1; // ocupa una fila
		constraints.insets = new Insets(5, 5, 5, 5);//separacion de las celdas
		//constraints.weightx = 1.0;
		//constraints.weighty = 1.0; // La fila 0 debe estirarse
		panel.add(etiqueta1, constraints);
		constraints.weighty = 0; // La fila 0 debe estirarse
		constraints.weightx = 0;
		
		constraints.gridx = 2;
		panel.add(text1, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(etiqueta2, constraints);
		constraints.weighty = 0;

		constraints.gridx = 2;
		panel.add(text2, constraints);
		
		constraints.gridy = 2;
		panel.add(boton2, constraints);
		
		constraints.ipadx = 0;
		constraints.ipady = 2;
		constraints.gridy = 3;
		panel.add(boton1, constraints);
		constraints.weighty = 0;
		
		constraints.gridy = 4;
		panel.add(boton3, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}
	
	private void crearUsuario() {
		
		ventanaPrincipal.setMinimumSize(new Dimension(600, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel etiqueta0 = new JLabel("Nombre de usuario ");
		//JLabel etiqueta1 = new JLabel("Email ");
		JLabel etiqueta2 = new JLabel("Contraseña ");
		JLabel etiqueta3 = new JLabel("Confirmar contraseña ");
		JTextField text0 = new JTextField(20);
		//JTextField text1 = new JTextField(20);
		JTextField text2 = new JTextField(20);
		JTextField text3 = new JTextField(20);
		
		JButton boton2 = new JButton("Crear");
		boton2.addActionListener(e1 -> {
			if( text2.getText().equals(text3.getText()) ) { //coinciden contraseñas
				if (bdPostre.consultaAgregarSQL("INSERT INTO usuario VALUES ('"+text0.getText()+"', '"+text2.getText()+"');", "Usuario ya existente") == 1) { //insertar el usuario, 1 salio todo bien, 0 hay un error
					JOptionPane.showMessageDialog(null, "Usuario creado.");
					inicio();
				}
			}else JOptionPane.showMessageDialog(null, "La contraseña no coincide.");
		});
		
		JButton boton3 = new JButton("Atras");
		boton3.addActionListener(e1 -> inicio());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);

		//constraints.gridy = 1;
		//panel.add(etiqueta1, constraints);

		constraints.gridy = 1;
		panel.add(etiqueta2, constraints);

		constraints.gridy = 2;
		panel.add(etiqueta3, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		panel.add(text0, constraints);

		//constraints.gridy = 1;
		//panel.add(text1, constraints);

		constraints.gridy = 1;
		panel.add(text2, constraints);

		constraints.gridy = 2;
		panel.add(text3, constraints);
		
		constraints.gridy = 3;
		panel.add(boton2, constraints);

		constraints.gridy = 4;
		panel.add(boton3, constraints);
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}
	
	private void iniciarSesion(String nombre, String contrasenia) {
		ResultSet resultado = null;
		boolean encontrado = false;
		resultado = bdPostre.consultaSQL("SELECT nombre, contrasenia FROM Usuario");
		try {
			while(resultado.next() && !encontrado){
				if (resultado.getString(1).equalsIgnoreCase(nombre) && resultado.getString(2).equals(contrasenia)) encontrado=true; 
			}
			if (encontrado) this.accesoUsuario();
			else JOptionPane.showMessageDialog(null, "Usuario no encontrado");
			resultado.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al tratar de iniciar sesión: " + e);
		}
	}

	private void accesoUsuario() {

		ventanaPrincipal.setMinimumSize(new Dimension(200, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		JButton boton0 = new JButton("Nueva Estacion");
		boton0.addActionListener(e -> crearEstacion());
		JButton boton1 = new JButton("Nueva Linea");
		boton1.addActionListener(e -> crearLinea());
		JButton boton2 = new JButton("Nuevo Mantenimiento");
		boton2.addActionListener(e -> crearMantenimiento());
		JButton boton3 = new JButton("Vender Boleto");
		boton3.addActionListener(e -> crearVenta());
		
		JButton boton4 = new JButton("Cerrar Sesión");
		boton4.addActionListener(e -> inicio());

		JLabel etiqueta = new JLabel("Menu Principal");
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta, constraints);
		constraints.gridy = 1;
		panel.add(boton0, constraints);
		constraints.gridy = 2;
		panel.add(boton1, constraints);
		constraints.gridy = 3;
		panel.add(boton2, constraints);
		constraints.gridy = 4;
		panel.add(boton3, constraints);
		constraints.gridy = 5;
		panel.add(boton4, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
		
	}
	
	private void crearVenta() {
		ventanaPrincipal.setMinimumSize(new Dimension(200, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JComboBox<String> estaciones0 = new JComboBox<String>();
		JComboBox<String> estaciones1 = new JComboBox<String>();
		
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener(e -> accesoUsuario());
		
		JButton boton1 = new JButton("Siguiente");
		boton1.addActionListener(e -> {
			if(!estaciones0.getSelectedItem().equals(estaciones1.getSelectedItem()))
			verRecorridos(estaciones0.getSelectedItem().toString(), estaciones1.getSelectedItem().toString());
			else JOptionPane.showMessageDialog(null, "Por favor seleccione estaciones diferentes.");
		});
		
		JLabel etiqueta0 = new JLabel("Desde");
		JLabel etiqueta1 = new JLabel("Hasta");
				
		ResultSet resultado = null;
		
		resultado = bdPostre.consultaSQL("SELECT nombre, horarioApertura, horarioCierre FROM Estacion WHERE abierta");
		
		try {
			while(resultado.next()) {
				estaciones0.addItem( resultado.getString(1) );
				estaciones1.addItem( resultado.getString(1) );
				}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de seleccion "+e1);
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridx = 1;
		panel.add(etiqueta1, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(estaciones0, constraints);
		constraints.gridx = 1;
		panel.add(estaciones1, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(boton0, constraints);
		constraints.gridx = 1;
		panel.add(boton1, constraints);

		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
		
	}

	private void verRecorridos(String origen, String destino) {
		Map <String, Estacion> estaciones = new HashMap<String, Estacion>();
		
		ventanaPrincipal.setMinimumSize(new Dimension(800, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		LineaTransporte lineaRapida = null, lineaCorta=null, lineaBarata=null;
		
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener(e -> crearVenta());
		
		JButton boton1 = new JButton("Siguiente");
		
		
		JLabel etiqueta0 = new JLabel("Selececcione");
		
		JCheckBox opcion1 = new JCheckBox("El más rápido");
		JCheckBox opcion2 = new JCheckBox("El de menor distancia");
		JCheckBox opcion3 = new JCheckBox("El más barato");
		
		ResultSet resultado = null;
		
		resultado = bdPostre.consultaSQL("SELECT nombre, horarioApertura, horarioCierre FROM Estacion WHERE abierta");
		
		try {
			while(resultado.next()) {
				//System.out.println(resultado.getString(1)+" "+resultado.getInt(2));
				estaciones.put(resultado.getString(1), new Estacion(resultado.getString(1), resultado.getInt(2), resultado.getInt(3)));
				}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de estaciones "+e1);
		}
		
		resultado = bdPostre.consultaSQL("SELECT nombre, color, activa FROM LineaTransporte WHERE activa");
		
		try {
			while(resultado.next()) {
				LineaTransporte linea = new LineaTransporte(resultado.getString(1), resultado.getString(2), resultado.getBoolean(3));
				
				ResultSet resultado2 = null;
				
				resultado2 = bdPostre.consultaSQL("SELECT distancia, duracion, costo, cantPasajeros, activa, nombre_origen, nombre_destino FROM Ruta WHERE nombre_linea='"+resultado.getString(1)+"' AND activa");
				while(resultado2.next()) {
					linea.agregarRuta(new Ruta(estaciones.get(resultado2.getString(6)), estaciones.get(resultado2.getString(7)), resultado2.getInt(1), resultado2.getInt(2), resultado2.getInt(3), resultado2.getInt(4), resultado2.getBoolean(5)));										
				}
				lineas.add(linea);
			}
		} catch (SQLException e1) {
			System.out.println("error en buscar lineas de transporte "+e1);
		}
		
		int velocidad=10000, tamanio=10, costo=10000;
		for(LineaTransporte linea: lineas) {
			int velCam = linea.velocidadCamino(estaciones.get(origen), estaciones.get(destino));
			if( velocidad>=velCam ){
				velocidad=velCam;
				lineaRapida=linea;
			}
			int tamCam = linea.tamCamino(estaciones.get(origen), estaciones.get(destino));
			if( tamanio>=tamCam ){
				tamanio=tamCam;
				lineaCorta=linea;
			}
			int costoCam = linea.costoCamino(estaciones.get(origen), estaciones.get(destino));
			if( costo>=costoCam ){
				costo=costoCam;
				lineaBarata=linea;
			}
		}
		LineaTransporte lineaR = lineaRapida, lineaC = lineaCorta, lineaB = lineaBarata;
		boton1.addActionListener(e -> {
			if(opcion1.isSelected()) {
				//mas rapido
				boleto(lineaR, estaciones.get(origen), estaciones.get(destino));
			}else {
				if(opcion2.isSelected()) {
					//menor distancia
					boleto(lineaC, estaciones.get(origen), estaciones.get(destino));
				}else {
					if(opcion3.isSelected()) {
						//mas barato
						boleto(lineaB, estaciones.get(origen), estaciones.get(destino));
					}else {
						//no seleccionao ninguno
						JOptionPane.showMessageDialog(null, "Trayecto no seleccionado.");
					}
				}
			}
		});
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(opcion1, constraints);
				
		constraints.gridy = 2;
		panel.add(opcion2, constraints);
		constraints.gridy = 3;
		panel.add(opcion3, constraints);

		constraints.gridwidth = 1;
		constraints.gridy = 4;
		panel.add(boton0, constraints);
		constraints.gridx = 1;
		panel.add(boton1, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.ipadx = 200;
		constraints.ipady = 30;
		constraints.insets = new Insets(0, 0, 0, 0);
		panel.add(lineaR.dibujar(estaciones.get(origen), estaciones.get(destino)), constraints);
		constraints.gridy = 2;
		panel.add(lineaC.dibujar(estaciones.get(origen), estaciones.get(destino)), constraints);
		constraints.gridy = 3;
		panel.add(lineaB.dibujar(estaciones.get(origen), estaciones.get(destino)), constraints);


		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}
	
	private void boleto(LineaTransporte linea, Estacion origen, Estacion destino) {
		ventanaPrincipal.setMinimumSize(new Dimension(200, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener(e -> crearVenta());

		JTextField correo = new JTextField(20);
		JTextField nombre = new JTextField(20);

		JButton boton1 = new JButton("Aceptar");
		

		int numBoleto = 0;
		ResultSet resultado = null;
		resultado = bdPostre.consultaSQL("SELECT numBoleto FROM Boleto ORDER BY 1 DESC LIMIT 1");
		
		try {
			if(resultado.next()) {
				numBoleto=resultado.getInt(1);
				}
		} catch (SQLException e1) {
			System.out.println("error en buscar el numero de boleto "+e1);
		}
		int num = numBoleto+1;
		boton1.addActionListener(e -> {
			//crear boleto
			if (bdPostre.consultaAgregarSQL("INSERT INTO Boleto VALUES ("+ num +", '"+origen.getNombre()+"', '"+destino.getNombre()+"', '"+correo.getText()+"', '"+nombre.getText()+"', '"+LocalDate.now().toString()+"', "+linea.costoCamino(origen, destino)+" );", "Boleto error.") == 1)
				JOptionPane.showMessageDialog(null, "Boleto creado.");
		});

		JLabel etiqueta0 = new JLabel("Nro. de Boleto: "+num);
		JLabel etiqueta1 = new JLabel("Correo electrónico del cliente: ");
		JLabel etiqueta2 = new JLabel("Nombre del cliente: ");
		JLabel etiqueta3 = new JLabel("Fecha de la venta: "+LocalDate.now().toString());
		JLabel etiqueta4 = new JLabel("Estación Origen: "+origen.getNombre());
		JLabel etiqueta5 = new JLabel("Estación Destino: "+destino.getNombre());
		JLabel etiqueta6 = new JLabel("Camino a seguir: "+linea.mostrarCamino(origen, destino));
		JLabel etiqueta7 = new JLabel("Costo del boleto: "+linea.costoCamino(origen, destino));
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(etiqueta1, constraints);
		constraints.gridx = 1;
		panel.add(correo, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(etiqueta2, constraints);
		constraints.gridx = 1;
		panel.add(nombre, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(etiqueta3, constraints);
		constraints.gridy = 4;
		panel.add(etiqueta4, constraints);
		constraints.gridy = 5;
		panel.add(etiqueta5, constraints);
		constraints.gridy = 6;
		panel.add(etiqueta6, constraints);
		constraints.gridy = 7;
		panel.add(etiqueta7, constraints);
		
		constraints.gridy = 8;
		panel.add(boton0, constraints);
		constraints.gridx = 1;
		panel.add(boton0, constraints);
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void crearMantenimiento() {
		ventanaPrincipal.setMinimumSize(new Dimension(600, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel etiqueta0 = new JLabel("Seleccionar Estación");
		JLabel etiqueta3 = new JLabel("Comentario");

		JTextField comentario = new JTextField(20);

		JComboBox<String> estaciones = new JComboBox<String>();
		
		

		JButton boton2 = new JButton("Comenzar");
		boton2.addActionListener( e -> {
			if (bdPostre.consultaAgregarSQL("INSERT INTO Mantenimiento VALUES ('"+estaciones.getSelectedItem()+"', '"+LocalDate.now().toString()+"');", "No se empeso el mantenimento.") == 1 && 
					bdPostre.consultaAgregarSQL("UPDATE Estacion SET abierta=false  WHERE nombre='"+estaciones.getSelectedItem()+"'", "No se finalizo el mantenimento.") == 1)
				JOptionPane.showMessageDialog(null, "Se pudo realizar el mantenimento");
			accesoUsuario();
		});
		
		JButton boton0 = new JButton("Finalizar");
		boton0.addActionListener( e -> {
			ResultSet resultado = null;
		
			resultado = bdPostre.consultaSQL("SELECT diaInicio, diaFin FROM Mantenimiento WHERE nombre_estacion='"+estaciones.getSelectedItem()+"' AND diaFin isnull;");
		
			try {
				resultado.next();
				do {
					if (bdPostre.consultaAgregarSQL("UPDATE Mantenimiento SET diaFin='"+LocalDate.now()+"'  WHERE nombre_estacion='"+estaciones.getSelectedItem()+"' AND diaInicio='"+resultado.getString(1)+"'", "No se finalizo el mantenimento.") == 1 && 
							bdPostre.consultaAgregarSQL("UPDATE Estacion SET abierta=true  WHERE nombre='"+estaciones.getSelectedItem()+"'", "No se finalizo el mantenimento.") == 1)
						JOptionPane.showMessageDialog(null, "Se pudo finalizar el mantenimento.");
				}while(resultado.next() && !resultado.getString(2).isEmpty());
			} catch (SQLException e1) {
				System.out.println("error en: mantenimento (finalizar)"+e1);
			}
			accesoUsuario();	
		});
		JButton boton1 = new JButton("Atras");
		boton1.addActionListener( e -> accesoUsuario());
		
		estaciones.addActionListener( e -> {
			
			ResultSet resultado = null;
			
			GridBagConstraints constraints = new GridBagConstraints();

			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.insets = new Insets(5, 5, 5, 5);
			constraints.gridy = 3;
			constraints.gridx = 2;
			
			resultado = bdPostre.consultaSQL("SELECT abierta FROM Estacion WHERE nombre='"+estaciones.getSelectedItem()+"'");

			try {

				while(resultado.next()) {
					
					if (!resultado.getBoolean(1)) {//esta en mantenimento
						
						panel.remove(boton2);
						panel.add(boton0, constraints);
						ventanaPrincipal.setVisible(true);
						ventanaPrincipal.repaint();
					}else {
						panel.remove(boton0);
						panel.add(boton2, constraints);
						ventanaPrincipal.setVisible(true);
						ventanaPrincipal.repaint();
					}
				}
			} catch (SQLException e1) {
				System.out.println("error en: lista desplegable");
			}
		});
		ResultSet resultado = null;
		
		resultado = bdPostre.consultaSQL("SELECT nombre FROM Estacion");
		
		try {
			while(resultado.next()) {
				estaciones.addItem( resultado.getString(1) );
			}
		} catch (SQLException e1) {
			System.out.println("Error en agregar a la lista de seleccion "+e1);
		}

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);

		constraints.gridy = 3;
		panel.add(boton1, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(etiqueta0, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(etiqueta3, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		panel.add(comentario, constraints);
		
		constraints.gridy = 1;
		panel.add(estaciones, constraints);

		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void crearLinea() {

		ventanaPrincipal.setMinimumSize(new Dimension(600, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JTextField nombre = new JTextField(20);
		JTextField color = new JTextField(20);

		JLabel etiqueta0 = new JLabel("Nombre ");
		JLabel etiqueta1 = new JLabel("Color ");
		JCheckBox estado = new JCheckBox("Habilitada");

		JButton boton0 = new JButton("Crear");
		boton0.addActionListener( e -> {
			if (bdPostre.consultaAgregarSQL("INSERT INTO LineaTransporte VALUES ('"+nombre.getText()+"', '"+color.getText()+"', "+estado.isSelected()+");", "No se pudo agregar la linea.") == 1)
				JOptionPane.showMessageDialog(null, "Linea agragada con exito.");
		});
		JButton boton1 = new JButton("Atras");
		boton1.addActionListener( e -> accesoUsuario());
		JButton boton2 = new JButton("Agregar una trayectoria");
		boton2.addActionListener( e -> crearTrayectoria());

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(etiqueta1, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(nombre, constraints);
		constraints.gridy = 1;
		panel.add(color, constraints);
		
		constraints.gridy = 3;
		panel.add(estado, constraints);

		constraints.gridy = 5;
		panel.add(boton0, constraints);
		constraints.gridx = 0;
		panel.add(boton1, constraints);
		constraints.gridy = 6;
		panel.add(boton2, constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		panel.add(estado, constraints);

		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}
	
	private void crearTrayectoria() {
		//seleccionar linea de transporte a la que se le agrega la trayectoria
		//mostrar las estacione y mediante un click se seleccionan en orden del trayecto

		ventanaPrincipal.setMinimumSize(new Dimension(600, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel etiqueta0 = new JLabel("Seleccionar linea");

		JComboBox<String> lineas = new JComboBox<String>();
		
		JButton boton0 = new JButton("Siguiente");
		boton0.addActionListener( e -> crearTrayecto( (String) lineas.getSelectedItem()));
		JButton boton1 = new JButton("Atras");
		boton1.addActionListener( e -> crearLinea());
		
		ResultSet resultado = null;
		
		resultado = bdPostre.consultaSQL("SELECT nombre FROM LineaTransporte");
		
		try {
			while(resultado.next()) {
				lineas.addItem( resultado.getString(1) );
			}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de seleccion "+e1);
		}

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(boton0, constraints);

		constraints.gridy = 3;
		panel.add(boton1, constraints);

		constraints.gridy = 0;
		panel.add(etiqueta0, constraints);
		
		constraints.gridy = 1;
		panel.add(lineas, constraints);

		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void crearTrayecto(String nombreLinea) {

		ventanaPrincipal.setMinimumSize(new Dimension(600, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JComboBox<String> estaciones0 = new JComboBox<String>();
		JComboBox<String> estaciones1 = new JComboBox<String>();

		JLabel etiqueta0 = new JLabel("Seleccionar");
		JLabel etiqueta1 = new JLabel("Distancia: ");
		JLabel etiqueta2 = new JLabel("Duración: ");
		JLabel etiqueta3 = new JLabel("Pasajeros: ");
		JLabel etiqueta4 = new JLabel("Costo: ");
		JLabel etiqueta5 = new JLabel("desde");
		JLabel etiqueta6 = new JLabel("hasta");

		JCheckBox estado = new JCheckBox("Activa");
		
		JTextField distancia = new JTextField(5);
		JTextField duracion = new JTextField(5);
		JTextField cantPasajeros = new JTextField(5);
		JTextField costo = new JTextField(5);
		
		JButton boton0 = new JButton("Agregar");
		boton0.addActionListener( e -> { 
			if (bdPostre.consultaAgregarSQL(
					"INSERT INTO Ruta VALUES ('"+nombreLinea+"', "+distancia.getText()+", "+duracion.getText()+", "+costo.getText()+", "+cantPasajeros.getText()+", "+estado.isSelected()+", '"+estaciones0.getSelectedItem()+"', '"+estaciones1.getSelectedItem()+"');", 
					"Error al agregar la trayectoria.") == 1)
				JOptionPane.showMessageDialog(null, "Trayectoria agragada con exito.");
		});
		JButton boton1 = new JButton("Finalizar");
		boton1.addActionListener( e -> crearLinea());
		
		ResultSet resultado = null;
		
		resultado = bdPostre.consultaSQL("SELECT nombre FROM Estacion");
		
		try {
			while(resultado.next()) {
				estaciones0.addItem( resultado.getString(1) );
				estaciones1.addItem( resultado.getString(1) );
			}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de seleccion "+e1);
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(etiqueta1, constraints);
		constraints.gridy = 4;
		panel.add(etiqueta2, constraints);
		constraints.gridy = 5;
		panel.add(etiqueta3, constraints);
		constraints.gridy = 6;
		panel.add(etiqueta4, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(etiqueta5, constraints);
		constraints.gridx = 2;
		panel.add(etiqueta6, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		panel.add(distancia, constraints);
		constraints.gridy = 4;
		panel.add(duracion, constraints);
		constraints.gridy = 5;
		panel.add(cantPasajeros, constraints);
		constraints.gridy = 6;
		panel.add(costo, constraints);
		constraints.gridwidth = 1;
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		panel.add(estaciones0, constraints);
		constraints.gridx = 2;
		panel.add(estaciones1, constraints);

		constraints.gridy = 7;
		panel.add(estado, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 8;
		constraints.gridwidth = 2;
		panel.add(boton0, constraints);
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		panel.add(boton1, constraints);

		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void crearEstacion() {

		ventanaPrincipal.setMinimumSize(new Dimension(600, 400));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JTextField nombre = new JTextField(20);
		JTextField horarioApertura0 = new JTextField(5);
		JTextField horarioApertura1 = new JTextField(5);
		JTextField horarioCierre0 = new JTextField(5);
		JTextField horarioCierre1 = new JTextField(5);
		JCheckBox estado = new JCheckBox("Habilitada");

		JLabel etiqueta0 = new JLabel("Nombre ");
		JLabel etiqueta1 = new JLabel("Horario de apertura: ");
		JLabel etiqueta2 = new JLabel("Horario de cierre: ");
		JLabel etiqueta3 = new JLabel(" : ");
		JLabel etiqueta4 = new JLabel(" : ");
		
		JButton boton0 = new JButton("Crear");
		boton0.addActionListener( e -> {
			try {
				Integer.parseInt(horarioApertura0.getText());
				Integer.parseInt(horarioCierre0.getText());
				Integer.parseInt(horarioApertura1.getText());
				Integer.parseInt(horarioCierre1.getText());
				if (bdPostre.consultaAgregarSQL("INSERT INTO Estacion VALUES ('"+nombre.getText()+"', "+horarioApertura0.getText()+horarioApertura1.getText()+", "+horarioCierre0.getText()+horarioCierre1.getText()+", "+estado.isSelected()+");", "No se pudo agregar la estación.") == 1)
					JOptionPane.showMessageDialog(null, "Estación agragada con exito.");
				if (!estado.isSelected()) bdPostre.consultaAgregarSQL("INSERT INTO Mantenimiento(nombre_estacion, diaInicio, observaciones) VALUES ('"+nombre.getText()+"', '"+LocalDate.now().toString()+"', 'Creación de la estación.');", "No se empeso el mantenimento de creado.");
					
			}catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Horario invalido");
			}
		});
		JButton boton1 = new JButton("Atras");
		boton1.addActionListener( e -> accesoUsuario());
		
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(etiqueta1, constraints);
		constraints.gridy = 2;
		panel.add(etiqueta2, constraints);
		constraints.gridy = 3;
		
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(nombre, constraints);
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		panel.add(horarioApertura0, constraints);
		constraints.gridy = 2;
		panel.add(horarioCierre0, constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		panel.add(horarioApertura1, constraints);
		constraints.gridy = 2;
		panel.add(horarioCierre1, constraints);

		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 3;
		panel.add(estado, constraints);

		constraints.gridwidth = 2;
		constraints.gridy = 4;
		panel.add(boton0, constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		panel.add(boton1, constraints);

		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(etiqueta3, constraints);
		constraints.gridy = 2;
		panel.add(etiqueta4, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
		
	}

	

}
