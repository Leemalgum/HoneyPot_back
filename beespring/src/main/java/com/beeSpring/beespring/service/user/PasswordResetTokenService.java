package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.User;

import java.util.Date;


public interface PasswordResetTokenService {
    String createPasswordResetTokenForUser(User user);

    Date calculateExpiryDate(int expiryTimeInMinutes);

    String validatePasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    void invalidateToken(String token);
}
