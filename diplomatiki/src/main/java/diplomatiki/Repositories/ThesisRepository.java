package diplomatiki.Repositories;

import diplomatiki.entity.Thesis;
import diplomatiki.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThesisRepository extends JpaRepository<Thesis, Integer> {

    List<Thesis> findByTopic_Professor(Professor professor);

    List<Thesis> findByStatus(String status);

    // ✅ Φέρνει το πιο πρόσφατο "completed" thesis του φοιτητή
    Optional<Thesis> findTopByStudent_StudentIdAndStatusOrderByAssignmentDateDesc(Integer studentId, String status);

    // ✅ Φέρνει το πιο πρόσφατο thesis ανεξαρτήτως status
    Optional<Thesis> findTopByStudent_StudentIdOrderByAssignmentDateDesc(Integer studentId);
}
