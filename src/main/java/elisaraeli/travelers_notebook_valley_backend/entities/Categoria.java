package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "categorie")
@NoArgsConstructor
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String categoria;

    public Categoria(String categoria) {
        this.categoria = categoria;
    }
}
