package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtenteResponse;
import elisaraeli.travelers_notebook_valley_backend.services.UtenteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/me")
    public UtenteResponse getProfile(@AuthenticationPrincipal Utente utente) {
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
