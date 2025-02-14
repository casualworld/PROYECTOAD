package custom;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
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

import design.GradientPanel;
import objeto.Alumno;
import objeto.Libro;
import objeto.Prestamo;
import objetoImp.AtacanteBDLiteImp;


@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class CustomPanelPrestamo extends JPanel{
	
	private TableRowSorter sorterAlumnos, sorterLibros;
	private JPanel estePanelP;
	private JButton btnPrestar;
	private final int CELLWIDTH =130;
	private AtacanteBDLiteImp atacanteLiteImp;
	private JScrollPane jscrollP,jscrollL, jscrollA;	
	private JTextField jtbuscaralumno, jtbuscarlibro;
	private JTable jtablapre, jtablalibro, jtablaalum;	
	public DefaultTableModel modelpre, modellibro, modelalumno;
	private final String[] columnsAlumno = {"DNI", "Nombre", "Apellido 1", "Apellido 2"};
	private final String[] columnsLibro = {"ISBN", "Título", "Autor", "Editorial","Asignatura","Estado"};
	private final String[] columnsPre = {"Alumno", "Libro", "Fecha_Prestamo", "Fecha_Devolucion","Estado"};
		
	public CustomPanelPrestamo() {
		agregarElementos();
		controlEventos();
	}
	
	private void agregarElementos() {
		
		atacanteLiteImp= new AtacanteBDLiteImp();
		
		estePanelP = new GradientPanel(Mine.FAVCOLORROSE, Mine.FAVCOLORBLACK);
		estePanelP.setLayout(new GridLayout(2,1,0,10));
		estePanelP.setBorder(new EmptyBorder(40,0,40,0));
		
		setModelTable();				
		
		jtablaalum = Mine.tablaForPanel(modelalumno);	
		jtablalibro = Mine.tablaForPanel(modellibro);
		jtablapre = Mine.tablaForPanel(modelpre);
		
		sorterAlumnos = new TableRowSorter(modelalumno);
		sorterLibros = new TableRowSorter(modellibro);
				
		jtablaalum.setRowSorter(sorterAlumnos);
		jtablalibro.setRowSorter(sorterLibros);
		
		setTablesPreFromBD();
				
		jscrollA = Mine.scrollForPanel(jtablaalum,360,250);	
		jscrollL = Mine.scrollForPanel(jtablalibro,360,250);
		jscrollP = Mine.scrollForPanel(jtablapre,800,300);		
		
		jtbuscaralumno = Mine.jtForPanel(300);
		jtbuscarlibro = Mine.jtForPanel(300);
		
		btnPrestar = Mine.btnForPanel("Generar Prestamo");
		btnPrestar.setEnabled(false);
				
		/********************** PANEL TOP ************************/
		JPanel panelTop = new JPanel(new GridLayout(1,2));
		panelTop.setOpaque(false);
		
		JPanel panelCol1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		panelCol1.setOpaque(false);
		panelCol1.add(jscrollA);
		panelCol1.add(Mine.jpForPanel("Ingrese el DNI del alumno:", jtbuscaralumno));
		
		JPanel panelCol2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		panelCol2.setOpaque(false);
		panelCol2.add(jscrollL);
		panelCol2.add(Mine.jpForPanel("Ingrese el ISBN del libro:", jtbuscarlibro));
		
		panelTop.add(panelCol1);
		panelTop.add(panelCol2);	
				
		/********************** PANEL BOT ************************/
		JPanel panelBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panelBot.setOpaque(false);
		panelBot.add(btnPrestar);
		panelBot.add(jscrollP);				
		
		/********************* MAIN PANEL ***********************/
		estePanelP.add(panelTop);
		estePanelP.add(panelBot);			
	}
	
	private void controlEventos() {
		
		jtbuscaralumno.setTransferHandler(null);
		jtbuscarlibro.setTransferHandler(null);
		
		jtbuscaralumno.getDocument().addDocumentListener(dcalumnos());
		jtbuscarlibro.getDocument().addDocumentListener(dclibros());
		
		jtbuscaralumno.addKeyListener(checkIfSelected());
		jtbuscarlibro.addKeyListener(checkIfSelected());
		
		jtablaalum.addMouseListener(checkTables());
		jtablalibro.addMouseListener(checkTables());
		
		btnPrestar.addActionListener(prestarLibro());
	}			
	
	private ActionListener prestarLibro() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int rowlibro = jtablalibro.getSelectedRow();
				String isbn = (String) jtablalibro.getValueAt(rowlibro, 0);
				
				if(isbnExists(isbn)) {
					JOptionPane.showMessageDialog(null, "El libro ha sido prestado", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
				}else {
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");				
					LocalDateTime ahora = LocalDateTime.now();
					LocalDate limite = LocalDate.now().plusDays(20);
					
					int rowalum = jtablaalum.getSelectedRow();					
					
					String dni = (String) jtablaalum.getValueAt(rowalum, 0);
					String fechapres = dtf.format(ahora);
					String fechadevol = dtf.format(limite);
					String estado = (String) jtablalibro.getValueAt(rowlibro, 5);
					
					Prestamo estePres = new Prestamo(dni, isbn, fechapres, fechadevol, estado);				
					atacanteLiteImp.agregarPrestamo(estePres);			
					
					setTablePrestamos();
					btnPrestar.setEnabled(false);
				}			
			}
		};
		return esteMetodo;
	}
	
	private boolean isbnExists(String isbn) {
		boolean isIn= false;
        for (int i = 0; i < jtablapre.getRowCount() && isIn==false; i++) {
        	String eachIsbn = (String) jtablapre.getValueAt(i, 1);
        	if (eachIsbn.equals(isbn)) {
        		isIn = true;
            }
        }
        return isIn;
	}
		
	private KeyListener checkIfSelected() {
		KeyListener esteMetodo = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int rowalum = jtablaalum.getSelectedRow();
				int rowlibro = jtablalibro.getSelectedRow();
									
				if(rowalum !=-1 && rowlibro != -1) {
					btnPrestar.setEnabled(true);
				}else {
					btnPrestar.setEnabled(false);
				}				
			}
		};
		return esteMetodo;
	}
	
	private MouseListener checkTables() {
		MouseListener esteMetodo = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == jtablaalum || e.getSource() == jtablalibro ) {
					int rowalum = jtablaalum.getSelectedRow();
					int rowlibro = jtablalibro.getSelectedRow();
										
					if(rowalum !=-1 && rowlibro != -1) {
						btnPrestar.setEnabled(true);
					}else {
						btnPrestar.setEnabled(false);
					}
				}
			}
		};
		return esteMetodo;
	}
	
	private DocumentListener dcalumnos() {
		DocumentListener esteMetodo = new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {filtroAlumnos();}
			public void insertUpdate(DocumentEvent e) {filtroAlumnos();}
			public void changedUpdate(DocumentEvent e) {filtroAlumnos();}
		};
		return esteMetodo;
	}
	
	private DocumentListener dclibros() {
		DocumentListener esteMetodo = new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {filtroLibros();}
			public void insertUpdate(DocumentEvent e) {filtroLibros();}
			public void changedUpdate(DocumentEvent e) {filtroLibros();}
		};
		return esteMetodo;
	}
	
	public void setTablesPreFromBD() {
		modelalumno.setRowCount(0);		
		ArrayList<Alumno> listaAlumnos = atacanteLiteImp.getAllAlumnos();
		
		Object nrowx[] = new Object[4];
		for (int i = 0; i < listaAlumnos.size(); i++) {
			nrowx[0] = listaAlumnos.get(i).getDni();  
			nrowx[1] = listaAlumnos.get(i).getNombre();  
			nrowx[2] = listaAlumnos.get(i).getApe1();  
			nrowx[3] = listaAlumnos.get(i).getApe2();  
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int j = 0; j <=3; j++) {
				jtablaalum.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
				jtablaalum.getColumnModel().getColumn(j).setMinWidth(CELLWIDTH);
			}						
			modelalumno.addRow(nrowx);
		}	
		
		modellibro.setRowCount(0);	
		ArrayList<Libro> listaLibros = atacanteLiteImp.getAllLibros();
		
		Object nrowy[] = new Object[6];	
		for (int i = 0; i < listaLibros.size(); i++) {
			nrowy[0] = listaLibros.get(i).getIsbn();  
			nrowy[1] = listaLibros.get(i).getTitulo();  
			nrowy[2] = listaLibros.get(i).getAutor();  
			nrowy[3] = listaLibros.get(i).getEditorial();  
			nrowy[4] = listaLibros.get(i).getAsignatura(); 
			nrowy[5] = listaLibros.get(i).getEstado();  
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int j = 0; j <=5; j++) {
				jtablalibro.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
				jtablalibro.getColumnModel().getColumn(j).setMinWidth(CELLWIDTH);
			}						
			modellibro.addRow(nrowy);
		}			
		setTablePrestamos();
	}
	
	public void setTablePrestamos() {
		modelpre.setRowCount(0);
		ArrayList<Prestamo> listaPrestamos = atacanteLiteImp.getAllPrestamos();
		
		Object nrowz[] = new Object[5];	
		for (int i = 0; i < listaPrestamos.size(); i++) {
			nrowz[0] = listaPrestamos.get(i).getAlumno();  
			nrowz[1] = listaPrestamos.get(i).getLibro();  
			nrowz[2] = listaPrestamos.get(i).getFechastrpres();  
			nrowz[3] = listaPrestamos.get(i).getFechastrdevol();  
			nrowz[4] = listaPrestamos.get(i).getEstado(); 
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int j = 0; j <=4; j++) {
				jtablapre.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
				jtablapre.getColumnModel().getColumn(j).setMinWidth(200);
			}			
			modelpre.addRow(nrowz);
		}	
	}
		
	public void setModelTable() {		
		modelalumno = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelalumno.setColumnIdentifiers(columnsAlumno);
		
		modellibro= new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modellibro.setColumnIdentifiers(columnsLibro);
		
		modelpre = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelpre.setColumnIdentifiers(columnsPre);
	}	
		
	private void filtroAlumnos() {
		RowFilter<DefaultTableModel, Object> rf = null;
		try {
			rf= RowFilter.regexFilter("^"+jtbuscaralumno.getText(),0);
		}catch (PatternSyntaxException e) {
			return;
		}
		sorterAlumnos.setRowFilter(rf);
	}
	
	private void filtroLibros() {
		RowFilter<DefaultTableModel, Object> rf = null;
		try {
			rf= RowFilter.regexFilter("^"+jtbuscarlibro.getText(),0);
		}catch (PatternSyntaxException e) {
			return;
		}
		sorterLibros.setRowFilter(rf);
	}

	/******************* DEVUELVE EL PANEL **************************/	
	public JPanel getPanel() {
		return estePanelP;
	}
}
