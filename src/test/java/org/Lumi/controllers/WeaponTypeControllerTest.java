package org.Lumi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Lumi.model.WeaponType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class WeaponTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeaponTypeService weaponTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    private WeaponType testWeaponType;

    @BeforeEach
    void setUp() {
        testWeaponType = new WeaponType();
        testWeaponType.setWeaponName("Phantom");
        testWeaponType.setCategory("Rifle");
    }

    @Test
    void createWeaponType_Success() throws Exception {
        mockMvc.perform(post("/api/weapon-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testWeaponType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weaponName").value("Phantom"))
                .andExpect(jsonPath("$.category").value("Rifle"));
    }

    @Test
    void getAllWeaponTypes_Success() throws Exception {
        weaponTypeService.createWeaponType(testWeaponType);

        mockMvc.perform(get("/api/weapon-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getWeaponTypeById_Found() throws Exception {
        WeaponType saved = weaponTypeService.createWeaponType(testWeaponType);

        mockMvc.perform(get("/api/weapon-types/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void getWeaponTypeById_NotFound() throws Exception {
        mockMvc.perform(get("/api/weapon-types/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWeaponType_Success() throws Exception {
        WeaponType saved = weaponTypeService.createWeaponType(testWeaponType);
        saved.setWeaponName("Updated Phantom");
        saved.setCategory("Advanced Rifle");

        mockMvc.perform(put("/api/weapon-types/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weaponName").value("Updated Phantom"))
                .andExpect(jsonPath("$.category").value("Advanced Rifle"));
    }

    @Test
    void updateWeaponType_NotFound() throws Exception {
        mockMvc.perform(put("/api/weapon-types/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testWeaponType)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWeaponType_Success() throws Exception {
        WeaponType saved = weaponTypeService.createWeaponType(testWeaponType);

        mockMvc.perform(delete("/api/weapon-types/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/weapon-types/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
