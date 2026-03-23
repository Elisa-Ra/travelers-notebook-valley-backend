package elisaraeli.travelers_notebook_valley_backend.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record ConferitaResponse(
        UUID id,
        LocalDate dataConferimento,
        UUID idMedaglia,
        UUID idUtente

) {
}
