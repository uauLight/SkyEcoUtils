package br.com.skyplugins.skyecoutils.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigUtils {
    private final FileConfiguration config;

    public ConfigUtils(FileConfiguration config) {
        this.config = config;
    }

    public String getMessage(String path) {
        String message = config.getString("messages." + path, "Mensagem não configurada!");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getFormattedMessage(String path, String... placeholders) {
        String message = getMessage(path);
        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }
        return message;
    }

    public void sendMessage(Player player, String path, String... placeholders) {
        player.sendMessage(getFormattedMessage(path, placeholders));
    }
}
