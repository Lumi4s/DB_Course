package org.Lumi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "skin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skin_id")
    private Integer id;

    @NotNull
    @Column(name = "skin_name", nullable = false, length = 100)
    private String skinName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "weapon_id", nullable = false)
    private WeaponType weaponType;

    @NotNull
    @Column(name = "rarity", length = 30)
    private String rarity;

    @PositiveOrZero
    @Column(name = "price_vp")
    private BigDecimal priceVp;
}

