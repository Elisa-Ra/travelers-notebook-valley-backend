package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MedagliaResponse;
import elisaraeli.travelers_notebook_valley_backend.services.MedagliaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/medaglie")
public class MedagliaController {

    private final MedagliaService medagliaService;

    public MedagliaController(MedagliaService medagliaService) {
        this.medagliaService = medagliaService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public MedagliaResponse create(
            @RequestBody @Validated MedagliaDTO body,
            BindingResult validation
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return medagliaService.create(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public MedagliaResponse getById(@PathVariable UUID id) {
        Medaglia m = medagliaService.findById(id);
        return new MedagliaResponse(
                m.getId(),
                m.getNome(),
                m.getDescrizione(),
                m.getIcona()
        );
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MedagliaResponse update(
            @PathVariable UUID id,
            @RequestBody @Validated MedagliaDTO body,
            BindingResult validation
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return medagliaService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        medagliaService.delete(id);
    }
}
