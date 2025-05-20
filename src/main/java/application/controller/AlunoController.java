package application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.record.AlunoDTO;
import application.record.CursoDTO;
import application.record.GenericResponse;
import application.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Alunos")
@RestController
@RequestMapping("/api")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Operation(
        summary = "Criar novo usuário",
        description = "Registra um novo aluno no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já em uso")
    })
    @PostMapping("/usuario")
    public GenericResponse registrarAluno(@Valid @RequestBody AlunoDTO alunoDTO) {
        return alunoService.registrarAluno(alunoDTO);
    }

    @Operation(
        summary = "Obter detalhes de um aluno",
        description = "Retorna os detalhes de um aluno específico",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do aluno retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @GetMapping("/alunos/{id}")
    public AlunoDTO getAlunoById(@PathVariable Long id) {
        return alunoService.getAlunoById(id).withoutSenha();
    }

    @Operation(
        summary = "Matricular em curso",
        description = "Matrícula do aluno logado em um curso",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrícula realizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno ou curso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @PostMapping("/alunos/matricular/{cursoId}")
    public GenericResponse matricularEmCurso(@PathVariable Long cursoId) {
        return alunoService.matricularEmCurso(cursoId);
    }

    @Operation(
        summary = "Cancelar matrícula",
        description = "Cancela a matrícula do aluno logado em um curso",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrícula cancelada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno ou curso não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @DeleteMapping("/alunos/cancelar/{cursoId}")
    public GenericResponse cancelarMatricula(@PathVariable Long cursoId) {
        return alunoService.cancelarMatricula(cursoId);
    }

    @Operation(
        summary = "Listar cursos do aluno",
        description = "Lista todos os cursos em que o aluno logado está matriculado",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado")
    })
    @GetMapping("/alunos/cursos")
    public List<CursoDTO> getCursosDoAluno() {
        return alunoService.getCursosDoAluno();
    }
}