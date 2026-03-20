package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Categoria;
import elisaraeli.travelers_notebook_valley_backend.entities.Monumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonumentoRepository extends JpaRepository<Monumento, UUID> {

    Optional<Monumento> findByNomeAndCategoria(String nome, Categoria categoria);

    List<Monumento> findAllByOrderByNomeAsc();
}

