package org.Lumi.service;

import org.Lumi.model.Inventory;
import org.Lumi.model.Player; // Предполагаю, что модель Player существует
import org.Lumi.model.Skin;   // Предполагаю, что модель Skin существует
import org.Lumi.repo.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory sampleInventory;

    @BeforeEach
    void setUp() {
        sampleInventory = new Inventory();
        sampleInventory.setId(1);
        sampleInventory.setIsEquipped(false);
    }

    @Test
    void getInventoryById_ShouldReturnInventory_WhenIdExists() {
        // Arrange
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(sampleInventory));

        // Act
        Optional<Inventory> result = inventoryService.getInventoryById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getInventoryById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        // Arrange
        when(inventoryRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Optional<Inventory> result = inventoryService.getInventoryById(99);

        // Assert
        assertTrue(result.isEmpty(), "Result should be empty, if ID wasn't found");
    }
    @Test
    void updateInventory_ShouldUpdateFields_WhenDetailsProvided() {
        // Arrange
        Inventory existingInventory = new Inventory();
        existingInventory.setId(1);
        existingInventory.setIsEquipped(false);

        Inventory updatedDetails = new Inventory();
        updatedDetails.setIsEquipped(true);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Inventory result = inventoryService.updateInventory(1, updatedDetails);

        // Assert
        assertTrue(result.getIsEquipped());
        verify(inventoryRepository).save(existingInventory);
    }

    @Test
    void updateInventory_ShouldNotOverwriteWithNull_WhenFieldsAreMissing() {
        // Arrange
        Player player = new Player();
        Inventory existingInventory = new Inventory();
        existingInventory.setId(1);
        existingInventory.setPlayer(player);

        Inventory updatedDetails = new Inventory();
        updatedDetails.setIsEquipped(true); 

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Inventory result = inventoryService.updateInventory(1, updatedDetails);

        // Assert
        assertTrue(result.getIsEquipped());
        assertNotNull(result.getPlayer(), "Player shouldn't transform into null!");
        assertEquals(player, result.getPlayer());
    }

    @Test
    void deleteInventory_ShouldCallRepositoryDelete() {
        // Act
        inventoryService.deleteInventory(1);

        // Assert
        verify(inventoryRepository, times(1)).deleteById(1);
    }
}