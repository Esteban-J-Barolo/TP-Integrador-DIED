package appTransporte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class PanelFigura extends JPanel{
	Color colorFondo = Color.GREEN;
	Color colorBorde = Color.BLACK;
	Color colorLetras = Color.BLACK;
	private int squareX = 0;//Posicion en todo el panel
	private int squareY = 0;
	private int squareW = 15;
	private int squareH = 30;
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		dibujarCirculo(g2d,"Q1");
	}
	
	public void posicion(int x, int y) {
		this.squareX=x;
		this.squareY=y;
	}

	private void dibujarCirculo(Graphics2D g2d, String nombre) {
		g2d.setColor(colorFondo);
		g2d.fillOval(squareX,squareY,squareW+nombre.length()*8,squareH);
		g2d.setColor(colorBorde);
		g2d.drawOval(squareX,squareY,squareW+nombre.length()*8,squareH);
		g2d.setColor(colorLetras);
		g2d.drawString(nombre, squareX+squareW-5, squareY+squareH-10);
		
	}

}
