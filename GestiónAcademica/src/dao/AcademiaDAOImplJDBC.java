package dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class AcademiaDAOImplJDBC implements AcademiaDAO {
	
	private String URLConexion = "jdbc:mysql://localhost:3306/dbformacion?user=root";
	
	private static final String FIND_ALL_ALUMNOS_SQL = "select id_alumno, nombre_alumno, foto from alumnos";
	private static final String FIND_ALUMNO_BY_ID_SQL = " select id_alumno, nombre_alumno, foto "+ " from alumnos " + " where id_alumno = ?";

	private static final String INSERT_ALUMNO_SQL = "insert into alumnos" + " (id_alumno, nombre_alumno, foto) " + " values (?,?,?) ";
	private static final String UPDATE_ALUMNO_SQL = "update alumnos set nombre_alumno=?, foto=? " + " where id_alumno=? ";
	private static final String DELETE_ALUMNO_SQL = "delete from alumnos where id_alumno = ?";
	
	private static final String FIND_ALL_CURSOS_SQL = "select id_curso, nombre_curso from cursos";
	private static final String FIND_CURSO_BY_ID_SQL = "select id_curso, nombre_curso from cursos where id_curso = ?";
	private static final String INSERT_CURSO_SQL = "insert into cursos (id_curso, nombre_curso) values (?, ?)";
	private static final String UPDATE_CURSO_SQL = "update cursos set nombre_curso = ? where id_curso = ?";
	private static final String DELETE_CURSO_SQL = "delete from cursos where id_curso = ?";
	
	private static final String FIND_ALL_MATRICULAS_SQL = "select id_matricula, id_alumno, id_curso, fecha_inicio from matriculas";
	private static final String FIND_MATRICULA_BY_ID_SQL = "select id_matricula, id_alumno, id_curso, fecha_inicio from matriculas where id_matricula = ?";
	private static final String INSERT_MATRICULA_SQL = "insert into matriculas (id_alumno, id_curso, fecha_inicio) values (?, ?, ?)";
	private static final String UPDATE_MATRICULA_SQL = "update matriculas set fecha_inicio = ? where id_matricula = ?";
	private static final String DELETE_MATRICULA_SQL = "delete from matriculas where id_matricula = ?";
	
	public AcademiaDAOImplJDBC() { }
	
	public AcademiaDAOImplJDBC(String URLConexion) {	
		this.URLConexion = URLConexion;
	}
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URLConexion);
	}

	private void releaseConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Collection<Alumno> cargarAlumnos() {
		Collection<Alumno> alumnos = new ArrayList<Alumno>();
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALL_ALUMNOS_SQL);
			ResultSet rs = ps.executeQuery();			
			while (rs.next()) {
				int id= rs.getInt(1);				
				String nombre = 
						(rs.getString(2)!=null?rs.getString(2):"sin nombre");
				Blob foto=rs.getBlob(3);
					
				Alumno alumno=new Alumno(id,nombre);
				
				if (foto!=null)
					alumno.setFoto(foto.getBytes(1L, (int)foto.length()));
				else
					alumno.setFoto(null);
				
				alumnos.add(alumno);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				System.err.println("Error: " + t);
			}
		} finally {
			releaseConnection(con);
		}
		return alumnos;
	}


	@Override
	public Alumno getAlumno(int idAlumno) {
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALUMNO_BY_ID_SQL);
			ps.setInt(1, idAlumno);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id_Alumno= rs.getInt(1);
								
				String nombreAlumno = 
					(rs.getString(2)!=null?rs.getString(2):"sin nombre");
				Blob foto=rs.getBlob(3);
				Alumno alumno=new Alumno(id_Alumno,nombreAlumno);
				if (foto!=null)
					alumno.setFoto(foto.getBytes(1L, (int)foto.length()));
				else
					alumno.setFoto(null);
				return alumno;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			for (Throwable t : e) {
				System.err.println("Errores: " + t);
			}
		} finally {
			releaseConnection(con);
		}
		return null;
	}


	@Override
	public int grabarAlumno(Alumno alumno) {
		int retorno=0;
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_ALUMNO_SQL);
			ps.setInt(1, alumno.getIdAlumno());			
			ps.setString(2, alumno.getNombreAlumno());	
			
			if (alumno.getFoto()!=null) {
				ps.setBinaryStream(3, 
						new ByteArrayInputStream(alumno.getFoto()));
			} else {
				ps.setBinaryStream(3, null);
			}			
			
			retorno=ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);			
		}
		return retorno;
	}


	@Override
	public int actualizarAlumno(Alumno alumno) {
		int retorno=0;
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_ALUMNO_SQL);
			ps.setString(1, alumno.getNombreAlumno());
			
			if (alumno.getFoto() != null) {
				ps.setBinaryStream(2, new ByteArrayInputStream(alumno
						.getFoto()));
			} else {
				ps.setBinaryStream(2, null);
			}
			
			ps.setInt(3, alumno.getIdAlumno());			
			retorno=ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return retorno;
	}

	@Override
	public int borrarAlumno(int idAlumno) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_ALUMNO_SQL);
			ps.setInt(1, idAlumno);
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}

	@Override
	public Collection<Curso> cargarCursos() {
		Collection<Curso> cursos = new ArrayList<Curso>();
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALL_CURSOS_SQL);
			ResultSet rs = ps.executeQuery();			
			while (rs.next()) {
				int id = rs.getInt(1);				
				String nombre = (rs.getString(2) != null ? rs.getString(2) : "sin nombre");
				cursos.add(new Curso(id, nombre));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return cursos;
	}

	@Override
	public Curso getCurso(int idCurso) {
		Curso curso = null;
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_CURSO_BY_ID_SQL);
			ps.setInt(1, idCurso);
			ResultSet rs = ps.executeQuery();			
			if (rs.next()) {
				int id = rs.getInt(1);				
				String nombre = (rs.getString(2) != null ? rs.getString(2) : "sin nombre");
				curso = new Curso(id, nombre);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return curso;
	}

	@Override
	public int grabarCurso(Curso curso) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_CURSO_SQL);
			ps.setInt(1, curso.getIdCurso());
			ps.setString(2, curso.getNombreCurso());
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}

	@Override
	public int actualizarCurso(Curso curso) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CURSO_SQL);
			ps.setString(1, curso.getNombreCurso());
			ps.setInt(2, curso.getIdCurso());
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}

	@Override
	public int borrarCurso(int idCurso) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_CURSO_SQL);
			ps.setInt(1, idCurso);
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}

	@Override
	public Collection<Matricula> cargarMatriculas() {
		Collection<Matricula> matriculas = new ArrayList<Matricula>();
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_ALL_MATRICULAS_SQL);
			ResultSet rs = ps.executeQuery();			
			while (rs.next()) {
				long idMatricula = rs.getLong(1);
				int idAlumno = rs.getInt(2);
				int idCurso = rs.getInt(3);
				Date fechaInicio = rs.getDate(4);
				matriculas.add(new Matricula(idMatricula, idAlumno, idCurso, fechaInicio));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return matriculas;
	}

	@Override
	public long getIdMatricula(int idAlumno, int idCurso) {
	    long idMatricula = -1;            // <- inicializamos a -1
	    Connection con = null;
	    try {
	        con = getConnection();
	        PreparedStatement ps = con.prepareStatement(
	            "select id_matricula from matriculas where id_alumno = ? and id_curso = ?");
	        ps.setInt(1, idAlumno);
	        ps.setInt(2, idCurso);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            idMatricula = rs.getLong(1);
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        releaseConnection(con);
	    }
	    return idMatricula;
	}


	@Override
	public Matricula getMatricula(long idMatricula) {
		Matricula matricula = null;
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(FIND_MATRICULA_BY_ID_SQL);
			ps.setLong(1, idMatricula);
			ResultSet rs = ps.executeQuery();			
			if (rs.next()) {
				long id = rs.getLong(1);
				int idAlumno = rs.getInt(2);
				int idCurso = rs.getInt(3);
				Date fechaInicio = rs.getDate(4);
				matricula = new Matricula(id, idAlumno, idCurso, fechaInicio);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return matricula;
	}

	@Override
	public int grabarMatricula(Matricula matricula) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_MATRICULA_SQL);
			ps.setInt(1, matricula.getIdAlumno());
			ps.setInt(2, matricula.getIdCurso());
			ps.setDate(3, new java.sql.Date(matricula.getFechaInicio().getTime()));
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}

	@Override
	public int actualizarMatricula(Matricula matricula) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_MATRICULA_SQL);
			ps.setDate(1, new java.sql.Date(matricula.getFechaInicio().getTime()));
			ps.setLong(2, matricula.getIdMatricula());
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}

	@Override
	public int borrarMatricula(long idMatricula) {
		Connection con = null;
		int result = 0;
		try {
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_MATRICULA_SQL);
			ps.setLong(1, idMatricula);
			result = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseConnection(con);
		}
		return result;
	}
}