package entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

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
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    public Matricula() {}
    
    public Matricula(long idMatricula, int idAlumno, int idCurso, Date fechaInicio) {
        this.idMatricula = idMatricula;
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.fechaInicio = fechaInicio;
    }

    // Getters y setters (mantener los mismos que ya tienes)
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