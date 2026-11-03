package ma.umpo.bibliotheque.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ma.umpo.bibliotheque.entity.Document;
import java.util.List;
import java.util.Optional;
import ma.umpo.bibliotheque.entity.TypeDocument;
import ma.umpo.bibliotheque.entity.StatutDocument;



@Stateless
public class RepositoireDocument {
    @PersistenceContext(unitName = "cataloguePU")
    private EntityManager em;
    //enregistrer un nouveau document
public Document save(Document document) {
    em.persist(document);
    return document;
}
// modifier le nom d'un document...
public Document update(Document document) {
    return em.merge(document);
}

public boolean delete(Long id) {
    Document doc = em.find(Document.class, id);
    if (doc != null) {
        em.remove(doc);
        return true;
    }
    return false;
    
}
///// RECHERCHE DOCUMENT 
// par son id 

public Optional<Document> findById(Long id) {
    return Optional.ofNullable(em.find(Document.class, id));
}

// par titre 
public List<Document> findAll() {
    return em.createQuery("SELECT d FROM Document d ORDER BY d.titre", Document.class)
             .getResultList();
}
//par type envoie une liste de ce type...
public List<Document> findByType(TypeDocument type) {
    return em.createQuery(
        "SELECT d FROM Document d WHERE d.typeDocument = :type ORDER BY d.titre",
        Document.class)
        .setParameter("type", type)
        .getResultList();
}
// par auteur aussi renvoie une liste trouvée
public List<Document> findAllOrderByAuteur() {
    return em.createQuery(
        "SELECT d FROM Document d ORDER BY d.auteur", 
        Document.class)
        .getResultList();
}

public List<Document> findAllOrderByAnnee() {
    return em.createQuery(
        "SELECT d FROM Document d ORDER BY d.anneePublication DESC", 
        Document.class)
        .getResultList();
}

public List<Document> findByStatut(StatutDocument statut) {
    return em.createQuery(
        "SELECT d FROM Document d WHERE d.statut = :statut ORDER BY d.titre", 
        Document.class)
        .setParameter("statut", statut)
        .getResultList();
}
// compter les documents par un caracteristique specifique
public long countTotal() {
    return em.createQuery("SELECT COUNT(d) FROM Document d", Long.class)
             .getSingleResult();
}

public long countByStatut(StatutDocument statut) {
    return em.createQuery(
        "SELECT COUNT(d) FROM Document d WHERE d.statut = :statut", Long.class)
        .setParameter("statut", statut)
        .getSingleResult();
}

public List<Object[]> countByDomaine() {
    return em.createQuery(
        "SELECT d.domaine, COUNT(d) FROM Document d GROUP BY d.domaine", Object[].class)
        .getResultList();
}
public List<Document> findByDomaine(String domaine) {
    return em.createQuery(
        "SELECT d FROM Document d WHERE LOWER(d.domaine) = LOWER(:domaine) ORDER BY d.titre",
        Document.class)
        .setParameter("domaine", domaine)
        .getResultList();
}

public List<Document> search(String keyword) {
    String pattern = "%" + keyword.toLowerCase() + "%";
    return em.createQuery(
        "SELECT d FROM Document d WHERE LOWER(d.titre) LIKE :kw OR LOWER(d.auteur) LIKE :kw ORDER BY d.titre",
        Document.class)
        .setParameter("kw", pattern)
        .getResultList();
}
// cherche les documents qui sont disponibles dans le bibliothèques
public List<Document> findDisponibles() {
    return em.createQuery(
        "SELECT d FROM Document d WHERE d.nbExemplairesDisponibles > 0 ORDER BY d.titre",
        Document.class)
        .getResultList();
}
}
