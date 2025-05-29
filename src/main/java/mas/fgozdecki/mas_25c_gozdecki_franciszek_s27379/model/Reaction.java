package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne()
    private User user;

    @ManyToOne
    @JoinColumn(name = "emoji_id", nullable = false)
    private Emoji emoji;

    @NotNull
    private LocalDateTime createdAt;
}
