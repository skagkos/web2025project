package diplomatiki.Repositories;

import diplomatiki.entity.ThesisGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThesisGradeRepository extends JpaRepository<ThesisGrade, Integer> {

    // Χρήση του "id" γιατί στο Thesis entity το primary key είναι private Integer
    // id;
    List<ThesisGrade> findByThesis_Id(Integer thesisId);

    // ή αν θέλεις μόνο τον πρώτο
    ThesisGrade findFirstByThesis_Id(Integer thesisId);
}
