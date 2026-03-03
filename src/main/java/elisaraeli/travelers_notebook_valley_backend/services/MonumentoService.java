package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Categoria;
import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.MonumentoResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.MonumentoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MonumentoService {

    private final MonumentoRepository monumentoRepository;
    private final CategoriaService categoriaService;

    public MonumentoService(MonumentoRepository monumentoRepository, CategoriaService categoriaService) {
        this.monumentoRepository = monumentoRepository;
        this.categoriaService = categoriaService;
    }

    public Monumento findById(UUID id) {
        return monumentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public MonumentoResponse create(MonumentoDTO body) {

        Categoria categoria = categoriaService.findById(body.idCategoria());

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
                categoria.getId()
        );
    }
}
