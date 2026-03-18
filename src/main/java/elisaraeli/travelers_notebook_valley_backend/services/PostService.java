package elisaraeli.travelers_notebook_valley_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import elisaraeli.travelers_notebook_valley_backend.entities.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MonumentoRepository monumentoRepository;
    private final UtenteRepository utenteRepository;
    private final MedagliaRepository medagliaRepository;
    private final ConferitaRepository conferitaRepository;
    private final Cloudinary cloudinary;

    public PostService(
            PostRepository postRepository,
            MonumentoRepository monumentoRepository,
            UtenteRepository utenteRepository,
            MedagliaRepository medagliaRepository,
            ConferitaRepository conferitaRepository,
            Cloudinary cloudinary
    ) {
        this.postRepository = postRepository;
        this.monumentoRepository = monumentoRepository;
        this.utenteRepository = utenteRepository;
        this.medagliaRepository = medagliaRepository;
        this.conferitaRepository = conferitaRepository;
        this.cloudinary = cloudinary;
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
                .ifPresent(medaglia -> {

                    boolean isConferita = conferitaRepository
                            .existsByUtenteIdAndMedagliaId(autore.getId(), medaglia.getId());

                    if (!isConferita) {
                        conferitaRepository.save(new Conferita(medaglia, autore));
                    }
                });

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
    public Page<Post> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return postRepository.findAll(pageable);
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
        return postRepository.findByUtenteIdOrderByDataCreazioneAsc(userId)
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    public PostResponse uploadFoto(UUID postId, MultipartFile file, UUID userId) {
        try {
            Post post = this.findById(postId);

            // Solo l’autore può caricare la foto
            if (!post.getUtente().getId().equals(userId)) {
                throw new BadRequestException("Non puoi modificare questo post.");
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("secure_url").toString();

            post.setFotoUrl(url);
            postRepository.save(post);

            return new PostResponse(post);

        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'upload della foto", e);
        }
    }
}
