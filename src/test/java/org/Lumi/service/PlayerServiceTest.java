package org.Lumi.service;

import org.Lumi.model.Player;
import org.Lumi.repo.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player samplePlayer;

    @BeforeEach
    void setUp() {
        samplePlayer = new Player();
        samplePlayer.setId(1);
        samplePlayer.setNickname("Nickname");
        samplePlayer.setLevel(10);
        samplePlayer.setVpBalance(new BigDecimal("100.00"));
    }

    @Test
    void getAllPlayers_ShouldReturnList() {
        // Arrange
        when(playerRepository.findAll()).thenReturn(Arrays.asList(samplePlayer, new Player()));

        // Act
        List<Player> result = playerService.getAllPlayers();

        // Assert
        assertEquals(2, result.size());
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void getPlayerById_ShouldReturnPlayer_WhenIdExists() {
        // Arrange
        when(playerRepository.findById(1)).thenReturn(Optional.of(samplePlayer));

        // Act
        Optional<Player> result = playerService.getPlayerById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void createPlayer_ShouldReturnSavedPlayer() {
        // Arrange
        when(playerRepository.save(any(Player.class))).thenReturn(samplePlayer);

        // Act
        Player result = playerService.createPlayer(samplePlayer);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void updatePlayer_ShouldUpdateFields_WhenDetailsProvided() {
        // Arrange
        Player updateDetails = new Player();
        updateDetails.setNickname("newNickname");
        updateDetails.setLevel(67);
        updateDetails.setVpBalance(new BigDecimal("1488.00"));

        when(playerRepository.findById(1)).thenReturn(Optional.of(samplePlayer));
        when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Player result = playerService.updatePlayer(1, updateDetails);

        // Assert
        assertEquals("newNickname", result.getNickname());
        assertEquals(67, result.getLevel());
        assertEquals(new BigDecimal("1488.00"), result.getVpBalance());
        verify(playerRepository).save(samplePlayer);
    }

    @Test
    void updatePlayer_ShouldNotOverwriteWithNull_WhenFieldsAreMissing() {
        // Arrange
        Player updatedDetails = new Player();
        updatedDetails.setNickname("OnlyNicknameChanged");

        when(playerRepository.findById(1)).thenReturn(Optional.of(samplePlayer));
        when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Player result = playerService.updatePlayer(1, updatedDetails);

        // Assert
        assertEquals("OnlyNicknameChanged", result.getNickname());
        assertEquals(10, result.getLevel(), "Level shouldn't be overwritten");
        assertEquals(new BigDecimal("100.00"), result.getVpBalance(), "VP shouldn't be overwritten");
    }

    @Test
    void updatePlayer_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(playerRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playerService.updatePlayer(99, new Player());
        });

        assertEquals("Player not found", exception.getMessage());
    }

    @Test
    void deletePlayer_ShouldCallRepositoryDelete() {
        // Act
        playerService.deletePlayer(1);

        // Assert
        verify(playerRepository, times(1)).deleteById(1);
    }

}
