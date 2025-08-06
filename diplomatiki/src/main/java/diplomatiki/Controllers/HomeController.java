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

    // Τα παρακάτω τα κρατάς αν τα χρησιμοποιείς
    // @GetMapping("/didaskon")
    // public String didaskon() {
    //     return "didaskon/didaskon";
    // }

    // @GetMapping("/didaskon/themata")
    // public String themata() {
    //     return "didaskon/themata";
    // }

    // ❌ Κάνε comment ή σβήσε το προβληματικό mapping
    // @GetMapping("/didaskon/anathesi")
    // public String anathesi() {
    //     return "didaskon/anathesi";
    // }

//    @GetMapping("/didaskon/lista_diplwmatikon")
//    public String listaDiplwmatikon() {
//        return "didaskon/lista_diplwmatikon";
//    }

//    @GetMapping("/didaskon/proskliseis")
//    public String proskliseis() {
//        return "didaskon/proskliseis";
//    }

    @GetMapping("/didaskon/statistika")
    public String statistika() {
        return "didaskon/statistika";
    }

    @GetMapping("/didaskon/diacheirisi")
    public String diacheirisi() {
        return "didaskon/diacheirisi";
    }

    @GetMapping("/foithths")
    public String foithths() {
        return "foithths/foithths";
    }
}
