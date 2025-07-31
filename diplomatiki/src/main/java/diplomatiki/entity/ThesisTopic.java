package diplomatiki.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "thesis_topics")
public class ThesisTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicId;

    private String title;

    @Column(length = 2000)
    private String summary;

    private String descriptionPdfPath;

    private boolean assigned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    // === Getters & Setters ===

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescriptionPdfPath() {
        return descriptionPdfPath;
    }

    public void setDescriptionPdfPath(String descriptionPdfPath) {
        this.descriptionPdfPath = descriptionPdfPath;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
