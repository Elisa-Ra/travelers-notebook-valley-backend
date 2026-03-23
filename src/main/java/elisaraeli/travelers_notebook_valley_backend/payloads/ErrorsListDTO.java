package elisaraeli.travelers_notebook_valley_backend.payloads;

import java.util.List;

public record ErrorsListDTO(String message, List<String> errors) {
}
