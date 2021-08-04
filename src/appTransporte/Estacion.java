package appTransporte;

import java.util.ArrayList;

public class Estacion {
	private String nombre;
    private int horarioApertura;
    private int horarioCierre;
    private boolean abierta = false;
    private ArrayList<Mantenimiento> mantenimentos = new ArrayList<Mantenimiento>();
    
    public Estacion(String nombre, int horarioApertura, int horarioCierre) {
    	this.nombre=nombre;
    	this.horarioApertura=horarioApertura;
    	this.horarioCierre=horarioCierre;
    	//crear un mentenimento de creacion
    }
    
    public void cambiarEstado() {
		if(abierta) {
			//crear mantenimiento
			this.abierta=false;
		}
		else {
			this.abierta=true;
			//finalizar mantenimiento
		}
	}
    
    public void setNombre(String nombre) {
    	this.nombre=nombre;
    }
    
    public void setHorarioApertura(int hora) {
    	this.horarioApertura=hora;
    }
    
    public void setHorarioCierre(int hora) {
    	this.horarioCierre=hora;
    }
    
    public Estacion buscar(String nombre) {
    	return null;
    }
    
    public Estacion buscar(int horario) {
    	return null;
    }

}
