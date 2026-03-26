# Traveller's Notebook Valley Edition - Backend
# [Link al sito](https://travelers-notebook-valley-frontend.vercel.app/) :woman_technologist:
# [Frontend](https://github.com/Elisa-Ra/travelers-notebook-valley-frontend)

Backend REST per l’applicazione **Traveller's Notebook Valley Edition**, un diario di viaggio digitale.


** Senza registrazione al sito, l'utente può: **
- Leggere informazioni culturali 
** Con registrazione al sito, l'utente può: **
- Creare e gestire (CRUD) il suo diario personale, sfogliabile (con flipbook)
- Scrivere pagine del proprio diario per raccontare pensieri ed esperienze riguardo ai monumenti/punti di interesse visitati
- Caricare foto nel diario per documentare la loro esperienza
- Ottenere medaglie/adesivi da sfoggiare nel proprio profilo
- Gestire e modificare il proprio profilo
** Solo admin **
- Gestione _categorie_ (es. Monumento, Museo, Mostra, Evento)
- Gestione _monumenti_/_punti d'interesse_ (es. Tempio della Concordia, Museo Archeologico)
- Gestione _medaglie_ (es. Medaglia per avere scritto la prima pagina del tuo diario)

---

## Tecnologie usate:

- **Linguaggio:** Java 21 
- **Framework:** Spring Boot (Web, Security JWT, Data JPA)
- **Database:** PostgreSQL
- **Object Relation Mapping:** Hibernate / JPA
- **Autenticazione:** JWT (JSON Web Token)
- **Build tool:** Maven
- **Hosting** Koyeb
- **Upload Foto** Cloudinary


---

## Funzionalità principali

- **Autenticazione & Autorizzazione**
  - Registrazione e login dell'utente
  - Endpoint `/auth/me` per ottenere i dati dell’utente loggato
  - Protezione degli endpoint con JWT

- **Diario di viaggio**
  - CRUD dei **post** (pagine del diario) associati a un monumento
  - Upload foto per ogni pagina
  - Recupero dei post dell’utente loggato (`/posts/me`)

- **Monumenti**
  - Lista dei monumenti disponibili
  - Associazione dei post a un monumento

- **Medaglie / Achievement**
  - Assegnazione medaglie ai monumenti
  - Gestione vincoli (es. un monumento può avere una sola medaglia)

- **Gestione errori**
  - Eccezioni custom (`NotFoundException`, `BadRequestException`, `UnauthorizedException`, ecc.)
  - `@RestControllerAdvice` per restituire errori in formato JSON coerente


## Architettura

  - `config` - Configurazione di Cloudinary + Configurazione CORS
  - `controllers` - Contengono gli endpoint REST
  - `entities` - Le entità JPA mappate sulle tabelle del database
  - `exceptions` - Le eccezioni custom e handler globali
  - `payloads` - I DTO, gli oggetti che trasferiscono dati verso/da il frontend
  - `repositories` - Le interfacce che facilitano l'accesso al database tramite Spring Data JPA
  - `runner` - Per creare l'admin
  - `security` - Per gestire la sicurezza, i token, la configurazione di CORS, le password ecc...
  - `services` - Contengono CRUD e logica

- **Sicurezza:**
  - Configurazione Spring Security con filtro JWT
  - Ruoli/permessi (`UTENTE`, `ADMIN`)
  - CORS configurato per permettere le chiamate dal frontend (Vite/React)


## Requisiti

- Java 21
- Maven 
- PostgreSQL in esecuzione (in locale o in cloud)
- Un file di configurazione (env.properties)


## Configurazione

Crea un file env.properties con la configurazione del tuo progetto in locale, ad esempio:

```properties
# SERVER
PORT=la_tua_port
# DATABASE POSTGRESQL
DB_URL=jdbc:postgresql://localhost:5432/nome_db
PG_USERNAME=tuo_pg_username
PG_PASSWORD=tua_pg_pass
# JWT 
JWT_SECRET=tuo_jwt_secret
JWT_EXPIRATION=86400000 (o la scadenza che vuoi, questa ad esempio dura 24 ore)
# CLOUDINARY (per caricare le foto)
CLOUDINARY_NAME=tuo_nome_cloudinary
CLOUDINARY_API_KEY=tua_api_key
CLOUDINARY_SECRET=tuo_cloudinary_secret
# ADMIN (per generare l'admin)
PASS=tua_password_admin
```


## COME AVVIARE IL PROGETTO IN LOCALE:
- **Clona il progetto**
git clone https://github.com/Elisa-Ra/travelers-notebook-valley-backend.git
cd travelers-notebook-valley-backend
- Crea un **database PostgreSQL** in locale nome_db (o il nome che vuoi)
- Crea nella root del progetto il file **env.properties** con le tue configurazioni


## AUTRICE: ELISA RAELI :woman_technologist:
