package co.uk.olm.group.security;

import co.uk.olm.group.exception.PermissionException;
import co.uk.olm.group.model.SecurityPermission;
import co.uk.olm.group.service.PermissionService;
import io.quarkus.oidc.IdTokenCredential;
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class ApplicationCustomSecurityIdentity implements SecurityIdentityAugmentor {

    private static final String ACCESS_TOKEN = "access_token";

    @Inject
    PermissionService permissionService;

    @SneakyThrows
    @Override
    public Uni<SecurityIdentity> augment(final SecurityIdentity securityIdentity,
                                         final AuthenticationRequestContext authenticationRequestContext) {
        return Uni.createFrom().item(build(securityIdentity));
    }

    /**
     * Build the security identity populating the roles list retrieved from Eclipse.
     * @param identity the security identity to be populated
     * @return the security identity populated
     */
    private SecurityIdentity build(final SecurityIdentity identity) throws PermissionException {
        // if the identity is anonymous we return the identity and it will force the log in on Azure
        // to get the correct identity
        if (identity.isAnonymous()) {
            return identity;
        }
        // Quarkus has a limitation about inject JsonWebToken in a SecurityIdentityAugmentor class
        // This is the best way to get the access token from an id token
        var token = (JsonWebToken) identity.getPrincipal();
        var accessToken = (String) ((IdTokenCredential) ((OidcJwtCallerPrincipal) token).getCredential())
            .getRoutingContext()
            .data()
            .get(ACCESS_TOKEN);

        var permissions = permissionService.getUserPermissions(accessToken);
        var roles = permissions.stream()
            .map(SecurityPermission::getResolvedPermissions)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        // adding a new role just for testing
        roles.add("Greeting.GET");
        return QuarkusSecurityIdentity.builder()
               .addAttributes(identity.getAttributes())
               .addCredentials(identity.getCredentials())
               .addRoles(roles)
               .setPrincipal(identity.getPrincipal())
               .setAnonymous(identity.isAnonymous())
               .build();
    }
}
