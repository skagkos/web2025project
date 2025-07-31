package diplomatiki.Controllers;

import diplomatiki.entity.ThesisTopic;
import diplomatiki.entity.Professor;
import diplomatiki.entity.User;
import diplomatiki.Repositories.ThesisTopicRepository;
import diplomatiki.Repositories.ProfessorRepository;
import diplomatiki.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/didaskon")
public class DidaskonController {

    private final ThesisTopicRepository topicRepo;
    private final ProfessorRepository professorRepo;
    private final UserRepository userRepository;

    private final Path uploadBase;

    public DidaskonController(ThesisTopicRepository topicRepo,
            ProfessorRepository professorRepo,
            UserRepository userRepository,
            @Value("${upload.dir:/tmp/uploads}") String uploadDir) throws IOException {
        this.topicRepo = topicRepo;
        this.professorRepo = professorRepo;
        this.userRepository = userRepository;
        this.uploadBase = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadBase);
    }

    private Optional<Professor> getProfessorForPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null)
            return Optional.empty();
        Optional<User> userOpt = userRepository.findByUsername(principal.getName());
        if (userOpt.isEmpty())
            return Optional.empty();
        return professorRepo.findByUser(userOpt.get());
    }

    @GetMapping
    public String didaskonHome() {
        return "didaskon/didaskon"; // templates/didaskon/didaskon.html
    }

    @GetMapping("/themata")
    public String showThemata(Model model, Principal principal) {
        Optional<Professor> profOpt = getProfessorForPrincipal(principal);
        if (profOpt.isEmpty()) {
            return "redirect:/login";
        }

        List<ThesisTopic> topics = topicRepo.findByProfessor(profOpt.get());
        model.addAttribute("topics", topics);
        return "didaskon/themata";
    }

    @PostMapping("/themata")
    public String createThema(
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("pdf") MultipartFile pdfFile,
            Model model,
            Principal principal) {

        Optional<Professor> professorOpt = getProfessorForPrincipal(principal);
        if (professorOpt.isEmpty()) {
            return "redirect:/login";
        }

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
            topic.setDescriptionPdfPath("/pdfs/" + fileName); // Υποθέτει πως ο /pdfs/** είναι mapped σε uploadBase
            topic.setAssigned(false);
            topic.setProfessor(professorOpt.get());

            topicRepo.save(topic);

        } catch (IOException e) {
            model.addAttribute("error", "❌ Σφάλμα κατά την αποθήκευση αρχείου.");
            // εδώ καλό είναι να logάρεις το e με logger
        }

        return "redirect:/didaskon/themata";
    }

    @GetMapping("/themata/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        Optional<ThesisTopic> topicOpt = topicRepo.findById(id);
        if (topicOpt.isEmpty()) {
            return "redirect:/didaskon/themata";
        }
        ThesisTopic topic = topicOpt.get();

        Optional<Professor> profOpt = getProfessorForPrincipal(principal);
        if (profOpt.isEmpty() || topic.getProfessor() == null
                || !profOpt.get().getId().equals(topic.getProfessor().getId())) {
            return "redirect:/didaskon/themata"; // δεν είναι ιδιοκτήτης
        }

        model.addAttribute("topic", topic);
        return "didaskon/edit_thematos";
    }

    @PostMapping("/themata/edit/{id}")
    public String updateThema(
            @PathVariable("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam(value = "pdf", required = false) MultipartFile pdfFile,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return "redirect:/login";
        }

        Optional<ThesisTopic> existingOpt = topicRepo.findById(id);
        if (existingOpt.isEmpty()) {
            return "redirect:/didaskon/themata";
        }

        ThesisTopic existing = existingOpt.get();

        Optional<Professor> profOpt = getProfessorForPrincipal(principal);
        if (profOpt.isEmpty() || existing.getProfessor() == null
                || !profOpt.get().getId().equals(existing.getProfessor().getId())) {
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
                } catch (IOException e) {
                    // log error αν έχεις logger
                }
            }
        }

        topicRepo.save(existing);
        return "redirect:/didaskon/themata";
    }
}
