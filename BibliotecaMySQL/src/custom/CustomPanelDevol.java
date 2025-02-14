package custom;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.opencsv.CSVWriter;

import design.GradientPanel;
import design.VerticalFlowLayout;
import objeto.Devolucion;
import objeto.Libro;
import objeto.Prestamo;
import objetoImp.AtacanteBDImp;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class CustomPanelDevol extends JPanel{

	private String usuario;
	private String pass;
	private JLabel nomusuario;
	private JPanel estePanelD;
	private JScrollPane jscrollP;
	private JTable jtablapre;
	private JTextField jtbuscarPre;
	private TableRowSorter sorterPre;
	private AtacanteBDImp atacanteImp;
	public DefaultTableModel modeldevol;
	private JButton btnDevol, btnGenerar;
	private JComboBox<String> comboestado = new JComboBox<String>(Mine.ESTADOS);
	private final String FICHEROH = Mine.CURRENTDIR+"/ficheros/devoluciones.csv";
	private final String[] columnsPre = {"Alumno", "Libro", "Fecha_Prestamo", "Fecha_Devolucion","Estado"};	
	
	public CustomPanelDevol(String usuario, String pass) {
		this.usuario = usuario;
		this.pass = pass;
		agregarElementos();				
		controlEventos();
	}
	
	private void agregarElementos() {
		atacanteImp = new AtacanteBDImp(this.usuario, this.pass);
		
		estePanelD = new GradientPanel(Mine.FAVCOLORROSE, Mine.FAVCOLORBLACK);
		estePanelD.setLayout(new VerticalFlowLayout());
		estePanelD.setBorder(new EmptyBorder(40,0,40,0));
						
		nomusuario = Mine.lblUser(this.usuario);
		nomusuario.setFont(Mine.FAVFONT18);
		
		setModelTable();	
		
		jtablapre = Mine.tablaForPanel(modeldevol);
		
		sorterPre = new TableRowSorter(modeldevol);
		
		jtablapre.setRowSorter(sorterPre);
		
		setTableDevFromBD();
		
		jscrollP = Mine.scrollForPanel(jtablapre, 600, 300);
		
		jtbuscarPre = Mine.jtForPanel(300);
		
		btnDevol = Mine.btnForPanel("  Devolver Libro  ");
		btnDevol.setEnabled(false);
		
		btnGenerar = Mine.btnForPanel(" Generar historial ");
		
		/************************* PANEL BOT ************************************/
		
		JPanel panelEstado = new JPanel(new FlowLayout());
		panelEstado.setOpaque(false);
		
		JLabel lblestado = new JLabel("Estado:");
		lblestado.setForeground(Color.WHITE);
		panelEstado.add(lblestado);
		panelEstado.add(comboestado);
						
		estePanelD.add(nomusuario);
		estePanelD.add(jscrollP);
		estePanelD.add(Mine.jpForPanel("Ingrese el ISBN del libro:", jtbuscarPre));
		estePanelD.add(panelEstado);
		estePanelD.add(btnDevol);
		estePanelD.add(btnGenerar);
	}
	
	private void controlEventos() {
		jtbuscarPre.setTransferHandler(null);
		
		jtablapre.addMouseListener(autoCompletar());
		
		jtbuscarPre.getDocument().addDocumentListener(dcprestamos());
		
		jtbuscarPre.addKeyListener(checkIfSelected());
		
		btnDevol.addActionListener(devolverLibro());
		btnGenerar.addActionListener(generarHistorial());
	}
	
	private ActionListener generarHistorial() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Devolucion> listaDev = atacanteImp.getAllDevoluciones();
				List<String[]> listaToWrite = new ArrayList<String[]>();
				//Listamos todo el contenido
				for (int i = 0; i < listaDev.size(); i++) {
					String li=listaDev.get(i).getLibro();
					String alu=listaDev.get(i).getAlumno();
					String fech=listaDev.get(i).getFechadevol().toString();
					String est=listaDev.get(i).getEstado();
					String [] ardev = {li,alu,fech,est};
					listaToWrite.add(ardev);
				}
				
				//Creamos el fichero
				CSVWriter writer = null;
				try {
					writer = new CSVWriter(new FileWriter(FICHEROH));
					writer.writeAll(listaToWrite);
					System.out.println(FICHEROH);
					JOptionPane.showMessageDialog(null, "Archivo Generado exitosamente","Historial devoluciones",JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException excp1) {
					System.out.println("Error en escribir archivo");
				}
				finally {
					try {
						writer.close();
					} catch (IOException excep2) {
						System.out.println("Error en cerrar archivo devoluciones.csv");
					}
				}
			}
		};
		return esteMetodo;
	}
		
	
	private ActionListener devolverLibro() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				//Borrar el prestamo
				int rowIdx = jtablapre.getSelectedRow();
				
				String dni = (String) jtablapre.getValueAt(rowIdx, 0);
				String isbn = (String) jtablapre.getValueAt(rowIdx, 1);	
				Date fechapres = (Date) jtablapre.getValueAt(rowIdx, 2);
				Date fechadevol = (Date) jtablapre.getValueAt(rowIdx, 3);
				String estado = (String) jtablapre.getValueAt(rowIdx, 4);
				
				Prestamo p = new Prestamo(dni, isbn, fechapres, fechadevol, estado);				
				atacanteImp.borrarPrestamo(p);
								
				//Actualiza el estado del libro
				String devestado =comboestado.getSelectedItem().toString();
				Libro devlibro = new Libro(isbn, devestado);
				
				atacanteImp.actualizarEstadoLibro(devlibro);
				
				//crea una tupla en la tabla devoluciones
				LocalDateTime ahora = LocalDateTime.now();
				String fechadev = dtf.format(ahora);
				
				Devolucion d = new Devolucion(isbn, dni, Date.valueOf(fechadev), devestado);
				atacanteImp.agregarDevolucion(d);
				
				modeldevol.removeRow(rowIdx);
				jtablapre.clearSelection();
				limpiar();
			}
		};
		return esteMetodo;
	}
		
	private KeyListener checkIfSelected() {
		KeyListener esteMetodo = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int rowPre = jtablapre.getSelectedRow();
									
				if(rowPre != -1) {
					btnDevol.setEnabled(true);
				}else {
					btnDevol.setEnabled(false);
				}				
			}
		};
		return esteMetodo;
	}
	
	private MouseListener autoCompletar() {
		MouseListener esteMetodo = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//Obtener la linea del prestamo seleccionado
				int rowIdx = jtablapre.rowAtPoint(e.getPoint());
		           
	           //Obtener el isbn y estado del prestamo seleccionado
				Object objisbn = modeldevol.getValueAt(rowIdx, 1) ;
				Object objestado= modeldevol.getValueAt(rowIdx, 4);
				
				jtbuscarPre.setText(objisbn.toString());
				comboestado.setSelectedItem(objestado.toString());
				
				btnDevol.setEnabled(true);	           		
			}
		};
		return esteMetodo;
	}
	
	private DocumentListener dcprestamos() {
		DocumentListener esteMetodo = new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {filtroPrestamos();}
			public void insertUpdate(DocumentEvent e) {filtroPrestamos();}
			public void changedUpdate(DocumentEvent e) {filtroPrestamos();}
		};
		return esteMetodo;
	}
	
	private void filtroPrestamos() {
		RowFilter<DefaultTableModel, Object> rf = null;
		try {
			rf= RowFilter.regexFilter("^"+jtbuscarPre.getText(),1);
		}catch (PatternSyntaxException e) {
			return;
		}
		sorterPre.setRowFilter(rf);
	}
	
	public void setTableDevFromBD() {
		modeldevol.setRowCount(0);
		ArrayList<Prestamo> listaPrestamos = atacanteImp.getAllPrestamos();
		
		Object nrowz[] = new Object[5];	
		for (int i = 0; i < listaPrestamos.size(); i++) {
			nrowz[0] = listaPrestamos.get(i).getAlumno();  
			nrowz[1] = listaPrestamos.get(i).getLibro();  
			nrowz[2] = listaPrestamos.get(i).getFechapres();  
			nrowz[3] = listaPrestamos.get(i).getFechadevol();  
			nrowz[4] = listaPrestamos.get(i).getEstado(); 
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int j = 0; j <=4; j++) {
				jtablapre.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
				jtablapre.getColumnModel().getColumn(j).setMinWidth(200);
			}			
			modeldevol.addRow(nrowz);
		}	
	}
	
	public void setModelTable(){
		this.modeldevol = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.modeldevol.setColumnIdentifiers(columnsPre);
	}
	
	private void limpiar() {
		jtbuscarPre.setText("");
		comboestado.setSelectedIndex(0);
	}
	
	/******************* DEVUELVE EL PANEL **************************/		
	public JPanel getPanel() {
		return this.estePanelD;
	}	
}
