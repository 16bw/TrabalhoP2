package application.record;

import application.model.Modulo;

public record ModuloDTO(
    Long id,
    String titulo,
    String descricao
) {
    public ModuloDTO(Modulo modulo) {
        this(modulo.getId(), modulo.getTitulo(), modulo.getDescricao());
    }
}