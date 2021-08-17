package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Estacion {
	private String nombre;
    private int horarioApertura;
    private int horarioCierre;
    private boolean abierta = false;
    
    public Estacion(String nombre, int horarioApertura, int horarioCierre) {
    	this.nombre=nombre;
    	this.horarioApertura=horarioApertura;
    	this.horarioCierre=horarioCierre;
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
    
    public String getNombre() {
    	return this.nombre;
    }
    
    public FiguraCirculo dibujar(String color) {
		return new FiguraCirculo(this.nombre, color);
    }

}
