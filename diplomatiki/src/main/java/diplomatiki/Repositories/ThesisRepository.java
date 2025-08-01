package diplomatiki.Repositories;

import diplomatiki.entity.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesisRepository extends JpaRepository<Thesis, Integer> {
    List<Thesis> findByStatus(String status);
}
