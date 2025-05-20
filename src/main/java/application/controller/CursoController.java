package application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import application.record.CursoDTO;
import application.record.GenericResponse;
import application.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cursos")
@RestController
@RequestMapping("/api/cursos")
@SecurityRequirement(name = "bearerAuth")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Operation(
        summary = "Listar todos os cursos", 
        description = "Retorna uma lista de todos os cursos disponíveis"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @GetMapping
    public List<CursoDTO> getAll() {
        return cursoService.getAllCursos();
    }

    @Operation(
        summary = "Obter detalhes de um curso", 
        description = "Retorna os detalhes de um curso específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalhes do curso retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @GetMapping("/{id}")
    public CursoDTO getById(@PathVariable Long id) {
        return cursoService.getCursoById(id);
    }

    @Operation(
        summary = "Criar novo curso", 
        description = "Cria um novo curso no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoDTO insert(@Valid @RequestBody CursoDTO cursoDTO) {
        return cursoService.criarCurso(cursoDTO);
    }

    @Operation(
        summary = "Atualizar curso", 
        description = "Atualiza um curso existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @PutMapping("/{id}")
    public CursoDTO update(@PathVariable Long id, @Valid @RequestBody CursoDTO cursoDTO) {
        return cursoService.atualizarCurso(id, cursoDTO);
    }

    @Operation(
        summary = "Excluir curso", 
        description = "Exclui um curso existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @DeleteMapping("/{id}")
    public GenericResponse delete(@PathVariable Long id) {
        return cursoService.deletarCurso(id);
    }
}
