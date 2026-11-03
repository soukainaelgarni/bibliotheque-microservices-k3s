package service;

import dao.EmpruntDAO;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import model.Emprunt;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class EmpruntService {

    @Inject
    private EmpruntDAO empruntDAO;

    @Inject
    private NotificationClient notificationClient;
    
    @Inject
    private CatalogueClient catalogueClient;

    // Créer un nouvel emprunt
   
    public Emprunt creerEmprunt(Emprunt emprunt) {
    if (!catalogueClient.estDisponible(emprunt.getDocumentId())) {
        throw new IllegalStateException("Document non disponible pour l'emprunt");
    }
    emprunt.setDateEmprunt(LocalDate.now());
    emprunt.setStatut("EN_COURS");
    return empruntDAO.create(emprunt);
}

    // Retourner un document
    public Emprunt retournerDocument(Long id) {
        Emprunt emprunt = empruntDAO.findById(id);
        if (emprunt == null) {
            return null;
        }

        emprunt.setDateRetourReelle(LocalDate.now());

        // Vérifier si c'est un retard
        boolean dejaSignale = "EN_RETARD".equals(emprunt.getStatut());
        if (emprunt.getDateRetourPrevue() != null
                && LocalDate.now().isAfter(emprunt.getDateRetourPrevue())) {
            emprunt.setStatut("EN_RETARD");
            if (!dejaSignale) {
                notificationClient.envoyerAlerteRetard(emprunt);
            }
        } else {
            emprunt.setStatut("RENDU");
        }

        return empruntDAO.update(emprunt);
    }

    // Lister tous les emprunts
    public List<Emprunt> listerTous() {
        return empruntDAO.findAll();
    }

    // Lister les emprunts en retard
    public List<Emprunt> listerRetards() {
        return empruntDAO.findRetards();
    }

    // Detecter les retards, mettre a jour leur statut et envoyer les alertes.
    public List<Emprunt> verifierRetards() {
        List<Emprunt> retards = empruntDAO.findRetardsADetecter(LocalDate.now());

        for (Emprunt emprunt : retards) {
            emprunt.setStatut("EN_RETARD");
            empruntDAO.update(emprunt);
            notificationClient.envoyerAlerteRetard(emprunt);
        }

        return retards;
    }

    // Executer automatiquement la verification toutes les minutes.
    @Schedule(hour = "*", minute = "*", second = "0", persistent = false)
    public void verifierRetardsAutomatiquement() {
        verifierRetards();
    }

    // Trouver un emprunt par ID
    public Emprunt trouverParId(Long id) {
        return empruntDAO.findById(id);
    }
}
