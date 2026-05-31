package org.Lumi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Lumi.model.Player;
import org.Lumi.service.PlayerService;
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
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player();
        testPlayer.setNickname("TestPlayer");
        testPlayer.setLevel(1);
        testPlayer.setVpBalance(new BigDecimal("100.00"));
    }

    @Test
    void createPlayer_Success() throws Exception {
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("TestPlayer"));
    }

    @Test
    void createPlayer_DuplicateNickname_ReturnsConflict() throws Exception {
        playerService.createPlayer(testPlayer);

        Player duplicatePlayer = new Player();
        duplicatePlayer.setNickname(testPlayer.getNickname());
        duplicatePlayer.setLevel(testPlayer.getLevel());
        duplicatePlayer.setVpBalance(testPlayer.getVpBalance());

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatePlayer)))
                .andExpect(status().isConflict());
    }

    @Test
    void createPlayer_InvalidData_ReturnsBadRequest() throws Exception {
        Player invalidPlayer = new Player();
        invalidPlayer.setNickname("");
        invalidPlayer.setVpBalance(new BigDecimal("-10.00"));

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPlayer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllPlayers_Success() throws Exception {
        playerService.createPlayer(testPlayer);

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getPlayerById_Found() throws Exception {
        Player saved = playerService.createPlayer(testPlayer);

        mockMvc.perform(get("/api/players/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void getPlayerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/players/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePlayer_Success() throws Exception {
        Player saved = playerService.createPlayer(testPlayer);
        saved.setNickname("UpdatedName");

        mockMvc.perform(put("/api/players/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("UpdatedName"));
    }

    @Test
    void deletePlayer_Success() throws Exception {
        Player saved = playerService.createPlayer(testPlayer);

        mockMvc.perform(delete("/api/players/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/players/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}