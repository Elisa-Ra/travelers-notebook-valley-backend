package elisaraeli.travelers_notebook_valley_backend.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record VisitaResponse(
        UUID id,
        LocalDate dataVisita,
        UUID idMonumento,
        UUID idUtente
) {
}

