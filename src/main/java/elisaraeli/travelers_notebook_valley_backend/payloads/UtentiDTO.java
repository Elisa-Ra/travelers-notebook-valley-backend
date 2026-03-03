package elisaraeli.travelers_notebook_valley_backend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Per registrare l'utente
public record UtentiDTO(

        @NotBlank(message = "L'username è obbligatorio.")
        String username,

        @Email(message = "L'email non è valida.")
        @NotBlank(message = "L'email è obbligatoria.")
        String email,

        @NotBlank(message = "La password è obbligatoria.")
        @Pattern(
                regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "La password deve essere di almeno otto caratteri, con almeno una lettera maiuscola, una minuscola, un numero e un carattere speciale."
        )
        String password

) {
}

