package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_id", nullable = false, updatable = false)
    private Server server;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Message> messages;

    private String name;

    @Min(value = 0, message = "Required access level cannot be negative")
    private Integer requiredAccessLevel;

    public boolean canUserAccess(Membership membership) {
        if (membership == null || membership.getRole() == null) {
            return false;
        }

        return membership.getRole().getAccessLevel() >= this.requiredAccessLevel;
    }
}
