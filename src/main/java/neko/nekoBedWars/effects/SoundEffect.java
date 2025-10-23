package neko.nekoBedWars.effects;

import neko.nekoBedWars.NekoBedWars;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;

public class SoundEffect {
    private NekoBedWars plugin;

    public SoundEffect(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    public void playBedDestroySound(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("sounds.bed_destroy");
        if (config != null) {
            String type = config.getString("type", "ENTITY_ENDERDRAGON_GROWL");
            // 检查音效是否存在，如果不存在则使用默认音效
            Sound sound;
            try {
                sound = Sound.valueOf(type);
            } catch (IllegalArgumentException e) {
                sound = Sound.ENTITY_ENDERDRAGON_GROWL; // 1.12.2中的对应音效
            }
            float volume = (float) config.getDouble("volume", 1.0);
            float pitch = (float) config.getDouble("pitch", 1.0);
            
            // 播放音效
            location.getWorld().playSound(location, sound, volume, pitch);
        }
    }

    public void playPlayerDeathSound(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("sounds.player_death");
        if (config != null) {
            String type = config.getString("type", "ENTITY_PLAYER_DEATH");
            // 检查音效是否存在，如果不存在则使用默认音效
            Sound sound;
            try {
                sound = Sound.valueOf(type);
            } catch (IllegalArgumentException e) {
                sound = Sound.ENTITY_PLAYER_DEATH; // 1.12.2中的对应音效
            }
            float volume = (float) config.getDouble("volume", 1.0);
            float pitch = (float) config.getDouble("pitch", 1.0);
            
            // 播放音效
            location.getWorld().playSound(location, sound, volume, pitch);
        }
    }

    public void playGameStartSound(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("sounds.game_start");
        if (config != null) {
            String type = config.getString("type", "ENTITY_WITHER_SPAWN");
            // 检查音效是否存在，如果不存在则使用默认音效
            Sound sound;
            try {
                sound = Sound.valueOf(type);
            } catch (IllegalArgumentException e) {
                sound = Sound.ENTITY_WITHER_SPAWN; // 1.12.2中的对应音效
            }
            float volume = (float) config.getDouble("volume", 1.0);
            float pitch = (float) config.getDouble("pitch", 1.0);
            
            // 播放音效
            location.getWorld().playSound(location, sound, volume, pitch);
        }
    }

    public void playGameEndSound(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("sounds.game_end");
        if (config != null) {
            String type = config.getString("type", "ENTITY_ENDERDRAGON_DEATH");
            // 检查音效是否存在，如果不存在则使用默认音效
            Sound sound;
            try {
                sound = Sound.valueOf(type);
            } catch (IllegalArgumentException e) {
                sound = Sound.ENTITY_ENDERDRAGON_DEATH; // 1.12.2中的对应音效
            }
            float volume = (float) config.getDouble("volume", 1.0);
            float pitch = (float) config.getDouble("pitch", 1.0);
            
            // 播放音效
            location.getWorld().playSound(location, sound, volume, pitch);
        }
    }

    public void playSoundForPlayers(List<Player> players, Location location, String soundType) {
        for (Player player : players) {
            // 播放音效给玩家
            playSound(player, location, soundType);
        }
    }

    private void playSound(Player player, Location location, String soundType) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("sounds." + soundType);
        if (config != null) {
            String type = config.getString("type", "ENTITY_PLAYER_LEVELUP");
            // 检查音效是否存在，如果不存在则使用默认音效
            Sound sound;
            try {
                sound = Sound.valueOf(type);
            } catch (IllegalArgumentException e) {
                sound = Sound.ENTITY_PLAYER_LEVELUP; // 1.12.2中的对应音效
            }
            float volume = (float) config.getDouble("volume", 1.0);
            float pitch = (float) config.getDouble("pitch", 1.0);
            
            // 播放音效给玩家
            player.playSound(location, sound, volume, pitch);
        }
    }
}