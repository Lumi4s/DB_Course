package org.Lumi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Lumi.model.Skin;
import org.Lumi.model.WeaponType;
import org.Lumi.service.SkinService;
import org.Lumi.service.WeaponTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SkinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SkinService skinService;

    @Autowired
    private WeaponTypeService weaponTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    private WeaponType testWeaponType;
    private Skin testSkin;

    @BeforeEach
    void setUp() {
        testWeaponType = new WeaponType();
        testWeaponType.setWeaponName("Vandal");
        testWeaponType.setCategory("Rifle");
        testWeaponType = weaponTypeService.createWeaponType(testWeaponType);

        testSkin = new Skin();
        testSkin.setSkinName("Prime Vandal");
        testSkin.setWeaponType(testWeaponType);
        testSkin.setRarity("Premium");
        testSkin.setPriceVp(new BigDecimal("2500"));
    }

    @Test
    void createSkin_Success() throws Exception {
        mockMvc.perform(post("/api/skins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSkin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skinName").value("Prime Vandal"))
                .andExpect(jsonPath("$.weaponType.id").value(testWeaponType.getId()));
    }

    @Test
    void getAllSkins_Success() throws Exception {
        skinService.createSkin(testSkin);

        mockMvc.perform(get("/api/skins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getSkinById_Found() throws Exception {
        Skin saved = skinService.createSkin(testSkin);

        mockMvc.perform(get("/api/skins/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void getSkinById_NotFound() throws Exception {
        mockMvc.perform(get("/api/skins/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSkin_Success() throws Exception {
        Skin saved = skinService.createSkin(testSkin);
        saved.setSkinName("Prime Vandal Updated");

        mockMvc.perform(put("/api/skins/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skinName").value("Prime Vandal Updated"));
    }

    @Test
    void updateSkin_NotFound() throws Exception {
        mockMvc.perform(put("/api/skins/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSkin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSkin_Success() throws Exception {
        Skin saved = skinService.createSkin(testSkin);

        mockMvc.perform(delete("/api/skins/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/skins/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
