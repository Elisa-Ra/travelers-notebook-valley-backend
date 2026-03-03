package elisaraeli.travelers_notebook_valley_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import elisaraeli.travelers_notebook_valley_backend.entities.Foto;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import elisaraeli.travelers_notebook_valley_backend.payloads.FotoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.FotoResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PostService postService;
    private final Cloudinary cloudinary;

    @Autowired
    public FotoService(FotoRepository fotoRepository, PostService postService, Cloudinary cloudinary) {
        this.fotoRepository = fotoRepository;
        this.postService = postService;
        this.cloudinary = cloudinary;
    }

    public FotoResponse create(FotoDTO body) {

        Post post = postService.findById(body.idPost());

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

    public FotoResponse upload(UUID idPost, MultipartFile file, String descrizione) {
        try {
            Post post = postService.findById(idPost);

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

}
