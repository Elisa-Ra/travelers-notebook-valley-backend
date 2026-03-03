package elisaraeli.travelers_notebook_valley_backend.payloads;

import elisaraeli.travelers_notebook_valley_backend.entities.UtenteRuolo;

import java.time.LocalDate;
import java.util.UUID;

// Payload di RISPOSTA, per non esporre dati sicuri
public record UtenteResponse(
        UUID id,
        String username,
        String email,
        String avatar,
        LocalDate dataRegistrazione,
        UtenteRuolo ruolo

) {
}
