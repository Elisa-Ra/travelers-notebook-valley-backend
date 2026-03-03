package elisaraeli.travelers_notebook_valley_backend.payloads;

import java.time.LocalDate;
import java.util.UUID;

// per restituire il post
public record PostResponse(
        UUID id,
        String titolo,
        String contenuto,
        LocalDate dataCreazione,
        LocalDate dataModifica,
        UUID idMonumento,
        UUID idUtente
) {
}
