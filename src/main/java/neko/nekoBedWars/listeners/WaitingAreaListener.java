package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.ChatColor;

public class WaitingAreaListener implements Listener {
    private NekoBedWars plugin;

    public WaitingAreaListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                // 检查玩家是否在等待区域
                if (isInWaitingArea(player.getLocation(), arena)) {
                    // 取消破坏方块事件
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "你不能在等待区域破坏方块!");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                // 检查玩家是否在等待区域
                if (isInWaitingArea(player.getLocation(), arena)) {
                    // 取消放置方块事件
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "你不能在等待区域放置方块!");
                }
            }
        }
    }

    /**
     * 检查玩家是否在等待区域
     */
    private boolean isInWaitingArea(Location location, GameArena arena) {
        Location pos1 = arena.getWaitingAreaPos1();
        Location pos2 = arena.getWaitingAreaPos2();
        
        if (pos1 == null || pos2 == null) {
            return false;
        }
        
        // 计算边界
        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());
        
        // 检查位置是否在边界内
        Location playerLoc = location;
        return playerLoc.getX() >= minX && playerLoc.getX() <= maxX &&
               playerLoc.getY() >= minY && playerLoc.getY() <= maxY &&
               playerLoc.getZ() >= minZ && playerLoc.getZ() <= maxZ;
    }
}