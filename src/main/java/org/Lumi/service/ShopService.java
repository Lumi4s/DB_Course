package org.Lumi.service;

import lombok.RequiredArgsConstructor;
import org.Lumi.model.*;
import org.Lumi.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final PlayerRepository playerRepository;
    private final SkinRepository skinRepository;
    private final PurchaseRepository purchaseRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * @param playerId
     * @param skinId
     * @throws RuntimeException 
     */
    @Transactional
    public void purchaseSkin(Integer playerId, Integer skinId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Игрок не найден"));
        
        Skin skin = skinRepository.findById(skinId)
                .orElseThrow(() -> new RuntimeException("Скин не найден"));

        if (player.getVpBalance().compareTo(skin.getPriceVp()) < 0) {
            throw new RuntimeException("Недостаточно VP для покупки данного скина!");
        }

        player.setVpBalance(player.getVpBalance().subtract(skin.getPriceVp()));
 
        Purchase purchase = new Purchase();
        purchase.setPlayer(player);
        purchase.setSkin(skin);
        purchaseRepository.save(purchase);

        Inventory inventory = new Inventory();
        inventory.setPlayer(player);
        inventory.setSkin(skin);
        inventory.setIsEquipped(false);
        inventoryRepository.save(inventory);
    }
}

