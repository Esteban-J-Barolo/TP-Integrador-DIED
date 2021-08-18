package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bdPostgre.Postgre;

public class MostrarCaminosPanel extends JPanel {

	private Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
	private ArrayList<String> estacionesNombre = new ArrayList<String>();
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	private ArrayList<LineaTransporte> lineas = new ArrayList<LineaTransporte>();

	public MostrarCaminosPanel(JFrame ventana, String origen, String destino, boolean criterio) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		Postgre.extraerLineas(lineas, estaciones, estacionesNombre);
		agregarElementos(origen, destino, criterio);
	}
	
	private void agregarElementos(String origen, String destino, boolean criterio) {
		Map<String, JCheckBox> agregado = new HashMap<String, JCheckBox>();
		agregarBotones(agregado, origen, destino, criterio);
		agregarCheck(agregado);
		agregarEtiquetas();
	}
	
	private void agregarBotones(Map<String, JCheckBox> agregado, String origen, String destino, boolean criterio) {
		JButton boton0 = new JButton("Atras");
		JButton boton1 = new JButton("Siguiente");
		boton0.addActionListener(e -> {
			ventana.setContentPane( new BoletoMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		
		ArrayList<Ruta> rutaR = new ArrayList<Ruta>(), rutaC = new ArrayList<Ruta>(), rutaB = new ArrayList<Ruta>();
		buscarRutas(origen, destino, criterio, rutaR, rutaC, rutaB);
		
		if(rutaB.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay una ruta de la estación "+origen+" a la "+destino+"");
			ventana.setContentPane( new BoletoMenuPanel(ventana) );
			ventana.setVisible(true);
		}else {
			boton1.addActionListener(e -> {
				if(agregado.get("opcion1").isSelected()) {
					//mas rapido
					ventana.setContentPane( new MenuBoletoPanel(ventana, estaciones.get(origen), estaciones.get(destino), rutaR) );
					ventana.setVisible(true);
				}else {
					if(agregado.get("opcion2").isSelected()) {
						//menor distancia
						ventana.setContentPane( new MenuBoletoPanel(ventana, estaciones.get(origen), estaciones.get(destino), rutaC) );
						ventana.setVisible(true);
					}else {
						if(agregado.get("opcion3").isSelected()) {
							//mas barato
							ventana.setContentPane( new MenuBoletoPanel(ventana, estaciones.get(origen), estaciones.get(destino), rutaB) );
							ventana.setVisible(true);
						}else {
							//no seleccionao ninguno
							JOptionPane.showMessageDialog(null, "Trayecto no seleccionado.");
						}
					}
				}
				
			});
			constraints.gridx = 0;
			constraints.gridy = 6;
			add(boton0, constraints);
			constraints.gridx = 1;
			add(boton1, constraints);
		}
	}
	
	private void agregarCheck(Map<String, JCheckBox> agregado) {
		JCheckBox opcion1 = new JCheckBox("El más rápido");
		JCheckBox opcion2 = new JCheckBox("El de menor distancia");
		JCheckBox opcion3 = new JCheckBox("El más barato");
		agregado.put("opcion1", opcion1);
		agregado.put("opcion2", opcion2);
		agregado.put("opcion3", opcion3);
		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.gridy = 1;
		add(opcion1, constraints);
		constraints.gridy = 3;
		add(opcion2, constraints);
		constraints.gridy = 5;
		add(opcion3, constraints);
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Selececcione");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		add(etiqueta0, constraints);
		constraints.gridwidth = 1;
	}
	
	private void buscarRutas(String origen, String destino, boolean criterio, ArrayList<Ruta> rutaR, ArrayList<Ruta> rutaC, ArrayList<Ruta> rutaB) {
		LineaTransporte rutasLineas = new LineaTransporte("Conjunto de rutas", "infinito", true);
		
		if(criterio) {	
			int velocidad=-1, tamanio=-1, costo=-1;
			for(LineaTransporte linea : lineas) {
				ArrayList<Ruta> rutaR2 = new ArrayList<Ruta>(), rutaC2 = new ArrayList<Ruta>(), rutaB2 = new ArrayList<Ruta>();
				int velCam = linea.velocidadCamino(estaciones.get(origen), estaciones.get(destino), rutaR2);
				if( (velocidad>velCam || velocidad==-1) && velCam!=-1 ){
					velocidad=velCam;
					rutaR.clear();
					for(Ruta ru : rutaR2) rutaR.add(ru);
					
				}
				int tamCam = linea.tamCamino(estaciones.get(origen), estaciones.get(destino), rutaC2);
				if( (tamanio>tamCam || tamanio==-1) && tamCam!=-1){
					tamanio=tamCam;
					rutaC.clear();
					for(Ruta ru : rutaC2) {
						rutaC.add(ru);
					}
				}
				int costoCam = linea.costoCamino(estaciones.get(origen), estaciones.get(destino), rutaB2);
				if( (costo>costoCam || costo==-1) && costoCam!=-1 ){
					costo=costoCam;
					rutaB.clear();
					for(Ruta ru : rutaB2) {
						rutaB.add(ru);
					}
				}
			}
		}else {
			
			for(LineaTransporte linea: lineas) {
				for(Ruta ruta : linea.getRutas()) rutasLineas.agregarRuta(ruta);
			}
			rutasLineas.velocidadCamino(estaciones.get(origen), estaciones.get(destino), rutaR);
			rutasLineas.tamCamino(estaciones.get(origen), estaciones.get(destino), rutaC);
			rutasLineas.costoCamino(estaciones.get(origen), estaciones.get(destino), rutaB);
		}
		constraints.gridx = 2;
		constraints.gridy = 1;
		System.out.println(constraints.ipadx);
		System.out.println(constraints.ipady);
		constraints.ipadx = 200;
		constraints.ipady = 30;
		constraints.insets = new Insets(0, 0, 0, 0);
		add(new DibujarRutaPanel(rutaR), constraints);
		constraints.gridy = 3;
		add(new DibujarRutaPanel(rutaC), constraints);
		constraints.gridy = 5;
		add(new DibujarRutaPanel(rutaB), constraints);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
	}
	
}
