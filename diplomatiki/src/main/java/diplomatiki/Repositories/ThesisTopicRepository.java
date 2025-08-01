package diplomatiki.Repositories;

import diplomatiki.entity.ThesisTopic;
import diplomatiki.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisTopicRepository extends JpaRepository<ThesisTopic, Integer> {
    List<ThesisTopic> findByProfessor(Professor professor);

    // ✅ Πρόσθεσε αυτή τη μέθοδο:
    List<ThesisTopic> findByAssignedFalse();
}
