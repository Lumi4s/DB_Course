CREATE TABLE WEAPON_TYPE (
    weapon_id INT PRIMARY KEY,
    weapon_name VARCHAR(50) NOT NULL,
    category VARCHAR(50)
);

CREATE TABLE PLAYER (
    player_id INT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL,
    level INT DEFAULT 1,
    vp_balance DECIMAL(15, 2) DEFAULT 0
);

CREATE TABLE SKIN (
    skin_id INT PRIMARY KEY,
    skin_name VARCHAR(100) NOT NULL,
    weapon_id INT NOT NULL,
    rarity VARCHAR(30),
    price_vp DECIMAL(10, 2),
    FOREIGN KEY (weapon_id) REFERENCES WEAPON_TYPE(weapon_id)
);

CREATE TABLE INVENTORY (
    inventory_id INT PRIMARY KEY,
    player_id INT NOT NULL,
    skin_id INT NOT NULL,
    is_equipped BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (player_id) REFERENCES PLAYER(player_id),
    FOREIGN KEY (skin_id) REFERENCES SKIN(skin_id)
);

CREATE TABLE PURCHASE (
    purchase_id INT PRIMARY KEY,
    player_id INT NOT NULL,
    skin_id INT NOT NULL,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES PLAYER(player_id),
    FOREIGN KEY (skin_id) REFERENCES SKIN(skin_id)
);