package neko.nekoBedWars.effects;

import neko.nekoBedWars.NekoBedWars;
import org.bukkit.Location;
import org.bukkit.Particle;
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
            Particle particle = Particle.valueOf(type);
            
            // 播放粒子效果
            location.getWorld().spawnParticle(particle, location, 50, 0.5, 0.5, 0.5, 0.1);
        }
    }

    public void playPlayerDeathEffect(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("effects.player_death");
        if (config != null) {
            String type = config.getString("type", "EXPLOSION_NORMAL");
            Particle particle = Particle.valueOf(type);
            
            // 播放粒子效果
            location.getWorld().spawnParticle(particle, location, 10, 0.5, 0.5, 0.5, 0.1);
        }
    }

    public void playResourceGenerateEffect(Location location) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("effects.resource_generate");
        if (config != null) {
            String type = config.getString("type", "VILLAGER_HAPPY");
            Particle particle = Particle.valueOf(type);
            
            // 播放粒子效果
            location.getWorld().spawnParticle(particle, location, 10, 0.5, 0.5, 0.5, 0.1);
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
            Particle particle = Particle.valueOf(type);
            
            // 播放粒子效果给玩家
            player.spawnParticle(particle, location, 10, 0.5, 0.5, 0.5, 0.1);
        }
    }
}