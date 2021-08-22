package co.uk.olm.group.resource;

import co.uk.olm.group.rest.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Slf4j
public class HelloResource {

    @GET
    @RolesAllowed("Greeting.GET")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse hello() {
        log.info("user has the Greeting.GET role access");
        return ApiResponse.builder()
               .status(HttpStatus.SC_OK)
               .message("Hi there, you're authenticated")
               .build();
    }
}