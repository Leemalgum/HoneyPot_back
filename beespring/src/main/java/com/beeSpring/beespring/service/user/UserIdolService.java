package com.beeSpring.beespring.service.user;

import com.beeSpring.beespring.domain.category.Idol;
import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.domain.user.UserIdol;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;
import com.beeSpring.beespring.dto.user.UserIdolDTO;
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
    private final UserRepository userRepository;
    private final IdolRepository idolRepository;
    private final UserIdolRepository userIdolRepository;

    @Transactional
    public void saveUserIdol(UserIdolDTO userIdolDTO) {
        User user = userRepository.findById(userIdolDTO.getSerialNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user serial number"));
        Idol idol = idolRepository.findByIdolId(userIdolDTO.getIdolId());

        UserIdol userIdol = new UserIdol(
                userIdolDTO.getUserIdolId(),
                user,
                idol
                );

        userIdolRepository.save(userIdol);
    }
}

