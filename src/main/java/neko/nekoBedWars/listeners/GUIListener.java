package neko.nekoBedWars.listeners;

import neko.nekoBedWars.gui.GameGUI;
import neko.nekoBedWars.gui.TeamSelectionGUI;
import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;

public class GUIListener implements Listener {
    private NekoBedWars plugin;
    private Map<Player, TeamSelectionGUI> teamSelectionGUIs = new HashMap<>();

    public GUIListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        
        // 检查是否是游戏GUI
        if (inventory.getHolder() == null && inventory.getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&6&l起床战争菜单"))) {
            handleGameGUI(event, player, inventory);
            return;
        }
        
        // 检查是否是队伍选择GUI
        if (inventory.getHolder() == null && inventory.getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&6&l选择队伍颜色"))) {
            handleTeamSelectionGUI(event, player, inventory);
            return;
        }
    }

    private void handleGameGUI(InventoryClickEvent event, Player player, Inventory inventory) {
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

    private void handleTeamSelectionGUI(InventoryClickEvent event, Player player, Inventory inventory) {
        event.setCancelled(true);
        
        int slot = event.getRawSlot();
        
        // 检查是否点击了羊毛方块
        if (slot >= 0 && slot <= 7) {
            org.bukkit.inventory.ItemStack item = event.getCurrentItem();
            if (item != null && item.getType() == Material.WOOL) {
                // 获取队伍名称
                String teamName = getTeamNameFromWool(item);
                
                // 获取GUI实例
                TeamSelectionGUI teamGUI = teamSelectionGUIs.get(player);
                if (teamGUI != null) {
                    // 根据类型设置床或出生点位置
                    GameArena arena = ArenaManager.getInstance().getActiveArena();
                    if (arena != null) {
                        if (teamGUI.getLocationType() == TeamSelectionGUI.LocationType.BED) {
                            arena.getBeds().put(teamName, teamGUI.getLocation());
                            player.sendMessage(ChatColor.GREEN + teamName + "队伍的床位置已设置");
                        } else if (teamGUI.getLocationType() == TeamSelectionGUI.LocationType.SPAWN) {
                            arena.getSpawns().put(teamName, teamGUI.getLocation());
                            player.sendMessage(ChatColor.GREEN + teamName + "队伍的出生点已设置");
                        }
                    }
                    
                    // 移除GUI引用
                    teamSelectionGUIs.remove(player);
                }
            }
        }
        
        // 关闭GUI
        player.closeInventory();
    }

    private String getTeamNameFromWool(org.bukkit.inventory.ItemStack woolItem) {
        short color = woolItem.getDurability();
        
        switch (color) {
            case 14: return "red";
            case 11: return "blue";
            case 13: return "green";
            case 4: return "yellow";
            case 1: return "orange";
            case 2: return "purple";
            case 6: return "pink";
            case 0: return "white";
            default: return "red"; // 默认红色
        }
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
    
    public void registerTeamSelectionGUI(Player player, TeamSelectionGUI gui) {

        teamSelectionGUIs.put(player, gui);

    }

}
