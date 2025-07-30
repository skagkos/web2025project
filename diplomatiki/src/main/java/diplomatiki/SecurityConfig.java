package diplomatiki;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import diplomatiki.service.UserDetailsServiceImpl;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/resources/**", "/static/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/student/**").hasAuthority("student")
                        .requestMatchers("/professor/**").hasAuthority("professor")
                        .requestMatchers("/secretary/**").hasAuthority("secretary")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Εδώ αναμένεται να έχεις υλοποιήσει δικό σου UserDetailsService
    // Θα συνδεθεί με το UserRepository σου για authentication
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(); // Πρέπει να υλοποιηθεί
    }
}
