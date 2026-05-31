package org.Lumi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Lumi.model.Inventory;
import org.Lumi.model.Player;
import org.Lumi.model.Skin;
import org.Lumi.model.WeaponType;
import org.Lumi.service.InventoryService;
import org.Lumi.service.PlayerService;
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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SkinService skinService;

    @Autowired
    private WeaponTypeService weaponTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Player testPlayer;
    private Skin testSkin;

    @BeforeEach
    void setUp() {
        testPlayer = createTestPlayer();
        testSkin = createTestSkin();
    }

    @Test
    void createInventory_Success() throws Exception {
        Map<String, Object> inventoryRequest = new HashMap<>();
        inventoryRequest.put("player", Map.of("id", testPlayer.getId()));
        inventoryRequest.put("skin", Map.of("id", testSkin.getId()));
        inventoryRequest.put("isEquipped", true);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.player.id").value(testPlayer.getId()))
                .andExpect(jsonPath("$.skin.id").value(testSkin.getId()))
                .andExpect(jsonPath("$.isEquipped").value(true));
    }

    @Test
    void getInventoryById_Found() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setPlayer(testPlayer);
        inventory.setSkin(testSkin);
        inventory.setIsEquipped(false);
        inventory = inventoryService.createInventory(inventory);

        mockMvc.perform(get("/api/inventory/{id}", inventory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(inventory.getId()));
    }

    @Test
    void getInventoryById_NotFound() throws Exception {
        mockMvc.perform(get("/api/inventory/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInventory_Success() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setPlayer(testPlayer);
        inventory.setSkin(testSkin);
        inventory.setIsEquipped(false);
        inventory = inventoryService.createInventory(inventory);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("player", Map.of("id", testPlayer.getId()));
        updateRequest.put("skin", Map.of("id", testSkin.getId()));
        updateRequest.put("isEquipped", true);

        mockMvc.perform(put("/api/inventory/{id}", inventory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isEquipped").value(true));
    }

    @Test
    void deleteInventory_Success() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setPlayer(testPlayer);
        inventory.setSkin(testSkin);
        inventory = inventoryService.createInventory(inventory);

        mockMvc.perform(delete("/api/inventory/{id}", inventory.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/inventory/{id}", inventory.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllInventories_Success() throws Exception {
        Inventory inventory = new Inventory();
        inventory.setPlayer(testPlayer);
        inventory.setSkin(testSkin);
        inventoryService.createInventory(inventory);

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateInventory_NotFound() throws Exception {
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("player", Map.of("id", testPlayer.getId()));
        updateRequest.put("skin", Map.of("id", testSkin.getId()));
        updateRequest.put("isEquipped", true);

        mockMvc.perform(put("/api/inventory/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    private Player createTestPlayer() {
        Player p = new Player();
        p.setNickname("InventoryTester");
        p.setLevel(1);
        p.setVpBalance(BigDecimal.ZERO);
        return playerService.createPlayer(p);
    }

    private Skin createTestSkin() {
        WeaponType wt = new WeaponType();
        wt.setWeaponName("Phantom");
        wt.setCategory("Rifle");
        wt = weaponTypeService.createWeaponType(wt);

        Skin skin = new Skin();
        skin.setSkinName("Prime Vandal");
        skin.setWeaponType(wt);
        skin.setRarity("Premium");
        skin.setPriceVp(new BigDecimal("20.00"));
        return skinService.createSkin(skin);
    }
}