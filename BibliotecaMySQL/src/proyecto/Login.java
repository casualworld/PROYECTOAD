package proyecto;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import conexiones.Pool;
import custom.Mine;
import design.GradientPanel;
import jtextfieldround.BorderLineRound;
import jtextfieldround.JTextFieldRound;
import java.sql.*;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private Connection cn;
	private JTextField jtuser;
	private GridBagConstraints c;
	private JPasswordField jtpass;
	private JButton btncon;
	private JPanel contentPane, pleft, pright, panelclose;
	private JLabel lblicon, lblclose, lblmsj, lbluser, lblpass;
	
	public void preparaGUI () {		
		contentPane= new JPanel();
		contentPane.setLayout(new GridLayout(1,2));
		contentPane.setBackground(Color.WHITE);
		
		setTitle("Login");
		setSize(800, 600);		
		setUndecorated(true);
		setContentPane(contentPane);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	public void agregarElementos() {
			
		//LADO IZQUIERDO
		pleft = new GradientPanel(Mine.FAVCOLORROSE, Mine.FAVCOLORBLACK);
		pleft.setLayout(new GridLayout(2,1));
		
		Icon icon1 = new ImageIcon(Mine.DIRIMG+"login.PNG");
		lblicon = new JLabel(icon1);		
		
		lblmsj= new JLabel(strLogin(), JLabel.CENTER);
		lblmsj.setForeground(Color.WHITE);
		lblmsj.setVerticalAlignment(JLabel.TOP);
		
		pleft.add(lblicon);
		pleft.add(lblmsj);
		
		//LADO DERECHO
		c = new GridBagConstraints();
		pright = new JPanel();
		pright.setLayout(new GridBagLayout());
		pright.setBackground(Mine.FAVCOLORBLACK);
		
		lblclose = new JLabel("x");
		lblclose.setForeground(Color.WHITE);
		lblclose.setFont(Mine.HELVETICA20);
		
		panelclose = new JPanel();
		panelclose.setBackground(Mine.FAVCOLORBLACK);
		panelclose.add(lblclose);			
		c.ipady = 0;    
		c.ipadx = 0;
		c.gridy = 0;       //row	
		c.gridx = 1;       //column
		c.insets = new Insets(0,0,0,0); 		
		c.anchor = GridBagConstraints.SOUTHEAST;
		pright.add(panelclose,c);

		lbluser = new JLabel("Usuario");
		lbluser.setForeground(Color.WHITE);
		c.ipady = 10;       
		c.ipadx = 0;
		c.gridy = 1;       //row	
		c.gridx = 0;       //column
		c.insets = new Insets(10,10,10,10); 
		pright.add(lbluser,c);		
		
		jtuser = new JTextFieldRound();
		jtuser.setBorder(new BorderLineRound(Color.black, true));
		jtuser.setFont(Mine.HELVETICA15);
		c.ipady = 10;       
		c.ipadx = 200;
		c.gridy = 1;       //row	
		c.gridx = 1;       //column
		c.insets = new Insets(10,10,10,10);
		pright.add(jtuser,c);
		
		lblpass = new JLabel("Password");
		lblpass.setForeground(Color.WHITE);
		c.ipady = 10;       
		c.ipadx = 0;
		c.gridy = 2;       //row	
		c.gridx = 0;       //column
		c.insets = new Insets(10,10,10,10);
		pright.add(lblpass,c);
		
		jtpass = new JPasswordField();
		jtpass.setBorder(new BorderLineRound(Color.GRAY, true));
		jtpass.setFont(Mine.HELVETICA15);
		jtpass.setHorizontalAlignment(SwingConstants.CENTER);
		c.ipady = 10;       
		c.ipadx = 200;
		c.gridy = 2;       //row	
		c.gridx = 1;       //column
		c.insets = new Insets(10,10,10,10);	
		pright.add(jtpass,c);
		
		btncon = new JButton("Conectar");
		btncon.setBorder(new BorderLineRound(Color.LIGHT_GRAY, true));
		btncon.setForeground(Color.WHITE);
		btncon.setBackground(Mine.FAVCOLORBLACK);
		c.ipady = 15;       
		c.ipadx = 15;
		c.gridy = 3;       //row	
		c.gridx = 0;       //column
		c.gridwidth = 2;
		c.insets = new Insets(10,10,10,10);	
		pright.add(btncon,c);		
				
		//CONTENEDOR PRINCIPAL
		contentPane.add(pleft);
		contentPane.add(pright);		
	}
	
	public void controlEvents() {
		lblclose.addMouseListener(cerrar());
		btncon.addActionListener(btnconectar());
		jtpass.addKeyListener(pressEnterToConnect());
	}
	
	public String strLogin() {
		return "<html><body><center><font size=\"6\">Bienvenid@ a la biblioteca</font></center> <br> <center>Visitenos en nuestra página oficial:</center> <br><center>www.biblioteca.com </center></body></html>";
	}
	
	public KeyListener pressEnterToConnect() {
		KeyListener esteMetodo = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	               connectWithDB();
	            }
	        }
		};
		return esteMetodo;
	}
		
	public ActionListener btnconectar() {
		ActionListener esteMetodo = new  ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectWithDB();		
			}
		};
		return esteMetodo;
	}
	
	public void connectWithDB() {		
		char[] chars = jtpass.getPassword();			
		String user = jtuser.getText();
		String pass = new String(chars);
		
		Pool mypool=new Pool(user,new String(pass));		
		Connection cn = null;        
		try {
			cn=mypool.dataSource.getConnection();	
			if(cn!=null) {	
				abrirVentanaGestionMySQL(user,pass);				
			}						
		}catch(SQLException except){					
			JOptionPane.showMessageDialog(contentPane, "Usuario y/o Contraseña incorrecta.","Error al conectarse",JOptionPane.ERROR_MESSAGE);
		}
		finally{
			desconectar();
		}						
	}
	
	private void abrirVentanaGestionMySQL(String user, String pass) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal(user,pass);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
	
	public MouseListener cerrar() {
		MouseListener esteMetodo = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {  
				dispose();
		    }
		};
		return esteMetodo;
	}
	
	private void desconectar() {
		try {
			if(cn!=null) {
				cn.close();
			}    		
        }catch (SQLException ex) {
        	System.out.println("Fallo desconectar");
        }
	}
	
	/********** CONSTRUCTOR DEL LOGIN ***************/
	public Login() {
		preparaGUI();
		agregarElementos();
		controlEvents();
	}	
}
