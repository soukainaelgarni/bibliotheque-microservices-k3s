package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import model.Emprunt;

@ApplicationScoped
public class NotificationClient {

    private static final String NOTIFICATION_URL = System.getenv().getOrDefault(
            "NOTIFICATION_URL",
            "http://notification-service:8080/notification-service/api/notifications"
    );

    // Envoyer au service Notifications une alerte pour un emprunt en retard.
    public void envoyerAlerteRetard(Emprunt emprunt) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("etudiantId", emprunt.getEtudiantId());
        notification.put("titre", "Retard de restitution");
        notification.put(
                "message",
                "La date de retour du document " + emprunt.getDocumentId() + " est depassee."
        );
        notification.put("type", "RETARD_LIVRE");
        notification.put("statut", "NON_LUE");

        try (Client client = ClientBuilder.newClient();
             Response response = client.target(NOTIFICATION_URL)
                     .request(MediaType.APPLICATION_JSON)
                     .post(Entity.entity(notification, MediaType.APPLICATION_JSON))) {

            if (response.getStatusInfo().getFamily()
                    != Response.Status.Family.SUCCESSFUL) {
                throw new IllegalStateException(
                        "Le service Notifications a retourne le statut " + response.getStatus()
                );
            }
        }
    }
}
