package application.record;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import application.model.Curso;

public record CursoDTO(
    Long id,
    String nome,
    String descricao,
    Integer cargaHoraria,
    List<ModuloDTO> modulos
) {
    public CursoDTO(Curso curso) {
        this(
            curso.getId(),
            curso.getNome(),
            curso.getDescricao(),
            curso.getCargaHoraria(),
            curso.getModulos() != null ? 
                curso.getModulos().stream()
                    .map(ModuloDTO::new)
                    .collect(Collectors.toList()) :
                Collections.emptyList()
        );
    }
    
    public CursoDTO(String nome, String descricao, Integer cargaHoraria) {
        this(null, nome, descricao, cargaHoraria, Collections.emptyList());
    }
}