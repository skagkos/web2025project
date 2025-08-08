package diplomatiki.Controllers;

import diplomatiki.entity.Thesis;
import diplomatiki.Repositories.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private ThesisRepository thesisRepository;

    @GetMapping("/foithths/foithths")
    public String studentHome() {
        return "foithths/foithths";
    }

    @GetMapping("/foithths/foititis_diplomatiki")
    public String diplomatikiPage() {
        return "foithths/foititis_diplomatiki";
    }

    @GetMapping("/foithths/foititis_peratomeni")
    public String peratomeniPage() {
        return "foithths/foititis_peratomeni";
    }

    @GetMapping("/foithths/foititis_provoli_thematon")
    public String provoliThematonPage(Model model) {
        List<Thesis> topics = thesisRepository.findByStatus("pending"); // ή άλλο ανάλογο status
        model.addAttribute("topics", topics);
        return "foithths/foititis_provoli_thematon";
    }

}
