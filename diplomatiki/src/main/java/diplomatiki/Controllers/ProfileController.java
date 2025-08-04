package diplomatiki.Controllers;

import diplomatiki.Repositories.UserRepository;
import diplomatiki.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Optional<User> getCurrentUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(principal.getName());
    }

    /**
     * Εμφάνιση / φόρμα επεξεργασίας προφίλ (μία σελίδα για view+edit).
     * Υποστηρίζει και το legacy path αν θέλεις.
     */
    @GetMapping({ "/profile", "/foithths/foititis_profile" })
    public String profileForm(Model model, Principal principal) {
        Optional<User> userOpt = getCurrentUser(principal);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User existing = userOpt.get();

        if (!model.containsAttribute("profileForm")) {
            ProfileForm form = new ProfileForm();
            form.setEmail(existing.getEmail());
            form.setAddress(existing.getAddress());
            form.setMobilePhone(existing.getMobilePhone());
            form.setLandlinePhone(existing.getLandlinePhone());
            form.setFirstName(existing.getFirstName());
            form.setLastName(existing.getLastName());
            model.addAttribute("profileForm", form);
        }
        model.addAttribute("user", existing); // για το username κτλ.
        return "foithths/foititis_profile"; // ενιαίο template
    }

    /**
     * Υποβολή της φόρμας επεξεργασίας.
     */
    @PostMapping({ "/profile", "/foithths/foititis_profile" })
    public String submitProfile(@Valid @ModelAttribute("profileForm") ProfileForm form,
            BindingResult bindingResult,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = getCurrentUser(principal);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        User existing = userOpt.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", existing);
            // ώστε το redirect να διατηρήσει τα errors αν κάνεις redirectAttributes, εδώ
            // απλώς επιστρέφεις view
            return "foithths/foititis_profile";
        }

        // Ενημέρωση πεδίων
        existing.setFirstName(form.getFirstName());
        existing.setLastName(form.getLastName());
        existing.setEmail(form.getEmail());
        existing.setAddress(form.getAddress());
        existing.setMobilePhone(form.getMobilePhone());
        existing.setLandlinePhone(form.getLandlinePhone());

        userRepository.save(existing);

        redirectAttributes.addFlashAttribute("success", "Οι αλλαγές αποθηκεύτηκαν επιτυχώς.");
        return "redirect:/profile";
    }

    /**
     * DTO για binding της φόρμας (προφίλ).
     */
    public static class ProfileForm {

        @NotBlank(message = "Το όνομα δεν μπορεί να είναι κενό.")
        private String firstName;

        @NotBlank(message = "Το επώνυμο δεν μπορεί να είναι κενό.")
        private String lastName;

        @NotBlank(message = "Το email δεν μπορεί να είναι κενό.")
        @Email(message = "Μη έγκυρη διεύθυνση email.")
        private String email;

        private String address;
        private String mobilePhone;
        private String landlinePhone;

        // getters / setters

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getLandlinePhone() {
            return landlinePhone;
        }

        public void setLandlinePhone(String landlinePhone) {
            this.landlinePhone = landlinePhone;
        }
    }
}
