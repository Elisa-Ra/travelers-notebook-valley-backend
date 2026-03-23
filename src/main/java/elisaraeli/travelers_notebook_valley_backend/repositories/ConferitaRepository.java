package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Conferita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConferitaRepository extends JpaRepository<Conferita, UUID> {


    List<Conferita> findByUtenteId(UUID idUtente);

    boolean existsByUtenteIdAndMedagliaId(UUID utenteId, UUID medagliaId);


}

