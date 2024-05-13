package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.exception.UserIdNotFoundException;
import com.beeSpring.beespring.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailServiceImpl implements UserDetailService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUserId(String userId) throws UserIdNotFoundException {
        System.out.println(userId);
        User user = userRepository.findByUserId(userId);
        if(user==null) {
            throw new UserIdNotFoundException("해당 유저가 존재하지 않습니다.");
        }
        return new PrincipalDetail(user);
    }
}
