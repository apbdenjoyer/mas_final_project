package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 2000)
    private String contents;

    @ManyToOne(optional = false)
    @JoinColumn(name = "channel_id", nullable = false, updatable = false)
    private TextChannel textChannel;

    @OneToMany(mappedBy = "message", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Reaction> reactions = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private Account author;

    @NotNull
    private LocalDateTime createdAt;

    public void addReaction(User user, Emoji emoji) {
        Reaction reaction = Reaction.builder()
                .message(this)
                .user(user)
                .emoji(emoji)
                .createdAt(LocalDateTime.now())
                .build();

        user.getReactions().add(reaction);
        emoji.getReactions().add(reaction);
    }

    public void removeReaction(Reaction r) {
        if (r == null) {
            throw new IllegalArgumentException("Reaction cannot be null");
        }

        User user = r.getUser();
        Emoji emoji = r.getEmoji();
        user.getReactions().remove(r);
        emoji.getReactions().remove(r);
        reactions.remove(r);
    }
}
