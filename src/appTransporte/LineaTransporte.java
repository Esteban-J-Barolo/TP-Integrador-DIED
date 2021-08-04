package appTransporte;

public class LineaTransporte {
	private String nombre;
	private String color;
	private boolean activa = false;
	private Trayecto trayecto;
	
	public LineaTransporte(String nombre, String color) {
		this.nombre=nombre;
		this.color=color;
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
	
	public LineaTransporte buscarN(String nombre) {
		return null;
	}
	
	public LineaTransporte buscarC(String color) {
		return null;
	}

}
