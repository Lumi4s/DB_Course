package org.Lumi.service;

import org.Lumi.model.Player;
import org.Lumi.model.Purchase;
import org.Lumi.model.Skin;
import org.Lumi.repo.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    private Purchase samplePurchase;
    private Player samplePlayer;
    private Skin sampleSkin;

    @BeforeEach
    void setUp() {
        samplePlayer = new Player();
        samplePlayer.setId(1);
        samplePlayer.setNickname("TestPlayer");

        sampleSkin = new Skin();
        sampleSkin.setId(10);
        sampleSkin.setSkinName("Elderflame");

        samplePurchase = new Purchase();
        samplePurchase.setId(100);
        samplePurchase.setPlayer(samplePlayer);
        samplePurchase.setSkin(sampleSkin);
        samplePurchase.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void getAllPurchases_ShouldReturnList() {
        // Arrange
        when(purchaseRepository.findAll()).thenReturn(Arrays.asList(samplePurchase, new Purchase()));

        // Act
        List<Purchase> result = purchaseService.getAllPurchases();

        // Assert
        assertEquals(2, result.size());
        verify(purchaseRepository, times(1)).findAll();
    }

    @Test
    void getPurchaseById_ShouldReturnPurchase_WhenIdExists() {
        // Arrange
        when(purchaseRepository.findById(100)).thenReturn(Optional.of(samplePurchase));

        // Act
        Optional<Purchase> result = purchaseService.getPurchaseById(100);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(100, result.get().getId());
    }

    @Test
    void createPurchase_ShouldReturnSavedPurchase() {
        // Arrange
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(samplePurchase);

        // Act
        Purchase result = purchaseService.createPurchase(samplePurchase);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getId());
        verify(purchaseRepository).save(samplePurchase);
    }

    @Test
    void updatePurchase_ShouldUpdateFields_WhenDetailsProvided() {
        // Arrange
        LocalDateTime oldDate = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime newDate = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        samplePurchase.setTransactionDate(oldDate);

        Player newPlayer = new Player();
        newPlayer.setId(2);
        newPlayer.setNickname("NewPlayer");

        Skin newSkin = new Skin();
        newSkin.setId(20);
        newSkin.setSkinName("NewSkinName");

        Purchase updateDetails = new Purchase();
        updateDetails.setPlayer(newPlayer);
        updateDetails.setSkin(newSkin);
        updateDetails.setTransactionDate(newDate);

        when(purchaseRepository.findById(100)).thenReturn(Optional.of(samplePurchase));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Purchase result = purchaseService.updatePurchase(100, updateDetails);

        // Assert
        assertEquals(2, result.getPlayer().getId());
        assertEquals("NewPlayer", result.getPlayer().getNickname());
        assertEquals(20, result.getSkin().getId());
        assertEquals("NewSkinName", result.getSkin().getSkinName());
        assertEquals(newDate, result.getTransactionDate());
        verify(purchaseRepository).save(samplePurchase);
    }

    @Test
    void updatePurchase_ShouldNotOverwriteWithNull_WhenFieldsAreMissing() {
        // Arrange
        Purchase updateDetails = new Purchase();
        updateDetails.setTransactionDate(LocalDateTime.now().plusDays(5));

        when(purchaseRepository.findById(100)).thenReturn(Optional.of(samplePurchase));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Purchase result = purchaseService.updatePurchase(100, updateDetails);

        // Assert
        assertEquals(samplePlayer.getId(), result.getPlayer().getId());
        assertEquals(sampleSkin.getId(), result.getSkin().getId());
        assertEquals(updateDetails.getTransactionDate(), result.getTransactionDate());
    }

    @Test
    void updatePurchase_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(purchaseRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            purchaseService.updatePurchase(999, new Purchase());
        });

        assertEquals("Purchase not found", exception.getMessage());
    }

    @Test
    void deletePurchase_ShouldCallRepositoryDelete() {
        // Act
        purchaseService.deletePurchase(100);

        // Assert
        verify(purchaseRepository, times(1)).deleteById(100);
    }
}