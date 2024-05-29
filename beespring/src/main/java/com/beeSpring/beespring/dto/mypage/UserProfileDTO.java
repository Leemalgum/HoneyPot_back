package com.beeSpring.beespring.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileDTO {
    private String profileImage;
    private String nickname;
    private String userAccount;
    private String serialNumber;
    private List<String> idolNames;
}
