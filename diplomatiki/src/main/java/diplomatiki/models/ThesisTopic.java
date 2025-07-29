package diplomatiki.models;

import jakarta.persistence.*;

@Entity
@Table(name = "thesis_topics")
public class ThesisTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicId;

    private String title;
    private String summary;

    @Column(name = "description_pdf_path")
    private String descriptionPdfPath;

    @Column(name = "is_assigned")
    private boolean isAssigned;

    // Για τώρα, μπορείς να σχολιάσεις τον professor για να αποφύγεις το error
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    // Getters & Setters

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
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
