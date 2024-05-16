package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.user.UserTokens;

public interface UserService {
    void registerUser(User user);
    User findUserByUserId(String userId);
    void saveUserTokens(UserTokens userTokens);
    UserTokens findUserTokensById(String userTokensId);
    void updateUser(User user);
    void deleteUser(String userId);
}
