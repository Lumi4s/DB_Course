package org.Lumi.controllers;

import lombok.RequiredArgsConstructor;
import org.Lumi.model.Skin;
import org.Lumi.service.SkinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skins")
@RequiredArgsConstructor
public class SkinController {

    private final SkinService skinService;

    @GetMapping
    public List<Skin> getAllSkins() {
        return skinService.getAllSkins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skin> getSkinById(@PathVariable Integer id) {
        return skinService.getSkinById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Skin> createSkin(@RequestBody Skin skin) {
        return ResponseEntity.ok(skinService.createSkin(skin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skin> updateSkin(@PathVariable Integer id, @RequestBody Skin skin) {
        try {
            return ResponseEntity.ok(skinService.updateSkin(id, skin));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkin(@PathVariable Integer id) {
        skinService.deleteSkin(id);
        return ResponseEntity.noContent().build();
    }
}
