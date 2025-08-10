package diplomatiki.Repositories;

import diplomatiki.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Μέσω της σχέσης @OneToOne private User user; (MapsId)
    Optional<Student> findByUser_Id(Long id); // αν το User.id είναι Long (συχνό)
    // Optional<Student> findByUser_Id(Integer id); // χρησιμοποίησέ το αν το
    // User.id σου είναι Integer

    // Εναλλακτικά, κατευθείαν από username του user
    Optional<Student> findByUser_Username(String username);

    // Αν χρειαστείς αναζήτηση με ΑΜ
    Optional<Student> findByAm(String am);
}
