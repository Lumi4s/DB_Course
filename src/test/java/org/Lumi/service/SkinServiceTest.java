package org.Lumi.service;

import org.Lumi.model.Skin;
import org.Lumi.repo.SkinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkinServiceTest {

    @Mock
    private SkinRepository skinRepository;

    @InjectMocks
    private SkinService skinService;

    private Skin sampleSkin;

    @BeforeEach
    void setUp() {
        sampleSkin = new Skin();
        sampleSkin.setId(10);
        sampleSkin.setSkinName("Default Skin");
        sampleSkin.setRarity("Common");
        sampleSkin.setPriceVp(new BigDecimal("500.00"));

    }

    @Test
    void getAllSkins_ShouldReturnList() {
        // Arrange
        when(skinRepository.findAll()).thenReturn(Arrays.asList(sampleSkin, new Skin()));

        // Act
        List<Skin> result = skinService.getAllSkins();

        // Assert
        assertEquals(2, result.size());
        verify(skinRepository).findAll();
    }

    @Test
    void getSkinById_ShouldReturnSkin_WhenIdExists() {
        // Arrange
        when(skinRepository.findById(10)).thenReturn(Optional.of(sampleSkin));

        // Act
        Optional<Skin> result = skinService.getSkinById(10);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Default Skin", result.get().getSkinName());
    }

    @Test
    void createSkin_ShouldReturnSavedSkin() {
        // Arrange
        when(skinRepository.save(any(Skin.class))).thenReturn(sampleSkin);

        // Act
        Skin result = skinService.createSkin(sampleSkin);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getId());
        verify(skinRepository).save(sampleSkin);
    }

    @Test
    void updateSkin_ShouldUpdateAllFields_WhenDetailsProvided() {
        // Arrange
        Skin updateDetails = new Skin();
        updateDetails.setSkinName("Legendary Skin");
        updateDetails.setRarity("Legendary");
        updateDetails.setPriceVp(new BigDecimal("2000.00"));

        when(skinRepository.findById(10)).thenReturn(Optional.of(sampleSkin));
        when(skinRepository.save(any(Skin.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Skin result = skinService.updateSkin(10, updateDetails);

        // Assert
        assertEquals("Legendary Skin", result.getSkinName());
        assertEquals("Legendary", result.getRarity());
        assertEquals(new BigDecimal("2000.00"), result.getPriceVp());
        verify(skinRepository).save(sampleSkin);
    }

    @Test
    void updateSkin_ShouldNotOverwriteWithNull_WhenFieldsAreMissing() {
        // Arrange
        Skin updateDetails = new Skin();
        updateDetails.setSkinName("Only Name Changed");

        when(skinRepository.findById(10)).thenReturn(Optional.of(sampleSkin));
        when(skinRepository.save(any(Skin.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Skin result = skinService.updateSkin(10, updateDetails);

        // Assert
        assertEquals("Only Name Changed", result.getSkinName());
        assertEquals("Common", result.getRarity(), "Rarity should remain unchanged");
        assertEquals(new BigDecimal("500.00"), result.getPriceVp(), "Price should remain unchanged");
    }

    @Test
    void updateSkin_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(skinRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            skinService.updateSkin(99, new Skin());
        });

        assertEquals("Skin not found", exception.getMessage());
    }

    @Test
    void deleteSkin_ShouldCallRepositoryDelete() {
        // Act
        skinService.deleteSkin(10);

        // Assert
        verify(skinRepository, times(1)).deleteById(10);
    }
}