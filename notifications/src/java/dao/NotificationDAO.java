package dao;

import model.Notification;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Cette classe contient les opérations avec la base de données MySQL
public class NotificationDAO {

    // Informations de connexion à MySQL
    // Ces valeurs seront récupérées depuis Docker Compose
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "notification_db");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "notification_pass");

    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // Méthode pour ouvrir la connexion avec MySQL
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }

    // Ajouter une nouvelle notification
    public Notification ajouter(Notification notification) {
        String sql = "INSERT INTO notifications(etudiant_id, titre, message, type, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, notification.getEtudiantId());
            ps.setString(2, notification.getTitre());
            ps.setString(3, notification.getMessage());
            ps.setString(4, notification.getType());

            // Si le statut est vide, on met NON_LUE par défaut
            if (notification.getStatut() == null || notification.getStatut().isEmpty()) {
                ps.setString(5, "NON_LUE");
            } else {
                ps.setString(5, notification.getStatut());
            }

            ps.executeUpdate();

            // Récupérer l'id généré automatiquement
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                notification.setId(rs.getInt(1));
            }

            notification.setDateCreation(LocalDateTime.now());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de la notification");
        }

        return notification;
    }

    // Afficher toutes les notifications
    public List<Notification> listerToutes() {
        List<Notification> notifications = new ArrayList<>();

        String sql = "SELECT * FROM notifications ORDER BY date_creation DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des notifications");
        }

        return notifications;
    }

    // Afficher les notifications d'un étudiant précis
    public List<Notification> listerParEtudiant(int etudiantId) {
        List<Notification> notifications = new ArrayList<>();

        String sql = "SELECT * FROM notifications WHERE etudiant_id = ? ORDER BY date_creation DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, etudiantId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des notifications de l'étudiant");
        }

        return notifications;
    }

    // Marquer une notification comme lue
    public boolean marquerCommeLue(int id) {
        String sql = "UPDATE notifications SET statut = 'LUE' WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la notification");
        }
    }

    // Supprimer une notification
    public boolean supprimer(int id) {
        String sql = "DELETE FROM notifications WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de la notification");
        }
    }

    // Transformer une ligne SQL en objet Notification
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();

        notification.setId(rs.getInt("id"));
        notification.setEtudiantId(rs.getInt("etudiant_id"));
        notification.setTitre(rs.getString("titre"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setStatut(rs.getString("statut"));

        Timestamp timestamp = rs.getTimestamp("date_creation");

        if (timestamp != null) {
            notification.setDateCreation(timestamp.toLocalDateTime());
        }

        return notification;
    }
}
