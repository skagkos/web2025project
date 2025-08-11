package diplomatiki.Repositories;

import diplomatiki.entity.ThesisPresentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThesisPresentationRepository extends JpaRepository<ThesisPresentation, Integer> {

    // Τελευταία (πιο πρόσφατη) παρουσίαση για μια διπλωματική
    Optional<ThesisPresentation> findFirstByThesis_IdOrderByPresentationDateDesc(Integer thesisId);
}
