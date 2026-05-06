package org.Lumi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Lumi.model.Player;
import org.Lumi.model.Purchase;
import org.Lumi.model.Skin;
import org.Lumi.model.WeaponType;
import org.Lumi.service.PlayerService;
import org.Lumi.service.PurchaseService;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseService purchaseService;

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
        WeaponType weaponType = new WeaponType();
        weaponType.setWeaponName("Vandal");
        weaponType.setCategory("Rifle");
        weaponType = weaponTypeService.createWeaponType(weaponType);

        testSkin = new Skin();
        testSkin.setSkinName("Reaver Vandal");
        testSkin.setWeaponType(weaponType);
        testSkin.setRarity("Premium");
        testSkin.setPriceVp(new BigDecimal("1775"));
        testSkin = skinService.createSkin(testSkin);

        testPlayer = new Player();
        testPlayer.setNickname("BuyerTest");
        testPlayer.setLevel(10);
        testPlayer.setVpBalance(new BigDecimal("5000.00"));
        testPlayer = playerService.createPlayer(testPlayer);
    }

    @Test
    void createPurchase_Success() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setPlayer(testPlayer);
        purchase.setSkin(testSkin);

        mockMvc.perform(post("/api/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.player.id").value(testPlayer.getId()))
                .andExpect(jsonPath("$.skin.id").value(testSkin.getId()));
    }

    @Test
    void getAllPurchases_Success() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setPlayer(testPlayer);
        purchase.setSkin(testSkin);
        purchaseService.createPurchase(purchase);

        mockMvc.perform(get("/api/purchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getPurchaseById_Found() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setPlayer(testPlayer);
        purchase.setSkin(testSkin);
        purchase = purchaseService.createPurchase(purchase);

        mockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(purchase.getId()));
    }

    @Test
    void getPurchaseById_NotFound() throws Exception {
        mockMvc.perform(get("/api/purchases/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePurchase_Success() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setPlayer(testPlayer);
        purchase.setSkin(testSkin);
        purchase = purchaseService.createPurchase(purchase);

        mockMvc.perform(delete("/api/purchases/{id}", purchase.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
                .andExpect(status().isNotFound());
    }
}
