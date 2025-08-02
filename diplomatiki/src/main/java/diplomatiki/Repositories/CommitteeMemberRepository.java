package diplomatiki.Repositories;

import diplomatiki.entity.CommitteeMember;
import diplomatiki.entity.Professor;
import diplomatiki.entity.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember, Integer> {
    List<CommitteeMember> findByProfessorAndAcceptedIsNull(Professor professor);

    List<CommitteeMember> findByThesis(Thesis thesis);
}
