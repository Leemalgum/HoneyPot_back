package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@RequiredArgsConstructor
@Service
public class UserIdolService {
    private final UserRepository userRepository;

    @Transactional
    public void saveUserIdols(String serialNumber, List<Integer> idolIds) {
        User user = userRepository.findById(serialNumber).orElseThrow(() -> new IllegalArgumentException("Invalid serial number"));

        user.setTag1(String.valueOf(idolIds.get(0)));
        user.setTag2(String.valueOf(idolIds.get(1)));
        user.setTag3(String.valueOf(idolIds.get(2)));

        userRepository.save(user);
    }
}