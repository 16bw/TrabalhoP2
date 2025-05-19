package application.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import application.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}