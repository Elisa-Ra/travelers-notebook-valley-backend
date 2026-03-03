package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotBlank;

public record MedagliaDTO(
        @NotBlank(message = "Il nome della medaglia è obbligatorio.")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria.")
        String descrizione,

        @NotBlank(message = "L'icona è obbligatoria.")
        String icona
) {
}
