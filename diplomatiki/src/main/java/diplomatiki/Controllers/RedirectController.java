package diplomatiki.Controllers;

import diplomatiki.entity.User;
import diplomatiki.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class RedirectController {

    private final UserRepository userRepository;

    @Autowired
    public RedirectController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/redirect")
    public String redirectAfterLogin(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        switch (user.getRole()) {
            case STUDENT:
                return "redirect:/foithths";
            case PROFESSOR:
                return "redirect:/didaskon";
            case SECRETARY:
                return "redirect:/gramateia";
            default:
                return "redirect:/login";
        }
    }
}
