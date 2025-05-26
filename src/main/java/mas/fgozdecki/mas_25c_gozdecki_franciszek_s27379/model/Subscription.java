package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

        SubscriptionLevel(Integer serverCountLimit, Double price) {
            this.serverCountLimit = serverCountLimit;
            this.price = price;
        }

        private final Integer serverCountLimit;
        private final Double price;
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
