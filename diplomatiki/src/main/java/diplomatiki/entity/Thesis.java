package diplomatiki.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "thesis")
public class Thesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thesis_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private ThesisTopic topic;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String status;

    @Column(name = "assignment_date")
    private LocalDate assignmentDate;

    @Column(name = "gs_protocol_number")
    private String gsProtocolNumber;

    @Column(name = "gs_protocol_year")
    private Integer gsProtocolYear;

    @Column(name = "repository_link")
    private String repositoryLink;

    // === Getters & Setters ===

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
