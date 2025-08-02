package diplomatiki.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "committee_members")
public class CommitteeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "committee_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thesis_id", nullable = false)
    private Thesis thesis;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    public enum Role {
        supervisor,
        member
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // null = σε αναμονή, true/false = απάντηση
    private Boolean accepted;

    @Column(name = "invitation_date")
    private LocalDate invitationDate;

    @Column(name = "response_date")
    private LocalDate responseDate;

    // === Getters & Setters ===

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDate getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(LocalDate invitationDate) {
        this.invitationDate = invitationDate;
    }

    public LocalDate getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }
}
