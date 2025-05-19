package application.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cursos")
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false)
    private Integer cargaHoraria;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Modulo> modulos = new ArrayList<>();
    
    @ManyToMany(mappedBy = "cursos")
    private Set<Aluno> alunos = new HashSet<>();
}