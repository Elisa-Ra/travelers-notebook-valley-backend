package elisaraeli.travelers_notebook_valley_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import elisaraeli.travelers_notebook_valley_backend.entities.Foto;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.entities.Utente;
import elisaraeli.travelers_notebook_valley_backend.entities.UtenteRuolo;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.FotoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.FotoResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.FotoRepository;
import elisaraeli.travelers_notebook_valley_backend.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PostRepository postRepository;
    private final Cloudinary cloudinary;
    private final UtenteService utenteService;

    @Autowired
    public FotoService(
            FotoRepository fotoRepository,
            PostRepository postRepository,
            Cloudinary cloudinary,
            UtenteService utenteService
    ) {
        this.fotoRepository = fotoRepository;
        this.postRepository = postRepository;
        this.cloudinary = cloudinary;
        this.utenteService = utenteService;
    }

    // Carico la foto da URL
    public FotoResponse create(FotoDTO body, UUID idUtente) {

        Post post = postRepository.findById(body.idPost())
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        // Solo autore del post può aggiungere foto
        if (!post.getUtente().getId().equals(idUtente)) {
            throw new BadRequestException("Solo l'autore del post può aggiungere una foto.");
        }

        // carico la foto su cloudinary
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    body.url(),
                    ObjectUtils.emptyMap()
            );

            String publicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");

            // Creo la foto
            Foto foto = new Foto(post, secureUrl, publicId, body.descrizione());
            fotoRepository.save(foto);

            // ritorno il DTO di risposta
            return new FotoResponse(
                    foto.getId(),
                    foto.getUrl(),
                    foto.getDescrizione(),
                    foto.getDataCaricamento()
            );

        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento della foto.", e);
        }
    }

    // Carico la foto da file
    public FotoResponse upload(UUID idPost, MultipartFile file, String descrizione, UUID idUtente) {
        try {
            Post post = postRepository.findById(idPost)
                    .orElseThrow(() -> new NotFoundException("Post non trovato"));
            // controllo che l'utente sia l'autore del post
            if (!post.getUtente().getId().equals(idUtente)) {
                throw new BadRequestException("Solo l'autore del post può aggiungere una foto.");
            }

            Map<String, Object> upload = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );

            String url = (String) upload.get("secure_url");
            String publicId = (String) upload.get("public_id");

            Foto foto = new Foto(post, url, publicId, descrizione);
            fotoRepository.save(foto);

            return new FotoResponse(
                    foto.getId(),
                    foto.getUrl(),
                    foto.getDescrizione(),
                    foto.getDataCaricamento()
            );

        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'upload del file", e);
        }
    }

    // trovo la foto per ID
    public Foto findById(UUID id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    // CANCELLO LA FOTO (autore del post o admin)
    public void delete(UUID idFoto, UUID idUtente) {

        Foto foto = this.findById(idFoto);
        Utente richiedente = utenteService.findById(idUtente);

        boolean isAutore = foto.getPost().getUtente().getId().equals(idUtente);
        boolean isAdmin = richiedente.getRuolo().equals(UtenteRuolo.ADMIN);

        if (!isAutore && !isAdmin) {
            throw new BadRequestException("Errore! Non hai il permesso di eliminare questa foto.");
        }

        try {
            cloudinary.uploader().destroy(foto.getPublicId(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'eliminazione da Cloudinary", e);
        }

        fotoRepository.delete(foto);
    }

    // recupero le foto di un post
    public List<FotoResponse> getByPost(UUID idPost) {

        postRepository.findById(idPost)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        return fotoRepository.findByPostId(idPost)
                .stream()
                .map(f -> new FotoResponse(
                        f.getId(),
                        f.getUrl(),
                        f.getDescrizione(),
                        f.getDataCaricamento()
                ))
                .toList();
    }
}
