package elisaraeli.travelers_notebook_valley_backend.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record MedagliaResponse(
        UUID id,
        String nome,
        String descrizione,
        String icona,
        LocalDate dataConferimento


) {
}
