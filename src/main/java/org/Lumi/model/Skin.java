package org.Lumi.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SKIN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skin_id")
    private Integer id;

    @Column(name = "skin_name", nullable = false, length = 100)
    private String skinName;

    @ManyToOne
    @JoinColumn(name = "weapon_id", nullable = false)
    private WeaponType weaponType;

    @Column(name = "rarity", length = 30)
    private String rarity;

    @Column(name = "price_vp")
    private BigDecimal priceVp;
}

