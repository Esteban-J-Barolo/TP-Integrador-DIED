package appTransporte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class FiguraFlecha extends JPanel{
	Color colorFondo = Color.BLACK;
		
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		dibujarCirculo(g2d);
	}
	
	private void dibujarCirculo(Graphics2D g2d) {
		g2d.setColor(colorFondo);
		g2d.drawString("-->", 0, 18);
	}
}
