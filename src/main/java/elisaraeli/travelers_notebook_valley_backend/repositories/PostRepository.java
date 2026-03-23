package elisaraeli.travelers_notebook_valley_backend.repositories;

import elisaraeli.travelers_notebook_valley_backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByMonumentoId(UUID idMonumento);

    List<Post> findByUtenteIdOrderByDataCreazioneAsc(UUID id);

    @Query("""
                SELECT m.nome AS nome, COUNT(p) AS posts
                FROM Post p
                JOIN p.monumento m
                GROUP BY m.id, m.nome
                ORDER BY posts DESC
            """)
    List<Map<String, Object>> countPostsByMonumento();
}