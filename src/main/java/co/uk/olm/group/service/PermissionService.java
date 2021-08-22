package co.uk.olm.group.service;

import co.uk.olm.group.client.PermissionClient;
import co.uk.olm.group.exception.PermissionException;
import co.uk.olm.group.model.SecurityPermission;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@Slf4j
@ApplicationScoped
public class PermissionService {

    private static final String BEARER = "Bearer ";

    @Inject
    @RestClient
    PermissionClient client;

    /**
     * Returns the user permissions.
     * @return list of user permissions
     */
    public List<SecurityPermission> getUserPermissions(final String token) throws PermissionException {
        var bearer = BEARER + token;
        try {
            return client.getPermissions(bearer);
        } catch (final Exception e) {
            log.error("Error getting permissions", e);
            throw new PermissionException("Error getting permissions", e);
        }
    }
}
