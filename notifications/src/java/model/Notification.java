package model;

import java.time.LocalDateTime;

// Classe modèle qui représente une notification
public class Notification {

    private int id;
    private int etudiantId;
    private String titre;
    private String message;
    private String type;
    private String statut;
    private LocalDateTime dateCreation;

    public Notification() {
    }

    public Notification(int id, int etudiantId, String titre, String message, String type, String statut, LocalDateTime dateCreation) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.titre = titre;
        this.message = message;
        this.type = type;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
    }


    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }


    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}