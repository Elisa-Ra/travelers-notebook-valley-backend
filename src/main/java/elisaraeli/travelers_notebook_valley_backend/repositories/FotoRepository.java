package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Foto;
import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FotoRepository extends JpaRepository<Foto, UUID> {
    List<Foto> findByPost(Post post);
}
