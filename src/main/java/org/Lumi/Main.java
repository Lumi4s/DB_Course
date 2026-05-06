package org.Lumi;

import lombok.RequiredArgsConstructor;
import org.Lumi.model.*;
import org.Lumi.service.ShopService;
import org.Lumi.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {

    private final ShopService shopService;
    private final PlayerRepository playerRepository;
    private final SkinRepository skinRepository;
    private final WeaponTypeRepository weaponTypeRepository;
    private final PurchaseRepository purchaseRepository;
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Приложение запущено. База данных готова (ddl-auto=update).");
        new Thread(this::mainMenu).start();
    }

    private void mainMenu() {
        System.out.println("=== Skin Management System CLI ===");
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> createPlayer();
                    case "2" -> addVpToPlayer();
                    case "3" -> createWeaponType();
                    case "4" -> createSkin();
                    case "5" -> purchaseSkin();
                    case "6" -> showPlayerData();
                    case "7" -> listAllSkins();
                    case "8" -> showPlayerInventory();
                    case "0" -> running = false;
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Выход из системы...");
    }

    private void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Создать игрока");
        System.out.println("2. Пополнить VP игроку");
        System.out.println("3. Создать тип оружия (WeaponType)");
        System.out.println("4. Создать скин (Skin)");
        System.out.println("5. Купить скин");
        System.out.println("6. Посмотреть данные игрока");
        System.out.println("7. Список всех скинов в магазине");
        System.out.println("8. Посмотреть инвентарь игрока");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void createPlayer() {
        System.out.print("Введите никнейм: ");
        String nickname = scanner.nextLine();
        Player player = new Player();
        player.setNickname(nickname);
        player.setLevel(1);
        player.setVpBalance(BigDecimal.ZERO);
        playerRepository.save(player);
        System.out.println("Игрок создан! ID: " + player.getId());
    }

    private void addVpToPlayer() {
        System.out.print("Введите ID игрока: ");
        int playerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Сколько VP добавить? ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Игрок не найден"));
        
        player.setVpBalance(player.getVpBalance().add(amount));
        playerRepository.save(player);
        System.out.println("Баланс обновлен!");
    }

    private void createWeaponType() {
        System.out.print("Название оружия (напр. Vandal): ");
        String name = scanner.nextLine();
        System.out.print("Категория (напр. Rifle): ");
        String category = scanner.nextLine();

        WeaponType wt = new WeaponType();
        wt.setWeaponName(name);
        wt.setCategory(category);
        weaponTypeRepository.save(wt);
        System.out.println("Тип оружия создан! ID: " + wt.getId());
    }

    private void createSkin() {
        System.out.print("Введите ID типа оружия (WeaponType): ");
        int wtId = Integer.parseInt(scanner.nextLine());
        WeaponType wt = weaponTypeRepository.findById(wtId)
                .orElseThrow(() -> new RuntimeException("Тип оружия не найден"));

        System.out.print("Название скина: ");
        String name = scanner.nextLine();
        System.out.print("Редкость (например, Epic): ");
        String rarity = scanner.nextLine();
        System.out.print("Цена в VP: ");
        BigDecimal price = new BigDecimal(scanner.nextLine());

        Skin skin = new Skin();
        skin.setSkinName(name);
        skin.setWeaponType(wt);
        skin.setRarity(rarity);
        skin.setPriceVp(price);
        skinRepository.save(skin);
        System.out.println("Скин создан! ID: " + skin.getId());
    }

    private void purchaseSkin() {
        System.out.print("Введите ID игрока: ");
        int playerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите ID скина: ");
        int skinId = Integer.parseInt(scanner.nextLine());

        shopService.purchaseSkin(playerId, skinId);
        System.out.println("Покупка успешно завершена!");
    }

    private void showPlayerData() {
        System.out.print("Введите ID игрока: ");
        int playerId = Integer.parseInt(scanner.nextLine());
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Игрок не найден"));

        System.out.println("\n--- Профиль игрока ---");
        System.out.println("Никнейм: " + player.getNickname());
        System.out.println("Уровень: " + player.getLevel());
        System.out.println("Баланс VP: " + player.getVpBalance());
    }

    private void listAllSkins() {
        System.out.println("\n--- Магазин скинов ---");
        skinRepository.findAll().forEach(skin -> {
            System.out.printf("ID: %d | %s [%s] | Цена: %s VP | Тип: %s%n",
                    skin.getId(), 
                    skin.getSkinName(), 
                    skin.getRarity(), 
                    skin.getPriceVp(),
                    skin.getWeaponType().getWeaponName());
        });
    }

    private void showPlayerInventory() {
        System.out.print("Введите ID игрока: ");
        Integer playerId = Integer.parseInt(scanner.nextLine());

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Игрок не найден"));

        System.out.println("\n--- Инвентарь игрока: " + player.getNickname() + " ---");

        var playerPurchases = purchaseRepository.findAll().stream()
                .filter(p -> p.getPlayer().getId().equals(playerId))
                .toList();

        if (playerPurchases.isEmpty()) {
            System.out.println("У игрока пока нет купленных скинов.");
        } else {
            playerPurchases.forEach(purchase -> {
                Skin skin = purchase.getSkin();
                System.out.printf("- %s [%s] | Тип: %s%n",
                        skin.getSkinName(),
                        skin.getRarity(),
                        skin.getWeaponType().getWeaponName());
            });
        }
    }
}