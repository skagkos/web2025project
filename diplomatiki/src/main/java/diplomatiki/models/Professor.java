package diplomatiki.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professors")
public class Professor {

    @Id
    @Column(name = "professor_id")
    private Integer professorId;

    private String fullName;
    private String department;

    @OneToMany(mappedBy = "professor")
    private List<ThesisTopic> topics;

    // Getters και Setters
    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<ThesisTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<ThesisTopic> topics) {
        this.topics = topics;
    }
}
