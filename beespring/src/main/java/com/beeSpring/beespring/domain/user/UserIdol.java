package com.beeSpring.beespring.domain.user;

import com.beeSpring.beespring.domain.category.Idol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="User_Idol")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserIdol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idol_id")
    private int userIdolId;

    @ManyToOne
    @JoinColumn(name="serial_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idol_id", nullable = false)
    private Idol idol;
}
