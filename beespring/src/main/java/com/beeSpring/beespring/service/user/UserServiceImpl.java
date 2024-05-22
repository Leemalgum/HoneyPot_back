/*
package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.user.UserTokens;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.repository.user.UserTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTokensRepository userTokensRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User user) {
        // 비밀번호 암호화 및 사용자 저장
        userRepository.save(user);
    }

    @Override
    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public void saveUserTokens(UserTokens userTokens) {
        userTokensRepository.save(userTokens);
    }

    @Override
    public UserTokens findUserTokensById(String userTokensId) {
        return userTokensRepository.findById(userTokensId).orElse(null);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUserId(user.getUserId());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}

*/
