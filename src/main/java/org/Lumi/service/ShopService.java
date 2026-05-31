package org.Lumi.service;

import lombok.RequiredArgsConstructor;
import org.Lumi.model.Inventory;
import org.Lumi.model.Player;
import org.Lumi.model.Purchase;
import org.Lumi.model.Skin;
import org.Lumi.repo.InventoryRepository;
import org.Lumi.repo.PlayerRepository;
import org.Lumi.repo.PurchaseRepository;
import org.Lumi.repo.SkinRepository;
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
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Skin skin = skinRepository.findById(skinId)
                .orElseThrow(() -> new RuntimeException("Skin not found"));

        if (player.getVpBalance().compareTo(skin.getPriceVp()) < 0) {
            throw new RuntimeException("Not enough VP");
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

