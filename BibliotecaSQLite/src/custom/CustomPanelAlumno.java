package custom;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import design.GradientPanel;
import objeto.Alumno;
import objetoImp.AtacanteBDLiteImp;

@SuppressWarnings("serial")
public class CustomPanelAlumno extends JPanel{
	
	private JTable jtablaAlum;	
	private JScrollPane jscrollAlum;
	private GridBagConstraints cAlum;
	private AtacanteBDLiteImp atacanteLiteImp;  
	private final int CELLWIDTH = 150;
	private DefaultTableModel modeltablaAlum;
	private JTextField jtdni,jtnombre,jtape1,jtape2;
	private JPanel estePanelA, panelTopA, panelLeftA, panelRightA, auxPanelA;
	private JButton btnquitar, btnagregar, btnaceptar, btnrechazar,btngrabar;
	private ArrayList<Alumno> arrayListAlumnos, alumnosagregados, alumnosborrados;
	private final String[] columnsAlumno = {"DNI", "Nombre", "Apellido 1", "Apellido 2"};	
	
	public CustomPanelAlumno() {				
		agregarElementos();
		controlEventos();
	}
	
	private void agregarElementos() {
		atacanteLiteImp = new AtacanteBDLiteImp();
		
		cAlum = new GridBagConstraints();
		estePanelA = new GradientPanel(Mine.FAVCOLORROSE, Mine.FAVCOLORBLACK);	
		estePanelA.setLayout(new GridBagLayout());
		estePanelA.setBackground(Color.WHITE);	
		       
        /************************* PANEL TOP ************************************/		
		panelTopA = new JPanel();
		panelTopA.setOpaque(false);
		cAlum.ipady = 300;
		cAlum.ipadx = 600;
		cAlum.gridy = 0;
		cAlum.gridx = 0;	
		cAlum.insets = new Insets(90,0,0,0);
		cAlum.fill = GridBagConstraints.HORIZONTAL;
		cAlum.anchor = GridBagConstraints.NORTH;
		cAlum.weighty=0.1;
		estePanelA.add(panelTopA,cAlum);
        
		setModelTable();				
		
		jtablaAlum = Mine.tablaForPanel(modeltablaAlum);
		
        setTableAlumFromBD();
		
		jscrollAlum = Mine.scrollForPanel(jtablaAlum,600,300);
			
		panelTopA.add(jscrollAlum);				
		
		/************************* PANEL ABAJO IZQUIERDA ************************************/
		
		panelLeftA = new JPanel();	
		panelLeftA.setLayout(new BoxLayout(panelLeftA, BoxLayout.Y_AXIS));
		panelLeftA.setBorder(Mine.myTiltedBorder("Agregar alumno:",10));		
		panelLeftA.setOpaque(false);
		cAlum.ipady = 0;
		cAlum.ipadx = 0;
		cAlum.gridy = 1;
		cAlum.gridx = 0;			
		cAlum.insets = new Insets(0,40,90,300);
		cAlum.anchor = GridBagConstraints.SOUTHWEST;		
		estePanelA.add(panelLeftA,cAlum);
				
		JLabel lbldni = Mine.lblForPanel("DNI");		
		JLabel lblnombre = Mine.lblForPanel("Nombre");
		JLabel lblape1 = Mine.lblForPanel("Apellido 1");
		JLabel lblape2 = Mine.lblForPanel("Apellido 2");
		
		jtdni = Mine.jtForPanel(200);			
		jtnombre = Mine.jtForPanel(300);	
		jtape1 = Mine.jtForPanel(300);	
		jtape2 = Mine.jtForPanel(200);			
				
		btnquitar = new JButton(Mine.escalarIMG(Mine.IMGMINUS,20, 20));
		btnquitar.setBackground(Color.WHITE);
		btnquitar.setOpaque(false);
		btnquitar.setBorder(null);
		cAlum.ipady = 0;
		cAlum.ipadx = 0;
		cAlum.gridy = 1;
		cAlum.gridx = 0;	
		cAlum.insets = new Insets(68,270,0,350);	
		cAlum.anchor = GridBagConstraints.NORTHWEST;
		estePanelA.add(btnquitar,cAlum);
		btnquitar.setEnabled(false);
		
		btnagregar = new JButton(Mine.escalarIMG(Mine.IMGPLUS, 20, 20));
		btnagregar.setBackground(Color.WHITE);
		btnagregar.setOpaque(false);
		btnagregar.setBorder(null);
		cAlum.ipady = 0;
		cAlum.ipadx = 0;
		cAlum.gridy = 1;
		cAlum.gridx = 0;	
		cAlum.insets = new Insets(203,270,0,350);	
		cAlum.anchor = GridBagConstraints.NORTHWEST;
		estePanelA.add(btnagregar,cAlum);
		btnagregar.setEnabled(false);
				
		//Agregar al panel izquierdo
		panelLeftA.add(lbldni);
		panelLeftA.add(jtdni);	
		panelLeftA.add(lblnombre);
		panelLeftA.add(jtnombre);
		panelLeftA.add(lblape1);
		panelLeftA.add(jtape1);
		panelLeftA.add(lblape2);
		panelLeftA.add(jtape2);
				
		/************************* PANEL ABAJO DERECHA ************************************/
		
		panelRightA = new JPanel();
		panelRightA.setLayout(new BoxLayout(panelRightA,BoxLayout.Y_AXIS));
		panelRightA.setOpaque(false);
		cAlum.ipady = 0;
		cAlum.ipadx = 0;
		cAlum.gridy = 1;
		cAlum.gridx = 0;			
		cAlum.insets = new Insets(0,360,0,0);	
		cAlum.anchor = GridBagConstraints.NORTHWEST;
		estePanelA.add(panelRightA,cAlum);
				
		JLabel lblIMGLibro = new JLabel(Mine.escalarIMG(Mine.IMGALUM, 200,200));		
		btnaceptar = Mine.btnForPanel("Aceptar");
		btnrechazar = Mine.btnForPanel("Rechazar");
		btngrabar = Mine.btnForPanel("Grabar");
				
		auxPanelA = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
		auxPanelA.setOpaque(false);
				
		lblIMGLibro.setAlignmentX( Component.CENTER_ALIGNMENT);
		btngrabar.setAlignmentX( Component.CENTER_ALIGNMENT);
		btngrabar.setEnabled(false);
		auxPanelA.setAlignmentX( Component.CENTER_ALIGNMENT);
		
		auxPanelA.add(btnaceptar);
		auxPanelA.add(btnrechazar);
				
		panelRightA.add(auxPanelA);	
		panelRightA.add(btngrabar);	
		panelRightA.add(lblIMGLibro);	
	}
	
	/******************* METODOS PARA EL CONTROL DE EVENTOS **************************/	
	private void controlEventos() {
		//JTextfield
		jtdni.setTransferHandler(null);		
		jtnombre.setTransferHandler(null);
		jtape1.setTransferHandler(null);
		jtape2.setTransferHandler(null);
		
		jtdni.addKeyListener(validarDNI());
		jtnombre.addKeyListener(comprobarKL());
		jtape1.addKeyListener(comprobarKL());
		jtape2.addKeyListener(comprobarKL());
		
		//JTable
		jtablaAlum.addMouseListener(listenerTabla());
				
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
				for (int i = 0; i < alumnosagregados.size(); i++) {
					atacanteLiteImp.agregarAlumno(alumnosagregados.get(i));
				}				
				for (int j = 0; j < alumnosborrados.size(); j++) {
					atacanteLiteImp.borrarAlumno(alumnosborrados.get(j));
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
				setTableAlumFromBD();	
				limpiar();				
				btngrabar.setEnabled(false);
			}
		};
		return esteMetodo;
	}
	
	private ActionListener aceptarCambios() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(alumnosagregados.size()==0 && alumnosborrados.size()==0) {
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
				if(nlinea == -1) {
					JOptionPane.showMessageDialog(null,"No se ha encontrado ningún libro con este ISBN","No Data Found",JOptionPane.INFORMATION_MESSAGE);
					limpiar();
				}else {
					Alumno esteAlumno = giveAlumnoFromIndice(nlinea);	
					//si el libro esta en la lista de agregados, lo quita
					if(alumnosagregados.contains(esteAlumno)) {
						alumnosagregados.remove(esteAlumno);
					}else {
						//y si no esta, lo agrega a la lista de borrados
						alumnosborrados.add(esteAlumno);
					}
					modeltablaAlum.removeRow(nlinea);
					limpiar();				
				}				
			}
		};
		return esteMetodo;
	}
	
	private ActionListener agregarFila() {
		ActionListener esteMetodo = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String jt1 = jtdni.getText();
				
				if(giveIndice()==-1) {//no existe en la tabla
					String jt2 = jtnombre.getText();
					String jt3 = jtape1.getText();
					String jt4 = jtape2.getText();
					Alumno nuevoAlumno = new Alumno(jt1, jt2, jt3, jt4);		
					
					alumnosagregados.add(nuevoAlumno);//agregamos el libro en nuestra lista para luego utilizarla al clicklear sobre el btnguardar
					
					modeltablaAlum.addRow(parseLibroToObject(nuevoAlumno));//agregamos a la tabla la nueva fila
					modeltablaAlum.fireTableDataChanged();//le decimos que muestre los cambios
					
					limpiar();					
				}else {
					JOptionPane.showMessageDialog(null,"El DNI ya existe dentro de la tabla","El DNi ya existe",JOptionPane.ERROR_MESSAGE);
					limpiar();
				}
			}
		};
		return esteMetodo;
	}	

	private MouseListener listenerTabla() {
		MouseListener esteMetodo = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//Obtener la linea del alumno seleccionado
				int rowIdx = jtablaAlum.rowAtPoint(e.getPoint());
		           
	           //Obtener el dni del alumno seleccionado
				Object obj = modeltablaAlum.getValueAt(rowIdx, 0) ;
				String str = obj.toString();
				jtdni.setText(str);
				btnquitar.setEnabled(true);
	           
				//Modifica el alumno haciendo doble click
				if(e.getClickCount()==2) {						
					Alumno alumnoSelec = giveAlumnoFromIndice(rowIdx);
					new CustomDialogAlumno(alumnoSelec, atacanteLiteImp, modeltablaAlum, rowIdx);
				}					
			}
		};
		return esteMetodo;
	}
	
	private KeyListener validarDNI() {
		KeyListener esteMetodo = new KeyListener() {				
			public void keyReleased(KeyEvent e) {		
				
				boolean correctoDni=false;
				
				if(jtdni.getText().length()>=9) {
					String dni = jtdni.getText().substring(0, 8);	
					if(dnivalido(dni)) {
						correctoDni=true;
					}
				}					

				if(correctoDni) {
					btnquitar.setEnabled(true);
				}else {
					btnquitar.setEnabled(false);
				}
								
				if(esValido() && correctoDni) {
					btnagregar.setEnabled(true);
				}else {
					btnagregar.setEnabled(false);
				}				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if ((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || jtdni.getText().length()>8){
					getToolkit().beep();
				    e.consume();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		};
		return esteMetodo;
	}
		
	//Metodo para habilitar/deshabilitar el boton agregar fila	 
	private KeyListener comprobarKL() {
		KeyListener esteMetodo = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {	
				String dni = jtdni.getText().substring(0, 8);	
				if(esValido() && dnivalido(dni)) {
					btnagregar.setEnabled(true);
				}else{
					btnagregar.setEnabled(false);
				}				
			}
		};
		return esteMetodo;
	}
		
	private Object[] parseLibroToObject(Alumno a) {
		Object linea[] = new Object[4];
		linea[0] = a.getDni();  
		linea[1] = a.getNombre();  
		linea[2] = a.getApe1();  
		linea[3] = a.getApe2();  
		return linea;
	}
	
	//Crea el modelo de la tabla
	private void setModelTable() {
		modeltablaAlum = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modeltablaAlum.setColumnIdentifiers(columnsAlumno);
	}
	
	//Carga los libros desde la base de datos
	private void setTableAlumFromBD() {		
		modeltablaAlum.setRowCount(0);
		alumnosagregados = new ArrayList<Alumno>();
		alumnosborrados = new ArrayList<Alumno>();
		arrayListAlumnos = new ArrayList<Alumno>();
		arrayListAlumnos = atacanteLiteImp.getAllAlumnos();
		
		Object linea[] = new Object[4];
		for (int i = 0; i < arrayListAlumnos.size(); i++) {
			linea[0] = arrayListAlumnos.get(i).getDni();  
			linea[1] = arrayListAlumnos.get(i).getNombre();  
			linea[2] = arrayListAlumnos.get(i).getApe1();  
			linea[3] = arrayListAlumnos.get(i).getApe2();  
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int j = 0; j <=3; j++) {
				jtablaAlum.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
				jtablaAlum.getColumnModel().getColumn(j).setMinWidth(CELLWIDTH);
			}									
			modeltablaAlum.addRow(linea);
		}		
	}	
	
	//verifica si el dni del JTextfieldDni existe o no
	private int giveIndice() {
		int indice=-1;
		boolean textoFound = false;		
		for(int i = 0; textoFound==false && i < jtablaAlum.getRowCount() ; i++){//For each row
	        for(int j = 0; textoFound==false && j < jtablaAlum.getColumnCount(); j++){//For each column in that row
	            if(jtablaAlum.getModel().getValueAt(i, j).equals(jtdni.getText())){//Search the model
	                indice=i;
	                textoFound=true;
	            }
	        }
	    }
		return indice;//devuelve -1 si no existe
	}	
	
	private Alumno giveAlumnoFromIndice(int indice) {
		String dni = modeltablaAlum.getValueAt(indice, 0).toString();
		String nombre = modeltablaAlum.getValueAt(indice, 1).toString();
		String ape1 = modeltablaAlum.getValueAt(indice, 2).toString();
		String ape2 = modeltablaAlum.getValueAt(indice, 3).toString();
		return new Alumno(dni,nombre,ape1,ape2);
	}
	
	private boolean esValido() {		
		boolean esteBool = false;
		
		int jt1 = jtdni.getText().length();
		int jt2 = jtnombre.getText().length();
		int jt3 = jtape1.getText().length();
		int jt4 = jtape2.getText().length();
		
		if(jt1>=1 && jt2>=1 && jt3>=1 && jt4>=1) {
			esteBool=true;
		}		
		return esteBool;
	}	
	
	private boolean dnivalido(String numdni) {		
		boolean esteBool = false;		
		try {
			String caracteres="TRWAGMYFPDXBNJZSQVHLCKE";
			int num = Integer.parseInt(numdni);			
	        int resto = num%23;		
	        String correctC = caracteres.charAt(resto)+"";
	        String jtdniC= jtdni.getText().charAt(8)+"";
	        if(jtdniC.equalsIgnoreCase(correctC)){
	        	esteBool = true;
	        }			
		} catch (Exception e2) {
			System.out.println("Error dni no valido");
		}	
		return esteBool;
	}
	
	private void limpiar() {
		jtdni.setText("");
		jtnombre.setText("");
		jtape1.setText("");
		jtape2.setText("");
	}
	
	/******************* DEVUELVE EL PANEL **************************/	
	public JPanel getPanel() {
		return estePanelA;
	}	
}
