package com.beeSpring.beespring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FindUserIdRequestDTO {
    private String name;
    private String mobileNumber;
}
