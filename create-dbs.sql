CREATE TABLE `game_stats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `guild_id` varchar(45) DEFAULT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  `game_name` varchar(100) DEFAULT 'DEFAULT',
  `time_played` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `guild_config` (
  `id` varchar(45) NOT NULL,
  `prefix` varchar(45) DEFAULT '<',
  `log_channel` varchar(45) DEFAULT NULL,
  `locale` varchar(45) NOT NULL DEFAULT 'en_US',
  `disabled_features` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_stats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) NOT NULL,
  `guild_id` varchar(45) NOT NULL,
  `messages_amount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;