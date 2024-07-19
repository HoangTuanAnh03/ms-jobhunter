package com.tuananh.authservice.security;

import com.tuananh.authservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component("userDetailsService")
@Slf4j
public class UserDetailsCustom implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsCustom(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.tuananh.authservice.entity.User> optionalUser = this.userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not existed");
        }

//        log.info(Collections.singletonList(new SimpleGrantedAuthority(optionalUser.get().getRole().getName())).toString());

        return new User(
                optionalUser.get().getEmail(),
                optionalUser.get().getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority(optionalUser.get().getRole().getName())));
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
    }

}
