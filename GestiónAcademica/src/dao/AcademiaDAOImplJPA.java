package dao;

import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class AcademiaDAOImplJPA implements AcademiaDAO {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    
    public AcademiaDAOImplJPA() {
        emf = Persistence.createEntityManagerFactory("GestionAcademica");
        em = emf.createEntityManager();
    }
    
    @Override
    public Collection<Alumno> cargarAlumnos() {
        TypedQuery<Alumno> query = em.createQuery("SELECT a FROM Alumno a", Alumno.class);
        return query.getResultList();
    }

    @Override
    public Alumno getAlumno(int idAlumno) {
        return em.find(Alumno.class, idAlumno);
    }

    @Override
    public int grabarAlumno(Alumno alumno) {
        try {
            em.getTransaction().begin();
            em.persist(alumno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int actualizarAlumno(Alumno alumno) {
        try {
            em.getTransaction().begin();
            em.merge(alumno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int borrarAlumno(int idAlumno) {
        try {
            em.getTransaction().begin();
            Alumno alumno = em.find(Alumno.class, idAlumno);
            if (alumno != null) {
                em.remove(alumno);
            }
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Collection<Curso> cargarCursos() {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c", Curso.class);
        return query.getResultList();
    }

    @Override
    public Curso getCurso(int idCurso) {
        return em.find(Curso.class, idCurso);
    }

    @Override
    public int grabarCurso(Curso curso) {
        try {
            em.getTransaction().begin();
            em.persist(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int actualizarCurso(Curso curso) {
        try {
            em.getTransaction().begin();
            em.merge(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int borrarCurso(int idCurso) {
        try {
            em.getTransaction().begin();
            Curso curso = em.find(Curso.class, idCurso);
            if (curso != null) {
                em.remove(curso);
            }
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Collection<Matricula> cargarMatriculas() {
        TypedQuery<Matricula> query = em.createQuery("SELECT m FROM Matricula m", Matricula.class);
        return query.getResultList();
    }

    @Override
    public long getIdMatricula(int idAlumno, int idCurso) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT m.idMatricula FROM Matricula m WHERE m.idAlumno = :alumno AND m.idCurso = :curso", 
            Long.class);
        query.setParameter("alumno", idAlumno);
        query.setParameter("curso", idCurso);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Matricula getMatricula(long idMatricula) {
        return em.find(Matricula.class, idMatricula);
    }

    @Override
    public int grabarMatricula(Matricula matricula) {
        try {
            em.getTransaction().begin();
            em.persist(matricula);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int actualizarMatricula(Matricula matricula) {
        try {
            em.getTransaction().begin();
            em.merge(matricula);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int borrarMatricula(long idMatricula) {
        try {
            em.getTransaction().begin();
            Matricula matricula = em.find(Matricula.class, idMatricula);
            if (matricula != null) {
                em.remove(matricula);
            }
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        super.finalize();
    }
}