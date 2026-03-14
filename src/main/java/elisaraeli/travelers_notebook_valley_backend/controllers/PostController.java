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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
        return new PostResponse(p);
    }

    // paginazione dei post
    @GetMapping
    public Page<PostResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataCreazione") String sortBy
    ) {
        return postService.getAll(page, size, sortBy)
                .map(PostResponse::new);
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

    // elimino il post
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente utente
    ) {
        postService.delete(id, utente.getId());
    }

    // tutti i post dell’utente loggato
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<PostResponse> getMyPosts(@AuthenticationPrincipal Utente utente) {
        return postService.getByUserId(utente.getId());
    }

    @PostMapping("/{id}/foto")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public PostResponse uploadFoto(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal Utente utente
    ) {
        return postService.uploadFoto(id, file, utente.getId());
    }
}