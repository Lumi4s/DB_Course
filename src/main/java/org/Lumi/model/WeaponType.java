package org.Lumi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "weapon_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeaponType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weapon_id")
    private Integer id;

    @NotNull
    @Column(name = "weapon_name", nullable = false, length = 50)
    private String weaponName;

    @NotNull
    @Column(name = "category", nullable = false, length = 50)
    private String category;
}

