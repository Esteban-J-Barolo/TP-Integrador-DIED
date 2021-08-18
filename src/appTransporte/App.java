package appTransporte;

import bdPostgre.Postgre;

public class App {
	
	public static void main(String[] args) {
		Ventana v1 = new Ventana();
		Postgre.con();
		v1.setVisible(true);
	}

}
