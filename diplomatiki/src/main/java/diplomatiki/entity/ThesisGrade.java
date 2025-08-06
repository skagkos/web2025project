package diplomatiki.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "thesis_grades")
public class ThesisGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "thesis_id", nullable = false)
    private Thesis thesis;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Column(name = "overall_grade", precision = 4, scale = 2)
    private BigDecimal overallGrade;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "submitted_at")
    private LocalDate submittedAt;

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

    public BigDecimal getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(BigDecimal overallGrade) {
        this.overallGrade = overallGrade;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDate submittedAt) {
        this.submittedAt = submittedAt;
    }
}
