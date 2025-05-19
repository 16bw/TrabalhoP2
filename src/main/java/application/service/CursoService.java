package application.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.model.Curso;
import application.record.CursoDTO;
import application.record.GenericResponse;
import application.repository.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<CursoDTO> getAllCursos() {
        List<Curso> cursos = cursoRepository.findAll();
        return cursos.stream()
                .map(CursoDTO::new)
                .collect(Collectors.toList());
    }

    public CursoDTO getCursoById(Long id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        
        if (cursoOpt.isEmpty()) {
            throw new NoSuchElementException("Curso não encontrado com id: " + id);
        }
        
        return new CursoDTO(cursoOpt.get());
    }

    @Transactional
    public CursoDTO criarCurso(CursoDTO dto) {
        Curso curso = new Curso();
        curso.setNome(dto.nome());
        curso.setDescricao(dto.descricao());
        curso.setCargaHoraria(dto.cargaHoraria());

        curso = cursoRepository.save(curso);
        return new CursoDTO(curso);
    }

    @Transactional
    public CursoDTO atualizarCurso(Long id, CursoDTO dto) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        
        if (cursoOpt.isEmpty()) {
            throw new NoSuchElementException("Curso não encontrado com id: " + id);
        }
        
        Curso curso = cursoOpt.get();
        curso.setNome(dto.nome());
        curso.setDescricao(dto.descricao());
        curso.setCargaHoraria(dto.cargaHoraria());

        curso = cursoRepository.save(curso);
        return new CursoDTO(curso);
    }

    @Transactional
    public GenericResponse deletarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new NoSuchElementException("Curso não encontrado com id: " + id);
        }
        cursoRepository.deleteById(id);
        return new GenericResponse("Curso excluído com sucesso");
    }
}