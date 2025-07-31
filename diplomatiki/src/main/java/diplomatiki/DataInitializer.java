package diplomatiki;

import diplomatiki.entity.*;
import diplomatiki.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner migrateAndSeed(UserRepository userRepo,
            ProfessorRepository professorRepo,
            PasswordEncoder encoder) {
        return args -> {
            // Μετατροπή υπαρχόντων plaintext passwords σε BCrypt αν δεν είναι ήδη hashed
            userRepo.findAll().forEach(user -> {
                String pwd = user.getPassword();
                if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                    user.setPassword(encoder.encode(pwd));
                    userRepo.save(user);
                }
            });

            // Δοκιμαστικοί χρήστες αν δεν υπάρχουν
            if (userRepo.findByUsername("prof1").isEmpty()) {
                User profUser = new User();
                profUser.setUsername("prof1");
                profUser.setEmail("prof1@example.com");
                profUser.setRole(Role.PROFESSOR);
                profUser.setPassword(encoder.encode("password"));
                userRepo.save(profUser);

                Professor professor = new Professor();
                professor.setUser(profUser);
                professorRepo.save(professor);
            }

            if (userRepo.findByUsername("student1").isEmpty()) {
                User student = new User();
                student.setUsername("student1");
                student.setEmail("student1@example.com");
                student.setRole(Role.STUDENT);
                student.setPassword(encoder.encode("password"));
                userRepo.save(student);
            }

            if (userRepo.findByUsername("sec1").isEmpty()) {
                User sec = new User();
                sec.setUsername("sec1");
                sec.setEmail("secretary@example.com");
                sec.setRole(Role.SECRETARY);
                sec.setPassword(encoder.encode("password"));
                userRepo.save(sec);
            }
        };
    }
}
