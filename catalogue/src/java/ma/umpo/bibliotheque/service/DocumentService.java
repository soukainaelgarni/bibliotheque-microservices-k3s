package ma.umpo.bibliotheque.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ma.umpo.bibliotheque.entity.Document;
import ma.umpo.bibliotheque.entity.TypeDocument;
import ma.umpo.bibliotheque.repository.RepositoireDocument;
import ma.umpo.bibliotheque.entity.StatutDocument;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;


@Stateless
public class DocumentService {

    @Inject
    private RepositoireDocument repository;
public List<Document> listerTous() {
    return repository.findAll();
}

public Optional<Document> trouverParId(Long id) {
    return repository.findById(id);
}

public List<Document> filtrerParType(String type) {
    TypeDocument typeEnum = TypeDocument.valueOf(type.toUpperCase());
    return repository.findByType(typeEnum);
}
public List<Document> listerParAuteur() {
    return repository.findAllOrderByAuteur();
}
public List<Document> listerParAnnee() {
    return repository.findAllOrderByAnnee();
}

public List<Document> filtrerParStatut(String statut) {
    try {
        StatutDocument statutEnum = StatutDocument.valueOf(statut.toUpperCase());
        return repository.findByStatut(statutEnum);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Statut invalide : " + statut +
            ". Valeurs acceptées : DISPONIBLE, EMPRUNTE, PERDU, ABIME");
    }
}

public List<Document> filtrerParDomaine(String domaine) {
    return repository.findByDomaine(domaine);
}

public List<Document> rechercher(String keyword) {
    return repository.search(keyword);
}

public List<Document> listerDisponibles() {
    return repository.findDisponibles();
}
public boolean estDisponible(Long id) {
    Document doc = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Document introuvable, id=" + id));
    return doc.getNbExemplairesDisponibles() > 0;
}

public Document enregistrerEmprunt(Long id) {
    Document doc = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Document introuvable, id=" + id));
    if (doc.getNbExemplairesDisponibles() <= 0) {
        throw new RuntimeException("Aucun exemplaire disponible : " + doc.getTitre());
    }
    doc.setNbExemplairesDisponibles(doc.getNbExemplairesDisponibles() - 1);
    return repository.update(doc);
}

public Document enregistrerRetour(Long id) {
    Document doc = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Document introuvable, id=" + id));
    if (doc.getNbExemplairesDisponibles() < doc.getNbExemplairesTotal()) {
        doc.setNbExemplairesDisponibles(doc.getNbExemplairesDisponibles() + 1);
    }
    return repository.update(doc);
}
public Document ajouterDocument(Document document) {
    // Validations
    if (document.getTitre() == null || document.getTitre().trim().isEmpty()) {
        throw new IllegalArgumentException("Le titre est obligatoire");
    }
    if (document.getAuteur() == null || document.getAuteur().trim().isEmpty()) {
        throw new IllegalArgumentException("L'auteur est obligatoire");
    }
    if (document.getTypeDocument() == null) {
        throw new IllegalArgumentException("Le type de document est obligatoire");
    }
    if (document.getNbExemplairesTotal() <= 0) {
        throw new IllegalArgumentException("Le nombre d'exemplaires doit être supérieur à 0");
    }
    document.setDateAjout(LocalDate.now());
    document.setNbExemplairesDisponibles(document.getNbExemplairesTotal());
    document.setStatut(StatutDocument.DISPONIBLE);
    return repository.save(document);
}
public Document modifierDocument(Long id, Document miseAJour) {
    Document existant = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Document introuvable, id=" + id));
    existant.setTitre(miseAJour.getTitre());
    existant.setAuteur(miseAJour.getAuteur());
    existant.setTypeDocument(miseAJour.getTypeDocument());
    existant.setIsbn(miseAJour.getIsbn());
    existant.setAnneePublication(miseAJour.getAnneePublication());
    existant.setEditeur(miseAJour.getEditeur());
    existant.setDomaine(miseAJour.getDomaine());
    existant.setDescription(miseAJour.getDescription());
    existant.setNbPages(miseAJour.getNbPages());
    existant.setLangue(miseAJour.getLangue());
    existant.setCote(miseAJour.getCote());
    existant.setStatut(miseAJour.getStatut());
    return repository.update(existant);
}
public Map<String, Object> getStatistiques() {
    Map<String, Object> stats = new java.util.HashMap<>();
    stats.put("totalDocuments", repository.countTotal());
    stats.put("disponibles", repository.countByStatut(StatutDocument.DISPONIBLE));
    stats.put("empruntes", repository.countByStatut(StatutDocument.EMPRUNTE));
    stats.put("perdus", repository.countByStatut(StatutDocument.PERDU));
    stats.put("abimes", repository.countByStatut(StatutDocument.ABIME));
    
    // Statistiques par domaine
    List<Object[]> parDomaine = repository.countByDomaine();
    Map<String, Long> statsParDomaine = new java.util.HashMap<>();
    for (Object[] row : parDomaine) {
        statsParDomaine.put((String) row[0], (Long) row[1]);
    }
    stats.put("parDomaine", statsParDomaine);
    
    return stats;
}
public boolean supprimerDocument(Long id) {
    return repository.delete(id);
}
}