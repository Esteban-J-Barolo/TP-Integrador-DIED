package appTransporte;

public class Ruta {
	private int distancia;//km
	private int duracion;//tiempo en minutos
	private int cantPasajeros;//maxima
	private boolean activa;
	private int costo;//moneda
	private Estacion origen;
	private Estacion destino;
	
	public Ruta(Estacion origen, Estacion destino, int distancia, int duracion, int costo, int cantPasajeros, boolean activa) {
		this.origen=origen;
		this.destino=destino;
		this.distancia=distancia;
		this.duracion=duracion;
		this.costo=costo;
		this.cantPasajeros=cantPasajeros;
		this.activa=activa;
	}
	
	public void cambiarEstado() {
		if(activa) this.activa=false;
		else this.activa=true;
	}
	
	
}
