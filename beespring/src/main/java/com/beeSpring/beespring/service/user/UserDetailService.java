package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailService {
    public UserDetails loadUserByUserId(String userId);
}
