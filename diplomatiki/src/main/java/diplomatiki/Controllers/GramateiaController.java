package diplomatiki.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GramateiaController {

    @GetMapping("/gramateia")
    public String main() {
        return "gramateia/gramateia_main";
    }

    @GetMapping("/gramateia/gramateia_provoli_de")
    public String provoliDe() {
        return "gramateia/gramateia_provoli_de";
    }

    @GetMapping("/gramateia/gramateia_import_json")
    public String importJson() {
        return "gramateia/gramateia_import_json";
    }

    @GetMapping("/gramateia/gramateia_diplomatiki")
    public String diplomatiki() {
        return "gramateia/gramateia_diplomatiki";
    }
}
