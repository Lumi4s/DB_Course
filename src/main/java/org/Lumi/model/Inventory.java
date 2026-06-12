package org.Lumi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "skin_id", nullable = false)
    private Skin skin;

    @Column(name = "is_equipped")
    private boolean isEquipped = false;
}

