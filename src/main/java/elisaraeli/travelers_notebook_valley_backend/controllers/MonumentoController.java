package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.services.MonumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Solo l'ADMIN può fare il crud dei monumenti
@RestController
@RequestMapping("/monumenti")
public class MonumentoController {

    private final MonumentoService monumentoService;

    public MonumentoController(MonumentoService monumentoService) {
        this.monumentoService = monumentoService;
    }

    // creo il monumento
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public MonumentoResponse create(
            @RequestBody @Validated MonumentoDTO body,
            BindingResult validation
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return monumentoService.create(body);
    }

    // cerco il monumento per id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public MonumentoResponse getById(@PathVariable UUID id) {
        Monumento m = monumentoService.findById(id);
        return new MonumentoResponse(
                m.getId(), m.getNome(), m.getDescrizione(),
                m.getFoto(), m.getPosizione(), m.getCategoria().getId()
        );
    }

    @GetMapping("/monumento/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<PostResponse> getByMonumento(@PathVariable UUID id) {
        return monumentoService.getByMonumento(id);
    }


    // modifico il monumento
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MonumentoResponse update(
            @PathVariable UUID id,
            @RequestBody @Validated MonumentoDTO body,
            BindingResult validation
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return monumentoService.update(id, body);
    }

    // elimino il monumento
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        monumentoService.delete(id);
    }
}

