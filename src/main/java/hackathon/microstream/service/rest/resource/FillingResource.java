package hackathon.microstream.service.rest.resource;

import hackathon.microstream.domain.entities.Filling;
import hackathon.microstream.service.provider.FillingService;
import hackathon.microstream.storage.entities.DBFilling;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/fillings")
@RequestScoped
public class FillingResource {

    @Inject
    private FillingService fillingService;

    @Path("/cleanAndUseDefaults")
    @GET
    @Operation(summary = "Cleans all fillings and uses the defaults; Only available in demo mode")
    @Produces(MediaType.TEXT_PLAIN)
    public Response cleanAndUseDefaults() {
        boolean cleaned = fillingService.cleanAndUseDefaults();

        return Response
                .status(cleaned ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(cleaned ? "Cleaned successfully" : "Failed to clean")
                .build();
    }

    @Path("/all")
    @GET
    @Operation(summary = "Returns all fillings")
    @APIResponse(description = "JSON list of fillings", responseCode = "200")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        final List<DBFilling> fillings = fillingService.getAll();

        return Response
                .status(Response.Status.OK)
                .entity(fillings)
                .build();
    }

    @Path("/{id}")
    @GET
    @Operation(summary = "Gets a filling by id")
    @APIResponses(
            value = {
                    @APIResponse(description = "The filling", responseCode = "200"),
                    @APIResponse(description = "Filling could not be found", responseCode = "404")
            }
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") UUID id) {
        final Filling dbFilling = this.fillingService.getById(id);

        return Response
                .status(Response.Status.OK)
                .entity(dbFilling)
                .build();
    }

    @PUT
    @Operation(summary = "Create a new filling")
    @APIResponse(description = "The created filling", responseCode = "200")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid Filling filling) {
        final Filling dbFilling = this.fillingService.add(filling);

        return Response
                .status(Response.Status.OK)
                .entity(dbFilling)
                .build();
    }

    @Path("/{id}")
    @POST
    @Operation(summary = "Updates a filling by id")
    @APIResponses(
            value = {
            @APIResponse(description = "The updated filling", responseCode = "200"),
            @APIResponse(description = "Filling could not be found", responseCode = "404")
        }
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") UUID id, @Valid Filling filling) {
        final Filling dbFilling = this.fillingService.update(id, filling);

        return Response
                .status(Response.Status.OK)
                .entity(dbFilling)
                .build();
    }

    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes a filling by id")
    @APIResponses(
            value = {
                    @APIResponse(description = "Filling deleted", responseCode = "200"),
                    @APIResponse(description = "Filling could not be found", responseCode = "404")
            }
    )
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") UUID id) {
        fillingService.delete(id);

        return Response
                .status(Response.Status.OK)
                .build();
    }
}
