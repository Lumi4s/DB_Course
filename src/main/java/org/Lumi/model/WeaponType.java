package org.Lumi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WEAPON_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeaponType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weapon_id")
    private Integer id;

    @Column(name = "weapon_name", nullable = false, length = 50)
    private String weaponName;

    @Column(name = "category", length = 50)
    private String category;
}

