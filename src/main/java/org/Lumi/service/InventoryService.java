package org.Lumi.service;

import org.Lumi.model.Inventory;
import org.Lumi.repo.InventoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Integer id) {
        return inventoryRepository.findById(id);
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Integer id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        
        if (inventoryDetails.getPlayer() != null) {
            inventory.setPlayer(inventoryDetails.getPlayer());
        }
        if (inventoryDetails.getSkin() != null) {
            inventory.setSkin(inventoryDetails.getSkin());
        }
        inventory.setIsEquipped(inventoryDetails.getIsEquipped());
        
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Integer id) {
        inventoryRepository.deleteById(id);
    }
}
