package diplomatiki.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Αρχική σελίδα
    @GetMapping("/")
    public String home() {
        return "login"; // templates/login.html
    }

    // Σελίδα Διδάσκοντα (Αρχική προβολή)
    // @GetMapping("/didaskon")
    // public String didaskon() {
    // return "didaskon/didaskon"; // templates/didaskon/didaskon.html
    // }

    // Υποσελίδες Διδάσκοντα
    // @GetMapping("/didaskon/themata")
    // public String themata() {
    // return "didaskon/themata"; // templates/didaskon/themata.html
    // }

    // @GetMapping("/didaskon/anathesi")
    // public String anathesi() {
    // return "didaskon/anathesi"; // templates/didaskon/anathesi.html
    // }

    @GetMapping("/didaskon/lista_diplwmatikon")
    public String listaDiplwmatikon() {
        return "didaskon/lista_diplwmatikon"; // templates/didaskon/lista_diplwmatikon.html
    }

    @GetMapping("/didaskon/proskliseis")
    public String proskliseis() {
        return "didaskon/proskliseis"; // templates/didaskon/proskliseis.html
    }

    @GetMapping("/didaskon/statistika")
    public String statistika() {
        return "didaskon/statistika"; // templates/didaskon/statistika.html
    }

    @GetMapping("/didaskon/diacheirisi")
    public String diacheirisi() {
        return "didaskon/diacheirisi"; // templates/didaskon/diacheirisi.html
    }

    // Σελίδα Φοιτητή
    @GetMapping("/foithths")
    public String foithths() {
        return "foithths/foithths"; // templates/foithths/foithths.html
    }
}