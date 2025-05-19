package application.record;


import application.model.Aluno;

public record AlunoDTO(
    Long id,
    
    String nome,
    
    String email,
    
    String senha
) {
    public AlunoDTO(Aluno aluno) {
        this(aluno.getId(), aluno.getNome(), aluno.getEmail(), null);
    }
    
    public AlunoDTO withoutSenha() {
        return new AlunoDTO(id, nome, email, null);
    }
}