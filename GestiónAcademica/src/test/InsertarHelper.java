package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import dao.AcademiaDAO;
import dao.AcademiaDAOFactory;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class InsertarHelper {
    
    private AcademiaDAO dao = null;
    
    public InsertarHelper() {
        System.out.println("Creando el DAO...");
        dao = AcademiaDAOFactory.getAcademiaDAO();
    }
    
    private void insertarAlumno(int id, String nombre, String rutaFoto) {
        if (dao.getAlumno(id) != null) {
            System.out.println("El alumno con ID " + id + " ya existe. No se insertará.");
            return;
        }

        System.out.println("\nCreando un alumno...");
        Alumno alumno = new Alumno(id, nombre);
        
        File file = new File(rutaFoto);
        
        byte[] foto=null;
        try {
            foto = getBytesFromFile(file);
        } catch (IOException e) { e.printStackTrace(); }
        
        alumno.setFoto(foto);
        System.out.println("Grabando el nuevo alumno...");
        try {
            if (dao.grabarAlumno(alumno) == 1) {
                System.out.println("Se ha grabado el alumno");
            }
        } catch (Exception e) {
            System.out.println("Error al grabar alumno");
        }
    }
    
    private void modificarAlumno(int id, String nombre, String rutaFoto) {
        Alumno alumno = dao.getAlumno(id);
        if (alumno == null) {
            System.out.println("No se encontró el alumno con ID: " + id);
            return;
        }
        System.out.println("\nModificando el nombre del alumno con id: " + id);
        alumno.setNombreAlumno(nombre);
        
        if (rutaFoto!=null) {
            System.out.println("\nModificando la foto del alumno con id: "+id+" y nombre: "+alumno.getNombreAlumno());
            File file = new File(rutaFoto);
            
            byte[] foto=null;
            try {
                foto = getBytesFromFile(file);
            } catch (IOException e) { e.printStackTrace(); }
            alumno.setFoto(foto);
        }

        
        try {
            if (dao.actualizarAlumno(alumno) == 1) {
                System.out.println("Se ha modificado el alumno con id: " + id);
            }
        } catch (Exception e) {
            System.out.println("Error al modificar el alumno con id: " + id);
        }
    }
    
    private void insertarCurso(int id, String nombre) {
        if (dao.getCurso(id) != null) {
            System.out.println("El curso con ID " + id + " ya existe. No se insertará.");
            return;
        }
        System.out.println("\nCreando un curso...");
        Curso curso = new Curso(id, nombre);
        System.out.println("Grabando el nuevo curso...");
        try {
            if (dao.grabarCurso(curso) == 1) {
                System.out.println("Se ha grabado el curso");
            }
        } catch (Exception e) {
            System.out.println("Error al grabar el curso");
        }
    }
    
    private void modificarCurso(int id, String nombre) {
        Curso curso = dao.getCurso(id);
        if (curso == null) {
            System.out.println("No se encontró el curso con ID: " + id);
            return;
        }
        System.out.println("\nModificando el nombre del curso con id: " + id);
        curso.setNombreCurso(nombre);
        try {
            if (dao.actualizarCurso(curso) == 1) {
                System.out.println("Se ha modificado el curso con id: " + id);
            }
        } catch (Exception e) {
            System.out.println("Error al modificar el curso con id: " + id);
        }
    }
    
    private void insertarMatricula(int id_alumno, int id_curso) {
    	long existente = dao.getIdMatricula(id_alumno, id_curso);
        if (existente > 0) {
            System.out.println("La matrícula del alumno " + id_alumno +
                               " en el curso " + id_curso + " ya existe. No se insertará.");
            return;
        }
        if (dao.getIdMatricula(id_alumno, id_curso) != -1) {
            System.out.println("La matrícula del alumno " + id_alumno + " en el curso " + id_curso + " ya existe. No se insertará.");
            return;
        }
        System.out.println("\nCreando una matrícula...");
        Matricula matricula = new Matricula(0, id_alumno, id_curso, new Date());
        System.out.println("Grabando la nueva matrícula...");
        try {
            if (dao.grabarMatricula(matricula) == 1) {
                System.out.println("Se ha grabado la matrícula");
            }
        } catch (Exception e) {
            System.out.println("Error al grabar la matrícula");
        }
    }
    
    private void modificarMatricula(int id_alumno, int id_curso, Date fecha) {
        long idMatricula = dao.getIdMatricula(id_alumno, id_curso);
        if (idMatricula == -1) {
            System.out.println("No se encontró la matrícula del alumno " + id_alumno + " en el curso " + id_curso);
            return;
        }
        Matricula matricula = dao.getMatricula(idMatricula);
        if (matricula == null) {
            System.out.println("No se pudo recuperar la matrícula con ID: " + idMatricula);
            return;
        }
        System.out.println("\nModificando fecha de la matrícula...");
        matricula.setFechaInicio(fecha);
        try {
            if (dao.actualizarMatricula(matricula) == 1) {
                System.out.println("Se ha modificado la matrícula");
            }
        } catch (Exception e) {
            System.out.println("Error al modificar la matrícula: " + e.getMessage());
        }
    }
    
    private void showAllData() {
        showData(dao.cargarAlumnos(), "Alumnos");
        showData(dao.cargarCursos(), "Cursos");
        showData(dao.cargarMatriculas(), "Matrículas");        
    }
    
    private void showData(Collection<?> coleccion, String entidad) {
        System.out.println("\nMostrando..." + entidad);
        for (Object obj : coleccion) 
            System.out.println(obj);        
    }
    
    private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();

        if (length > Integer.MAX_VALUE) {            
            System.out.println("Fichero demasiado grande!");
            System.exit(1);
        }

        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("No se ha podido leer complemtamente el fichero "+file.getName());
        }

        is.close();
        return bytes;
    }

    
    public static void main(String[] args) {
        InsertarHelper programa = new InsertarHelper();
        programa.insertarAlumno(1000,"Daniel","imagenes/cara2.jpg");
        programa.insertarAlumno(1001,"Francisco","imagenes/cara4.jpg");
        programa.modificarAlumno(1000, "Ezequiel",null);        
        programa.modificarAlumno(1000, "Agapito","imagenes/cara1.jpg");
        programa.insertarCurso(500, "Java");
        programa.insertarCurso(501, ".NET");
        programa.modificarCurso(500, "Java avanzado");
        programa.insertarMatricula(1000, 500);
        programa.insertarMatricula(1000, 501);
        programa.insertarMatricula(1001, 500);
        Calendar fecha = GregorianCalendar.getInstance();
        fecha.set(Calendar.MONTH, 11);
        programa.modificarMatricula(1001, 500, fecha.getTime());
        programa.showAllData();
        System.out.println("\nFin del programa.");
    }
}