package com.josephnzi.monade.service;

import com.josephnzi.monade.model.User;
import com.josephnzi.monade.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
public class UserDetailsServiceImpli implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);
        User user = userOptional.orElseThrow(()->new UsernameNotFoundException("No User found"+
                "with the given username: "+ username));
        return  new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                true,true,true,getAuthority("USER"));

    }

    private Collection<? extends GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
