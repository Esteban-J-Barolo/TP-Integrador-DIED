package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DibujarRutaPanel extends JPanel {

		private GridBagConstraints constraints = new GridBagConstraints();

		public DibujarRutaPanel(ArrayList<Ruta> ruta) {
			setLayout(new GridBagLayout());
			agregarElementos(ruta);
		}
		
		private void agregarElementos(ArrayList<Ruta> ruta) {
			agregarEstaciones(ruta);
		}
		
		private void agregarEstaciones(ArrayList<Ruta> camino) {
			int cant=0, ix;
			for (Ruta estacion : camino) {
				if(cant!=0) {
					constraints.gridx = cant;
					constraints.ipadx = 10;
					add(new FiguraFlecha(), constraints);
					cant++;
				}else {
					ix = 15+estacion.estacionOrigen().getNombre().length()*8;
					constraints.gridx = cant;
					constraints.gridy = 0;
					constraints.ipadx = ix;
					constraints.ipady = 30;
					add(estacion.estacionOrigen().dibujar(estacion.getColor()), constraints);
					cant++;

					constraints.gridx = cant;
					constraints.ipadx = 10;
					add(new FiguraFlecha(), constraints);
					cant++;
				}
				ix = 15+estacion.estacionDestino().getNombre().length()*8;
				constraints.gridx = cant;
				constraints.gridy = 0;
				constraints.ipadx = ix;
				constraints.ipady = 30;
				add(estacion.estacionDestino().dibujar(estacion.getColor()), constraints);
				cant++;
		}
	}
}
