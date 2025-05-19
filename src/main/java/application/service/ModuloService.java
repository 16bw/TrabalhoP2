package application.service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.model.Curso;
import application.model.Modulo;
import application.record.GenericResponse;
import application.record.ModuloDTO;
import application.repository.CursoRepository;
import application.repository.ModuloRepository;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<ModuloDTO> getModulosByCursoId(Long cursoId) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new NoSuchElementException("Curso não encontrado com id: " + cursoId);
        }

        List<Modulo> modulos = moduloRepository.findByCursoId(cursoId);
        return modulos.stream()
                .map(ModuloDTO::new)
                .collect(Collectors.toList());
    }

    public ModuloDTO getModuloById(Long id) {
        Optional<Modulo> moduloOpt = moduloRepository.findById(id);
        
        if (moduloOpt.isEmpty()) {
            throw new NoSuchElementException("Módulo não encontrado com id: " + id);
        }
        
        return new ModuloDTO(moduloOpt.get());
    }

    @Transactional
    public ModuloDTO criarModulo(Long cursoId, ModuloDTO dto) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        
        if (cursoOpt.isEmpty()) {
            throw new NoSuchElementException("Curso não encontrado com id: " + cursoId);
        }
        
        Curso curso = cursoOpt.get();
        
        Modulo modulo = new Modulo();
        modulo.setTitulo(dto.titulo());
        modulo.setDescricao(dto.descricao());
        modulo.setCurso(curso);

        modulo = moduloRepository.save(modulo);
        return new ModuloDTO(modulo);
    }

    @Transactional
    public ModuloDTO atualizarModulo(Long id, ModuloDTO dto) {
        Optional<Modulo> moduloOpt = moduloRepository.findById(id);
        
        if (moduloOpt.isEmpty()) {
            throw new NoSuchElementException("Módulo não encontrado com id: " + id);
        }
        
        Modulo modulo = moduloOpt.get();
        modulo.setTitulo(dto.titulo());
        modulo.setDescricao(dto.descricao());

        modulo = moduloRepository.save(modulo);
        return new ModuloDTO(modulo);
    }

    @Transactional
    public GenericResponse deletarModulo(Long id) {
        if (!moduloRepository.existsById(id)) {
            throw new NoSuchElementException("Módulo não encontrado com id: " + id);
        }
        moduloRepository.deleteById(id);
        return new GenericResponse("Módulo excluído com sucesso");
    }
}