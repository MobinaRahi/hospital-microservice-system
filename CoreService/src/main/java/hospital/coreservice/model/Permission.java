package hospital.coreservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity(name="permissionEntity")
@Table(name="permissions",
        uniqueConstraints = @UniqueConstraint(name = "uk_permission_name", columnNames = "name"),
        indexes = {
                @Index(name = "idx_permission_deleted", columnList = "deleted"),
        })
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder

/**
 * Represents a fine-grained permission for access control.
 *
 * @author Mobina
 */
public class Permission extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "permission_resource", nullable = false, length = 100)
    private String resource;

    @Column(name = "action", nullable = false, length = 255)
    private String action;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

}
