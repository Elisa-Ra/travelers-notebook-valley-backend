package elisaraeli.travelers_notebook_valley_backend.payloads;


import elisaraeli.travelers_notebook_valley_backend.entities.Foto;

import java.time.LocalDate;
import java.util.UUID;

// payload di risposta
public record FotoResponse(
        UUID id,
        String url,
        String descrizione,
        LocalDate dataCaricamento
) {
    public FotoResponse(Foto f) {
        this(
                f.getId(),
                f.getUrl(),
                f.getDescrizione(),
                f.getDataCaricamento()
        );
    }
}

