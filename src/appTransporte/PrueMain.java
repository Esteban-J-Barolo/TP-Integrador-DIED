package appTransporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrueMain {
	
	public static void main(String[] args) {
		ArrayList<Ruta> camino = new ArrayList<Ruta>();
		ArrayList<String> ests = new ArrayList<String>();
		Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
		ests.add("E1");
		ests.add("E2");
		ests.add("E3");
		ests.add("E4");
		ests.add("E5");
		ests.add("E6");
		estaciones.put("E1", new Estacion("E1", 0, 0));
		estaciones.put("E2", new Estacion("E2", 0, 0));
		estaciones.put("E3", new Estacion("E3", 0, 0));
		estaciones.put("E4", new Estacion("E4", 0, 0));
		estaciones.put("E5", new Estacion("E5", 0, 0));
		estaciones.put("E6", new Estacion("E6", 0, 0));
		LineaTransporte linea = new LineaTransporte("L1", null, false);
		linea.agregarRuta(new Ruta(estaciones.get("E1"), estaciones.get("E2"), 0, 0, 4, 0, false, "Rojo"));
		linea.agregarRuta(new Ruta(estaciones.get("E2"), estaciones.get("E3"), 0, 0, 1, 0, false, "Verde"));
		linea.agregarRuta(new Ruta(estaciones.get("E1"), estaciones.get("E3"), 0, 0, 2, 0, false, "Amarillo"));
		linea.agregarRuta(new Ruta(estaciones.get("E3"), estaciones.get("E1"), 0, 0, 3, 0, false, "Azul"));
		Map<String, Double> pageRank = linea.pageRank(ests);
		for(String est : ests) System.out.println(est+"->"+pageRank.get(est));
		
	}

}
