package org.Lumi.controllers;

import lombok.RequiredArgsConstructor;
import org.Lumi.model.WeaponType;
import org.Lumi.service.WeaponTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weapon-types")
@RequiredArgsConstructor
public class WeaponTypeController {

    private final WeaponTypeService weaponTypeService;

    @GetMapping
    public List<WeaponType> getAllWeaponTypes() {
        return weaponTypeService.getAllWeaponTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeaponType> getWeaponTypeById(@PathVariable Integer id) {
        return weaponTypeService.getWeaponTypeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WeaponType> createWeaponType(@RequestBody WeaponType weaponType) {
        return ResponseEntity.ok(weaponTypeService.createWeaponType(weaponType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeaponType> updateWeaponType(@PathVariable Integer id, @RequestBody WeaponType weaponType) {
        try {
            return ResponseEntity.ok(weaponTypeService.updateWeaponType(id, weaponType));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeaponType(@PathVariable Integer id) {
        weaponTypeService.deleteWeaponType(id);
        return ResponseEntity.noContent().build();
    }
}
