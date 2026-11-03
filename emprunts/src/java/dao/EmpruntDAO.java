package dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Emprunt;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class EmpruntDAO {

    @PersistenceContext(unitName = "empruntsPU")
    private EntityManager em;

    // Sauvegarder un nouvel emprunt
    public Emprunt create(Emprunt emprunt) {
        em.persist(emprunt);
        return emprunt;
    }

    // Trouver un emprunt par son ID
    public Emprunt findById(Long id) {
        return em.find(Emprunt.class, id);
    }

    // Lister tous les emprunts
    public List<Emprunt> findAll() {
        return em.createQuery("SELECT e FROM Emprunt e", Emprunt.class)
                 .getResultList();
    }

    // Mettre à jour un emprunt
    public Emprunt update(Emprunt emprunt) {
        return em.merge(emprunt);
    }

    // Lister les emprunts en retard
    public List<Emprunt> findRetards() {
        return em.createQuery(
            "SELECT e FROM Emprunt e WHERE e.statut = 'EN_RETARD'",
            Emprunt.class)
                 .getResultList();
    }

    // Rechercher les emprunts en cours dont la date de retour est depassee.
    public List<Emprunt> findRetardsADetecter(LocalDate dateActuelle) {
        return em.createQuery(
            "SELECT e FROM Emprunt e "
            + "WHERE e.statut = 'EN_COURS' "
            + "AND e.dateRetourPrevue IS NOT NULL "
            + "AND e.dateRetourPrevue < :dateActuelle",
            Emprunt.class)
                .setParameter("dateActuelle", dateActuelle)
                .getResultList();
    }
}
