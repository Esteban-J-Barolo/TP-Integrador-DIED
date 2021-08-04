package appTransporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Postgres {
	
public void accion() {
		
		String driver = null;
		String usuario = "postgre";
		String contrasenia = "nabetse";
		Connection conex = null;
		String urlDataBase = "jdbc:postgresql://localhost:5432/DIED";
		
		try {
			Class.forName(driver);
			conex = DriverManager.getConnection(urlDataBase, usuario, contrasenia);
			java.sql.Statement st = conex.createStatement();
			
			String sql = "accion sql";
			
			ResultSet result = st.executeQuery(sql);
			
			conex.close();
			st.close();
		} catch (Exception e) {
			System.out.println("no funca" + e);
		}
	}

}
