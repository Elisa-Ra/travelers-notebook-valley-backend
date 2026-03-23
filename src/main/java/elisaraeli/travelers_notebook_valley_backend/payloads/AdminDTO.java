package elisaraeli.travelers_notebook_valley_backend.payloads;

public record AdminDTO(
        String username,
        String email,
        String password,
        String ruolo
) {
}
