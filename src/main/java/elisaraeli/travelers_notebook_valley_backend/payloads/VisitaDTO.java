package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// per la creazione
public record VisitaDTO(
        @NotNull(message = "È necessario specificare il monumento.")
        UUID idMonumento
) {
}
