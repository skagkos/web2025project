package diplomatiki.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "thesis")
public class Thesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer thesisId;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private ThesisTopic topic;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String status;
    private LocalDate assignmentDate;
    private String gsProtocolNumber;
    private Integer gsProtocolYear;
    private String repositoryLink;

    // === Getters & Setters ===

    public Integer getThesisId() {
        return thesisId;
    }

    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
    }

    public ThesisTopic getTopic() {
        return topic;
    }

    public void setTopic(ThesisTopic topic) {
        this.topic = topic;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getGsProtocolNumber() {
        return gsProtocolNumber;
    }

    public void setGsProtocolNumber(String gsProtocolNumber) {
        this.gsProtocolNumber = gsProtocolNumber;
    }

    public Integer getGsProtocolYear() {
        return gsProtocolYear;
    }

    public void setGsProtocolYear(Integer gsProtocolYear) {
        this.gsProtocolYear = gsProtocolYear;
    }

    public String getRepositoryLink() {
        return repositoryLink;
    }

    public void setRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }
}
