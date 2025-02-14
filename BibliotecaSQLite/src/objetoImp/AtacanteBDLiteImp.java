package objetoImp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import conexiones.Pool;
import interfaces.AtacanteBDDAO;
import objeto.Alumno;
import objeto.Devolucion;
import objeto.Libro;
import objeto.Prestamo;

public class AtacanteBDLiteImp implements AtacanteBDDAO{
	
	private Connection cn;
	private Pool mypool;
	
	public AtacanteBDLiteImp() {
	}

	/********************** LIBROS *************************/
	@Override
	public void agregarLibro(Libro libro) {
		String strquery = "INSERT INTO libros VALUES(?,?,?,?,?,?)";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, libro.getIsbn());
				ps.setString(2, libro.getTitulo());
				ps.setString(3, libro.getAutor());
				ps.setString(4, libro.getEditorial());
				ps.setString(5, libro.getAsignatura());
				ps.setString(6, libro.getEstado());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en agregar libro");
		}		
		finally {
			desconectar();
		}				
	}
	
	@Override
	public void borrarLibro(Libro libro) {
		String strquery = "DELETE FROM libros WHERE isbn=?";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, libro.getIsbn());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en borrar libro");
		}		
		finally {
			desconectar();
		}	
	}

	@Override
	public void actualizarLibro(Libro libro) {
		String strquery = "UPDATE libros SET titulo=?, autor=?, editorial=?, asignatura=?, estado=? WHERE isbn=?";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, libro.getTitulo());
				ps.setString(2, libro.getAutor());
				ps.setString(3, libro.getEditorial());
				ps.setString(4, libro.getAsignatura());
				ps.setString(5, libro.getEstado());
				ps.setString(6, libro.getIsbn());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en actualizar libro");
		}		
		finally {
			desconectar();
		}		
	}

	@Override
	public void actualizarEstadoLibro(Libro libro) {
		String strquery = "UPDATE libros SET estado=? WHERE isbn=?";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, libro.getEstado());
				ps.setString(2, libro.getIsbn());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en actualizar estado libro");
		}		
		finally {
			desconectar();
		}				
	}
	
	@Override
	public ArrayList<Libro> getAllLibros() {	
		ArrayList<Libro> estaLista = new ArrayList<Libro>();
		String strquery = "SELECT isbn, titulo, autor, editorial, asignatura, estado FROM libros";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Libro nuevoLibro = new Libro(rs.getString("isbn"), rs.getString("titulo"), rs.getString("autor"), rs.getString("editorial"), rs.getString("asignatura"), rs.getString("estado"));
					estaLista.add(nuevoLibro);
				}			
			}
		} catch (Exception e) {
			System.out.println("Error en obtener todos los libros");
		}		
		finally {
			desconectar();
		}		
		return estaLista;		
	}

	/********************** ALUMNOS *************************/	
	@Override
	public void agregarAlumno(Alumno alumno) {
		String strquery = "INSERT INTO alumnos VALUES(?,?,?,?)";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, alumno.getDni());
				ps.setString(2, alumno.getNombre());
				ps.setString(3, alumno.getApe1());
				ps.setString(4, alumno.getApe2());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en agregar alumno");
		}		
		finally {
			desconectar();
		}	
	}

	@Override
	public void borrarAlumno(Alumno alumno) {
		String strquery = "DELETE FROM alumnos WHERE dni=?";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, alumno.getDni());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en borrar alumno");
		}		
		finally {
			desconectar();
		}		
	}

	@Override
	public void actualizarAlumno(Alumno alumno) {
		String strquery = "UPDATE alumnos SET nombre=?, apellido1=?, apellido2=? WHERE dni=?";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, alumno.getNombre());
				ps.setString(2, alumno.getApe1());
				ps.setString(3, alumno.getApe2());
				ps.setString(4, alumno.getDni());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en actualizar alumno");
		}		
		finally {
			desconectar();
		}			
	}
	
	@Override
	public ArrayList<Alumno> getAllAlumnos() {
		ArrayList<Alumno> estaLista = new ArrayList<Alumno>();
		String strquery = "SELECT dni, nombre, apellido1, apellido2 FROM alumnos";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Alumno nuevoAlumno = new Alumno(rs.getString("dni"), rs.getString("nombre"), rs.getString("apellido1"),rs.getString("apellido2"));
					estaLista.add(nuevoAlumno);
				}			
			}else {
				
			}
		} catch (Exception e) {
			System.out.println("Error en obtener los alumnos");
		}		
		finally {
			desconectar();
		}		
		return estaLista;		
	}	

	/********************** PRESTAMOS *************************/
	@Override
	public void agregarPrestamo(Prestamo prestamo) {
		String strquery = "INSERT INTO prestamos VALUES(?,?,?,?,?)";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, prestamo.getAlumno());
				ps.setString(2, prestamo.getLibro());
				ps.setString(3, prestamo.getFechastrpres());
				ps.setString(4, prestamo.getFechastrdevol());
				ps.setString(5, prestamo.getEstado());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en agregar prestamo");
		}		
		finally {
			desconectar();
		}			
	}

	@Override
	public void borrarPrestamo(Prestamo prestamo) {
		String strquery = "DELETE FROM prestamos WHERE alumno=? AND libro=?";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, prestamo.getAlumno());
				ps.setString(2, prestamo.getLibro());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en borrar prestamo");
		}		
		finally {
			desconectar();
		}			
	}

	@Override
	public ArrayList<Prestamo> getAllPrestamos() {
		ArrayList<Prestamo> estaLista = new ArrayList<Prestamo>();
		String strquery = "SELECT alumno, libro, fechapres, fechadevol, estado FROM prestamos";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Prestamo cadaPrestamo = new Prestamo(rs.getString("alumno"), rs.getString("libro"), rs.getString("fechapres"),rs.getString("fechadevol"), rs.getString("estado"));
					estaLista.add(cadaPrestamo);
				}			
			}else {
				
			}
		} catch (Exception e) {
			System.out.println("Error en obtener todos los prestamos");
		}		
		finally {
			desconectar();
		}		
		return estaLista;	
	}

	/********************** DEVOLUCION *************************/
	@Override
	public void agregarDevolucion(Devolucion devolucion) {
		String strquery = "INSERT INTO devoluciones VALUES(?,?,?,?)";
		PreparedStatement ps=null;
		try {
			if (conectar()) {
				ps = cn.prepareStatement(strquery);	
				ps.setString(1, devolucion.getLibro());
				ps.setString(2, devolucion.getAlumno());				
				ps.setString(3, devolucion.getFechastrdev());
				ps.setString(4, devolucion.getEstado());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Error en agregar devolucion");
		}		
		finally {
			desconectar();
		}	
		
	}

	@Override
	public ArrayList<Devolucion> getAllDevoluciones() {
		//Metodo que llama a la funcion almacenada Obtenerdevoluciones
		ArrayList<Devolucion> estaLista = new ArrayList<Devolucion>();
		try {
			if (conectar()) {						
				CallableStatement cst = cn.prepareCall("{call Obtenerdevoluciones ()}");
				ResultSet rs = cst.executeQuery();
				while(rs.next()) {
					Devolucion cadaDev = new Devolucion(rs.getString("libro"),rs.getString("alumno"),  rs.getString("fechadevol"),rs.getString("estado"));
					estaLista.add(cadaDev);
				}			
			}else {
				
			}
		} catch (Exception e) {
			System.out.println("Error en obtener todos las devoluciones");
		}		
		finally {
			desconectar();
		}		
		return estaLista;
	}
	
	
	/********************** CONECTAR Y DESCONECTAR A LA BASE DE DATOS *************************/
	private boolean conectar() {
		boolean conectado=false;		
		mypool = new Pool();
		cn = null;        
		try {
			cn=mypool.dataSource.getConnection();
			if(cn!=null)
				conectado=true;
			}catch (SQLException e) {
		           System.out.println(e);
		    }
		return conectado;
	}
	
	private void desconectar() {
		try {
    		cn.close();
        }catch (SQLException ex) {
        	System.out.println(ex);		            	
        }
	}

}
