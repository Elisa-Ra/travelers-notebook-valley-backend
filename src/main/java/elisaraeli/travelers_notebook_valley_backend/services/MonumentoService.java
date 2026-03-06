package elisaraeli.travelers_notebook_valley_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import elisaraeli.travelers_notebook_valley_backend.entities.Categoria;
import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.MonumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// SOLO L'ADMIN POTRA' FARE IL CRUD DEI MONUMENTI
@Service
public class MonumentoService {

    private final MonumentoRepository monumentoRepository;
    private final CategoriaService categoriaService;
    private final Cloudinary cloudinary;

    public MonumentoService(MonumentoRepository monumentoRepository, CategoriaService categoriaService, Cloudinary cloudinary) {
        this.monumentoRepository = monumentoRepository;
        this.categoriaService = categoriaService;
        this.cloudinary = cloudinary;
    }

    // CERCO IL MONUMENTO PER ID
    public Monumento findById(UUID id) {
        return monumentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public List<MonumentoResponse> getAll() {
        return monumentoRepository.findAll()
                .stream()
                .map(m -> new MonumentoResponse(
                        m.getId(),
                        m.getNome(),
                        m.getDescrizione(),
                        m.getFoto(),
                        m.getPosizione(),
                        m.getCategoria().getCategoria()
                ))
                .toList();
    }


    // CREO IL MONUMENTO
    public MonumentoResponse create(MonumentoDTO body) {

        Categoria categoria = categoriaService.findByNome(body.nomeCategoria());

        // controllo che non esista già un monumento con lo stesso nome nella stessa categoria
        monumentoRepository.findByNomeAndCategoria(body.nome(), categoria)
                .ifPresent(m -> {
                    throw new BadRequestException(
                            "Attenzione, un monumento chiamato " + body.nome() +
                                    " è già presente nella categoria " + categoria.getCategoria() + "."
                    );
                });

        Monumento m = new Monumento(
                body.nome(),
                body.descrizione(),
                body.foto(),
                body.posizione(),
                categoria
        );

        monumentoRepository.save(m);

        return new MonumentoResponse(
                m.getId(),
                m.getNome(),
                m.getDescrizione(),
                m.getFoto(),
                m.getPosizione(),
                categoria.getCategoria()
        );
    }

    // MODIFICO IL MONUMENTO
    public MonumentoResponse update(UUID id, MonumentoDTO body) {
        Monumento m = this.findById(id);
        Categoria categoria = categoriaService.findByNome(body.nomeCategoria());

        m.setNome(body.nome());
        m.setDescrizione(body.descrizione());
        m.setFoto(body.foto());
        m.setPosizione(body.posizione());
        m.setCategoria(categoria);

        monumentoRepository.save(m);

        return new MonumentoResponse(
                m.getId(),
                m.getNome(),
                m.getDescrizione(),
                m.getFoto(),
                m.getPosizione(),
                categoria.getCategoria()

        );
    }

    // CANCELLO IL MONUMENTO
    public void delete(UUID id) {
        Monumento m = this.findById(id);
        monumentoRepository.delete(m);
    }

    public MonumentoResponse uploadFoto(UUID id, MultipartFile file) {
        try {
            Monumento m = this.findById(id);

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("secure_url").toString();

            m.setFoto(url);
            monumentoRepository.save(m);

            return new MonumentoResponse(
                    m.getId(),
                    m.getNome(),
                    m.getDescrizione(),
                    m.getFoto(),
                    m.getPosizione(),
                    m.getCategoria().getCategoria()
            );

        } catch (IOException e) {
            throw new RuntimeException("Errore durante l'upload della foto", e);
        }
    }


}
