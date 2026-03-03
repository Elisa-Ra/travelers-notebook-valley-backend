package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// creazione e modifica del monumento (potrà farlo solo l'admin)
public record MonumentoDTO(
        @NotBlank(message = "Il nome del monumento è obbligatorio.")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria.")
        String descrizione,

        @NotBlank(message = "La foto è obbligatoria.")
        String foto,

        @NotBlank(message = "La posizione è obbligatoria.")
        String posizione,

        @NotNull(message = "La categoria è obbligatoria.")
        UUID idCategoria

) {
}
