package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConferitaDTO(
        @NotNull(message = "L'id della medaglia è obbligatoria.")
        UUID idMedaglia,

        @NotNull(message = "L'id dell'utente è obbligatorio.")
        UUID idUtente
) {
}
