package elisaraeli.travelers_notebook_valley_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "monumenti")
@NoArgsConstructor
@Getter
@Setter
public class Monumento {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descrizione;

    private String foto;

    private String posizione;

    @ManyToOne
    @JoinColumn(name = "categoria", nullable = false)
    private Categoria categoria;


    public Monumento(String nome, String descrizione, String foto, String posizione, Categoria categoria) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.foto = foto;
        this.posizione = posizione;
        this.categoria = categoria;
    }
}
