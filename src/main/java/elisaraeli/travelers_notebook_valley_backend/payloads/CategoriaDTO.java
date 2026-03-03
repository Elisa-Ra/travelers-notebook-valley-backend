package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
        @NotBlank(message = "Il nome della categoria è obbligatorio.")
        String categoria

) {
}
