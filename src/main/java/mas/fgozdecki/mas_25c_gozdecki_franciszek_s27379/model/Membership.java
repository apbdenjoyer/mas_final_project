package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id",
        "server_id", "joinDate"}))
@ToString()
@Builder
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable =
            false)
    @NotNull
    private Account member;

    @ManyToOne
    @JoinColumn(name = "server_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Server server;

    @NotNull
    private LocalDateTime joinDate;

    private LocalDateTime leaveDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Role role;

    @ManyToOne
    @JoinColumn(name = "voice_channel_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private VoiceChannel voiceChannel;


    public Membership(Account member, Server server) {
        this.member = member;
        this.server = server;
        this.joinDate = LocalDateTime.now();

        server.addMembership(this);
        member.addMembership(this);
    }

    public Membership() {}

    public Account getMember() {
        return member;
    }

    public void setMember(Account member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }
        this.member = member;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }
        this.server = server;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        if (joinDate == null) {
            throw new IllegalArgumentException("Join date cannot be null.");
        }
        this.joinDate = joinDate;
    }

    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDateTime leaveDate) {
        if (leaveDate != null) {
            throw new IllegalArgumentException("Leave date already set.");
        }

        if (leaveDate.isBefore(joinDate)) {
            throw new IllegalArgumentException("Leave date cannot be before join date.");
        }

        this.leaveDate = leaveDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        this.role = role;
    }

    public Account getUser() {
        return member;
    }



}
