package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import application.model.Aluno;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);

    Boolean existsByEmail(String email);

    public Aluno findByNome(String nome);

    @Query("SELECT a FROM Aluno a LEFT JOIN FETCH a.cursos WHERE a.email = :email")
    Optional<Aluno> findByEmailWithCursos(@Param("email") String email);

}
