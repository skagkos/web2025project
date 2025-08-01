package diplomatiki.Controllers;

import diplomatiki.Repositories.*;
import diplomatiki.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/didaskon")
public class DidaskonController {

    private final ThesisTopicRepository topicRepo;
    private final ProfessorRepository professorRepo;
    private final UserRepository userRepository;
    private final StudentRepository studentRepo;
    private final ThesisRepository thesisRepo;

    private final Path uploadBase;

    public DidaskonController(ThesisTopicRepository topicRepo,
            ProfessorRepository professorRepo,
            UserRepository userRepository,
            StudentRepository studentRepo,
            ThesisRepository thesisRepo,
            @Value("${upload.dir:/tmp/uploads}") String uploadDir) throws IOException {
        this.topicRepo = topicRepo;
        this.professorRepo = professorRepo;
        this.userRepository = userRepository;
        this.studentRepo = studentRepo;
        this.thesisRepo = thesisRepo;
        this.uploadBase = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadBase);
    }

    private Optional<Professor> getProfessorForPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null)
            return Optional.empty();
        Optional<User> userOpt = userRepository.findByUsername(principal.getName());
        return userOpt.flatMap(user -> professorRepo.findByUser(user));
    }

    @GetMapping
    public String didaskonHome() {
        return "didaskon/didaskon";
    }

    @GetMapping("/themata")
    public String showThemata(Model model, Principal principal) {
        Optional<Professor> profOpt = getProfessorForPrincipal(principal);
        if (profOpt.isEmpty())
            return "redirect:/login";
        List<ThesisTopic> topics = topicRepo.findByProfessor(profOpt.get());
        model.addAttribute("topics", topics);
        return "didaskon/themata";
    }

    @PostMapping("/themata")
    public String createThema(@RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("pdf") MultipartFile pdfFile,
            Model model,
            Principal principal) {

        Optional<Professor> professorOpt = getProfessorForPrincipal(principal);
        if (professorOpt.isEmpty())
            return "redirect:/login";

        if (pdfFile == null || pdfFile.isEmpty()) {
            model.addAttribute("error", "❌ Το PDF είναι υποχρεωτικό.");
            return "didaskon/themata";
        }

        String originalFilename = StringUtils.cleanPath(Optional.ofNullable(pdfFile.getOriginalFilename()).orElse(""));
        if (originalFilename.isBlank()) {
            model.addAttribute("error", "❌ Άκυρο όνομα αρχείου.");
            return "didaskon/themata";
        }

        try {
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path target = uploadBase.resolve(fileName);
            Files.copy(pdfFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            ThesisTopic topic = new ThesisTopic();
            topic.setTitle(title);
            topic.setSummary(summary);
            topic.setDescriptionPdfPath("/pdfs/" + fileName);
            topic.setAssigned(false);
            topic.setProfessor(professorOpt.get());

            topicRepo.save(topic);
        } catch (IOException e) {
            model.addAttribute("error", "❌ Σφάλμα κατά την αποθήκευση αρχείου.");
        }

        return "redirect:/didaskon/themata";
    }

    @GetMapping("/themata/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        Optional<ThesisTopic> topicOpt = topicRepo.findById(id);
        if (topicOpt.isEmpty())
            return "redirect:/didaskon/themata";

        ThesisTopic topic = topicOpt.get();
        Optional<Professor> profOpt = getProfessorForPrincipal(principal);
        if (profOpt.isEmpty() || topic.getProfessor() == null ||
                !profOpt.get().getId().equals(topic.getProfessor().getId())) {
            return "redirect:/didaskon/themata";
        }

        model.addAttribute("topic", topic);
        return "didaskon/edit_thematos";
    }

    @PostMapping("/themata/edit/{id}")
    public String updateThema(@PathVariable("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam(value = "pdf", required = false) MultipartFile pdfFile,
            Principal principal) {

        if (principal == null || principal.getName() == null)
            return "redirect:/login";

        Optional<ThesisTopic> existingOpt = topicRepo.findById(id);
        if (existingOpt.isEmpty())
            return "redirect:/didaskon/themata";

        ThesisTopic existing = existingOpt.get();
        Optional<Professor> profOpt = getProfessorForPrincipal(principal);
        if (profOpt.isEmpty() || existing.getProfessor() == null ||
                !profOpt.get().getId().equals(existing.getProfessor().getId())) {
            return "redirect:/didaskon/themata";
        }

        existing.setTitle(title);
        existing.setSummary(summary);

        if (pdfFile != null && !pdfFile.isEmpty()) {
            String originalFilename = StringUtils
                    .cleanPath(Optional.ofNullable(pdfFile.getOriginalFilename()).orElse(""));
            if (!originalFilename.isBlank()) {
                try {
                    String fileName = UUID.randomUUID() + "_" + originalFilename;
                    Path target = uploadBase.resolve(fileName);
                    Files.copy(pdfFile.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                    existing.setDescriptionPdfPath("/pdfs/" + fileName);
                } catch (IOException ignored) {
                }
            }
        }

        topicRepo.save(existing);
        return "redirect:/didaskon/themata";
    }

    @GetMapping("/anathesi")
    public String showAssignmentPage(Model model) {
        List<ThesisTopic> availableTopics = topicRepo.findByAssignedFalse();
        List<Student> students = studentRepo.findAll();
        List<Thesis> pendingAssignments = thesisRepo.findByStatus("pending");

        model.addAttribute("availableTopics", availableTopics);
        model.addAttribute("students", students);
        model.addAttribute("pendingAssignments", pendingAssignments);

        return "didaskon/anathesi";
    }

    @PostMapping("/anathesi")
    public String assignThesis(@RequestParam("studentId") int studentId,
            @RequestParam("topicId") int topicId) {

        Optional<Student> studentOpt = studentRepo.findById(studentId);
        Optional<ThesisTopic> topicOpt = topicRepo.findById(topicId);

        if (studentOpt.isEmpty() || topicOpt.isEmpty()) {
            return "redirect:/didaskon/anathesi?error";
        }

        Thesis thesis = new Thesis();
        thesis.setStudent(studentOpt.get());
        thesis.setTopic(topicOpt.get());
        thesis.setStatus("pending");
        thesis.setAssignmentDate(LocalDate.now());

        thesisRepo.save(thesis);

        ThesisTopic topic = topicOpt.get();
        topic.setAssigned(true);
        topicRepo.save(topic);

        return "redirect:/didaskon/anathesi?success";
    }

    @PostMapping("/anathesi/delete")
    public String cancelAssignment(@RequestParam("thesisId") int thesisId) {
        Optional<Thesis> thesisOpt = thesisRepo.findById(thesisId);
        if (thesisOpt.isPresent()) {
            Thesis thesis = thesisOpt.get();
            ThesisTopic topic = thesis.getTopic();
            topic.setAssigned(false);
            topicRepo.save(topic);
            thesisRepo.delete(thesis);
        }
        return "redirect:/didaskon/anathesi";
    }
}
