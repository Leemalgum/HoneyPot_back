package com.beeSpring.beespring.domain.user;

import com.beeSpring.beespring.domain.category.Idol;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="User_idol")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIdol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idol_id")
    private Integer userIdolId;

    @ManyToOne
    @JoinColumn(name="serial_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idol_id", nullable = false)
    private Idol idol;
}
