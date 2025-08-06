package diplomatiki.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id")
    private Integer id;

    @Column(name = "am", nullable = false, unique = true)
    private String am;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String address;
    private String phoneMobile;
    private String phoneLandline;

    @OneToOne
    @JoinColumn(name = "student_id")
    private User user;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAm() { return am; }
    public void setAm(String am) { this.am = am; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneMobile() { return phoneMobile; }
    public void setPhoneMobile(String phoneMobile) { this.phoneMobile = phoneMobile; }

    public String getPhoneLandline() { return phoneLandline; }
    public void setPhoneLandline(String phoneLandline) { this.phoneLandline = phoneLandline; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
