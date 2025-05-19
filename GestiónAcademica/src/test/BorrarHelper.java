package test;

import java.util.Collection;
import dao.AcademiaDAO;
import dao.AcademiaDAOFactory;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class BorrarHelper {
    
    private AcademiaDAO dao = null;
    
    public BorrarHelper() {
        System.out.println("Creando el DAO...");
        dao = AcademiaDAOFactory.getAcademiaDAO();
    }
    
    private void borrarMatriculas() {
        System.out.println("Borrando cualquier matricula previa...");
        Collection<Matricula> matriculas = dao.cargarMatriculas();
        for (Matricula matricula : matriculas) {
            if (dao.borrarMatricula(matricula.getIdMatricula()) == 1) {
                System.out.println("Se ha borrado la matricula");
            }
        }
    }
    
    private void borrarAlumnos() {
        System.out.println("Borrando cualquier alumno previo...");
        Collection<Alumno> alumnos = dao.cargarAlumnos();
        for (Alumno alumno : alumnos) {
            if (dao.borrarAlumno(alumno.getIdAlumno()) == 1) {
                System.out.println("Se ha borrado el alumno");
            }
        }
    }
    
    private void borrarCursos() {
        System.out.println("Borrando cualquier curso previo...");
        Collection<Curso> cursos = dao.cargarCursos();
        for (Curso curso : cursos) {
            if (dao.borrarCurso(curso.getIdCurso()) == 1) {
                System.out.println("Se ha borrado el curso");
            }
        }
    }
    
    public static void main(String[] args) {
        BorrarHelper programa = new BorrarHelper();
        programa.borrarMatriculas();
        programa.borrarAlumnos();
        programa.borrarCursos();
        System.out.println("\nfin del programa.");
    }
}