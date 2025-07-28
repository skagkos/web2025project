package diplomatiki.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/foithths/foithths")
    public String studentHome() {
        return "foithths/foithths";
    }

    @GetMapping("/foithths/foititis_profile")
    public String profilePage() {
        return "foithths/foititis_profile";
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
    public String provoliThematonPage() {
        return "foithths/foititis_provoli_thematon";
    }
}
