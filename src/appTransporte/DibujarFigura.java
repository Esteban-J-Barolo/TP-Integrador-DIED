package appTransporte;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DibujarFigura {
	public static void main(String[] args) {
		JFrame jframe = new JFrame("Ejemplo Grafico");

		PanelFigura mc = new PanelFigura();
		jframe.add(mc); // lo agrega en el centro
		jframe.pack(); //SIN ESTE METODO NO se ve la ventana.
		jframe.setSize(500, 450);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		}
}
