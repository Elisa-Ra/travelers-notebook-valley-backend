package elisaraeli.travelers_notebook_valley_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"password", "accountNonExpired", "accountNonLocked", "authorities", "credentialsNonExpired", "enabled"})
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Column(nullable = false, name = "data_registrazione")
    private LocalDate dataRegistrazione = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private UtenteRuolo ruolo = UtenteRuolo.USER;

    public Utente(String username, String email, String password, UtenteRuolo ruolo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;


        // Avatar di default con l'username dell'utente
        // per evitare caratteri speciali e cose del genere, lo trasformo prima nello standard UTF8
        // l'avatar si potrà poi cambiare modificando il profilo
        String safeUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        this.avatar = "https://ui-avatars.com/api/?name=" + safeUsername;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruolo.name()));
    }
}
