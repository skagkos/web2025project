package diplomatiki.service;

import diplomatiki.entity.User;
import diplomatiki.Repositories.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Ο χρήστης δεν βρέθηκε: " + username));

        String authority = user.getRole().authority(); // π.χ. ROLE_PROFESSOR
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(grantedAuthority));
    }
}
