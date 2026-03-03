package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FotoDTO(
        @NotNull(message = "Bisogna indicare il post a cui aggiungere la foto.")
        UUID idPost,

        @NotBlank(message = "L'URL della foto è obbligatorio.")
        String url,

        String descrizione

) {
}
