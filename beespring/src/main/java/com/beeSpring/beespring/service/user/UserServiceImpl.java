package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDTO userDTO;

}
