package com.beeSpring.beespring.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name="User_Idol")
public class UserIdol {
    @Id
    @Column(name="user_idol_id")
    private int userIdolId;
    @Column(name="serial_number")
    private String serialNumber;
    @Column(name="idol_id")
    private int idolId;
}
