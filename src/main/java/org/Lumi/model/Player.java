package org.Lumi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "player")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Integer id;

    @NotNull
    @Column(name = "nickname", nullable = false, length = 50, unique = true)
    private String nickname;

    @PositiveOrZero
    @Column(name = "level")
    private Integer level;

    @PositiveOrZero
    @Column(name = "vp_balance")
    private BigDecimal vpBalance;
}

