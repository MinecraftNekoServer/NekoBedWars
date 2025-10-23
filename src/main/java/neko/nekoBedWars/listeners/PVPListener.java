package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

public class PVPListener implements Listener {
    private NekoBedWars plugin;

    public PVPListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            Entity damager = event.getDamager();
            Entity damaged = event.getEntity();
            
            // 检查是否是玩家之间的伤害
            if (damager instanceof Player && damaged instanceof Player) {
                Player attacker = (Player) damager;
                Player victim = (Player) damaged;
                
                GameArena arena = ArenaManager.getInstance().getActiveArena();
                if (arena != null) {
                    // 检查两个玩家是否都在游戏中
                    if (arena.getPlayers().contains(attacker.getUniqueId()) && 
                        arena.getPlayers().contains(victim.getUniqueId())) {
                        // 检查游戏是否处于等待或倒计时状态
                        if (arena.getState() == GameArena.GameState.WAITING || 
                            arena.getState() == GameArena.GameState.STARTING) {
                            // 取消伤害事件，禁止PVP
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}