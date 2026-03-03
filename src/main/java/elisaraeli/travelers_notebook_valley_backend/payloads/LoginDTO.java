package elisaraeli.travelers_notebook_valley_backend.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Per il login dell'utente
public record LoginDTO(

        @NotBlank(message = "L'email è obbligatoria.")
        @Email(message = "Inserisci un indirizzo email valido.")
        String email,

        @NotBlank(message = "La password è obbligatoria.")
        String password

) {
}

