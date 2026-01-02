package com.example.loopa.security;

import com.example.loopa.entity.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BlockedUserAspect {

    @Before("@annotation(com.example.loopa.annotation.CheckBlocked)")
    public void check(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (user.isBlocked()) {
            throw new AccessDeniedException("Sizning amallaringiz vaqtinchalik cheklangan!");
        }
    }
}
