package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bdPostgre.Postgre;	

public class MenuBoletoPanel extends JPanel {

	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;

	public MenuBoletoPanel(JFrame ventana, Estacion origen, Estacion destino, ArrayList<Ruta> camino) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		agregarElementos(origen, destino, camino);
	}
	
	private void agregarElementos(Estacion origen, Estacion destino, ArrayList<Ruta> camino) {
		Map<String, JTextField> agregado = new HashMap<String, JTextField>();
		int num=0;
		agregarBotones(agregado, origen, destino, num, camino);
		agregarCampText(agregado);
		agregarEtiquetas(origen, destino, camino, num);
	}
	
	private void agregarBotones(Map<String, JTextField> agregado, Estacion origen, Estacion destino, int num, ArrayList<Ruta> camino) {
		JButton boton0 = new JButton("Atras");
		JButton boton1 = new JButton("Aceptar");
		boton0.addActionListener(e -> {
			ventana.setContentPane( new BoletoMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		boton1.addActionListener(e -> {
			//crear boleto en la base de datos
			if (Postgre.consultaAgregarSQL("INSERT INTO Boleto VALUES ("+ num +", '"+origen.getNombre()+"', '"+destino.getNombre()+"', '"+agregado.get("correo").getText()+"', '"+agregado.get("nombre").getText()+"', '"+LocalDate.now().toString()+"', "+costoCamino(camino)+" );", "Boleto error.") == 1)
				JOptionPane.showMessageDialog(null, "Boleto creado.");
			ventana.setContentPane( new BoletoMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		constraints.gridx = 0;
		constraints.gridy = 8;
		add(boton0, constraints);
		constraints.gridx = 1;
		add(boton1, constraints);
	}
	
	private void agregarCampText(Map<String, JTextField> agregado) {
		JTextField correo = new JTextField(20);
		JTextField nombre = new JTextField(20);
		agregado.put("correo", correo);
		agregado.put("nombre", nombre);
		constraints.gridy = 1;
		constraints.gridx = 1;
		add(correo, constraints);
		constraints.gridy = 2;
		add(nombre, constraints);
	}
	
	private void agregarEtiquetas(Estacion origen, Estacion destino, ArrayList<Ruta> camino, int num) {
		int numBoleto = 0;
		ResultSet resultado = null;
		resultado = Postgre.consultaSQL("SELECT numBoleto FROM Boleto ORDER BY 1 DESC LIMIT 1");
		try {
			if(resultado.next()) {
				numBoleto=resultado.getInt(1);
				}
		} catch (SQLException e1) {
			System.out.println("error en buscar el numero de boleto "+e1);
		}
		num = numBoleto+1;
		JLabel etiqueta0 = new JLabel("Nro. de Boleto: ");
		JLabel etiqueta1 = new JLabel("Correo electrónico del cliente: ");
		JLabel etiqueta2 = new JLabel("Nombre del cliente: ");
		JLabel etiqueta3 = new JLabel("Fecha de la venta: ");
		JLabel etiqueta4 = new JLabel("Estación Origen: ");
		JLabel etiqueta5 = new JLabel("Estación Destino: ");
		JLabel etiqueta6 = new JLabel("Camino a seguir: ");
		JLabel etiqueta7 = new JLabel("Costo del boleto: ");
		JLabel etiqueta00 = new JLabel(String.valueOf(num));
		JLabel etiqueta30 = new JLabel(LocalDate.now().toString());
		JLabel etiqueta40 = new JLabel(origen.getNombre());
		JLabel etiqueta50 = new JLabel(destino.getNombre());
		JLabel etiqueta60 = new JLabel(this.mostrarCamino(camino));
		JLabel etiqueta70 = new JLabel( String.valueOf(this.costoCamino(camino)) );
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
		constraints.gridy = 1;
		add(etiqueta1, constraints);
		constraints.gridy = 2;
		add(etiqueta2, constraints);
		constraints.gridy = 3;
		add(etiqueta3, constraints);
		constraints.gridy = 4;
		add(etiqueta4, constraints);
		constraints.gridy = 5;
		add(etiqueta5, constraints);
		constraints.gridy = 6;
		add(etiqueta6, constraints);
		constraints.gridy = 7;
		add(etiqueta7, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		add(etiqueta00, constraints);
		constraints.gridy = 3;
		add(etiqueta30, constraints);
		constraints.gridy = 4;
		add(etiqueta40, constraints);
		constraints.gridy = 5;
		add(etiqueta50, constraints);
		constraints.gridy = 6;
		add(etiqueta60, constraints);
		constraints.gridy = 7;
		add(etiqueta70, constraints);
	}

	private int costoCamino(ArrayList<Ruta> camino) {
		int ret=0;
		for(Ruta ru : camino) ret+=ru.costo();
		return ret;
	}

	private String mostrarCamino(ArrayList<Ruta> camino) {
		String salida="";
		String salida2="";
		for(Ruta estacion : camino) {
			salida=salida.concat(estacion.estacionOrigen().getNombre()+" ");
			salida2=estacion.estacionDestino().getNombre();
		}
		return salida+salida2;
	}
}
