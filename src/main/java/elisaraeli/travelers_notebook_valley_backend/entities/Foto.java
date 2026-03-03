package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "foto")
@NoArgsConstructor
@Getter
@Setter
public class Foto {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false, name = "id_public")
    private String publicId;

    @Column
    private String descrizione;

    @Column(nullable = false, name = "data_caricamento")
    private LocalDate dataCaricamento = LocalDate.now();

    public Foto(Post post, String url, String publicId, String descrizione) {
        this.post = post;
        this.url = url;
        this.publicId = publicId;
        this.descrizione = descrizione;
    }
}
