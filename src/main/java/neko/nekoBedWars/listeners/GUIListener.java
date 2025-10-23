package neko.nekoBedWars.listeners;

import neko.nekoBedWars.gui.GameGUI;
import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.ChatColor;

public class GUIListener implements Listener {
    private NekoBedWars plugin;

    public GUIListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        
        // 检查是否是我们的GUI
        if (inventory.getHolder() != null || !inventory.getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&6&l起床战争菜单"))) {
            return;
        }
        
        event.setCancelled(true);
        
        int slot = event.getRawSlot();
        
        switch (slot) {
            case 11: // 加入游戏按钮
                handleJoinGame(player);
                break;
            case 13: // 离开游戏按钮
                handleLeaveGame(player);
                break;
            case 15: // 个人数据按钮
                handleViewStats(player);
                break;
        }
        
        // 关闭GUI
        player.closeInventory();
    }

    private void handleJoinGame(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "没有激活的地图");
            return;
        }
        
        if (arena.addPlayer(player)) {
            player.sendMessage(ChatColor.GREEN + "成功加入游戏");
            // TODO: 传送玩家到等待区域
        } else {
            player.sendMessage(ChatColor.RED + "无法加入游戏，可能已满人或游戏已开始");
        }
    }

    private void handleLeaveGame(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "你不在游戏中");
            return;
        }
        
        arena.removePlayer(player);
        player.sendMessage(ChatColor.GREEN + "已离开游戏");
        // TODO: 传送玩家到大厅
    }

    private void handleViewStats(Player player) {
        // TODO: 实现个人数据查看逻辑
        player.sendMessage(ChatColor.GREEN + "你的游戏数据:");
        player.sendMessage(ChatColor.YELLOW + "- 击杀数: 0");
        player.sendMessage(ChatColor.YELLOW + "- 死亡数: 0");
        player.sendMessage(ChatColor.YELLOW + "- 胜利场次: 0");
        player.sendMessage(ChatColor.YELLOW + "- 失败场次: 0");
    }
}