package ma.umpo.bibliotheque.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.umpo.bibliotheque.entity.Document;
import ma.umpo.bibliotheque.service.DocumentService;
import ma.umpo.bibliotheque.entity.StatutDocument;
import java.util.Map;
import java.util.List;

@Path("/catalogue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {

    @Inject
    private DocumentService service;
    @GET
public Response listerTous() {
    List<Document> documents = service.listerTous();
    return Response.ok(documents).build();
}
// dans cette classe on structure les chemins et ce qu'on peut trouver dans chacun de cs chemins
@GET
@Path("/disponibles")
public Response listerDisponibles() {
    return Response.ok(service.listerDisponibles()).build();
}

@GET
@Path("/{id}")
public Response trouverParId(@PathParam("id") Long id) {
    return service.trouverParId(id)
        .map(doc -> Response.ok(doc).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
}

@GET
@Path("/search")
public Response rechercher(@QueryParam("q") String keyword,
                           @QueryParam("type") String type,
                           @QueryParam("domaine") String domaine) {
    if (type != null) return Response.ok(service.filtrerParType(type)).build();
    if (domaine != null) return Response.ok(service.filtrerParDomaine(domaine)).build();
    return Response.ok(service.rechercher(keyword)).build();
}
@GET
@Path("/tri/auteur")
public Response listerParAuteur() {
    return Response.ok(service.listerParAuteur()).build();
}
@GET
@Path("/tri/annee")// ici par exemple on peut trouver l'ensemble des documents lister par année
public Response listerParAnnee() {
    return Response.ok(service.listerParAnnee()).build();
}

@GET
@Path("/statut/{statut}")
public Response filtrerParStatut(@PathParam("statut") String statut) {
    try {
        return Response.ok(service.filtrerParStatut(statut)).build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(Map.of("erreur", e.getMessage()))
                       .build();
    }
}
@GET
@Path("/{id}/disponibilite")
public Response verifierDisponibilite(@PathParam("id") Long id) {
    Document doc = service.trouverParId(id).orElse(null);
    if (doc == null) return Response.status(Response.Status.NOT_FOUND).build();
    return Response.ok(doc.getNbExemplairesDisponibles() > 0).build();
}

@POST
@Path("/{id}/emprunt")
public Response enregistrerEmprunt(@PathParam("id") Long id) {
    Document doc = service.enregistrerEmprunt(id);
    return Response.ok(doc).build();
}

@POST
@Path("/{id}/retour")
public Response enregistrerRetour(@PathParam("id") Long id) {
    Document doc = service.enregistrerRetour(id);
    return Response.ok(doc).build();
}

@POST
public Response ajouterDocument(Document document) {
    try {
        Document cree = service.ajouterDocument(document);
        return Response.status(Response.Status.CREATED).entity(cree).build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(Map.of("erreur", e.getMessage()))
                       .build();
    }
}
@PUT
@Path("/{id}")
public Response modifierDocument(@PathParam("id") Long id, Document document) {
    Document maj = service.modifierDocument(id, document);
    return Response.ok(maj).build();
}
@POST
@Path("/statut/{id}")
@Consumes(MediaType.WILDCARD)
public Response changerStatut(@PathParam("id") Long id, @QueryParam("statut") String statut) {
    try {
        Document doc = service.trouverParId(id).orElse(null);
        if (doc == null) return Response.status(Response.Status.NOT_FOUND).build();
        doc.setStatut(StatutDocument.valueOf(statut.toUpperCase()));
        Document maj = service.modifierDocument(id, doc);
        return Response.ok(maj).build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(Map.of("erreur", "Statut invalide : " + statut))
                       .build();
    }
}
@GET
@Path("/stats")
public Response getStatistiques() {
    return Response.ok(service.getStatistiques()).build();
}
@DELETE
@Path("/{id}")
public Response supprimerDocument(@PathParam("id") Long id) {
    boolean supprime = service.supprimerDocument(id);
    if (supprime) return Response.noContent().build();
    return Response.status(Response.Status.NOT_FOUND).build();
}

}
