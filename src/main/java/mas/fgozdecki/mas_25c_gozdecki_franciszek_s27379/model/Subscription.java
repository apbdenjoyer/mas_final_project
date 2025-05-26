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
        NONE(25, 4.99),
        CLASSIC(50, 7.99),
        PRO(250, 9.99);

        SubscriptionLevel(Integer embedMbLimit, Double price) {
            this.embedMbLimit = embedMbLimit;
            this.price = price;
        }

        private final Integer embedMbLimit;
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
