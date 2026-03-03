package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.VisitaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.VisitaResponse;
import elisaraeli.travelers_notebook_valley_backend.services.VisitaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/visite")
public class VisitaController {

    private final VisitaService visitaService;

    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitaResponse create(
            @RequestBody @Validated VisitaDTO body,
            BindingResult validation,
            @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return visitaService.registraVisita(body, utente.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public VisitaResponse getById(@PathVariable UUID id) {
        return visitaService.getById(id);
    }
}
