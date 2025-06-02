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

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionLevel level;

    @Transient
    public Integer getServerLimit() {
        return level != null ? level.getServerCountLimit() : SubscriptionLevel.NONE.getServerCountLimit();
    }

    @Transient
    public Double getMonthlyPrice() {
        return level != null ? level.getMonthlyPrice() :
                SubscriptionLevel.NONE.getMonthlyPrice();
    }


    @NotNull
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable =
            false, updatable = false)
    private User user;
}


