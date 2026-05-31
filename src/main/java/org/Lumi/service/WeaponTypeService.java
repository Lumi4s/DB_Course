package org.Lumi.service;

import org.Lumi.model.WeaponType;
import org.Lumi.repo.WeaponTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeaponTypeService {

    private final WeaponTypeRepository weaponTypeRepository;

    public WeaponTypeService(WeaponTypeRepository weaponTypeRepository) {
        this.weaponTypeRepository = weaponTypeRepository;
    }

    public List<WeaponType> getAllWeaponTypes() {
        return weaponTypeRepository.findAll();
    }

    public Optional<WeaponType> getWeaponTypeById(Integer id) {
        return weaponTypeRepository.findById(id);
    }

    public WeaponType createWeaponType(WeaponType weaponType) {
        return weaponTypeRepository.save(weaponType);
    }

    public WeaponType updateWeaponType(Integer id, WeaponType weaponTypeDetails) {
        WeaponType weaponType = weaponTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeaponType not found"));

        if (weaponTypeDetails.getWeaponName() != null) {
            weaponType.setWeaponName(weaponTypeDetails.getWeaponName());
        }
        if (weaponTypeDetails.getCategory() != null) {
            weaponType.setCategory(weaponTypeDetails.getCategory());
        }

        return weaponTypeRepository.save(weaponType);
    }

    public void deleteWeaponType(Integer id) {
        weaponTypeRepository.deleteById(id);
    }
}

