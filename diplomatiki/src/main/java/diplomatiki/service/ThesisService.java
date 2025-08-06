package diplomatiki.service;

import diplomatiki.Repositories.ProfessorRepository;
import diplomatiki.Repositories.ThesisGradeRepository;
import diplomatiki.Repositories.ThesisNoteRepository;
import diplomatiki.Repositories.ThesisRepository;
import diplomatiki.Repositories.UserRepository;
import diplomatiki.entity.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ThesisService {

    private final ThesisRepository thesisRepository;
    private final ThesisNoteRepository thesisNoteRepository;
    private final ThesisGradeRepository thesisGradeRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;

    public ThesisService(ThesisRepository thesisRepository,
                         ThesisNoteRepository thesisNoteRepository,
                         ThesisGradeRepository thesisGradeRepository,
                         ProfessorRepository professorRepository,
                         UserRepository userRepository) {
        this.thesisRepository = thesisRepository;
        this.thesisNoteRepository = thesisNoteRepository;
        this.thesisGradeRepository = thesisGradeRepository;
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
    }

    public List<Thesis> getThesesForProfessor(Professor professor) {
        return thesisRepository.findByTopic_Professor(professor);
    }

    public Optional<Professor> getProfessorFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) return Optional.empty();
        return userRepository.findByUsername(principal.getName())
                .flatMap(professorRepository::findByUser);
    }

    public void saveNote(Integer thesisId, Integer professorId, String noteText) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        ThesisNote note = new ThesisNote();
        note.setThesis(thesis);
        note.setProfessor(professor);
        note.setNoteText(noteText);
        note.setCreatedAt(LocalDate.now());

        thesisNoteRepository.save(note);
    }

    public void cancelAssignment(Integer thesisId, String reason) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found"));
        thesis.setStatus("Ακυρώθηκε: " + reason);
        thesisRepository.save(thesis);
    }

    public void submitGrade(Integer thesisId, Integer professorId, Double grade, String details) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new RuntimeException("Thesis not found"));

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        BigDecimal roundedGrade = BigDecimal.valueOf(grade).setScale(2, RoundingMode.HALF_UP);

        ThesisGrade gradeEntry = new ThesisGrade();
        gradeEntry.setThesis(thesis);
        gradeEntry.setProfessor(professor);
        gradeEntry.setOverallGrade(roundedGrade);
        gradeEntry.setDetails(details);
        gradeEntry.setSubmittedAt(LocalDate.now());

        thesisGradeRepository.save(gradeEntry);
    }
}
