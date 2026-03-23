package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.ConferitaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.ConferitaResponse;
import elisaraeli.travelers_notebook_valley_backend.services.ConferitaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/conferite")
public class ConferitaController {

    private final ConferitaService conferitaService;

    public ConferitaController(ConferitaService conferitaService) {
        this.conferitaService = conferitaService;
    }

    // CREAZIONE -> conferisco la medaglia
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ConferitaResponse assign(
            @RequestBody @Validated ConferitaDTO body,
            BindingResult validation
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return conferitaService.assegnaMedaglia(body);
    }

    // Cerco il conferimento per id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ConferitaResponse getById(@PathVariable UUID id) {
        return conferitaService.getById(id);
    }
}

