package diplomatiki.Repositories;

import diplomatiki.models.ThesisTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesisTopicRepository extends JpaRepository<ThesisTopic, Integer> {
}
