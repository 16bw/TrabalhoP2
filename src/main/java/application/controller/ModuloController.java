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

import application.record.GenericResponse;
import application.record.ModuloDTO;
import application.service.ModuloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Módulos")
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class ModuloController {

    @Autowired
    private ModuloService moduloService;

    @Operation(
        summary = "Listar módulos de um curso", 
        description = "Retorna todos os módulos de um curso específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de módulos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @GetMapping("/cursos/{cursoId}/modulos")
    public List<ModuloDTO> getAllByCursoId(@PathVariable Long cursoId) {
        return moduloService.getModulosByCursoId(cursoId);
    }

    @Operation(
        summary = "Obter detalhes de um módulo", 
        description = "Retorna os detalhes de um módulo específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalhes do módulo retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @GetMapping("/modulos/{id}")
    public ModuloDTO getById(@PathVariable Long id) {
        return moduloService.getModuloById(id);
    }

    @Operation(
        summary = "Criar novo módulo", 
        description = "Cria um novo módulo para um curso específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Módulo criado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @PostMapping("/cursos/{cursoId}/modulos")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuloDTO insert(@PathVariable Long cursoId, @Valid @RequestBody ModuloDTO moduloDTO) {
        return moduloService.criarModulo(cursoId, moduloDTO);
    }

    @Operation(
        summary = "Atualizar módulo", 
        description = "Atualiza um módulo existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @PutMapping("/modulos/{id}")
    public ModuloDTO update(@PathVariable Long id, @Valid @RequestBody ModuloDTO moduloDTO) {
        return moduloService.atualizarModulo(id, moduloDTO);
    }

    @Operation(
        summary = "Excluir módulo", 
        description = "Exclui um módulo existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @DeleteMapping("/modulos/{id}")
    public GenericResponse delete(@PathVariable Long id) {
        return moduloService.deletarModulo(id);
    }
}