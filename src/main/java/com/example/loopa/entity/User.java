package com.example.loopa.entity;

import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(nullable = false, unique = true)
    private String chatId;

    private String tgUsername;

    private String phone;

    @Builder.Default
    private boolean newUser = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    private boolean premium;

    @Builder.Default
    private boolean isBlocked = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_favourite_categories", joinColumns = @JoinColumn(name = "user_chat_id"))
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private List<Category> favouriteCategories;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return chatId; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return !isBlocked; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
