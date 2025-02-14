package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import design.GradientPanel;
import objeto.Libro;
import objetoImp.AtacanteBDLiteImp;

@SuppressWarnings("serial")
public class CustomPanelLibro extends JPanel{
	
	private JTable jtabla;	
	private JScrollPane jscroll;
	private GridBagConstraints c;
	private AtacanteBDLiteImp atacanteImp; 
	private final int CELLWIDTH = 130;
	private DefaultTableModel modeltabla;
	private ArrayList<Libro> arrayListLibros, librosagregados, librosborrados;
	private JTextField jtisbn,jtitulo,jtautor,jtedi,jtasig;
	private JPanel estePanel, panelTop, panelLeft, panelRight, auxPanel;
	private JButton btnquitar, btnagregar, btnaceptar, btnrechazar,btngrabar;
	private JComboBox<String> comboestado = new JComboBox<String>(Mine.ESTADOS);
	private final String[] columnsLibro = {"ISBN", "Título", "Autor", "Editorial","Asignatura","Estado"};
	
	public CustomPanelLibro() {		
		agregarElementos();
		controlEventos();
	}
	
	private void agregarElementos() {
		atacanteImp = new AtacanteBDLiteImp();	
		
		c = new GridBagConstraints();
		estePanel = new GradientPanel(Mine.FAVCOLORROSE,Mine.FAVCOLORBLACK);
		estePanel.setLayout(new GridBagLayout());
		estePanel.setBackground(Color.WHITE);	
		       
        /************************* PANEL TOP ************************************/
		panelTop = new JPanel();
		panelTop.setOpaque(false);
		c.ipady = 300;
		c.ipadx = 600;
		c.gridy = 0;
		c.gridx = 0;	
		c.insets = new Insets(80,0,0,0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty=0.1;
		estePanel.add(panelTop,c);
        
		setModelTable();				
		
		jtabla = Mine.tablaForPanel(modeltabla);         
		
        setTableLibroFromBD();
		
		jscroll = Mine.scrollForPanel(jtabla,600,300);
			
		panelTop.add(jscroll);				
		
		/************************* PANEL ABAJO IZQUIERDA ************************************/
		
		panelLeft = new JPanel();	
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		panelLeft.setBorder(Mine.myTiltedBorder("Agregar libro:",10));		
		panelLeft.setOpaque(false);
		c.ipady = 0;
		c.ipadx = 0;
		c.gridy = 1;
		c.gridx = 0;			
		c.insets = new Insets(0,40,50,300);
		c.anchor = GridBagConstraints.SOUTHWEST;		
		estePanel.add(panelLeft,c);
				
		JLabel lblisbn = Mine.lblForPanel("ISBN");		
		JLabel lbltitulo = Mine.lblForPanel("Título");
		JLabel lblautor = Mine.lblForPanel("Autor");
		JLabel lbledi = Mine.lblForPanel("Editorial");
		JLabel lblasig = Mine.lblForPanel("Asignatura");
		JLabel lblestado = Mine.lblForPanel("Estado");
		
		jtisbn = Mine.jtForPanel(200);			
		jtitulo = Mine.jtForPanel(300);	
		jtautor = Mine.jtForPanel(300);	
		jtedi = Mine.jtForPanel(300);		
		jtasig = Mine.jtForPanel(300);		
		
		comboestado.setPreferredSize(new Dimension(200,20));
		comboestado.setMaximumSize(comboestado.getPreferredSize());
		comboestado.setAlignmentX( Component.LEFT_ALIGNMENT);
		comboestado.setOpaque(true);
		
		btnquitar = new JButton(Mine.escalarIMG(Mine.IMGMINUS,20, 20));
		btnquitar.setBackground(Color.WHITE);
		btnquitar.setOpaque(false);
		btnquitar.setBorder(null);
		c.ipady = 0;
		c.ipadx = 0;
		c.gridy = 1;
		c.gridx = 0;	
		c.insets = new Insets(53,270,0,350);	
		c.anchor = GridBagConstraints.NORTHWEST;
		estePanel.add(btnquitar,c);
		btnquitar.setEnabled(false);
		
		btnagregar = new JButton(Mine.escalarIMG(Mine.IMGPLUS, 20, 20));
		btnagregar.setBackground(Color.WHITE);
		btnagregar.setOpaque(false);
		btnagregar.setBorder(null);
		c.ipady = 0;
		c.ipadx = 0;
		c.gridy = 1;
		c.gridx = 0;	
		c.insets = new Insets(278,270,0,350);	
		c.anchor = GridBagConstraints.NORTHWEST;
		estePanel.add(btnagregar,c);
		btnagregar.setEnabled(false);
				
		//Agregar al panel izquierdo
		panelLeft.add(lblisbn);
		panelLeft.add(jtisbn);	
		panelLeft.add(lbltitulo);
		panelLeft.add(jtitulo);
		panelLeft.add(lblautor);
		panelLeft.add(jtautor);
		panelLeft.add(lbledi);
		panelLeft.add(jtedi);
		panelLeft.add(lblasig);
		panelLeft.add(jtasig);
		panelLeft.add(lblestado);
		panelLeft.add(comboestado);
				
		/************************* PANEL ABAJO DERECHA ************************************/
		
		panelRight = new JPanel();
		panelRight.setLayout(new BoxLayout(panelRight,BoxLayout.Y_AXIS));
		panelRight.setOpaque(false);
		c.ipady = 0;
		c.ipadx = 0;
		c.gridy = 1;
		c.gridx = 0;			
		c.insets = new Insets(20,360,0,0);	
		c.anchor = GridBagConstraints.NORTHWEST;
		estePanel.add(panelRight,c);
				
		JLabel lblIMGLibro = new JLabel(Mine.escalarIMG(Mine.IMGLIBRO, 200,200));		
		btnaceptar = Mine.btnForPanel("Aceptar");
		btnrechazar = Mine.btnForPanel("Rechazar");
		btngrabar = Mine.btnForPanel("Grabar");
				
		auxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
		auxPanel.setOpaque(false);
				
		lblIMGLibro.setAlignmentX( Component.CENTER_ALIGNMENT);
		btngrabar.setAlignmentX( Component.CENTER_ALIGNMENT);
		btngrabar.setEnabled(false);
		auxPanel.setAlignmentX( Component.CENTER_ALIGNMENT);
		
		auxPanel.add(btnaceptar);
		auxPanel.add(btnrechazar);
				
		panelRight.add(auxPanel);	
		panelRight.add(btngrabar);	
		panelRight.add(lblIMGLibro);	
	}
	
	/******************* METODOS PARA EL CONTROL DE EVENTOS **************************/	
	private void controlEventos() {
		//JTextfield
		jtisbn.setTransferHandler(null);		
		jtitulo.setTransferHandler(null);
		jtautor.setTransferHandler(null);
		jtedi.setTransferHandler(null);
		jtasig.setTransferHandler(null);
		
		jtisbn.addKeyListener(onlyNumbers());
		jtitulo.addKeyListener(comprobarKL());
		jtautor.addKeyListener(comprobarKL());
		jtedi.addKeyListener(comprobarKL());
		jtasig.addKeyListener(comprobarKL());
		
		//JTable
		jtabla.addMouseListener(listenerTabla());
		
		//Combo
		comboestado.addActionListener(comprobarAL());	
		
		//Botones
		btnagregar.addActionListener(agregarFila());	
		btnquitar.addActionListener(borrarFila());
		btnaceptar.addActionListener(aceptarCambios());
		btnrechazar.addActionListener(rechazarCambios());
		btngrabar.addActionListener(grabarTablaOnBD());
	}	
		
	private ActionListener grabarTablaOnBD() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//verifica la lista de agregados y borrados y los refleja en la BD
				for (int i = 0; i < librosagregados.size(); i++) {
					atacanteImp.agregarLibro(librosagregados.get(i));
				}				
				for (int j = 0; j < librosborrados.size(); j++) {
					atacanteImp.borrarLibro(librosborrados.get(j));
				}				
				btngrabar.setEnabled(false);
				limpiar();
			}
		};
		return esteMetodo;
	}
	
	private ActionListener rechazarCambios() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {									
				setTableLibroFromBD();			
				limpiar();
				btngrabar.setEnabled(false);
			}
		};
		return esteMetodo;
	}
	
	private ActionListener aceptarCambios() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(librosagregados.size()==0 && librosborrados.size()==0) {
					JOptionPane.showMessageDialog(null, "No se han hecho cambios");
				}else {
					JOptionPane.showMessageDialog(null, "Pulse GRABAR para guardar los cambios en la BD.","Guardar cambios",JOptionPane.INFORMATION_MESSAGE);
					btngrabar.setEnabled(true);
				}
			}
		};
		return esteMetodo;
	}
	
	private ActionListener borrarFila() {		
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nlinea = giveIndice();
				//devolver un objeto por el indice dado y agregarlo a la lista de listaborrados
				if(nlinea == -1) {
					JOptionPane.showMessageDialog(null,"No se ha encontrado ningún libro con este ISBN","No Data Found",JOptionPane.INFORMATION_MESSAGE);
				}else {					
					Libro esteLibro = giveLibroFromIndice(nlinea);	
					//si el libro esta en la lista de agregados, lo quita
					if(librosagregados.contains(esteLibro)) {
						librosagregados.remove(esteLibro);
					}else {
						//y si no esta, lo agrega a la lista de borrados
						librosborrados.add(esteLibro);
					}
					modeltabla.removeRow(nlinea);
					limpiar();
				}				
			}
		};
		return esteMetodo;
	}
	
	private ActionListener agregarFila() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String jt1 = jtisbn.getText();
				
				if(giveIndice()==-1) {//No existe en la tabla					
					String jt2 = jtitulo.getText();
					String jt3 = jtautor.getText();
					String jt4 = jtedi.getText();
					String jt5 = jtasig.getText();
					String est = (String)comboestado.getSelectedItem();
					Libro nuevoLibro = new Libro(jt1, jt2, jt3, jt4, jt5, est);		
										
					librosagregados.add(nuevoLibro);//agregamos el libro en nuestra lista para luego utilizarla al clicklear sobre el boton grabar
					
					modeltabla.addRow(parseLibroToObject(nuevoLibro));//agregamos a la tabla la nueva fila
					modeltabla.fireTableDataChanged();//le decimos que muestre los cambios
					
					limpiar();//resetamos los campos
				}else {
					JOptionPane.showMessageDialog(null,"El ISBN ya existe dentro de la tabla","El ISBN ya existe",JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		return esteMetodo;
	}
	
	private MouseListener listenerTabla() {
		MouseListener esteMetodo = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {				
				if (jtabla.equals(e.getSource())) {
					
					//Obtener la linea del libro seleccionado
					int rowIdx = jtabla.rowAtPoint(e.getPoint());
			           
		           //Obtener el isbn del libro seleccionado
					Object obj = modeltabla.getValueAt(rowIdx, 0) ;
					String str = obj.toString();
					jtisbn.setText(str);
					btnquitar.setEnabled(true);
		           
					//Modifica el libro haciendo doble click
					if(e.getClickCount()==2) {						
						Libro libroSelec = giveLibroFromIndice(rowIdx);
						new CustomDialogLibro(libroSelec,atacanteImp,modeltabla,rowIdx);				
					}		           
		        }				
			}
		};
		return esteMetodo;
	}	
	
	private KeyListener onlyNumbers() {
		KeyListener esteMetodo = new KeyListener() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)) || jtisbn.getText().length()>12){
					getToolkit().beep();
				    e.consume();
				}
			}
			public void keyReleased(KeyEvent arg0) {
				if(jtisbn.getText().length()>=9) {
					btnquitar.setEnabled(true);
					if(esValido()) {
						btnagregar.setEnabled(true);
					}
				}else{
					btnquitar.setEnabled(false);
					btnagregar.setEnabled(false);
				}				
			}
			public void keyPressed(KeyEvent arg0) {}
		};
		return esteMetodo;
	}
	
	//Metodo para habilitar/deshabilitar el boton agregar fila	 
	private KeyListener comprobarKL() {
		KeyListener esteMetodo = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {				
				if(esValido()) {
					btnagregar.setEnabled(true);
				}else{
					btnagregar.setEnabled(false);
				}				
			}
		};
		return esteMetodo;
	}
	
	
	//Metodo del combobox para habilitar o deshabilitar el boton agregar fila 	 
	private ActionListener comprobarAL() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(esValido()) {
					btnagregar.setEnabled(true);
				}else{
					btnagregar.setEnabled(false);
				}	
			}
		};
		return esteMetodo;
	}
			 
	private Object[] parseLibroToObject(Libro l) {
		Object linea[] = new Object[6];
		linea[0] = l.getIsbn();  
		linea[1] = l.getTitulo();  
		linea[2] = l.getAutor();  
		linea[3] = l.getEditorial();  
		linea[4] = l.getAsignatura(); 
		linea[5] = l.getEstado(); 
		return linea;
	}
	
	public void setModelTable() {		
		modeltabla = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modeltabla.setColumnIdentifiers(columnsLibro);
	}
	
	//Carga los libros desde la base de datos
	public void setTableLibroFromBD() {	
		modeltabla.setRowCount(0);	
		librosagregados = new ArrayList<Libro>();
		librosborrados = new ArrayList<Libro>();
		arrayListLibros = new ArrayList<Libro>();
		arrayListLibros = atacanteImp.getAllLibros();
		
		Object linea[] = new Object[6];	
		for (int i = 0; i < arrayListLibros.size(); i++) {
			linea[0] = arrayListLibros.get(i).getIsbn();  
			linea[1] = arrayListLibros.get(i).getTitulo();  
			linea[2] = arrayListLibros.get(i).getAutor();  
			linea[3] = arrayListLibros.get(i).getEditorial();  
			linea[4] = arrayListLibros.get(i).getAsignatura(); 
			linea[5] = arrayListLibros.get(i).getEstado();  
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int j = 0; j <=5; j++) {
				jtabla.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
				jtabla.getColumnModel().getColumn(j).setMinWidth(CELLWIDTH);
			}						
			modeltabla.addRow(linea);
		}		
	}	
	
	//verifica si el isbn en el JTextfield existe o no
	private int giveIndice() {
		int indice=-1;
		boolean textoFound = false;		
		for(int i = 0; textoFound==false && i < jtabla.getRowCount() ; i++){//For each row
			if(modeltabla.getValueAt(i, 0).equals(jtisbn.getText())){//Search the model
                indice=i;
                textoFound=true;
            }
	    }
		return indice;//indice -1 si no existe
	}
	
	private Libro giveLibroFromIndice(int indice) {
		String isbn = modeltabla.getValueAt(indice, 0).toString();
		String titulo = modeltabla.getValueAt(indice, 1).toString();
		String autor = modeltabla.getValueAt(indice, 2).toString();
		String editorial = modeltabla.getValueAt(indice, 3).toString();
		String asignatura = modeltabla.getValueAt(indice, 4).toString();
		String estado = modeltabla.getValueAt(indice, 5).toString();		
		return new Libro(isbn,titulo,autor,editorial,asignatura,estado);
	}
	
	//Utilizado para que todos verificar que los campos no esten vacios	 
	private boolean esValido() {		
		boolean esteBool = false;
		
		int jt1 = jtisbn.getText().length();
		int jt2 = jtitulo.getText().length();
		int jt3 = jtautor.getText().length();
		int jt4 = jtedi.getText().length();
		int jt5 = jtasig.getText().length();
		int est = comboestado.getSelectedIndex();
		
		if(jt1>=1 && jt2>=1 && jt3>=1 && jt4>=1 && jt5>=1 && est!=0) {
			esteBool=true;
		}
		return esteBool;
	}	
	
	private void limpiar() {
		jtisbn.setText("");
		jtitulo.setText("");
		jtautor.setText("");
		jtedi.setText("");
		jtasig.setText("");
		comboestado.setSelectedIndex(0);
	}
	
	/******************* DEVUELVE EL PANEL **************************/	
	public JPanel getPanel() {
		return estePanel;
	}
	
	public JScrollPane getJScrollLibro() {
		return jscroll;
	}
}
