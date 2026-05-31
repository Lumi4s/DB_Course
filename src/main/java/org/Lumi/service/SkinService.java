package org.Lumi.service;

import org.Lumi.model.Skin;
import org.Lumi.repo.SkinRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkinService {

    private final SkinRepository skinRepository;

    public SkinService(SkinRepository skinRepository) {
        this.skinRepository = skinRepository;
    }

    public List<Skin> getAllSkins() {
        return skinRepository.findAll();
    }

    public Optional<Skin> getSkinById(Integer id) {
        return skinRepository.findById(id);
    }

    public Skin createSkin(Skin skin) {
        return skinRepository.save(skin);
    }

    public Skin updateSkin(Integer id, Skin skinDetails) {
        Skin skin = skinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skin not found"));

        if (skinDetails.getSkinName() != null) {
            skin.setSkinName(skinDetails.getSkinName());
        }
        if (skinDetails.getWeaponType() != null) {
            skin.setWeaponType(skinDetails.getWeaponType());
        }
        if (skinDetails.getRarity() != null) {
            skin.setRarity(skinDetails.getRarity());
        }
        if (skinDetails.getPriceVp() != null) {
            skin.setPriceVp(skinDetails.getPriceVp());
        }

        return skinRepository.save(skin);
    }

    public void deleteSkin(Integer id) {
        skinRepository.deleteById(id);
    }
}

