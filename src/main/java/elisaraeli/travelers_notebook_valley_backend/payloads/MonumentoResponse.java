package elisaraeli.travelers_notebook_valley_backend.payloads;

import java.util.UUID;

public record MonumentoResponse(
        UUID id,
        String nome,
        String descrizione,
        String foto,
        String posizione,
        String nomeCategoria


) {
}
