package appTransporte;

public class Ruta {
	private int distancia;//km
	private int duracion;//tiempo en minutos
	private int cantPasajeros;//maxima
	private boolean activa;
	private int costo;//moneda
	private Estacion origen;
	private Estacion destino;
	private String color;
	
	public Ruta(Estacion origen, Estacion destino, int distancia, int duracion, int costo, int cantPasajeros, boolean activa, String color) {
		this.origen=origen;
		this.destino=destino;
		this.distancia=distancia;
		this.duracion=duracion;
		this.costo=costo;
		this.cantPasajeros=cantPasajeros;
		this.activa=activa;
		this.color=color;
	}
	
	public void cambiarEstado() {
		if(activa) this.activa=false;
		else this.activa=true;
	}

	public boolean isOrigen(Estacion origen2) {
		return this.origen.getNombre().equals(origen2.getNombre());
	}
	
	public boolean isDestino(Estacion destino2) {
		return this.destino.getNombre().equals(destino2.getNombre());
	}

	public Estacion estacionDestino() {
		return this.destino;
	}
	public Estacion estacionOrigen() {
		return this.origen;
	}
	
	public int costo() {
		return this.costo;
	}

	public int duracion() {
		return this.duracion;
	}

	public int tam() {
		return this.distancia;
	}
	
	public int cantPasajeros() {
		return this.cantPasajeros;
	}

	public String getColor() {
		return this.color;
	}	
}
