package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TextChannel extends Channel {

    @ElementCollection
    @CollectionTable(name = "blacklisted_words", joinColumns =
    @JoinColumn(name =
            "textchannel_id"))
    private Set<String> blacklist;

    @Min(0)
    private Integer slowmode;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Message> messages;
}
