package appTransporte;

import java.util.HashMap;
import java.util.Map;

public class MainPrue {

	public static void main(String[] args) {
		LineaTransporte l1 = new LineaTransporte("L1", "Rojo", true);
		Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
		estaciones.put("E1", new Estacion("E1", 0, 0));
		estaciones.put("E2", new Estacion("E2", 0, 0));
		estaciones.put("E3", new Estacion("E3", 0, 0));
		estaciones.put("E4", new Estacion("E4", 0, 0));
		estaciones.put("E5", new Estacion("E5", 0, 0));
		l1.agregarRuta(new Ruta(estaciones.get("E1"), estaciones.get("E2"), 9, 1, 9, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E1"), estaciones.get("E3"), 1, 9, 1, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E2"), estaciones.get("E3"), 9, 9, 9, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E2"), estaciones.get("E4"), 9, 1, 9, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E3"), estaciones.get("E4"), 1, 9, 9, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E3"), estaciones.get("E5"), 9, 9, 1, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E4"), estaciones.get("E5"), 1, 1, 9, 9, true));
		l1.agregarRuta(new Ruta(estaciones.get("E4"), estaciones.get("E1"), 9, 9, 9, 9, true));
		
		System.out.println(l1.tamCamino(estaciones.get("E1"), estaciones.get("E5")));
		
	}

}
