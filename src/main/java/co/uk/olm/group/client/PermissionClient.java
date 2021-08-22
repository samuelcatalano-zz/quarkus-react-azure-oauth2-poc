package co.uk.olm.group.client;

import co.uk.olm.group.model.SecurityPermission;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.HeaderParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@RegisterRestClient(configKey = "olm.msp.models.securitypermission.api")
public interface PermissionClient {

    @GET
    @Path("/aggregatedPermission")
    List<SecurityPermission> getPermissions(@HeaderParam("Authorization") final String basic);
}
