package neko.nekoBedWars.effects;

import neko.nekoBedWars.NekoBedWars;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;

public class ParticleEffect {
    private NekoBedWars plugin;

    public ParticleEffect(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    public void playBedDestroyEffect(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("effects.bed_destroy");
        if (config != null) {
            String type = config.getString("type", "REDSTONE");
            
            // 播放粒子效果 (1.12.2版本使用不同的API)
            location.getWorld().playEffect(location, org.bukkit.Effect.valueOf(type), 0);
        }
    }

    public void playPlayerDeathEffect(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("effects.player_death");
        if (config != null) {
            String type = config.getString("type", "EXPLOSION");
            
            // 播放粒子效果 (1.12.2版本使用不同的API)
            location.getWorld().playEffect(location, org.bukkit.Effect.valueOf(type), 0);
        }
    }

    public void playResourceGenerateEffect(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("effects.resource_generate");
        if (config != null) {
            String type = config.getString("type", "HAPPY_VILLAGER");
            
            // 播放粒子效果 (1.12.2版本使用不同的API)
            location.getWorld().playEffect(location, org.bukkit.Effect.valueOf(type), 0);
        }
    }

    public void playEffectForPlayers(List<Player> players, Location location, String effectType) {
        for (Player player : players) {
            // 播放粒子效果给玩家
            playEffect(player, location, effectType);
        }
    }

    private void playEffect(Player player, Location location, String effectType) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("effects." + effectType);
        if (config != null) {
            String type = config.getString("type", "REDSTONE");
            
            // 播放粒子效果给玩家 (1.12.2版本使用不同的API)
            player.playEffect(location, org.bukkit.Effect.valueOf(type), 0);
        }
    }
}