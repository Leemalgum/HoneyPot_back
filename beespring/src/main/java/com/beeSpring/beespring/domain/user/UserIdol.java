package com.beeSpring.beespring.domain.user;

import com.beeSpring.beespring.domain.category.Idol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="User_Idol")
@Getter
@NoArgsConstructor
public class UserIdol {
    @Id
    @Column(name="user_idol_id")
    private int userIdolId;

    @ManyToOne
    @JoinColumn(name="serial_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idol_id", nullable = false)
    private Idol idol;
}
