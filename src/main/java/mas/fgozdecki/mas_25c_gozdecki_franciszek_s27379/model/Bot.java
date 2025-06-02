package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Bot extends Account{

    @NotBlank
    @Length(min =    1, max = 255)
    private String token;

    @ElementCollection
    @CollectionTable(name = "bot_features", joinColumns = @JoinColumn(name = "bot_id"))
    private Set<String> features;

    @ManyToOne()
    @JoinColumn(name = "owner_id", nullable = false)
    private User maintainer;
}
