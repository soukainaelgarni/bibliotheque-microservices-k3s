package rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Emprunt;
import service.EmpruntService;
import java.util.List;

@Path("/emprunts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpruntRessource {

    @Inject
    private EmpruntService empruntService;

    // GET /emprunts → lister tous les emprunts
    @GET
    public List<Emprunt> listerTous() {
        return empruntService.listerTous();
    }

    // GET /emprunts/{id} → trouver un emprunt
    @GET
    @Path("/{id}")
    public Response trouverParId(@PathParam("id") Long id) {
        Emprunt emprunt = empruntService.trouverParId(id);
        if (emprunt == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(emprunt).build();
    }

    // POST /emprunts → créer un emprunt
    @POST
    public Response creerEmprunt(Emprunt emprunt) {
        Emprunt nouveau = empruntService.creerEmprunt(emprunt);
        return Response.status(Response.Status.CREATED)
                       .entity(nouveau).build();
    }

    // PUT /emprunts/{id}/retour → retourner un document
    @PUT
    @Path("/{id}/retour")
    public Response retournerDocument(@PathParam("id") Long id) {
        Emprunt emprunt = empruntService.retournerDocument(id);
        if (emprunt == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(emprunt).build();
    }

    // GET /emprunts/retards → lister les retards
    @GET
    @Path("/retards")
    public List<Emprunt> listerRetards() {
        return empruntService.listerRetards();
    }

    // POST /emprunts/verifier-retards : detecter les retards et envoyer les alertes.
    @POST
    @Path("/verifier-retards")
    public Response verifierRetards() {
        List<Emprunt> retards = empruntService.verifierRetards();
        return Response.ok(retards).build();
    }
}
