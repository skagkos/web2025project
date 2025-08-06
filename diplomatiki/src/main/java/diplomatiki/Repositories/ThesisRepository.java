package diplomatiki.Repositories;

import diplomatiki.entity.Professor;
import diplomatiki.entity.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisRepository extends JpaRepository<Thesis, Integer> {
    List<Thesis> findByTopic_Professor(Professor professor);

    List<Thesis> findByStatus(String pending);
}
