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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id",
        "startDate"}))
public class Subscription {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public enum SubscriptionLevel {
        NONE(25, 0.00),
        CLASSIC(50, 19.99),
        PRO(250, 29.99);

        SubscriptionLevel(Integer serverCountLimit, Double monthlyPrice) {
            this.serverCountLimit = serverCountLimit;
            this.monthlyPrice = monthlyPrice;
        }

        private final Integer serverCountLimit;
        private final Double monthlyPrice;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionLevel level;

    @NotNull
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable =
            false, updatable = false)
    private User user;
}
