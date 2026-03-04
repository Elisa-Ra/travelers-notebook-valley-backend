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
    private final PostService postService;
    private final Cloudinary cloudinary;
    private final UtenteService utenteService;


    @Autowired
    public FotoService(FotoRepository fotoRepository, PostService postService, Cloudinary cloudinary, UtenteService utenteService) {
        this.fotoRepository = fotoRepository;
        this.postService = postService;
        this.cloudinary = cloudinary;
        this.utenteService = utenteService;

    }

    // Carico la foto da url
    public FotoResponse create(FotoDTO body, UUID idUtente) {

        Post post = postService.findById(body.idPost());

        // Solo autore del post può aggiungere foto
        if (!post.getUtente().getId().equals(idUtente)) {
            throw new BadRequestException("Solo l'autore del post può aggiungere una foto.");
        }

        try {
            // Carico la foto su Cloudinary
            Map uploadResult = cloudinary.uploader().upload(body.url(), ObjectUtils.emptyMap());

            String publicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");

            // Creo la foto
            Foto foto = new Foto(
                    post,
                    secureUrl,
                    publicId,
                    body.descrizione()
            );

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
            Post post = postService.findById(idPost);

            // controllo che l'utente sia l'autore del post
            if (!post.getUtente().getId().equals(idUtente)) {
                throw new BadRequestException("Solo l'autore del post può aggiungere una foto.");
            }

            Map upload = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

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

        fotoRepository.delete(foto);
    }

    // recupero le foto di un post
    public List<FotoResponse> getByPost(UUID idPost) {
        Post post = postService.findById(idPost);

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
