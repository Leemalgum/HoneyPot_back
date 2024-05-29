package com.beeSpring.beespring.dto.user;

import com.beeSpring.beespring.domain.user.State;
import lombok.Data;

@Data
public class UpdateUserStateRequest {
    private String userId;
    private State state;
}
