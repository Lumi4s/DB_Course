package org.Lumi.service;

import org.Lumi.model.Player;
import org.Lumi.repo.PlayerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Integer id) {
        return playerRepository.findById(id);
    }

    public Player createPlayer(Player player) {
        if (playerRepository.findByNickname(player.getNickname()).isPresent()) {
            throw new DataIntegrityViolationException("Player with nickname '" + player.getNickname() + "' already exists");
        }
        return playerRepository.save(player);
    }

    public Player updatePlayer(Integer id, Player playerDetails) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (playerDetails.getNickname() != null) {
            player.setNickname(playerDetails.getNickname());
        }
        if (playerDetails.getLevel() != null) {
            player.setLevel(playerDetails.getLevel());
        }
        if (playerDetails.getVpBalance() != null) {
            player.setVpBalance(playerDetails.getVpBalance());
        }

        return playerRepository.save(player);
    }

    public void deletePlayer(Integer id) {
        playerRepository.deleteById(id);
    }
}

