package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.UtenteRuolo;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    // CREO IL POST
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

    // trovo il post per ID
    public Post findById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    // MODIFICA DEI POST
    // i post possono essere modificati dall'autore del post
    public PostResponse update(UUID postId, PostDTO body, UUID idUtente) {

        // prendo il post
        Post post = this.findById(postId);
        Utente richiedente = utenteService.findById(idUtente);

        // controllo che l'utente sia l'autore del post
        boolean isAutore = post.getUtente().getId().equals(idUtente);


        if (!isAutore) {
            throw new BadRequestException("Non hai il permesso di modificare il post. Solo l'autore del post può modificarlo");
        }

        // aggiorno i campi
        post.setTitolo(body.titolo());
        post.setContenuto(body.contenuto());
        Monumento monumento = monumentoService.findById(body.idMonumento());
        post.setMonumento(monumento);
        post.setDataModifica(LocalDate.now());

        // salvo
        postRepository.save(post);

        return new PostResponse(
                post.getId(),
                post.getTitolo(),
                post.getContenuto(),
                post.getDataCreazione(),
                post.getDataModifica(),
                monumento.getId(),
                post.getUtente().getId()
        );

    }

    // CANCELLO UN POST
    // Il post può essere cancellato dall'autore ma anche dall'admin (per questioni di moderazione)
    public void delete(UUID postId, UUID idUtente) {

        Post post = this.findById(postId);
        Utente richiedente = utenteService.findById(idUtente);

        boolean isAutore = post.getUtente().getId().equals(idUtente);
        boolean isAdmin = richiedente.getRuolo().equals(UtenteRuolo.ADMIN);

        if (!isAutore && !isAdmin) {
            throw new BadRequestException("Non puoi eliminare questo post.");
        }

        postRepository.delete(post);
    }

    // GET ALL con paginazione
    public Page<PostResponse> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return postRepository.findAll(pageable)
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitolo(),
                        post.getContenuto(),
                        post.getDataCreazione(),
                        post.getDataModifica(),
                        post.getMonumento().getId(),
                        post.getUtente().getId()
                ));
    }

    // Cerco un post per id del monumento
    public List<Post> findByMonumentoId(UUID idMonumento) {
        return postRepository.findByMonumentoId(idMonumento);
    }

    // Prendo i post dell'utente
    public List<PostResponse> getByUtente(UUID idUtente) {
        return postRepository.findByUtenteId(idUtente)
                .stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitolo(),
                        post.getContenuto(),
                        post.getDataCreazione(),
                        post.getDataModifica(),
                        post.getMonumento().getId(),
                        post.getUtente().getId()
                ))
                .toList();
    }

    public List<PostResponse> getByMonumento(UUID idMonumento) {
        return postRepository.findByMonumentoId(idMonumento)
                .stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitolo(),
                        post.getContenuto(),
                        post.getDataCreazione(),
                        post.getDataModifica(),
                        post.getMonumento().getId(),
                        post.getUtente().getId()
                ))
                .toList();
    }

}

