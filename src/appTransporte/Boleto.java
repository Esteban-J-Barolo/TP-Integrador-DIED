package appTransporte;

import java.time.LocalDate;
import java.util.ArrayList;

public class Boleto {
	private String origen;
	private String destino;
	private int numBoleto;
	private String emailCliente;
	private String nombreCliente;
	private LocalDate venta;
	private ArrayList<Estacion> recorrido;
	private int costo;

}
