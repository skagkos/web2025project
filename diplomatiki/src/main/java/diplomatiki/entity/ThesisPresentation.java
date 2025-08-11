package diplomatiki.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thesis_presentation")
public class ThesisPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presentation_id")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "thesis_id", nullable = false)
    private Thesis thesis;

    @Column(name = "presentation_date", nullable = false)
    private LocalDateTime presentationDate;

    @Column(name = "presentation_type", nullable = false) // "in_person" | "online"
    private String presentationType;

    @Column(name = "location_or_link", nullable = false)
    private String locationOrLink;

    @Column(name = "announcement_text")
    private String announcementText;

    // getters & setters
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

    public LocalDateTime getPresentationDate() {
        return presentationDate;
    }

    public void setPresentationDate(LocalDateTime presentationDate) {
        this.presentationDate = presentationDate;
    }

    public String getPresentationType() {
        return presentationType;
    }

    public void setPresentationType(String presentationType) {
        this.presentationType = presentationType;
    }

    public String getLocationOrLink() {
        return locationOrLink;
    }

    public void setLocationOrLink(String locationOrLink) {
        this.locationOrLink = locationOrLink;
    }

    public String getAnnouncementText() {
        return announcementText;
    }

    public void setAnnouncementText(String announcementText) {
        this.announcementText = announcementText;
    }
}
