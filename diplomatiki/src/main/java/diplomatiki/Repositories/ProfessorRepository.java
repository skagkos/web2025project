package diplomatiki.Repositories;

import diplomatiki.entity.Professor;
import diplomatiki.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {

    // Για login: βρίσκει καθηγητή από User
    Optional<Professor> findByUser(User user);
}
