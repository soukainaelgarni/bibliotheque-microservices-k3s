package rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// Toutes tes API seront accessibles via /api/...
// Exemple : /api/emprunts
@ApplicationPath("/api")
public class RestConfig extends Application {
    // Rien à écrire ici, Jakarta EE détecte
    // automatiquement tes ressources REST
}