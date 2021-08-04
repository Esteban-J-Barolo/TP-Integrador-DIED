package bdPostgre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Postgre extends Thread {
	private String usuario = "postgres";
	private String contrasenia = "nabetse";
	private Connection conex = null;
	private String urlDataBase = "jdbc:postgresql://localhost:5432/tp_died";
	private String driver = "org.postgresql.Driver";
	
	private void conectar() {
		try {
			Class.forName(driver);
			conex = DriverManager.getConnection(urlDataBase, usuario, contrasenia);
			JOptionPane.showMessageDialog(null, "Conección a la BD exitosa");
		} catch (Exception e) {
			System.out.println("No se pudo realizar la conección: " + e);
		}
	}
	
	public void desconectar() {
		try {
			this.conex.close();
			JOptionPane.showMessageDialog(null, "Desconección a la BD exitosa");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al desconectar la base de datos: " + e);
		}
	}
	
	public int consultaAgregarSQL(String sql, String error) {
		try {
			java.sql.Statement st = conex.createStatement();
			st.execute(sql);
			return 1;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, error+e);
		}
		return 0;
	}
	
	public ResultSet consultaSQL(String sql) {
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
	
	

}
