package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Medaglia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MedagliaRepository extends JpaRepository<Medaglia, UUID> {
    Optional<Medaglia> findByNome(String nome);

    Optional<Medaglia> findByMonumento_Id(UUID idMonumento);


}
