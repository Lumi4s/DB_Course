package org.Lumi.service;

import org.Lumi.model.*;
import org.Lumi.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private SkinRepository skinRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ShopService shopService;

    private Player samplePlayer;
    private Skin sampleSkin;

    @BeforeEach
    void setUp() {
        samplePlayer = new Player();
        samplePlayer.setId(1);
        samplePlayer.setNickname("ProGamer");
        samplePlayer.setVpBalance(new BigDecimal("1000.00"));

        sampleSkin = new Skin();
        sampleSkin.setId(50);
        sampleSkin.setSkinName("Prime Skin");
        sampleSkin.setPriceVp(new BigDecimal("400.00"));
    }

    @Test
    void purchaseSkin_ShouldSucceed_WhenBalanceIsEnough() {
        // Arrange
        when(playerRepository.findById(1)).thenReturn(Optional.of(samplePlayer));
        when(skinRepository.findById(50)).thenReturn(Optional.of(sampleSkin));

        // Act
        shopService.purchaseSkin(1, 50);

        // Assert
        assertEquals(new BigDecimal("600.00"), samplePlayer.getVpBalance());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void purchaseSkin_ShouldThrowException_WhenPlayerNotFound() {
        // Arrange
        when(playerRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shopService.purchaseSkin(99, 50);
        });

        assertEquals("Player not found", exception.getMessage());
        verifyNoInteractions(skinRepository, purchaseRepository, inventoryRepository);
    }

    @Test
    void purchaseSkin_ShouldThrowException_WhenSkinNotFound() {
        // Arrange
        when(playerRepository.findById(1)).thenReturn(Optional.of(samplePlayer));
        when(skinRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shopService.purchaseSkin(1, 99);
        });

        assertEquals("Skin not found", exception.getMessage());
        verifyNoInteractions(purchaseRepository, inventoryRepository);
    }

    @Test
    void purchaseSkin_ShouldThrowException_WhenInsufficientBalance() {
        // Arrange
        samplePlayer.setVpBalance(new BigDecimal("100.00"));
        when(playerRepository.findById(1)).thenReturn(Optional.of(samplePlayer));
        when(skinRepository.findById(50)).thenReturn(Optional.of(sampleSkin));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shopService.purchaseSkin(1, 50);
        });

        assertEquals("Not enough VP", exception.getMessage());

        assertEquals(new BigDecimal("100.00"), samplePlayer.getVpBalance());
        verifyNoInteractions(purchaseRepository, inventoryRepository);
    }
}