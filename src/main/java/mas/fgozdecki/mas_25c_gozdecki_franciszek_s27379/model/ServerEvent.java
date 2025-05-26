package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.servlet.http.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "server_id", "startTime", "endTime"}))
public class ServerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min=5, max=64)
    private String name;

    @Length(max=500)
    private String description;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "server_id")
    private Server server;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<Participation> participations;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    public enum EventStatus {
        SCHEDULED, ONGOING, COMPLETED, CANCELLED
    }


}
