package application.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import application.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query("SELECT c FROM Curso c JOIN c.alunos a WHERE a.id = :alunoId")
    List<Curso> findCursosByAlunoId(@Param("alunoId") Long alunoId);
}