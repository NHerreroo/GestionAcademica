package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "matriculas")
public class Matricula implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private long idMatricula;
    
    @Column(name = "id_alumno")
    private int idAlumno;
    
    @Column(name = "id_curso")
    private int idCurso;
    
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    public Matricula() {}
    
    public Matricula(long idMatricula, int idAlumno, int idCurso, Date fechaInicio) {
        this.idMatricula = idMatricula;
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.fechaInicio = fechaInicio;
    }

    public long getIdMatricula() {
        return this.idMatricula;
    }

    public void setIdMatricula(long idMatricula) {
        this.idMatricula = idMatricula;
    }

    public int getIdAlumno() {
        return this.idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdCurso() {
        return this.idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public Date getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public String toString() {
        return this.idMatricula + " - " + this.idAlumno + " - " + this.idCurso + " - " + this.fechaInicio;
    }
}