package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class LineaTransporte {
	private String nombre;
	private String color;
	private boolean activa = false;
	private ArrayList<Ruta> rutas = new ArrayList<Ruta>();
	
	public LineaTransporte(String nombre, String color, boolean estado) {
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

	public String mostrarCamino(Estacion origen, Estacion destino) {
		Estacion actual=origen;
		for (Ruta ruta : rutas) {
			if(ruta.isOrigen(actual)) {
				actual=ruta.estacionDestino();
				if(ruta.isDestino(destino)) {
					return origen.getNombre()+", "+destino.getNombre();
				}else {
					return origen.getNombre()+", "+mostrarCamino(actual, destino);
				}
			}
		}
		return "No hay ruta.";
	}

	public int costoCamino(Estacion origen, Estacion destino) {
		Estacion actual=origen;
		for (Ruta ruta : rutas) {
			if(ruta.isOrigen(actual)) {
				actual=ruta.estacionDestino();
				if(ruta.isDestino(destino)) {
					return ruta.costo();
				}else {
					return ruta.costo()+costoCamino(actual, destino);
				}
			}
		}
		return 10000;
	}

	public int velocidadCamino(Estacion origen, Estacion destino) {
		Estacion actual=origen;
		for (Ruta ruta : rutas) {
			if(ruta.isOrigen(actual)) {
				actual=ruta.estacionDestino();
				if(ruta.isDestino(destino)) {
					return ruta.duracion();
				}else {
					return ruta.duracion()+costoCamino(actual, destino);
				}
			}
		}
		return 10000;
	}

	public int tamCamino(Estacion origen, Estacion destino) {
		ArrayList<Ruta> ru = rutas;
		return caminoCorto(ru, origen, destino, 0);
	}
	
	private int caminoCorto(ArrayList<Ruta> ru, Estacion origen, Estacion destino, int tam) {
		ArrayList<Ruta> encontrado = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado2 = new ArrayList<Ruta>();
		if (!ru.isEmpty()) {
			
			for (Ruta ruta : ru) {
				if(ruta.isOrigen(origen)) {
					
					if(ruta.isDestino(destino)) {
						encontrado2.add(ruta);
					}else {
						encontrado.add(ruta);
					}
					
				}
			}
			
			if(encontrado2.size()>0 || encontrado.size()>0) {
				int r1=-1, r2=-1;
				if(encontrado2.size()>0) {
					int a = encontrado2.get(0).tam();
					encontrado2.remove(0);
					System.out.println(a+" a s");
					for(Ruta ruta : encontrado2) {
						if( ruta.tam() < a ) a=ruta.tam();
					}
					r1= a+tam;
				}
				if(encontrado.size()>0) {
					int s=0;
					if(encontrado.size()==1) {
						ru.remove(encontrado.get(0));
						r2 = caminoCorto(ru, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).tam());
					}else {
						s = caminoCorto(ru, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).tam());
						for(Ruta ruta : encontrado) {
							ru.remove(ruta);
							int a = caminoCorto(ru, ruta.estacionDestino(), destino, tam+ruta.tam());
							if( s > a ) {
								s = a;
							}
						}
						r2 = s+tam;
					}
				}
				System.out.println(r1+" - "+r2);
				if(r1==-1) {
					return r2;
				}else {
					if(r2==-1) {
						return r1;
					}else {
						if(r1<r2)
							return r1;
						else
							return r2;
					}
				}
				
			}else {System.out.println("q");
				return 50000;
			}
		}else {System.out.println("q2");
			return 50000;
		}
	}
	
	public int cantCamino(Estacion origen, Estacion destino) {
		Estacion actual=origen;
		for (Ruta ruta : rutas) {
			if(ruta.isOrigen(actual)) {
				actual=ruta.estacionDestino();
				if(ruta.isDestino(destino)) {
					return 2;
				}else {
					return 1+tamCamino(actual, destino);
				}
			}
		}
		return 10;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public JPanel dibujar(Estacion origen, Estacion destino) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		dibuRep(panel, origen, destino, 0);
		
		return panel;
	}
	
	private void dibuRep(JPanel pan, Estacion origen, Estacion destino, int cant) {

		GridBagConstraints constraints = new GridBagConstraints();
		for (Ruta ruta : rutas) {

			int ix = 15+origen.getNombre().length()*8;
			constraints.gridx = cant;
			constraints.gridy = 0;
			constraints.ipadx = ix;
			constraints.ipady = 30;
			
			if(ruta.isOrigen(origen)) {
				pan.add(origen.dibujar(), constraints);
				cant++;
				constraints.gridx = cant;
				constraints.ipadx = 10;
				pan.add(new FiguraFlecha(), constraints);
				cant++;
				if(ruta.isDestino(destino)) {
					cant++;
					constraints.gridx = cant;
					ix = 15+destino.getNombre().length()*8;
					constraints.ipadx = ix;
					pan.add(destino.dibujar(), constraints);
				}else {
					dibuRep(pan, ruta.estacionDestino(), destino, cant);
				}
			}
			
		}
		
	}

}
