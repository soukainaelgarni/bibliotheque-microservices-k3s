/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resource;
import dao.NotificationDAO;
import model.Notification;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class NotificationResource{
    private final NotificationDAO notificationDAO = new NotificationDAO();
    
    @GET
    @Path("/test")
            public Response test(){
                return Response.ok("{\"message\":\"Service Notifications fonctionne correctement\"}").build();
            }
            
    @POST
    public Response creerNotification(Notification notification){
        Notification nouvelleNotification = notificationDAO.ajouter(notification);
        return Response.status(Response.Status.CREATED).entity(nouvelleNotification).build();
        
    }
    @GET
    public Response getToutesNotifications(){
        List<Notification> notifications = notificationDAO.listerToutes();
        return Response.ok(notifications).build();
    }
    
    @GET
    @Path("/etudiant/{etudiantId}")
    public Response getNotificationsParEtudiant(@PathParam("etudiantId") int etudiantId ){
        List<Notification> notifications = notificationDAO.listerParEtudiant(etudiantId);
        return Response.ok(notifications).build();
    }
    
    @PUT
    @Path("/{id}/lue")
    public Response marquerCommeLue(@PathParam("id") int id){
        boolean updated=notificationDAO.marquerCommeLue(id);
        
        if(updated){
            return Response.ok("{\"message\":\"Notification marquee comme lue\"}").build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Notification non trouvee \"}").build();
        }
    }
    
    
    @DELETE
    @Path("/{id}")
    public Response supprimerNotification(@PathParam("id") int id){
        boolean deleted=notificationDAO.supprimer(id);
        
        if(deleted){
            return Response.ok("{\"message\":\"Notification supprimee\"}").build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Notification non trouvee\"}").build();
        }
    }
    

    
            
            
            
            
            
            
            
    
    
    
    
    
    
    
}