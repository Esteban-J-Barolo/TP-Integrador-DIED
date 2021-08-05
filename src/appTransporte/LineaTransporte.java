package appTransporte;

import java.util.ArrayList;

public class LineaTransporte {
	private String nombre;
	private String color;
	private boolean activa = false;
	private ArrayList<Ruta> rutas = new ArrayList<Ruta>();
	
	public LineaTransporte(String nombre, String color, Boolean estado) {
		this.nombre=nombre;
		this.color=color;
		this.activa=estado;
	}
	
	public void setNombre(String nombre) {
		this.nombre=nombre;
	}
	
	public void setColor(String color) {
		this.color=color;
	}
	
	public void cambiarEstado() {
		if(activa) this.activa=false;
		else this.activa=true;
	}
	
	public void agregarRuta(Ruta r) {
		rutas.add(r);
	}

}
