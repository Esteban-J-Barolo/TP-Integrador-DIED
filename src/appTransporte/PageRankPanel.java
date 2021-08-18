package appTransporte;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import bdPostgre.Postgre;	

public class PageRankPanel extends JPanel {
	
	private Map<String, Estacion> estaciones = new HashMap<String, Estacion>();
	private ArrayList<String> estacionesNombre = new ArrayList<String>();
	private GridBagConstraints constraints = new GridBagConstraints();
	private JFrame ventana=null;
	private ArrayList<LineaTransporte> lineas = new ArrayList<LineaTransporte>();

	public PageRankPanel(JFrame ventana) {
		this.ventana=ventana;
		setLayout(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		Postgre.extraerLineas(lineas, estaciones, estacionesNombre);
		agregarElementos();
	}
	
	private void agregarElementos() {
		agregarBotones();
		agregarEtiquetas();
		agregarTabla();
	}
	
	private void agregarBotones() {
		JButton boton0 = new JButton("Atras");
		boton0.addActionListener( e -> {
			ventana.setContentPane( new EstacionMenuPanel(ventana) );
			ventana.setVisible(true);
		});
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(boton0, constraints);
	}
	
	private void agregarTabla() {

		String titColumna[] = new String[2];
		titColumna[0]="Estación";
		titColumna[1]="Page Rank";
		String datoColumna[] = new String[2];
		ArrayList<String[]> datoColumnaList = new ArrayList<String[]>();
		DefaultTableModel modelo =  new DefaultTableModel();
		modelo.setColumnIdentifiers(titColumna);
		
		JTable tabla = new JTable();
		
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setFillsViewportHeight(true);
        tabla.setModel(modelo);
        modelo.setRowCount(0);
		LineaTransporte rutasLineas = new LineaTransporte("Conjunto de rutas", "infinito", true);
        for(LineaTransporte linea: lineas) {
			for(Ruta ruta : linea.getRutas()) rutasLineas.agregarRuta(ruta);
		}
		Map<String, Double> pageR = rutasLineas.pageRank(estacionesNombre);
		for(String estacion : estacionesNombre) {
			datoColumna[0]=estacion;
			datoColumna[1]=new DecimalFormat("#0.0#").format( pageR.get(estacion) );
			datoColumnaList.add(datoColumna.clone());
		}
		datoColumnaList.sort(new Comparator<String[]>() {
		    public int compare(String[] o1, String[] o2) {
		        return o1[1].compareTo(o2[1]);
		    }
		});
		Collections.reverse(datoColumnaList);
		for(String[] str : datoColumnaList) {
			modelo.addRow(str);
		}
		JScrollPane panelTabla = new JScrollPane(tabla);
		panelTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.ipadx = 130;
		constraints.ipady = 100;
		add(panelTabla, constraints);
	}
	
	private void agregarEtiquetas() {
		JLabel etiqueta0 = new JLabel("Page Rank");
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(etiqueta0, constraints);
	}
}
