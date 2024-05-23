package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.category.Idol;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.domain.user.UserIdol;
import com.beeSpring.beespring.repository.category.IdolRepository;
import com.beeSpring.beespring.repository.user.UserIdolRepository;
import com.beeSpring.beespring.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@RequiredArgsConstructor
@Service
public class UserIdolService {
    private final UserIdolRepository userIdolRepository;
    private final UserRepository userRepository;
    private final IdolRepository idolRepository;

    @Transactional
    public void saveUserIdols(String serialNumber, List<Integer> idolIds) {
        User user = userRepository.findById(serialNumber).orElseThrow(() -> new IllegalArgumentException("Invalid serial number"));

        //userIdolRepository.deleteBySerialNumber(serialNumber);

        // 새로운 아이돌 선택 데이터 삽입 및 User 엔티티의 tag1, tag2, tag3 업데이트
        for (int i = 0; i < idolIds.size(); i++) {
            Integer idolId = idolIds.get(i);
            Idol idol = idolRepository.findById(idolId).orElseThrow(() -> new IllegalArgumentException("Invalid idol ID"));

            UserIdol userIdol = new UserIdol();
            userIdol.setUser(user);
            userIdol.setIdol(idol);
            userIdolRepository.save(userIdol);

            // Update tag1, tag2, tag3 in User entity
            if (i == 0) {
                user.setTag1(String.valueOf(idolId));
            } else if (i == 1) {
                user.setTag2(String.valueOf(idolId));
            } else if (i == 2) {
                user.setTag3(String.valueOf(idolId));
            }
        }

        userRepository.save(user); // Save the user entity with updated tags
    }
}

