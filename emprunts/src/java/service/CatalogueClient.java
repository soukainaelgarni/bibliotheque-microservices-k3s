package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CatalogueClient {

   private static final String CATALOGUE_URL = System.getenv().getOrDefault(
        "CATALOGUE_URL",
        "http://app-catalogue:8080/bibliotheque-catalogue-projet/api/catalogue"
);

    // Vérifie auprès du service Catalogue si un document est disponible.
    public boolean estDisponible(Long documentId) {
        try (Client client = ClientBuilder.newClient();
             Response response = client.target(CATALOGUE_URL)
                     .path(String.valueOf(documentId))
                     .path("disponibilite")
                     .request()
                     .get()) {

            if (response.getStatusInfo().getFamily()
                    != Response.Status.Family.SUCCESSFUL) {
                throw new IllegalStateException(
                        "Le service Catalogue a retourne le statut " + response.getStatus()
                );
            }

            return response.readEntity(Boolean.class);
        }
    }
}