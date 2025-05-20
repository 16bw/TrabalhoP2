package application.service;

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

        Optional<Aluno> alunoOpt = alunoRepository.findByEmailWithCursos(email);

        if (alunoOpt.isEmpty()) {
            throw new NoSuchElementException("Aluno não encontrado");
        }

        Aluno aluno = alunoOpt.get();

        return aluno.getCursos().stream()
                .map(curso -> new CursoDTO(curso))
                .collect(Collectors.toList());
    }
}