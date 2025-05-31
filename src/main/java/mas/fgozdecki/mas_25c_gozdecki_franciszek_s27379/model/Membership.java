package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id",
        "server_id", "joinDate"}))
@ToString()
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable =
            false)
    @NotNull
    private Account member;

    @ManyToOne
    @JoinColumn(name = "server_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Server server;

    @NotNull
    private LocalDateTime joinDate;

    private LocalDateTime leaveDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Role role;


}
