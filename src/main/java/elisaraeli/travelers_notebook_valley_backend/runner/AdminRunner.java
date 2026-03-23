package elisaraeli.travelers_notebook_valley_backend.runner;

import elisaraeli.travelers_notebook_valley_backend.payloads.AdminDTO;
import elisaraeli.travelers_notebook_valley_backend.services.UtenteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminRunner implements CommandLineRunner {

    private final UtenteService utenteService;

    @Value("${PASS}")
    private String adminPassword;

    public AdminRunner(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Controllo se esiste già un admin con questa mail
        try {
            utenteService.findByEmail("admin@email.it");
            System.out.println("Questa email è già stata usata.");
            return;
        } catch (Exception ignored) {
            // Se non esiste, lo creo
        }

        AdminDTO admin = new AdminDTO(
                "admino",
                "admin@email.it",
                adminPassword,
                "ADMIN"
        );

        utenteService.createAdmin(admin);

        System.out.println("L'admin è stato creato correttamente!");
    }
}

