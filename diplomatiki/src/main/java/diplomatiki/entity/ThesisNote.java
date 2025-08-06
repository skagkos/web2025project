package diplomatiki.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "thesis_notes")
public class ThesisNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "thesis_id", nullable = false)
    private Thesis thesis;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Column(name = "note_text", length = 300, nullable = false)
    private String noteText;

    @Column(name = "created_at")
    private LocalDate createdAt;

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

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
