package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoResponse;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.services.MonumentoService;
import elisaraeli.travelers_notebook_valley_backend.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

// Solo l'ADMIN può fare il crud dei monumenti
@RestController
@RequestMapping("/monumento")
public class MonumentoController {

    private final MonumentoService monumentoService;
    private final PostService postService;

    public MonumentoController(MonumentoService monumentoService, PostService postService) {
        this.monumentoService = monumentoService;
        this.postService = postService;
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

    // GET DEI MONUMENTI
    // cerco il monumento per id
    @GetMapping("/{id}")
    public MonumentoResponse getById(@PathVariable UUID id) {
        Monumento m = monumentoService.findById(id);
        return new MonumentoResponse(
                m.getId(), m.getNome(), m.getDescrizione(),
                m.getFoto(), m.getPosizione(), m.getCategoria().getCategoria()
        );
    }

    @GetMapping("/{id}/posts")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<PostResponse> getByMonumento(@PathVariable UUID id) {
        return postService.getByMonumento(id);
    }

    @GetMapping
    public List<MonumentoResponse> getAll() {
        return monumentoService.getAll();
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

    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public MonumentoResponse uploadFoto(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file
    ) {
        return monumentoService.uploadFoto(id, file);
    }

}

