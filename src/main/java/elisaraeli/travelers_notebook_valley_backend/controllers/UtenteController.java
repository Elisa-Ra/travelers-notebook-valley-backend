package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.UtenteResponse;
import elisaraeli.travelers_notebook_valley_backend.services.ConferitaService;
import elisaraeli.travelers_notebook_valley_backend.services.PostService;
import elisaraeli.travelers_notebook_valley_backend.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;
    private final PostService postService;
    private final ConferitaService conferitaService;


    public UtenteController(
            UtenteService utenteService,
            PostService postService,
            ConferitaService conferitaService
    ) {
        this.utenteService = utenteService;
        this.postService = postService;
        this.conferitaService = conferitaService;
    }


    // Recupero l'utente loggato
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

    // Lista degli utenti (solo admin)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UtenteResponse> getAll() {
        return utenteService.findAll();
    }

    // Elimina utente (solo admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        utenteService.delete(id);
    }

    // Recupero i post dell'utente loggato
    @GetMapping("/me/post")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<PostResponse> getMyPosts(@AuthenticationPrincipal Utente utente) {
        return postService.getByUtente(utente.getId());
    }

    // Recupero le medaglie dell'utente loggato
    @GetMapping("/me/medaglie")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<MedagliaResponse> getMyMedaglie(@AuthenticationPrincipal Utente utente) {
        return conferitaService.getMedaglieByUtente(utente.getId());
    }

}
