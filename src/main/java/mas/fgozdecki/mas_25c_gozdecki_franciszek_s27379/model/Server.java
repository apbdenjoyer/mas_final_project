package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 5, max = 64)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private User owner;

    @OneToMany(mappedBy = "server", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Channel> channels = new HashSet<>();

    @OneToMany(mappedBy = "server", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Emoji> emojis = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "server", cascade = CascadeType.REMOVE)
    private Set<Membership> memberships = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "server", cascade = CascadeType.REMOVE)
    private Set<ServerEvent> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Server name cannot be null");
        }
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    private void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        if (channels == null) {
            throw new IllegalArgumentException("Channels cannot be null");
        }
        this.channels = channels;
    }

    public Set<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(Set<Emoji> emojis) {
        if (emojis == null) {
            throw new IllegalArgumentException("Emojis cannot be null");
        }
        this.emojis = emojis;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        if (memberships == null) {
            throw new IllegalArgumentException("Memberships cannot be null");
        }
        this.memberships = memberships;
    }

    public Set<ServerEvent> getEvents() {
        return events;
    }

    public void setEvents(Set<ServerEvent> events) {
        if (events == null) {
            throw new IllegalArgumentException("Events cannot be null");
        }
        this.events = events;
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }
}
