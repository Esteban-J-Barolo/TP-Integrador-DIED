package appTransporte;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Ventana extends JFrame {
	
	public Ventana() {
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setTitle("Transporte Multimodal");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new InicioPanel(this));
	}

}
