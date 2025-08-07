package diplomatiki.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "professors")
public class Professor {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public Professor() {
    }

    public Professor(User user) {
        this.user = user;
    }

    // === Getters & Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
