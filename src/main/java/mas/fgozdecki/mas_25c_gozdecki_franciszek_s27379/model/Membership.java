package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator.ValidJoinDate;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator.ValidLeaveDate;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.validator.ValidServerLimit;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id",
        "server_id", "joinDate"}))
@ToString()
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Setter
@ValidJoinDate
@ValidLeaveDate
@ValidServerLimit
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
