package elisaraeli.travelers_notebook_valley_backend.controllers;


import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.ValidationException;
import elisaraeli.travelers_notebook_valley_backend.payloads.LoginDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.LoginResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtenteResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtentiDTO;
import elisaraeli.travelers_notebook_valley_backend.services.AuthService;
import elisaraeli.travelers_notebook_valley_backend.services.UtenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    @Autowired
    public AuthController(AuthService authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginDTO body) {
        return new LoginResponse(this.authService.checkCredentialAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteResponse register(@RequestBody @Validated UtentiDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorList);
        } else {
            return this.utenteService.saveUtente(payload);
        }
    }

    @GetMapping("/me")
    public UtenteResponse me(@AuthenticationPrincipal Utente utente) {
        return new UtenteResponse(
                utente.getId(),
                utente.getUsername(),
                utente.getEmail(),
                utente.getAvatar(),
                utente.getDataRegistrazione(),
                utente.getRuolo()
        );
    }


}
