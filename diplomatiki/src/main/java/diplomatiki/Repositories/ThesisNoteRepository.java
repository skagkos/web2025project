package diplomatiki.Repositories;

import diplomatiki.entity.ThesisNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesisNoteRepository extends JpaRepository<ThesisNote, Integer> {
    // Αν χρειαστείς π.χ. σημειώσεις ανά καθηγητή/διπλωματική μπορείς να προσθέσεις custom queries εδώ
}
