package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.UtenteRuolo;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.PostResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.*;
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
    private final MonumentoRepository monumentoRepository;
    private final UtenteRepository utenteRepository;
    private final MedagliaRepository medagliaRepository;
    private final ConferitaRepository conferitaRepository;
    private final ConferitaService conferitaService;

    public PostService(
            PostRepository postRepository,
            MonumentoRepository monumentoRepository,
            UtenteRepository utenteRepository,
            MedagliaRepository medagliaRepository,
            ConferitaRepository conferitaRepository,
            ConferitaService conferitaService
    ) {
        this.postRepository = postRepository;
        this.monumentoRepository = monumentoRepository;
        this.utenteRepository = utenteRepository;
        this.medagliaRepository = medagliaRepository;
        this.conferitaRepository = conferitaRepository;
        this.conferitaService = conferitaService;
    }

    // CREO IL POST
    public PostResponse create(PostDTO body, UUID userId) {

        Utente autore = utenteRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Monumento monumento = monumentoRepository.findById(body.idMonumento())
                .orElseThrow(() -> new NotFoundException("Monumento non trovato"));

        Post post = new Post();
        post.setTitolo(body.titolo());
        post.setContenuto(body.contenuto());
        post.setDataCreazione(LocalDate.now());
        post.setDataModifica(LocalDate.now());
        post.setUtente(autore);
        post.setMonumento(monumento);

        postRepository.save(post);

        // ASSEGNO LA MEDAGLIA AUTOMATICAMENTE
        medagliaRepository.findByMonumento_Id(monumento.getId())
                .ifPresent(medaglia -> conferitaService.assegnaSeNonPresente(medaglia, autore));

        return new PostResponse(post);
    }

    // trovo il post per ID
    public Post findById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    // MODIFICA DEI POST
    // i post possono essere modificati dall'autore del post
    public PostResponse update(UUID postId, PostDTO body, UUID idUtente) {

        Post post = this.findById(postId);

        if (!post.getUtente().getId().equals(idUtente)) {
            throw new BadRequestException("Non hai il permesso di modificare il post.");
        }

        // aggiorno i campi
        post.setTitolo(body.titolo());
        post.setContenuto(body.contenuto());

        Monumento monumento = monumentoRepository.findById(body.idMonumento())
                .orElseThrow(() -> new NotFoundException("Monumento non trovato"));

        post.setMonumento(monumento);
        post.setDataModifica(LocalDate.now());

        // salvo
        postRepository.save(post);

        return new PostResponse(post);
    }

    // CANCELLO UN POST
    // Il post può essere cancellato dall'autore ma anche dall'admin (per questioni di moderazione)
    public void delete(UUID postId, UUID idUtente) {

        Post post = this.findById(postId);

        Utente richiedente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

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
                .map(PostResponse::new);
    }

    // POST PER MONUMENTO
    public List<PostResponse> getByMonumento(UUID idMonumento) {
        return postRepository.findByMonumentoId(idMonumento)
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    // POST PER UTENTE
    public List<PostResponse> getByUserId(UUID userId) {
        return postRepository.findByUtenteId(userId)
                .stream()
                .map(PostResponse::new)
                .toList();
    }
}
