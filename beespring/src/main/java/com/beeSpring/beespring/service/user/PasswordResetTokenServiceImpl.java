package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.PasswordResetToken;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.PasswordResetTokenRepository;
import com.beeSpring.beespring.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private static final int EXPIRATION = 60 * 24; // 24 hours
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public String createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUserId(user.getUserId());
        myToken.setExpiryDate(calculateExpiryDate(EXPIRATION));
        tokenRepository.save(myToken);
        return token;
    }

    @Override
    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken == null) {
            return "invalidToken";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }

        return null;
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken != null) {
            return userRepository.findByProviderAndUserId("service", passToken.getUserId()).orElse(null);
        }
        return null;
    }

    @Override
    public void invalidateToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        if (passToken != null) {
            tokenRepository.delete(passToken);
        }
    }
}
