package hospital.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hospital.authservice.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "roleEntity")
@Table(name = "roles",
        uniqueConstraints = @UniqueConstraint(name = "uk_role_name", columnNames = "name"),
        indexes = {
                @Index(name = "idx_role_name", columnList = "name"),
                @Index(name = "idx_role_deleted", columnList = "deleted")
        })

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            foreignKey = @ForeignKey(name = "fk_role_permissions_role"),
            inverseForeignKey = @ForeignKey(name = "fk_role_permissions_permission"),
            indexes = {
                    @Index(name = "idx_role_permissions_role", columnList = "role_id"),
                    @Index(name = "idx_role_permissions_permission", columnList = "permission_id")
            }
    )

    @JsonIgnore
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    @Builder.Default
    private Set<User> users = new HashSet<>();

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean hasPermission(String permissionName) {
        return permissions.stream().anyMatch(p -> p.getName() == permissionName);
    }

}
