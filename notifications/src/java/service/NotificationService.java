package service;

import dao.NotificationDAO;
import model.Notification;
import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    public List<Notification> getAllNotifications() {
        return notificationDAO.listerToutes();
    }

    public List<Notification> getNotificationByEtudiant(int etudiantId) {
        return notificationDAO.listerParEtudiant(etudiantId);
    }

    public Notification creerNotification(Notification notification) {
        if (notification.getType() == null || notification.getType().isEmpty()) {
            notification.setType("INFO");
        }

        return notificationDAO.ajouter(notification);
    }

    public boolean marquerCommeLue(int id) {
        return notificationDAO.marquerCommeLue(id);
    }

    public boolean supprimerNotification(int id) {
        return notificationDAO.supprimer(id);
    }
}