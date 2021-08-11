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
	
	public String getColor() {
		return this.color;
	}
	
	public void cambiarEstado() {
		if(activa) this.activa=false;
		else this.activa=true;
	}
	
	public void agregarRuta(Ruta r) {
		rutas.add(r);
	}

	public String mostrarCamino(ArrayList<Estacion> camino) {
		String salida="";
		for(Estacion estacion : camino) salida=salida.concat(estacion.getNombre()+" ");
		return salida;
	}
	
	public int costoCamino(Estacion origen, Estacion destino, ArrayList<Estacion> camino) {
		ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
		for(Ruta ruta : rutas) ruCop.add(ruta);
		return caminoCortoCosto(ruCop, origen, destino, 0, camino);
	}
	
	private int caminoCortoCosto(ArrayList<Ruta> ru, Estacion origen, Estacion destino, int tam, ArrayList<Estacion> camino) {
		ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado2 = new ArrayList<Ruta>();
		if (!ru.isEmpty()) {
			for (Ruta ruta : ru) {
				if(ruta.isOrigen(origen)) {					
					if(ruta.isDestino(destino)) encontrado2.add(ruta);
					else encontrado.add(ruta);
				}
			}
			for(Ruta ruta : ru) ruCop.add(ruta);
			for(Ruta ruta : encontrado) ruCop.remove(ruta);
			ArrayList<Estacion> caminoCop = new ArrayList<Estacion>(), caminoRet = null, caminoRet2 = new ArrayList<Estacion>();
			for(Estacion cam : camino) caminoCop.add(cam);
			if(encontrado2.size()==1 || encontrado.size()>0) {
				int r1=-1, r2=-1;
				if( encontrado2.size()==1 ) {
					r1=encontrado2.get(0).costo()+tam;
					caminoRet2.add(destino);
				}
				if(encontrado.size()>0) {
					if(encontrado.size()==1) {
						caminoRet = caminoCop;
						r2 = caminoCortoCosto(ruCop, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).costo(), caminoCop);
					}else {
						caminoRet = caminoCop;
						r2 = caminoCortoCosto(ruCop, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).costo(), caminoCop);
						for(Ruta ruta : encontrado) {
							ArrayList<Ruta> ruCop2 = new ArrayList<Ruta>();
							for(Ruta ruta2 : ru) ruCop2.add(ruta2);
							for(Ruta ruta3 : encontrado) ruCop2.remove(ruta3);
							ArrayList<Estacion> caminoCop2 = new ArrayList<Estacion>();
							for(Estacion cam : camino) caminoCop2.add(cam);
							int a = caminoCortoCosto(ruCop2, ruta.estacionDestino(), destino, tam+ruta.costo(), caminoCop2);
							if( (a < r2 || r2==-1) && a!=-1 ) {
								r2=a;
								caminoRet = caminoCop2;
							}
						}
					}
				}
				if(r1==-1) {
					if(r2!=-1) {
						camino.add(origen);
						for(Estacion estacion : caminoRet) camino.add(estacion);
					}
					return r2;
				}else {
					if(r2==-1) {
						camino.add(origen);
						for(Estacion estacion : caminoRet2) camino.add(estacion);
						return r1;
					}else {
						camino.add(origen);
						if(r1<r2) {
							for(Estacion estacion : caminoRet2) camino.add(estacion);
							return r1;
						}
						else {
							for(Estacion estacion : caminoRet) camino.add(estacion);
							return r2;
						}
					}
				}
			}else {
				return -1;
			}
		}else {
			return -1;
		}
	}

	public int velocidadCamino(Estacion origen, Estacion destino, ArrayList<Estacion> camino) {
		ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
		for(Ruta ruta : rutas) ruCop.add(ruta);
		return caminoCortoDuracion(ruCop, origen, destino, 0, camino);
	}
	
	private int caminoCortoDuracion(ArrayList<Ruta> ru, Estacion origen, Estacion destino, int tam, ArrayList<Estacion> camino) {
		ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado2 = new ArrayList<Ruta>();
		if (!ru.isEmpty()) {
			for (Ruta ruta : ru) {
				if(ruta.isOrigen(origen)) {					
					if(ruta.isDestino(destino)) encontrado2.add(ruta);
					else encontrado.add(ruta);
				}
			}
			for(Ruta ruta : ru) ruCop.add(ruta);
			for(Ruta ruta : encontrado) ruCop.remove(ruta);
			ArrayList<Estacion> caminoCop = new ArrayList<Estacion>(), caminoRet = null, caminoRet2 = new ArrayList<Estacion>();
			for(Estacion cam : camino) caminoCop.add(cam);
			if(encontrado2.size()==1 || encontrado.size()>0) {
				int r1=-1, r2=-1;
				if( encontrado2.size()==1 ) {
					r1=encontrado2.get(0).duracion()+tam;
					caminoRet2.add(destino);
				}
				if(encontrado.size()>0) {
					if(encontrado.size()==1) {
						caminoRet = caminoCop;
						r2 = caminoCortoDuracion(ruCop, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).duracion(), caminoCop);
					}else {
						caminoRet = caminoCop;
						r2 = caminoCortoDuracion(ruCop, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).duracion(), caminoCop);
						for(Ruta ruta : encontrado) {
							ArrayList<Ruta> ruCop2 = new ArrayList<Ruta>();
							for(Ruta ruta2 : ru) ruCop2.add(ruta2);
							for(Ruta ruta3 : encontrado) ruCop2.remove(ruta3);
							ArrayList<Estacion> caminoCop2 = new ArrayList<Estacion>();
							for(Estacion cam : camino) caminoCop2.add(cam);
							int a = caminoCortoDuracion(ruCop2, ruta.estacionDestino(), destino, tam+ruta.duracion(), caminoCop2);
							if( (a < r2 || r2==-1) && a!=-1 ) {
								r2=a;
								caminoRet = caminoCop2;
							}
						}
					}
				}
				if(r1==-1) {
					if(r2!=-1) {
						camino.add(origen);
						for(Estacion estacion : caminoRet) camino.add(estacion);
					}
					return r2;
				}else {
					if(r2==-1) {
						camino.add(origen);
						for(Estacion estacion : caminoRet2) camino.add(estacion);
						return r1;
					}else {
						camino.add(origen);
						if(r1<r2) {
							for(Estacion estacion : caminoRet2) camino.add(estacion);
							return r1;
						}
						else {
							for(Estacion estacion : caminoRet) camino.add(estacion);
							return r2;
						}
					}
				}
			}else {
				return -1;
			}
		}else {
			return -1;
		}
	}

	public int tamCamino(Estacion origen, Estacion destino, ArrayList<Estacion> camino) {
		ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
		for(Ruta ruta : rutas) ruCop.add(ruta);
			return caminoCortoTam(ruCop, origen, destino, 0, camino);
	}
	
	private int caminoCortoTam(ArrayList<Ruta> ru, Estacion origen, Estacion destino, int tam, ArrayList<Estacion> camino) {
		ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado2 = new ArrayList<Ruta>();
		if (!ru.isEmpty()) {
			for (Ruta ruta : ru) {
				if(ruta.isOrigen(origen)) {					
					if(ruta.isDestino(destino)) encontrado2.add(ruta);
					else encontrado.add(ruta);
				}
			}
			for(Ruta ruta : ru) ruCop.add(ruta);
			for(Ruta ruta : encontrado) ruCop.remove(ruta);
			ArrayList<Estacion> caminoCop = new ArrayList<Estacion>(), caminoRet = null, caminoRet2 = new ArrayList<Estacion>();
			for(Estacion cam : camino) caminoCop.add(cam);
			if(encontrado2.size()==1 || encontrado.size()>0) {
				int r1=-1, r2=-1;
				if( encontrado2.size()==1 ) {
					r1=encontrado2.get(0).tam()+tam;
					caminoRet2.add(destino);
				}
				if(encontrado.size()>0) {
					if(encontrado.size()==1) {
						caminoRet = caminoCop;
						r2 = caminoCortoTam(ruCop, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).tam(), caminoCop);
					}else {
						caminoRet = caminoCop;
						r2 = caminoCortoTam(ruCop, encontrado.get(0).estacionDestino(), destino, tam+encontrado.get(0).tam(), caminoCop);
						for(Ruta ruta : encontrado) {
							ArrayList<Ruta> ruCop2 = new ArrayList<Ruta>();
							for(Ruta ruta2 : ru) ruCop2.add(ruta2);
							for(Ruta ruta3 : encontrado) ruCop2.remove(ruta3);
							ArrayList<Estacion> caminoCop2 = new ArrayList<Estacion>();
							for(Estacion cam : camino) caminoCop2.add(cam);
							int a = caminoCortoTam(ruCop2, ruta.estacionDestino(), destino, tam+ruta.tam(), caminoCop2);
							if( (a < r2 || r2==-1) && a!=-1 ) {
								r2=a;
								caminoRet = caminoCop2;
							}
						}
					}
				}
				if(r1==-1) {
					if(r2!=-1) {
						camino.add(origen);
						for(Estacion estacion : caminoRet) camino.add(estacion);
					}
					return r2;
				}else {
					if(r2==-1) {
						camino.add(origen);
						for(Estacion estacion : caminoRet2) camino.add(estacion);
						return r1;
					}else {
						camino.add(origen);
						if(r1<r2) {
							for(Estacion estacion : caminoRet2) camino.add(estacion);
							return r1;
						}
						else {
							for(Estacion estacion : caminoRet) camino.add(estacion);
							return r2;
						}
					}
				}
			}else {
				return -1;
			}
		}else {
			return -1;
		}
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public JPanel dibujar(ArrayList<Estacion> camino) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		int cant=0, ix;
		GridBagConstraints constraints = new GridBagConstraints();
		for (Estacion estacion : camino) {
			if(cant!=0) {
				constraints.gridx = cant;
				constraints.ipadx = 10;
				panel.add(new FiguraFlecha(), constraints);
				cant++;
			}
			ix = 15+estacion.getNombre().length()*8;
			constraints.gridx = cant;
			constraints.gridy = 0;
			constraints.ipadx = ix;
			constraints.ipady = 30;
			panel.add(estacion.dibujar(), constraints);
			cant++;
		}
		
		return panel;
	}

	public int flujoMax(Estacion origen, Estacion destino) {
		if(origen.equals(destino)) {
			return 0;
		}else {
			int cant=0;
			ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
			for(Ruta ruta : rutas) {
				if(ruta.isOrigen(origen)) {
					cant=ruta.cantPasajeros();
				}
				ruCop.add(ruta);
			}
			return flujoMaxRep(ruCop, origen, destino, cant);
		}
	}

	private int flujoMaxRep(ArrayList<Ruta> ru, Estacion origen, Estacion destino, int cant) {
		ArrayList<Ruta> encontrado = new ArrayList<Ruta>();
		ArrayList<Ruta> encontrado2 = new ArrayList<Ruta>();
		if (!ru.isEmpty()) {
			for (Ruta ruta : ru) {
				if(ruta.isOrigen(origen)) {					
					if(ruta.isDestino(destino)) encontrado2.add(ruta);
					else encontrado.add(ruta);
				}
			}
			if(encontrado2.size()==1 || encontrado.size()>0) {
				int r=0, c=0;
				if( encontrado2.size()==1 ) {
					c=encontrado2.get(0).cantPasajeros();
					if(c>cant) {
						c=cant;
					}
				}
				if(encontrado.size()>0) {
					if(encontrado.size()==1) {
						ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
						for(Ruta ruta : ru) ruCop.add(ruta);
						for(Ruta ruta : encontrado) ruCop.remove(ruta);
						r=flujoMaxRep(ruCop, encontrado.get(0).estacionDestino(), destino, cant);
						if(r>cant && r!=0) {
							r=cant;
						}
					}else {
						for(Ruta ruta : encontrado) {
							ArrayList<Ruta> ruCop = new ArrayList<Ruta>();
							for(Ruta ruta1 : ru) ruCop.add(ruta1);
							for(Ruta ruta1 : encontrado) ruCop.remove(ruta1);
							r=flujoMaxRep(ruCop, ruta.estacionDestino(), destino, cant);
							if(r>cant && r!=0) {
								r=cant;
							}
						}
					}
				}
				if(r<c && c!=0) return c;
				else return r;
			}else {
				return 0;
			}
		}else {
			return 0;
		}
	}

	public int pageRank(Estacion estacion) {
		int cant=0;
		for(Ruta ru : rutas) if(ru.isDestino(estacion)) cant++;
		return cant;
	}
}
