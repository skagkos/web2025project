package diplomatiki.Controllers;

import diplomatiki.entity.Professor;
import diplomatiki.entity.Thesis;
import diplomatiki.service.ThesisService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/didaskon")
public class ThesisController {

    private final ThesisService thesisService;

    public ThesisController(ThesisService thesisService) {
        this.thesisService = thesisService;
    }

    @GetMapping("/lista_diplwmatikon")
    public String showThesisList(Model model, Principal principal) {
        Optional<Professor> profOpt = thesisService.getProfessorFromPrincipal(principal);
        if (profOpt.isEmpty()) {
            return "redirect:/login";
        }

        List<Thesis> theses = thesisService.getThesesForProfessor(profOpt.get());
        model.addAttribute("theses", theses);
        return "didaskon/lista_diplwmatikon";
    }

    @PostMapping("/save_note")
    public String saveNote(@RequestParam Integer thesisId,
                           @RequestParam Integer professorId,
                           @RequestParam String noteText,
                           HttpServletRequest request) {
        thesisService.saveNote(thesisId, professorId, noteText);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/cancel_assignment")
    public String cancelAssignment(@RequestParam Integer thesisId,
                                   @RequestParam String reason,
                                   HttpServletRequest request) {
        thesisService.cancelAssignment(thesisId, reason);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/submit_grade")
    public String submitGrade(@RequestParam Integer thesisId,
                              @RequestParam Integer professorId,
                              @RequestParam Double grade,
                              @RequestParam(required = false) String details,
                              HttpServletRequest request) {
        thesisService.submitGrade(thesisId, professorId, grade, details);
        return "redirect:" + request.getHeader("Referer");
    }
}
