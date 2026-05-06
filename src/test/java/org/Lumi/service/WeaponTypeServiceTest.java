package org.Lumi.service;

import org.Lumi.model.WeaponType;
import org.Lumi.repo.WeaponTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeaponTypeServiceTest {
    @Mock
    private WeaponTypeRepository weaponTypeRepository;

    @InjectMocks
    private WeaponTypeService weaponTypeService;

    private WeaponType sampleWeaponType;

    @BeforeEach
    void setUp() {
        sampleWeaponType = new WeaponType();
        sampleWeaponType.setId(5);
        sampleWeaponType.setWeaponName("Operator");
        sampleWeaponType.setCategory("Sniper Rifles");
    }

    @Test
    void getAllWeaponTypes_ShouldReturnList() {
        // Arrange
        when(weaponTypeRepository.findAll()).thenReturn(Arrays.asList(sampleWeaponType, new WeaponType()));

        // Act
        List<WeaponType> result = weaponTypeService.getAllWeaponTypes();

        // Assert
        assertEquals(2, result.size());
        verify(weaponTypeRepository).findAll();
    }

    @Test
    void getWeaponTypeById_ShouldReturnWeaponType_WhenIdExists() {
        // Arrange
        when(weaponTypeRepository.findById(1)).thenReturn(Optional.of(sampleWeaponType));

        // Act
        Optional<WeaponType> result = weaponTypeService.getWeaponTypeById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Sniper Rifles", result.get().getCategory());
    }

    @Test
    void createWeaponType_ShouldReturnSavedWeaponType() {
        // Arrange
        when(weaponTypeRepository.save(any(WeaponType.class))).thenReturn(sampleWeaponType);

        // Act
        WeaponType result = weaponTypeService.createWeaponType(sampleWeaponType);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getId());
        verify(weaponTypeRepository).save(sampleWeaponType);
    }

    @Test
    void updateWeaponType_ShouldUpdateAllFields_WhenDetailsProvided() {
        // Arrange
        WeaponType updateDetails= new WeaponType();
        updateDetails.setWeaponName("Vandal");
        updateDetails.setCategory("Rifles");

        when(weaponTypeRepository.findById(5)).thenReturn(Optional.of(sampleWeaponType));
        when(weaponTypeRepository.save(any(WeaponType.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        WeaponType result = weaponTypeService.updateWeaponType(5, updateDetails);

        // Assert
        assertEquals("Vandal", result.getWeaponName());
        assertEquals("Rifles", result.getCategory());
        verify(weaponTypeRepository).save(sampleWeaponType);
    }

    @Test
    void updateWeaponType_ShouldNotOverwriteUpdateAllFields_WhenFieldsAreMissing() {
        // Arrange
        WeaponType updateDetails = new WeaponType();
        updateDetails.setWeaponName("Marshall");

        when(weaponTypeRepository.findById(5)).thenReturn(Optional.of(sampleWeaponType));
        when(weaponTypeRepository.save(any(WeaponType.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        WeaponType result = weaponTypeService.updateWeaponType(5, updateDetails);

        // Assert
        assertEquals("Marshall", result.getWeaponName());
        assertEquals("Sniper Rifles", result.getCategory(), "Category should remain unchanged");
    }

    @Test
    void deleteWeaponType_ShouldCallRepositoryDelete() {
        // Act
        weaponTypeService.deleteWeaponType(5);

        // Assert
        verify(weaponTypeRepository, times(1)).deleteById(5);
    }
}
