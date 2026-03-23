package elisaraeli.travelers_notebook_valley_backend.services;

import elisaraeli.travelers_notebook_valley_backend.entities.Categoria;
import elisaraeli.travelers_notebook_valley_backend.exceptions.BadRequestException;
import elisaraeli.travelers_notebook_valley_backend.exceptions.NotFoundException;
import elisaraeli.travelers_notebook_valley_backend.payloads.CategoriaDTO;
import elisaraeli.travelers_notebook_valley_backend.payloads.CategoriaResponse;
import elisaraeli.travelers_notebook_valley_backend.repositories.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// SOLO L'ADMIN POTRA' FARE IL CRUD DI CATEGORIA
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // CERCO UNA CATEGORIA PER ID
    public Categoria findById(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public List<CategoriaResponse> getAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getCategoria()))
                .toList();
    }

    // CREO UNA CATEGORIA
    public CategoriaResponse create(CategoriaDTO body) {

        // Aggiungo un controllo per vedere se la categoria è già presente
        categoriaRepository.findByCategoria(body.categoria())
                .ifPresent(c -> {
                    throw new BadRequestException("La categoria '" + body.categoria() + "' esiste già.");
                });

        Categoria nuova = new Categoria(body.categoria());
        categoriaRepository.save(nuova);

        return new CategoriaResponse(
                nuova.getId(),
                nuova.getCategoria()
        );
    }


    // MODIFICO LA CATEGORIA
    public CategoriaResponse update(UUID id, CategoriaDTO body) {
        Categoria c = this.findById(id);
        c.setCategoria(body.categoria());
        categoriaRepository.save(c);
        return new CategoriaResponse(c.getId(), c.getCategoria());
    }

    // ELIMINO LA CATEGORIA
    public void delete(UUID id) {
        Categoria c = this.findById(id);
        categoriaRepository.delete(c);
    }

    public Categoria findByNome(String nome) {
        return categoriaRepository.findByCategoria(nome)
                .orElseThrow(() -> new NotFoundException("Categoria non trovata: " + nome));
    }

}
