package elisaraeli.travelers_notebook_valley_backend.payloads;

import elisaraeli.travelers_notebook_valley_backend.entities.Post;

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
        UUID idUtente,
        String fotoUrl
) {
    public PostResponse(Post p) {
        this(
                p.getId(),
                p.getTitolo(),
                p.getContenuto(),
                p.getDataCreazione(),
                p.getDataModifica(),
                p.getMonumento().getId(),
                p.getUtente().getId(),
                p.getFotoUrl()
        );
    }
}

