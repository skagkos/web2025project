package diplomatiki.Controllers;

import diplomatiki.entity.Student;
import diplomatiki.entity.Thesis;
import diplomatiki.entity.ThesisGrade;
import diplomatiki.entity.ThesisPresentation;
import diplomatiki.entity.User;
import diplomatiki.Repositories.StudentRepository;
import diplomatiki.Repositories.ThesisGradeRepository;
import diplomatiki.Repositories.ThesisPresentationRepository;
import diplomatiki.Repositories.ThesisRepository;
import diplomatiki.Repositories.UserRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {

    private final ThesisRepository thesisRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ThesisGradeRepository thesisGradeRepository;
    private final ThesisPresentationRepository thesisPresentationRepository;

    public StudentController(ThesisRepository thesisRepository,
            StudentRepository studentRepository,
            UserRepository userRepository,
            ThesisGradeRepository thesisGradeRepository,
            ThesisPresentationRepository thesisPresentationRepository) {
        this.thesisRepository = thesisRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.thesisGradeRepository = thesisGradeRepository;
        this.thesisPresentationRepository = thesisPresentationRepository;
    }

    // Αρχική φοιτητή -> redirect στη σελίδα διαχείρισης
    @GetMapping("/foithths/foithths")
    public String studentHome() {
        return "foithths/foithths";
    }

    // Σελίδα διαχείρισης διπλωματικής
    @GetMapping("/foithths/foititis_diplomatiki")
    public String diplomatikiPage(@AuthenticationPrincipal UserDetails principal, Model model) {
        if (principal != null) {
            userRepository.findByUsername(principal.getUsername())
                    .ifPresent(user -> studentRepository.findByUser_Id(user.getId())
                            .ifPresent(student -> thesisRepository
                                    .findTopByStudent_StudentIdOrderByAssignmentDateDesc(student.getStudentId())
                                    .ifPresent(thesis -> {
                                        model.addAttribute("thesis", thesis);
                                        thesisPresentationRepository
                                                .findFirstByThesis_IdOrderByPresentationDateDesc(thesis.getId())
                                                .ifPresent(p -> model.addAttribute("presentation", p));
                                    })));
        }
        return "foithths/foititis_diplomatiki";
    }

    // Προβολή περατωμένης (ή της πιο πρόσφατης) για τον τρέχοντα φοιτητή
    @GetMapping("/foithths/foititis_peratomeni")
    public String peratomeniForCurrentStudent(@AuthenticationPrincipal UserDetails principal, Model model) {

        if (principal == null)
            return "redirect:/login";

        // 1) User
        User user = userRepository.findByUsername(principal.getUsername()).orElse(null);
        if (user == null) {
            model.addAttribute("thesis", null);
            model.addAttribute("infoMessage", "Ο λογαριασμός χρήστη δεν βρέθηκε.");
            return "foithths/foititis_peratomeni";
        }

        // 2) Student
        Student student = studentRepository.findByUser_Id(user.getId()).orElse(null);
        if (student == null) {
            model.addAttribute("thesis", null);
            model.addAttribute("infoMessage", "Δεν βρέθηκε προφίλ φοιτητή για τον χρήστη.");
            return "foithths/foititis_peratomeni";
        }

        Integer sid = student.getStudentId();

        // 3) Διπλωματική
        Optional<Thesis> completed = thesisRepository
                .findTopByStudent_StudentIdAndStatusOrderByAssignmentDateDesc(sid, "completed");

        Thesis thesis = completed.orElseGet(
                () -> thesisRepository.findTopByStudent_StudentIdOrderByAssignmentDateDesc(sid).orElse(null));

        if (thesis == null) {
            model.addAttribute("thesis", null);
            model.addAttribute("infoMessage", "Δεν υπάρχει καμία διπλωματική για τον λογαριασμό σου.");
            return "foithths/foititis_peratomeni";
        }

        if (!"completed".equalsIgnoreCase(thesis.getStatus())) {
            model.addAttribute("infoMessage",
                    "Η διπλωματική δεν είναι ακόμη 'completed' (τρέχουσα κατάσταση: " + thesis.getStatus() + ").");
        }

        // 4) Βαθμός επιβλέποντα
        Integer supervisorId = null;
        if (thesis.getTopic() != null && thesis.getTopic().getProfessor() != null) {
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

    // Προβολή θεμάτων
    @GetMapping("/foithths/foititis_provoli_thematon")
    public String provoliThematonPage(Model model) {
        List<Thesis> topics = thesisRepository.findByStatus("pending");
        model.addAttribute("topics", topics);
        return "foithths/foititis_provoli_thematon";
    }

    // Αποθήκευση στοιχείων παρουσίασης / αποθετηρίου
    @PostMapping("/student/thesis/manage")
    public String manageThesis(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime presentationDate,
            @RequestParam(required = false) String presentationType,
            @RequestParam(required = false) String locationOrLink,
            @RequestParam(required = false) String announcementText,
            @RequestParam(required = false) String repositoryLink,
            Model model) {

        if (principal == null) {
            model.addAttribute("error", "Δεν είστε συνδεδεμένος.");
            return "foithths/foititis_diplomatiki";
        }

        User user = userRepository.findByUsername(principal.getUsername()).orElse(null);
        if (user == null) {
            model.addAttribute("error", "Ο χρήστης δεν βρέθηκε.");
            return "foithths/foititis_diplomatiki";
        }

        Student student = studentRepository.findByUser_Id(user.getId()).orElse(null);
        if (student == null) {
            model.addAttribute("error", "Δεν βρέθηκε φοιτητής για τον χρήστη.");
            return "foithths/foititis_diplomatiki";
        }

        Thesis thesis = thesisRepository.findTopByStudent_StudentIdOrderByAssignmentDateDesc(student.getStudentId())
                .orElse(null);
        if (thesis == null) {
            model.addAttribute("error", "Δεν βρέθηκε διπλωματική για τον φοιτητή.");
            return "foithths/foititis_diplomatiki";
        }

        // Ενημέρωση αποθετηρίου
        if (repositoryLink != null && !repositoryLink.isBlank()) {
            thesis.setRepositoryLink(repositoryLink);
            thesisRepository.save(thesis);
        }

        // Αν έχει συμπληρωθεί παρουσίαση -> αποθήκευση νέας εγγραφής
        if (presentationDate != null && presentationType != null && !presentationType.isBlank()
                && locationOrLink != null && !locationOrLink.isBlank()) {

            ThesisPresentation presentation = new ThesisPresentation();
            presentation.setThesis(thesis);
            presentation.setPresentationDate(presentationDate);
            presentation.setPresentationType(presentationType);
            presentation.setLocationOrLink(locationOrLink);
            presentation.setAnnouncementText(announcementText);

            thesisPresentationRepository.save(presentation);
            model.addAttribute("presentation", presentation);
        }

        model.addAttribute("success", "Τα στοιχεία αποθηκεύτηκαν με επιτυχία!");
        model.addAttribute("thesis", thesis);

        return "foithths/foititis_diplomatiki";
    }
}
