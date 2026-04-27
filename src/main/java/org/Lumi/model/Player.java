package org.Lumi.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PLAYER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Integer id;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "level")
    private Integer level = 1;

    @Column(name = "vp_balance")
    private BigDecimal vpBalance = BigDecimal.ZERO;
}

