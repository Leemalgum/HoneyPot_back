package com.beeSpring.beespring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManageUserDTO {
    private String serialNumber;
    private String userId;
    private String gender;
    private LocalDate birthdate;
    private LocalDateTime registrationDate;
    private String state;
}
