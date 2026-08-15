package com.deshkar.security;

import com.deshkar.code.service.CodeService;
import com.deshkar.model.Users;
import com.deshkar.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    private final CodeService codeService;

    public CustomUserDetailsService(UserRepo userRepo, CodeService codeService) {
        this.userRepo = userRepo;
        this.codeService = codeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(codeService.getCode(u.getRole()).getCode()))
        );
    }
}