package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.Aluno;
import application.record.AlunoDTO;
import application.record.GenericResponse;
import application.record.JwtDTO;
import application.record.LoginDTO;
import application.repository.AlunoRepository;
import application.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário e retorna um token JWT")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            
            
            // Buscar o aluno pelo email
            Optional<Aluno> alunoOpt = alunoRepository.findByEmail(loginDTO.email());
            if (alunoOpt.isEmpty()) {
                
                return ResponseEntity.badRequest()
                        .body(new GenericResponse("Aluno não encontrado com este email"));
            }
            
            // Verificar a senha
            Aluno aluno = alunoOpt.get();
            
            
            
            
            if (!passwordEncoder.matches(loginDTO.senha(), aluno.getSenha())) {
                
                return ResponseEntity.badRequest()
                        .body(new GenericResponse("Senha incorreta"));
            }
            
            // Gerar token JWT
            String token = tokenService.generateToken(aluno);
            
            
            return ResponseEntity.ok(
                    new JwtDTO(token, aluno.getId(), aluno.getNome(), aluno.getEmail())
            );
            
        } catch (Exception e) {
            
            return ResponseEntity.badRequest()
                    .body(new GenericResponse("Erro no login: " + e.getMessage()));
        }
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar novo usuário", description = "Registra um novo usuário no sistema")
    public ResponseEntity<?> registro(@RequestBody AlunoDTO alunoDTO) {
        try {
            
            
            if (alunoRepository.existsByEmail(alunoDTO.email())) {
                
                return ResponseEntity.badRequest()
                        .body(new GenericResponse("Email já está em uso!"));
            }

            // Criar e salvar o novo usuário
            Aluno aluno = new Aluno();
            aluno.setNome(alunoDTO.nome());
            aluno.setEmail(alunoDTO.email());
            
            String senhaCriptografada = passwordEncoder.encode(alunoDTO.senha());
            aluno.setSenha(senhaCriptografada);
            
            
            
            
            
            Aluno alunoSalvo = alunoRepository.save(aluno);
            
            
            // Gerar token automaticamente para o novo usuário
            String token = tokenService.generateToken(alunoSalvo);
            
            return ResponseEntity.ok(
                    new JwtDTO(token, alunoSalvo.getId(), alunoSalvo.getNome(), alunoSalvo.getEmail())
            );
        } catch (Exception e) {
            
            return ResponseEntity.badRequest()
                    .body(new GenericResponse("Erro ao registrar: " + e.getMessage()));
        }
    }
    
    @GetMapping("/teste")
    public ResponseEntity<GenericResponse> teste() {
        
        return ResponseEntity.ok(new GenericResponse("API de autenticação funcionando!"));
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(alunoRepository.findAll());
    }
}