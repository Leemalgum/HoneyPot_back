package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByProviderAndProviderId(String provider, String providerId);
}

