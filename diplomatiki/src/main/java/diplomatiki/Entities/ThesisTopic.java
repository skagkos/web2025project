/*
 * package diplomatiki.models;
 * 
 * import jakarta.persistence.*;
 * 
 * @Entity
 * 
 * @Table(name = "thesis_topics")
 * public class ThesisTopic {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY)
 * private Integer topic_id;
 * 
 * private String title;
 * private String summary;
 * 
 * @Column(name = "description_pdf_path")
 * private String descriptionPdfPath;
 * 
 * @Column(name = "is_assigned")
 * private boolean isAssigned;
 * 
 * @ManyToOne
 * 
 * @JoinColumn(name = "professor_id")
 * private Professor professor; // Θα πρέπει να υπάρχει και η κλάση Professor
 * 
 * // Getters & Setters
 * } /*
 */
