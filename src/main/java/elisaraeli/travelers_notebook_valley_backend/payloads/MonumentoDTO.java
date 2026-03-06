package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// creazione e modifica del monumento (potrà farlo solo l'admin)
public record MonumentoDTO(
        @NotBlank(message = "Il nome del monumento è obbligatorio.")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria.")
        String descrizione,

        @Nullable
        String foto,

        @NotBlank(message = "La posizione è obbligatoria.")
        String posizione,

        @NotNull(message = "La categoria è obbligatoria.")
        String nomeCategoria

) {
}
