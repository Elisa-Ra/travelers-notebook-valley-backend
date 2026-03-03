package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record FotoUploadDTO(
        @NotNull UUID idPost,
        @NotNull MultipartFile file,
        String descrizione
) {
}
