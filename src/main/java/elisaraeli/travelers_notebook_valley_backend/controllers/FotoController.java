package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.FotoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.FotoResponse;
import elisaraeli.travelers_notebook_valley_backend.services.FotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/foto")
public class FotoController {

    private final FotoService fotoService;

    public FotoController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    // CREO LA FOTO
    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FotoResponse create(
            @RequestBody @Validated FotoDTO body,
            BindingResult validation,
            @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return fotoService.create(body, utente.getId());
    }

    // upload come file
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FotoResponse upload(
            @RequestPart("idPost") UUID idPost,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "descrizione", required = false) String descrizione,
            @AuthenticationPrincipal Utente utente
    ) {
        return fotoService.upload(idPost, file, descrizione, utente.getId());
    }

    // elimino la foto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente utente
    ) {
        fotoService.delete(id, utente.getId());
    }
}
