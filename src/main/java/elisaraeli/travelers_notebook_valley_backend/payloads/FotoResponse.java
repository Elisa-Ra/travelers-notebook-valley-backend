package elisaraeli.travelers_notebook_valley_backend.payloads;


import java.time.LocalDate;
import java.util.UUID;

// payload di risposta
public record FotoResponse(
        UUID id,
        String url,
        String descrizione,
        LocalDate dataCaricamento

) {
}
