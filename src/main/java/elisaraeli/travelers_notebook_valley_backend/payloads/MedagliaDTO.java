package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MedagliaDTO(
        @NotBlank(message = "Il nome della medaglia è obbligatorio.")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria.")
        String descrizione,

        @NotNull
        String icona,
     
        UUID idMonumento
) {
}
