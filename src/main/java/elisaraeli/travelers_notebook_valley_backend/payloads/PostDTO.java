package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Per la creazione di un post
public record PostDTO(
        @NotBlank(message = "Il titolo è obbligatorio.")
        String titolo,

        @NotBlank(message = "Il contenuto è obbligatorio.")
        String contenuto,

        @NotNull(message = "È necessario specificare il monumento.")
        UUID idMonumento

) {
}
