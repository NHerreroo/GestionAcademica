package entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "id_curso")
    private int idCurso;
    
    @Column(name = "nombre_curso")
    private String nombreCurso;

    public Curso() {}
    
    public Curso(int idCurso, String nombreCurso) {
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
    }

    public int getIdCurso() {
        return this.idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return this.nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }
    
    public String toString() {
        return this.idCurso + " - " + this.nombreCurso;
    }
}