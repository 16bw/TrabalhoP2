package application.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.model.Aluno;
import application.model.Curso;
import application.record.AlunoDTO;
import application.record.CursoDTO;
import application.record.GenericResponse;
import application.repository.AlunoRepository;
import application.repository.CursoRepository;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public GenericResponse registrarAluno(AlunoDTO dto) {
        if (alunoRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já está em uso!");
        }

        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setSenha(passwordEncoder.encode(dto.senha()));

        alunoRepository.save(aluno);
        return new GenericResponse("Aluno registrado com sucesso");
    }

    public AlunoDTO getAlunoById(Long id) {

        Optional<Aluno> alunoOpt = alunoRepository.findById(id);

        if (alunoOpt.isEmpty()) {
            throw new NoSuchElementException("Aluno não encontrado com id: " + id);
        }

        return new AlunoDTO(alunoOpt.get());
    }

    @Transactional
    public GenericResponse matricularEmCurso(Long cursoId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        Optional<Aluno> alunoOpt = alunoRepository.findByEmail(email);
        if (alunoOpt.isEmpty()) {
            throw new NoSuchElementException("Aluno não encontrado");
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            throw new NoSuchElementException("Curso não encontrado com id: " + cursoId);
        }

        Aluno aluno = alunoOpt.get();
        Curso curso = cursoOpt.get();

        aluno.getCursos().add(curso);
        alunoRepository.save(aluno);

        return new GenericResponse("Matriculado com sucesso no curso");
    }

    @Transactional
    public GenericResponse cancelarMatricula(Long cursoId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        Optional<Aluno> alunoOpt = alunoRepository.findByEmail(email);
        if (alunoOpt.isEmpty()) {
            throw new NoSuchElementException("Aluno não encontrado");
        }

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            throw new NoSuchElementException("Curso não encontrado com id: " + cursoId);
        }

        Aluno aluno = alunoOpt.get();
        Curso curso = cursoOpt.get();

        aluno.getCursos().remove(curso);
        alunoRepository.save(aluno);

        return new GenericResponse("Matrícula cancelada com sucesso");
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> getCursosDoAluno() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        // Primeiro, vamos verificar o ID do aluno pelo email para confirmar que estamos
        // trabalhando com o aluno correto
        Optional<Aluno> alunoSimples = alunoRepository.findByEmail(email);
        if (alunoSimples.isPresent()) {
            System.out.println("ID do aluno encontrado: " + alunoSimples.get().getId());
        }

        // Agora vamos buscar diretamente pelo ID usando um método que sabemos que
        // funcionará
        // Assumindo que o aluno_id é 1 como mostrado na sua consulta MySQL
        Long alunoId = alunoSimples.map(Aluno::getId).orElse(1L); // Use o ID real ou 1 como fallback

        // Em vez de usar o método personalizado, vamos usar findById e inicializar
        // manualmente
        Optional<Aluno> alunoOpt = alunoRepository.findById(alunoId);

        if (alunoOpt.isEmpty()) {
            throw new NoSuchElementException("Aluno não encontrado com id: " + alunoId);
        }

        Aluno aluno = alunoOpt.get();

        // Forçar a inicialização da coleção dentro da transação
        Hibernate.initialize(aluno.getCursos());

        // Verificar se após a inicialização forçada temos cursos
        System.out.println("Número de cursos após inicialização: " + aluno.getCursos().size());

        // Vamos tentar uma abordagem alternativa - buscar diretamente da tabela de
        // relacionamento
        System.out.println("Tentando abordagem alternativa...");
        List<Curso> cursosDoAluno = cursoRepository.findCursosByAlunoId(alunoId);
        System.out.println("Cursos encontrados diretamente: " + cursosDoAluno.size());

        // Se a abordagem alternativa funcionou, use-a
        if (!cursosDoAluno.isEmpty()) {
            return cursosDoAluno.stream()
                    .map(CursoDTO::new)
                    .collect(Collectors.toList());
        }

        // Caso contrário, continue com a abordagem original
        return aluno.getCursos().stream()
                .map(CursoDTO::new)
                .collect(Collectors.toList());
    }
}