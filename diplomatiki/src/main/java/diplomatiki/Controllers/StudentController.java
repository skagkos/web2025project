package diplomatiki.Controllers;

import diplomatiki.entity.Student;
import diplomatiki.entity.Thesis;
import diplomatiki.entity.ThesisGrade;
import diplomatiki.entity.User;
import diplomatiki.Repositories.StudentRepository;
import diplomatiki.Repositories.ThesisGradeRepository;
import diplomatiki.Repositories.ThesisRepository;
import diplomatiki.Repositories.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {

    private final ThesisRepository thesisRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ThesisGradeRepository thesisGradeRepository;

    public StudentController(ThesisRepository thesisRepository,
            StudentRepository studentRepository,
            UserRepository userRepository,
            ThesisGradeRepository thesisGradeRepository) {
        this.thesisRepository = thesisRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.thesisGradeRepository = thesisGradeRepository;
    }

    @GetMapping("/foithths/foithths")
    public String studentHome() {
        return "redirect:/foithths/foititis_diplomatiki";
    }

    @GetMapping("/foithths/foititis_diplomatiki")
    public String diplomatikiPage() {
        return "foithths/foititis_diplomatiki";
    }

    // Προβολή περατωμένης (ή της πιο πρόσφατης) για τον τρέχοντα φοιτητή
    @GetMapping("/foithths/foititis_peratomeni")
    public String peratomeniForCurrentStudent(@AuthenticationPrincipal UserDetails principal, Model model) {

        if (principal == null)
            return "redirect:/login";

        // 1) User από username
        String username = principal.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            model.addAttribute("thesis", null);
            model.addAttribute("infoMessage", "Ο λογαριασμός χρήστη δεν βρέθηκε.");
            return "foithths/foititis_peratomeni";
        }

        // 2) Student μέσω user.id (MapsId)
        Student student = studentRepository.findByUser_Id(user.getId()).orElse(null);
        if (student == null) {
            model.addAttribute("thesis", null);
            model.addAttribute("infoMessage", "Δεν βρέθηκε προφίλ φοιτητή για τον χρήστη.");
            return "foithths/foititis_peratomeni";
        }

        Integer sid = student.getStudentId();

        // 3) Προτίμησε completed, αλλιώς πιο πρόσφατη γενικά
        Optional<Thesis> completed = thesisRepository
                .findTopByStudent_StudentIdAndStatusOrderByAssignmentDateDesc(sid, "completed");

        Thesis thesis = completed.orElseGet(
                () -> thesisRepository.findTopByStudent_StudentIdOrderByAssignmentDateDesc(sid).orElse(null));

        if (thesis == null) {
            model.addAttribute("thesis", null);
            model.addAttribute("infoMessage", "Δεν υπάρχει καμία διπλωματική για τον λογαριασμό σου.");
            return "foithths/foititis_peratomeni";
        }

        // Αν δεν είναι completed, εμφάνισε φιλικό μήνυμα
        if (!"completed".equalsIgnoreCase(thesis.getStatus())) {
            model.addAttribute("infoMessage",
                    "Η διπλωματική δεν είναι ακόμη 'completed' (τρέχουσα κατάσταση: " + thesis.getStatus() + ").");
        }

        // 4) Βαθμός επιβλέποντα (supervisor)
        Integer supervisorId = null;
        if (thesis.getTopic() != null && thesis.getTopic().getProfessor() != null) {
            // Αν ο Professor έχει Long id, κάνουμε intValue()
            supervisorId = thesis.getTopic().getProfessor().getId().intValue();
        }

        if (supervisorId != null) {
            ThesisGrade supervisorGrade = thesisGradeRepository
                    .findTopByThesis_IdAndProfessor_IdOrderBySubmittedAtDesc(thesis.getId(), supervisorId);
            if (supervisorGrade != null) {
                model.addAttribute("grade", supervisorGrade.getOverallGrade());
            }
        }

        model.addAttribute("thesis", thesis);
        return "foithths/foititis_peratomeni";
    }

    @GetMapping("/foithths/foititis_provoli_thematon")
    public String provoliThematonPage(Model model) {
        List<Thesis> topics = thesisRepository.findByStatus("pending");
        model.addAttribute("topics", topics);
        return "foithths/foititis_provoli_thematon";
    }
}
