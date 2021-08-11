package appTransporte;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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
		ventanaPrincipal.setMinimumSize(new Dimension(800, 600));
		ventanaPrincipal.setLocationRelativeTo(null);
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
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel etiqueta0 = new JLabel("Nombre de usuario ");
		JLabel etiqueta2 = new JLabel("Contraseña ");
		JLabel etiqueta3 = new JLabel("Confirmar contraseña ");
		JTextField text0 = new JTextField(20);
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

		constraints.gridy = 1;
		panel.add(etiqueta2, constraints);

		constraints.gridy = 2;
		panel.add(etiqueta3, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		panel.add(text0, constraints);

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
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		JButton boton0 = new JButton("Estaciones");
		boton0.addActionListener(e -> crearEstacion());
		JButton boton1 = new JButton("Lineas");
		boton1.addActionListener(e -> crearLinea());
		JButton boton2 = new JButton("Mantenimiento");
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
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		LineaTransporte lineaRapida = null, lineaCorta=null, lineaBarata=null;
		ArrayList<Estacion> rutaR = new ArrayList<Estacion>(), rutaC= new ArrayList<Estacion>(), rutaB= new ArrayList<Estacion>();
		
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener(e -> crearVenta());
		
		JButton boton1 = new JButton("Siguiente");
		
		
		JLabel etiqueta0 = new JLabel("Selececcione");
		JLabel etiqueta1 = new JLabel();
		JLabel etiqueta2 = new JLabel();
		JLabel etiqueta3 = new JLabel();
		
		JCheckBox opcion1 = new JCheckBox("El más rápido");
		JCheckBox opcion2 = new JCheckBox("El de menor distancia");
		JCheckBox opcion3 = new JCheckBox("El más barato");
		
		actualizarLinea(estaciones, new ArrayList<String>());
		
		int velocidad=-1, tamanio=-1, costo=-1;
		for(LineaTransporte linea: lineas) {
			ArrayList<Estacion> rutaR2 = new ArrayList<Estacion>(), rutaC2= new ArrayList<Estacion>(), rutaB2= new ArrayList<Estacion>();
			int velCam = linea.velocidadCamino(estaciones.get(origen), estaciones.get(destino), rutaR2);
			if( (velocidad>=velCam || velocidad==-1) && velCam!=-1 ){
				velocidad=velCam;
				lineaRapida=linea;
				rutaR=rutaR2;
				etiqueta1.setText("Linea: "+linea.getNombre()+" Color: "+linea.getColor());
			}
			int tamCam = linea.tamCamino(estaciones.get(origen), estaciones.get(destino), rutaC2);
			if( (tamanio>=tamCam || tamanio==-1) && tamCam!=-1){
				tamanio=tamCam;
				lineaCorta=linea;
				rutaC=rutaC2;
				etiqueta2.setText("Linea: "+linea.getNombre()+" Color: "+linea.getColor());
			}
			int costoCam = linea.costoCamino(estaciones.get(origen), estaciones.get(destino), rutaB2);
			if( (costo>=costoCam || costo==-1) && costoCam!=-1 ){
				costo=costoCam;
				lineaBarata=linea;
				rutaB=rutaB2;
				etiqueta3.setText("Linea: "+linea.getNombre()+" Color: "+linea.getColor());
			}
		}
		if(rutaR.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay una ruta de la estación "+origen+" a la "+destino+"");
			crearVenta();
		}else {
			LineaTransporte lineaR = lineaRapida, lineaC = lineaCorta, lineaB = lineaBarata;
			ArrayList<Estacion> rutaR2=rutaR, rutaC2=rutaC, rutaB2=rutaB;
			boton1.addActionListener(e -> {
				if(opcion1.isSelected()) {
					//mas rapido
					boleto(lineaR, estaciones.get(origen), estaciones.get(destino), rutaR2);
				}else {
					if(opcion2.isSelected()) {
						//menor distancia
						boleto(lineaC, estaciones.get(origen), estaciones.get(destino), rutaC2);
					}else {
						if(opcion3.isSelected()) {
							//mas barato
							boleto(lineaB, estaciones.get(origen), estaciones.get(destino), rutaB2);
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
					
			constraints.gridy = 3;
			panel.add(opcion2, constraints);
			constraints.gridy = 5;
			panel.add(opcion3, constraints);

			constraints.gridwidth = 1;
			constraints.gridy = 6;
			panel.add(boton0, constraints);
			constraints.gridx = 1;
			panel.add(boton1, constraints);
			
			constraints.gridx = 2;
			constraints.gridy = 0;
			panel.add(etiqueta1, constraints);
			constraints.gridy = 2;
			panel.add(etiqueta2, constraints);
			constraints.gridy = 4;
			panel.add(etiqueta3, constraints);
			
			constraints.gridx = 2;
			constraints.gridy = 1;
			constraints.ipadx = 200;
			constraints.ipady = 30;
			constraints.insets = new Insets(0, 0, 0, 0);
			panel.add(lineaR.dibujar(rutaR), constraints);
			constraints.gridy = 3;
			panel.add(lineaC.dibujar(rutaC), constraints);
			constraints.gridy = 5;
			panel.add(lineaB.dibujar(rutaB), constraints);

			ventanaPrincipal.setContentPane(panel);
			ventanaPrincipal.setVisible(true);
			
		}
	}
	
	private void boleto(LineaTransporte linea, Estacion origen, Estacion destino, ArrayList<Estacion> camino) {
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
			//crear boleto en la base de datos
			if (bdPostre.consultaAgregarSQL("INSERT INTO Boleto VALUES ("+ num +", '"+origen.getNombre()+"', '"+destino.getNombre()+"', '"+correo.getText()+"', '"+nombre.getText()+"', '"+LocalDate.now().toString()+"', "+linea.costoCamino(origen, destino, new ArrayList<Estacion>())+" );", "Boleto error.") == 1)
				JOptionPane.showMessageDialog(null, "Boleto creado.");
			crearVenta();
		});

		JLabel etiqueta0 = new JLabel("Nro. de Boleto: ");
		JLabel etiqueta1 = new JLabel("Correo electrónico del cliente: ");
		JLabel etiqueta2 = new JLabel("Nombre del cliente: ");
		JLabel etiqueta3 = new JLabel("Fecha de la venta: ");
		JLabel etiqueta4 = new JLabel("Estación Origen: ");
		JLabel etiqueta5 = new JLabel("Estación Destino: ");
		JLabel etiqueta6 = new JLabel("Camino a seguir: ");
		JLabel etiqueta7 = new JLabel("Costo del boleto: ");
		JLabel etiqueta00 = new JLabel(String.valueOf(num));
		JLabel etiqueta30 = new JLabel(LocalDate.now().toString());
		JLabel etiqueta40 = new JLabel(origen.getNombre());
		JLabel etiqueta50 = new JLabel(destino.getNombre());
		JLabel etiqueta60 = new JLabel(linea.mostrarCamino(camino));
		JLabel etiqueta70 = new JLabel( String.valueOf(linea.costoCamino(origen, destino, new ArrayList<Estacion>())) );
		
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
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(etiqueta00, constraints);
		constraints.gridy = 3;
		panel.add(etiqueta30, constraints);
		constraints.gridy = 4;
		panel.add(etiqueta40, constraints);
		constraints.gridy = 5;
		panel.add(etiqueta50, constraints);
		constraints.gridy = 6;
		panel.add(etiqueta60, constraints);
		constraints.gridy = 7;
		panel.add(etiqueta70, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 8;
		panel.add(boton0, constraints);
		constraints.gridx = 1;
		panel.add(boton1, constraints);
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void crearMantenimiento() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel etiqueta0 = new JLabel("Seleccionar Estación");
		JLabel etiqueta3 = new JLabel("Comentario");

		JTextField comentario = new JTextField(20);

		JComboBox<String> estaciones = new JComboBox<String>();
		
		

		JButton boton2 = new JButton("Comenzar");
		boton2.addActionListener( e -> {
			if (bdPostre.consultaAgregarSQL("INSERT INTO Mantenimiento VALUES ('"+estaciones.getSelectedItem()+"', '"+LocalDate.now().toString()+"');", "ERROR, no se empezo el mantenimento.") == 1 && 
					bdPostre.consultaAgregarSQL("UPDATE Estacion SET abierta=false  WHERE nombre='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la estación (false).") == 1 && 
					bdPostre.consultaAgregarSQL("UPDATE Ruta SET activa=false  WHERE nombre_origen='"+estaciones.getSelectedItem()+"' OR nombre_destino='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la ruta (false).") == 1)
				JOptionPane.showMessageDialog(null, "Se pudo realizar el mantenimento");
			accesoUsuario();
		});
		
		JButton boton0 = new JButton("Finalizar");
		boton0.addActionListener( e -> {
			ResultSet resultado = null;
			//Busca mantenimiento que no este finalizado
			resultado = bdPostre.consultaSQL("SELECT diaInicio, diaFin FROM Mantenimiento WHERE nombre_estacion='"+estaciones.getSelectedItem()+"' AND diaFin isnull;");
			try {
				resultado.next();
				do {
					if (bdPostre.consultaAgregarSQL("UPDATE Mantenimiento SET diaFin='"+LocalDate.now()+"'  WHERE nombre_estacion='"+estaciones.getSelectedItem()+"' AND diaInicio='"+resultado.getString(1)+"'", "ERROR, no se finalizo el mantenimento.") == 1 && 
							bdPostre.consultaAgregarSQL("UPDATE Estacion SET abierta=true  WHERE nombre='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la estación (true).") == 1 && 
							bdPostre.consultaAgregarSQL("UPDATE Ruta SET activa=true  WHERE nombre_origen='"+estaciones.getSelectedItem()+"' OR nombre_destino='"+estaciones.getSelectedItem()+"'", "ERROR al actualizar estado de la ruta (true).") == 1)
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
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);

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
		JButton boton2 = new JButton("Trayectorias");
		boton2.addActionListener( e -> menuTrayectoria());

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
	
	private void menuTrayectoria() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());


		JLabel etiqueta0 = new JLabel("Seleccionar linea");

		JComboBox<String> lineas = new JComboBox<String>();

		JButton boton0 = new JButton("Atras");
		boton0.addActionListener( e -> crearLinea());
		
		JButton boton1 = new JButton("Crear");
		boton1.addActionListener( e -> crearTrayecto( (String) lineas.getSelectedItem()));
		JButton boton2 = new JButton("Cambiar estado");
		boton2.addActionListener( e -> estadoTrayectoria((String) lineas.getSelectedItem()));
		
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
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(lineas, constraints);
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		panel.add(boton1, constraints);
		constraints.gridx = 1;
		panel.add(boton2, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		panel.add(boton0, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void estadoTrayectoria(String nombreLinea) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JComboBox<String> trayectos = new JComboBox<String>();
		
		JLabel etiqueta0 = new JLabel("Trayectos");
		JLabel etiqueta1 = new JLabel("Linea");
		JLabel etiqueta2 = new JLabel("Estado");
		JLabel etiqueta3 = new JLabel(nombreLinea);
		JLabel etiqueta4 = new JLabel();
		
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener( e -> crearLinea());
		JButton boton1 = new JButton("Cambiar");
		boton1.addActionListener( e -> {
			ResultSet resultado = null;
			String[] result = trayectos.getSelectedItem().toString().split(" - ");
			resultado = bdPostre.consultaSQL("SELECT activa FROM Ruta WHERE nombre_linea ='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'");
			try {
				resultado.next();
				if(resultado.getBoolean(1)) {
					if (bdPostre.consultaAgregarSQL("UPDATE Ruta SET activa=false  WHERE nombre_linea='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'", "ERROR al actualizar estado de la trayectoria (false).") == 1 )
						JOptionPane.showMessageDialog(null, "Se pudo realizar el cambio de estado");
				}else {
					if (bdPostre.consultaAgregarSQL("UPDATE Ruta SET activa=true  WHERE nombre_linea='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'", "ERROR al actualizar estado de la trayectoria (true).") == 1 )
						JOptionPane.showMessageDialog(null, "Se pudo realizar el cambio de estado");
				}
				etiqueta4.setText( String.valueOf(!resultado.getBoolean(1)) );
				panel.repaint();
			} catch (SQLException e1) {
				System.out.println("Error consulta. "+e1);
			}
		});

		ResultSet resultado = null;
		
		resultado = bdPostre.consultaSQL("SELECT nombre_origen, nombre_destino FROM Ruta WHERE nombre_linea ='"+nombreLinea+"'");
		
		try {
			while(resultado.next()) {
				trayectos.addItem( (resultado.getString(1)+" - "+resultado.getString(2)) );
			}
		} catch (SQLException e1) {
			System.out.println("Error en agregar a la lista de seleccion de trayectos. "+e1);
		}
		GridBagConstraints constraints = new GridBagConstraints();

		trayectos.addActionListener( e -> {
			String[] result = trayectos.getSelectedItem().toString().split(" - ");
			ResultSet resultado2 = null;
			resultado2 = bdPostre.consultaSQL("SELECT activa FROM Ruta WHERE nombre_linea ='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'");
			try {
				resultado2.next();
				etiqueta4.setText( String.valueOf(resultado2.getBoolean(1)) );
				panel.repaint();
			} catch (SQLException e1) {
				System.out.println("Error consulta. "+e1);
			}
		});
		String[] result = trayectos.getSelectedItem().toString().split(" - ");
		ResultSet resultado2 = null;
		resultado2 = bdPostre.consultaSQL("SELECT activa FROM Ruta WHERE nombre_linea ='"+nombreLinea+"' AND nombre_origen='"+result[0]+"' AND nombre_destino='"+result[1]+"'");
		try {
			resultado2.next();
			etiqueta4.setText( String.valueOf(resultado2.getBoolean(1)) );
		} catch (SQLException e1) {
			System.out.println("Error consulta. "+e1);
		}

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(trayectos, constraints);
		constraints.gridy = 2;
		panel.add(boton1, constraints);
		constraints.gridy = 3;
		panel.add(boton0, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(etiqueta1, constraints);
		constraints.gridy = 1;
		panel.add(etiqueta3, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		panel.add(etiqueta2, constraints);
		constraints.gridy = 1;
		panel.add(etiqueta4, constraints);
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void crearTrayecto(String nombreLinea) {
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
		
		JTextField distancia = new JTextField("0",5);
		JTextField duracion = new JTextField("0",5);
		JTextField cantPasajeros = new JTextField("0",5);
		JTextField costo = new JTextField("0",5);
		
		JButton boton0 = new JButton("Agregar");
		boton0.addActionListener( e -> { 
			if(!estaciones0.getSelectedItem().toString().equals(estaciones1.getSelectedItem().toString())) {
				if (bdPostre.consultaAgregarSQL(
						"INSERT INTO Ruta VALUES ('"+nombreLinea+"', "+distancia.getText()+", "+duracion.getText()+", "+costo.getText()+", "+cantPasajeros.getText()+", "+estado.isSelected()+", '"+estaciones0.getSelectedItem()+"', '"+estaciones1.getSelectedItem()+"');", 
						"Error al agregar la trayectoria.") == 1)
					JOptionPane.showMessageDialog(null, "Trayectoria agragada con exito.");
				
			}else {
				JOptionPane.showMessageDialog(null, "Estaciones iguales: tienen que ser diferente.");
			}
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
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JTextField nombre = new JTextField(20);
		JTextField horarioApertura0 = new JTextField("00",5);
		JTextField horarioApertura1 = new JTextField("00",5);
		JTextField horarioCierre0 = new JTextField("00",5);
		JTextField horarioCierre1 = new JTextField("00",5);
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
		JButton boton2 = new JButton("Page Rank");
		boton2.addActionListener( e -> pageRankEstaciones());
		JButton boton3 = new JButton("Calcular flujo");
		boton3.addActionListener( e -> flujoEstaciones());
		
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
		constraints.gridy = 5;
		panel.add(boton2, constraints);
		constraints.gridx = 1;
		panel.add(boton3, constraints);

		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(etiqueta3, constraints);
		constraints.gridy = 2;
		panel.add(etiqueta4, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
		
	}

	private void flujoEstaciones() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JComboBox<String> estaciones0 = new JComboBox<String>();
		JComboBox<String> estaciones1 = new JComboBox<String>();

		JButton boton0 = new JButton("Atras");
		boton0.addActionListener( e -> crearEstacion());

		JLabel etiqueta0 = new JLabel("Origen");
		JLabel etiqueta1= new JLabel("Destino");
		JLabel etiqueta2 = new JLabel("Flujo Maximo");
		JLabel etiqueta3 = new JLabel();

		Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
		ArrayList<String> estaciones2 = new ArrayList<String>();
		actualizarLinea(estaciones, estaciones2);
		
		for(String str : estaciones2) {
			estaciones0.addItem(str);
			estaciones1.addItem(str);
		}
		
		estaciones0.addActionListener( e -> {
			int flujo=0;
			for(LineaTransporte linea : lineas) {
				flujo+=linea.flujoMax(estaciones.get(estaciones0.getSelectedItem().toString()), estaciones.get(estaciones1.getSelectedItem().toString()));	
			}
			etiqueta3.setText(String.valueOf(flujo));
		});
		estaciones1.addActionListener( e -> {
			int flujo=0;
			for(LineaTransporte linea : lineas) {
				flujo+=linea.flujoMax(estaciones.get(estaciones0.getSelectedItem().toString()), estaciones.get(estaciones1.getSelectedItem().toString()));	
			}
			etiqueta3.setText(String.valueOf(flujo));
		});
		int flujo=0;
		for(LineaTransporte linea : lineas) {
			flujo+=linea.flujoMax(estaciones.get(estaciones0.getSelectedItem().toString()), estaciones.get(estaciones1.getSelectedItem().toString()));	
		}
		etiqueta3.setText(String.valueOf(flujo));

		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		constraints.gridy = 1;
		panel.add(estaciones0, constraints);
		constraints.gridx = 1;
		panel.add(estaciones1, constraints);
		constraints.gridy = 0;
		panel.add(etiqueta1, constraints);
		constraints.gridx = 2;
		panel.add(etiqueta2, constraints);
		constraints.gridy = 1;
		panel.add(etiqueta3, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		panel.add(boton0, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}
	
	private void pageRankEstaciones() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
		ArrayList<String> estaciones2 = new ArrayList<String>();

		JLabel etiqueta0 = new JLabel("Page Rank");
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener( e -> crearEstacion());
		

		//-------------------
		actualizarLinea(estaciones, estaciones2);

		 String titColumna[] = new String[2];
		 titColumna[0]="Estación";
		 titColumna[1]="Page Rank";
		 String datoColumna[] = new String[2];
		 ArrayList<String[]> datoColumnaList = new ArrayList<String[]>();
		DefaultTableModel modelo =  new DefaultTableModel();
		modelo.setColumnIdentifiers(titColumna);
		
		JTable tabla = new JTable();

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setFillsViewportHeight(true);
		
        tabla.setModel(modelo);
        
        modelo.setRowCount(0);
        int pageR=0;
		for(String estacion : estaciones2) {
			datoColumna[0]=estacion;
			for(LineaTransporte linea : lineas) {
				pageR+=linea.pageRank(estaciones.get(estacion));
			}
			datoColumna[1]=String.valueOf( pageR );
			datoColumnaList.add(datoColumna.clone());
			pageR=0;
		}
		datoColumnaList.sort(new Comparator<String[]>() {
		    public int compare(String[] o1, String[] o2) {
		        return o1[1].compareTo(o2[1]);
		    }
		});
		Collections.reverse(datoColumnaList);
		for(String[] str : datoColumnaList) {
			modelo.addRow(str);
		}
		JScrollPane panelTabla = new JScrollPane(tabla);
		
		panelTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		panel.add(etiqueta0, constraints);
		
		constraints.gridy = 2;
		panel.add(boton0, constraints);
		
		constraints.gridy = 1;
		constraints.ipadx = 130;
		constraints.ipady = 100;
		panel.add(panelTabla, constraints);
		
		ventanaPrincipal.setContentPane(panel);
		ventanaPrincipal.setVisible(true);
	}

	private void actualizarLinea(Map<String, Estacion> estaciones, ArrayList<String> estacionesNombre) {
		lineas.clear();
		ResultSet resultado = null;
		resultado = bdPostre.consultaSQL("SELECT nombre, horarioApertura, horarioCierre FROM Estacion WHERE abierta");
		
		try {
			while(resultado.next()) {
				estacionesNombre.add(resultado.getString(1));
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
				
				resultado2 = bdPostre.consultaSQL("SELECT distancia, duracion, costo, cantPasajeros, activa, nombre_origen, nombre_destino FROM Ruta WHERE nombre_linea='"+resultado.getString(1)+"'");
				while(resultado2.next()) {
					linea.agregarRuta(new Ruta(estaciones.get(resultado2.getString(6)), estaciones.get(resultado2.getString(7)), resultado2.getInt(1), resultado2.getInt(2), resultado2.getInt(3), resultado2.getInt(4), resultado2.getBoolean(5)));										
				}
				lineas.add(linea);
			}
		} catch (SQLException e1) {
			System.out.println("error en buscar lineas de transporte "+e1);
		}
	}
}
