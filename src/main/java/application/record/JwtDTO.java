package application.record;


public record JwtDTO(
    String token,
    Long id,
    String nome,
    String email
) {
}