package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import lombok.*;


@Getter
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
