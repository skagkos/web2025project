package diplomatiki.Repositories;

import diplomatiki.entity.ThesisGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesisGradeRepository extends JpaRepository<ThesisGrade, Integer> {
    // Αν χρειαστείς βαθμούς ανά thesis/professor, μπορείς να προσθέσεις methods εδώ
}
