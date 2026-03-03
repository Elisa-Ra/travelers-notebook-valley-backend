package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Categoria;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.CategoriaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.CategoriaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria findById(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public CategoriaResponse create(CategoriaDTO body) {

        Categoria c = new Categoria(body.categoria());
        categoriaRepository.save(c);

        return new CategoriaResponse(
                c.getId(),
                c.getCategoria()
        );
    }
}
