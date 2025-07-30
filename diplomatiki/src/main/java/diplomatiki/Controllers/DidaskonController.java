package diplomatiki.Controllers;

import diplomatiki.Repositories.ThesisTopicRepository;
import diplomatiki.Repositories.ProfessorRepository;
import diplomatiki.models.ThesisTopic;
import diplomatiki.models.Professor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class DidaskonController {

    private final ThesisTopicRepository topicRepo;
    private final ProfessorRepository professorRepo;

    public DidaskonController(ThesisTopicRepository topicRepo, ProfessorRepository professorRepo) {
        this.topicRepo = topicRepo;
        this.professorRepo = professorRepo;
    }

    // ✅ Προβολή θεμάτων
    @GetMapping("/didaskon/themata")
    public String showThemata(Model model) {
        List<ThesisTopic> topics = topicRepo.findAll();
        model.addAttribute("topics", topics);
        return "didaskon/themata";
    }

    // ✅ Δημιουργία νέου θέματος
    @PostMapping("/didaskon/themata")
    public String createThema(
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("pdf") MultipartFile pdfFile,
            Model model) {
        try {
            // Αποθήκευση PDF
            String uploadDir = new File("src/main/resources/static/pdfs").getCanonicalPath();
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + pdfFile.getOriginalFilename();
            File destination = new File(uploadDir + File.separator + fileName);
            pdfFile.transferTo(destination);

            // Καθηγητής (π.χ. ID=1)
            Optional<Professor> professorOpt = professorRepo.findById(1);
            if (professorOpt.isEmpty()) {
                model.addAttribute("error", "❌ Δεν βρέθηκε καθηγητής.");
                return "didaskon/themata";
            }

            // Δημιουργία θέματος
            ThesisTopic topic = new ThesisTopic();
            topic.setTitle(title);
            topic.setSummary(summary);
            topic.setDescriptionPdfPath("/pdfs/" + fileName);
            topic.setAssigned(false);
            topic.setProfessor(professorOpt.get());

            topicRepo.save(topic);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "❌ Σφάλμα κατά την αποθήκευση αρχείου.");
        }

        return "redirect:/didaskon/themata";
    }

    // ✅ Εμφάνιση φόρμας επεξεργασίας
    @GetMapping("/didaskon/themata/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        ThesisTopic topic = topicRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("❌ Μη έγκυρο ID: " + id));
        model.addAttribute("topic", topic);
        return "didaskon/edit_thematos";
    }

    // ✅ Ενημέρωση υπάρχοντος θέματος
    @PostMapping("/didaskon/themata/edit")
    public String updateThema(
            @ModelAttribute("topic") ThesisTopic formTopic,
            @RequestParam(value = "pdf", required = false) MultipartFile pdfFile) {

        Optional<ThesisTopic> existingOpt = topicRepo.findById(formTopic.getTopicId());
        if (existingOpt.isEmpty())
            return "redirect:/didaskon/themata"; // ή εμφάνιση error

        ThesisTopic existing = existingOpt.get();

        // Ενημέρωση τίτλου & σύνοψης
        existing.setTitle(formTopic.getTitle());
        existing.setSummary(formTopic.getSummary());

        // Ανέβηκε νέο PDF
        if (pdfFile != null && !pdfFile.isEmpty()) {
            try {
                String uploadDir = new File("src/main/resources/static/pdfs").getCanonicalPath();
                File dir = new File(uploadDir);
                if (!dir.exists())
                    dir.mkdirs();

                String fileName = UUID.randomUUID() + "_" + pdfFile.getOriginalFilename();
                File destination = new File(uploadDir + File.separator + fileName);
                pdfFile.transferTo(destination);

                existing.setDescriptionPdfPath("/pdfs/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Αν δεν έχει καθηγητή, όρισε default
        if (existing.getProfessor() == null) {
            professorRepo.findById(1).ifPresent(existing::setProfessor);
        }

        topicRepo.save(existing);
        return "redirect:/didaskon/themata";
    }
}