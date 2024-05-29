package com.beeSpring.beespring.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ManageUserProjection {
    String getSerialNumber();
    String getUserId();
    String getGender();
    LocalDate getBirthdate();
    LocalDateTime getRegistrationDate();
    String getState();
}
