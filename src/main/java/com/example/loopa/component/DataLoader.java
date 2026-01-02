package com.example.loopa.component;

import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Role;
import com.example.loopa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;


    @Override
    public void run(String... args) throws Exception {
        if (ddl.equals("create") || ddl.equals("create-drop")){
            User admin = User.builder()
                    .role(Role.ADMIN)
                    .chatId("12345")
                    .username("adminjon")
                    .isBlocked(false)
                    .newUser(false)
                    .build();

            userRepository.save(admin);
        }
    }
}
