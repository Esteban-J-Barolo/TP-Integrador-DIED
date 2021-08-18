package bdPostgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import appTransporte.Estacion;
import appTransporte.LineaTransporte;
import appTransporte.Ruta;

public class Postgre extends Thread {
	private static String usuario = "postgres";
	private static String contrasenia = "nabetse";
	private static Connection conex = null;
	private static String urlDataBase = "jdbc:postgresql://localhost:5432/tp_died";
	private static String driver = "org.postgresql.Driver";
	
	private void conectar() {
		try {
			Class.forName(driver);
			conex = DriverManager.getConnection(urlDataBase, usuario, contrasenia);
			JOptionPane.showMessageDialog(null, "Conección a la BD exitosa");
		} catch (Exception e) {
			System.out.println("No se pudo realizar la conección: " + e);
		}
	}
	
	public static int consultaAgregarSQL(String sql, String error) {
		try {
			java.sql.Statement st = conex.createStatement();
			st.execute(sql);
			return 1;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, error+e);
		}
		return 0;
	}
	
	public static ResultSet consultaSQL(String sql) {
		ResultSet result = null;
		try {
			java.sql.Statement st = conex.createStatement();
			result = st.executeQuery(sql);
			//st.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al consultar a la base de datos: " + e);
		}
		return result;
	}

	public void run() {
		this.conectar();
	}
	
	public static void con() {
		Postgre p = new Postgre();
		p.start();
	}
	public static void desc() {
		try {
			conex.close();
			JOptionPane.showMessageDialog(null, "Desconección a la BD exitosa");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al desconectar la base de datos: " + e);
		}
	}
	
	public static void extraerLineas(ArrayList<LineaTransporte> lineas, Map<String, Estacion> estaciones, ArrayList<String> estacionesNombre) {
		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT nombre, horarioApertura, horarioCierre FROM Estacion WHERE abierta");
		try {
			while(resultado.next()) {
				estacionesNombre.add(resultado.getString(1));
				estaciones.put(resultado.getString(1), new Estacion(resultado.getString(1), resultado.getInt(2), resultado.getInt(3)));
				}
		} catch (SQLException e1) {
			System.out.println("error en agregar a la lista de estaciones "+e1);
		}

		resultado = Postgre.consultaSQL("SELECT nombre, color, activa FROM LineaTransporte WHERE activa");
		
		try {
			while(resultado.next()) {
				LineaTransporte linea = new LineaTransporte(resultado.getString(1), resultado.getString(2), resultado.getBoolean(3));
				
				ResultSet resultado2 = null;
				
				resultado2 = Postgre.consultaSQL("SELECT distancia, duracion, costo, cantPasajeros, activa, nombre_origen, nombre_destino FROM Ruta WHERE nombre_linea='"+resultado.getString(1)+"' AND activa");
				while(resultado2.next()) {
					linea.agregarRuta(new Ruta(estaciones.get(resultado2.getString(6)), estaciones.get(resultado2.getString(7)), resultado2.getInt(1), resultado2.getInt(2), resultado2.getInt(3), resultado2.getInt(4), resultado2.getBoolean(5), resultado.getString(2)));										
				}
				lineas.add(linea);
			}
		} catch (SQLException e1) {
			System.out.println("error en buscar lineas de transporte "+e1);
		}
	}

}
