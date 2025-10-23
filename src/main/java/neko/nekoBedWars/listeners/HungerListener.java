package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.entity.Player;

public class HungerListener implements Listener {
    private NekoBedWars plugin;

    public HungerListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                GameArena arena = ArenaManager.getInstance().getActiveArena();
                if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                    // 设置玩家饥饿值为20（满）
                    player.setFoodLevel(20);
                    // 取消饥饿值变化事件
                    event.setCancelled(true);
                }
            }
        }
    }
}