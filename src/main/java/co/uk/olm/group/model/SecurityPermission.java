package co.uk.olm.group.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SecurityPermission implements Serializable {

    private String domain;
    private List<String> permissions;

    public Set<String> getResolvedPermissions() {
        return permissions.stream().map(p -> String.format("%s.%s", domain, p)).collect(Collectors.toSet());
    }
}
