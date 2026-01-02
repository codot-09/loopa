package com.example.loopa.security;

import com.example.loopa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String chatId) throws UsernameNotFoundException {
        return userRepository.findById(chatId)
                .orElseThrow(() -> new UsernameNotFoundException("User topilmadi: " + chatId));
    }
}