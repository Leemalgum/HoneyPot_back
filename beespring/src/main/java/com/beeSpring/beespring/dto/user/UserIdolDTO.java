package com.beeSpring.beespring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdolDTO {
    private int userIdolId;
    private String serialNumber;
    private int idolId;
}
