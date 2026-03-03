package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MonumentoService monumentoService;
    private final UtenteService utenteService;

    public PostService(PostRepository postRepository, MonumentoService monumentoService, UtenteService utenteService) {
        this.postRepository = postRepository;
        this.monumentoService = monumentoService;
        this.utenteService = utenteService;
    }

    public PostResponse create(PostDTO body, UUID idUtente) {

        Utente autore = utenteService.findById(idUtente);
        Monumento monumento = monumentoService.findById(body.idMonumento());

        Post nuovo = new Post(
                autore,
                body.titolo(),
                body.contenuto(),
                monumento
        );

        postRepository.save(nuovo);

        return new PostResponse(
                nuovo.getId(),
                nuovo.getTitolo(),
                nuovo.getContenuto(),
                nuovo.getDataCreazione(),
                nuovo.getDataModifica(),
                monumento.getId(),
                autore.getId()
        );
    }

    public Post findById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }
}
