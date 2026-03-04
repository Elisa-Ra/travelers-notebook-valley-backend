package elisaraeli.travelers_notebook_valley_backend.controllers;

import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Creo il post
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(
            @RequestBody @Validated PostDTO body,
            BindingResult validation,
            @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return postService.create(body, utente.getId());
    }

    // cerco il post per id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public PostResponse getById(@PathVariable UUID id) {
        Post p = postService.findById(id);
        return new PostResponse(
                p.getId(), p.getTitolo(), p.getContenuto(),
                p.getDataCreazione(), p.getDataModifica(),
                p.getMonumento().getId(), p.getUtente().getId()
        );
    }

    // paginazione dei post
    @GetMapping
    public Page<PostResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataCreazione") String sortBy
    ) {
        return postService.getAll(page, size, sortBy);
    }


    // modifico il post
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public PostResponse update(
            @PathVariable UUID id,
            @RequestBody @Validated PostDTO body,
            BindingResult validation,
            @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return postService.update(id, body, utente.getId());
    }

    // elimino il post (lo user può eliminare il post solo se ne è l'autore)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente utente
    ) {
        postService.delete(id, utente.getId());
    }
}

