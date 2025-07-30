package diplomatiki.Controllers;

import diplomatiki.entity.Role;
import diplomatiki.entity.User;
import diplomatiki.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class RedirectController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/redirect")
    public String redirectAfterLogin(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Παίρνουμε τον πρώτο ρόλο (αν υπάρχει)
        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("");

        Long userId = user.getId(); // το σωστό όνομα πεδίου

        switch (role) {
            case "student":
                return "redirect:/student/" + userId;
            case "professor":
                return "redirect:/professor/" + userId;
            case "secretary":
                return "redirect:/secretary/" + userId;
            default:
                return "redirect:/login";
        }
    }
}
