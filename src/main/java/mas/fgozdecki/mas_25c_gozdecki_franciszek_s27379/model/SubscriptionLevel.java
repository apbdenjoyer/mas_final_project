package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public enum SubscriptionLevel {
    NONE(5, 0.00),
    CLASSIC(15, 19.99),
    PRO(50, 29.99);

    SubscriptionLevel(Integer serverCountLimit, Double monthlyPrice) {
        this.serverCountLimit = serverCountLimit;
        this.monthlyPrice = monthlyPrice;
    }

    @Min(0)
    private final Integer serverCountLimit;
    @Min(0)
    private final Double monthlyPrice;
}
