package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.MembershipRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString()
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Login cannot be blank")
    @Length(min = 5, max = 20, message = "Login must be between 5 and 20 characters")
    private String login;

    @NotNull
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Message> messages = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<Membership> memberships = new HashSet<>();


    public void addToServer(Server s) {
        if (s == null) {
            throw new IllegalArgumentException("Server cannot be null");
        }

        for (Membership m : memberships) {
            if (m.getServer().equals(s)) {
                /*Such a membership already exists*/
                throw new IllegalArgumentException("User is already in this server");
            }
        }

        Membership membership = new Membership(this, s);
    }

    public void removeFromServer(Server s) {
        if (s == null) {
            throw new IllegalArgumentException("Server cannot be null");
        }

        for (Membership m : memberships) {
            if (m.getServer().equals(s) && m.getLeaveDate() != null) {
                m.setLeaveDate(LocalDateTime.now());
            }
        }
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Login cannot be null");
        }
        this.login = login;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    private void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("Messages cannot be null");
        }
        this.messages = messages;
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

}
