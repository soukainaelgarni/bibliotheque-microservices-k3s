package ma.umpo.bibliotheque.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "documents")
public class Document implements Serializable {
// recherche de document,on a structuré de tels sortes que les documents qui ont les memes auteurs soit prets l'un à coté de l'autre 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false, length = 300)
    private String titre;

    @Column(name = "auteur", nullable = false, length = 200)
    private String auteur;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_document", nullable = false, length = 20)
    private TypeDocument typeDocument;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;// pour la recherche

    @Column(name = "annee_publication")
    private Integer anneePublication;

    @Column(name = "editeur", length = 150)
    private String editeur;

    @Column(name = "domaine", length = 100)
    private String domaine;// informatique,biologie...

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "nb_pages")
    private Integer nbPages;

    @Column(name = "langue", length = 50)
    private String langue;

    @Column(name = "nb_exemplaires_total", nullable = false)
    private int nbExemplairesTotal;// nbre d'exemplaires d'un document dans la bibliothèque

    @Column(name = "nb_exemplaires_disponibles", nullable = false)
    private int nbExemplairesDisponibles;

    @Column(name = "date_ajout")
    private LocalDate dateAjout;

    @Column(name = "cote", length = 50)
    private String cote;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutDocument statut;//abime,disponible,empruntes...

    
    public Document() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnneePublication() { return anneePublication; }
    public void setAnneePublication(Integer anneePublication) { this.anneePublication = anneePublication; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public String getDomaine() { return domaine; }
    public void setDomaine(String domaine) { this.domaine = domaine; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getNbPages() { return nbPages; }
    public void setNbPages(Integer nbPages) { this.nbPages = nbPages; }

    public String getLangue() { return langue; }
    public void setLangue(String langue) { this.langue = langue; }

    public int getNbExemplairesTotal() { return nbExemplairesTotal; }
    public void setNbExemplairesTotal(int nbExemplairesTotal) { this.nbExemplairesTotal = nbExemplairesTotal; }

    public int getNbExemplairesDisponibles() { return nbExemplairesDisponibles; }
    public void setNbExemplairesDisponibles(int nbExemplairesDisponibles) { this.nbExemplairesDisponibles = nbExemplairesDisponibles; }

    public LocalDate getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDate dateAjout) { this.dateAjout = dateAjout; }

    public String getCote() { return cote; }
    public void setCote(String cote) { this.cote = cote; }

    public StatutDocument getStatut() { return statut; }
    public void setStatut(StatutDocument statut) { this.statut = statut; }
}