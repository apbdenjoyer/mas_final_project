package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "event_id", "signDate"}))
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime signDate;

    @ManyToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable =
            false)
    @NotNull
    private Membership participant;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private ServerEvent event;

}
