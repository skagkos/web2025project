package diplomatiki.Repositories;

import diplomatiki.entity.ThesisGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesisGradeRepository extends JpaRepository<ThesisGrade, Integer> {

    // Τελευταίος βαθμός από συγκεκριμένο καθηγητή για μια διπλωματική
    ThesisGrade findTopByThesis_IdAndProfessor_IdOrderBySubmittedAtDesc(Integer thesisId, Integer professorId);

}
