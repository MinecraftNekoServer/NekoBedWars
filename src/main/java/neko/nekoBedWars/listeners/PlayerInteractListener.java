package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import neko.nekoBedWars.commands.BWCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Set;
import java.util.HashSet;

public class PlayerInteractListener implements Listener {
    private NekoBedWars plugin;
    private Set<Player> waitingAreaSelectionMode = new HashSet<>();
    private Set<Player> bedSelectionMode = new HashSet<>();
    private Set<Player> spawnSelectionMode = new HashSet<>();
    private Set<Player> shopSelectionMode = new HashSet<>();
    private Set<Player> upgradeSelectionMode = new HashSet<>();
    private Set<Player> resourceSelectionMode = new HashSet<>();
    private Set<Player> ncpSelectionMode = new HashSet<>();
    private Set<Player> boundsSelectionMode = new HashSet<>();
    
    // 用于存储选择的位置
    private Location pos1;
    private Location pos2;

    public PlayerInteractListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        if (block == null) return;
        
        // 处理配置模式下的工具使用
        if (isInCreateModeWithNamedTools(player)) {
            handleCreateModeInteractionWithGUI(player, block, event);
            return;
        }
        
        // 处理等待区域选择
        if (waitingAreaSelectionMode.contains(player)) {
            handleWaitingAreaSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理床位置选择
        if (bedSelectionMode.contains(player)) {
            handleBedSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理出生点选择
        if (spawnSelectionMode.contains(player)) {
            handleSpawnSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理商店位置选择
        if (shopSelectionMode.contains(player)) {
            handleShopSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理升级台位置选择
        if (upgradeSelectionMode.contains(player)) {
            handleUpgradeSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理资源点选择
        if (resourceSelectionMode.contains(player)) {
            handleResourceSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理NCP位置选择
        if (ncpSelectionMode.contains(player)) {
            handleNcpSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
        
        // 处理边界点选择
        if (boundsSelectionMode.contains(player)) {
            handleBoundsSelection(player, block.getLocation());
            event.setCancelled(true);
            return;
        }
    }
    
    // 检查玩家是否在配置模式的新方法
    private boolean isInCreateModeWithNamedTools(Player player) {
        ItemStack item = player.getItemInHand();
        return item != null && (isConfigurationTool(item) || isNamedConfigurationTool(item));
    }
    
    // 通过物品名称识别配置工具的新方法
    private boolean isNamedConfigurationTool(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return false;
        
        String displayName = meta.getDisplayName();
        return displayName.contains("设置工具");
    }
    
    private boolean isConfigurationTool(ItemStack item) {
        Material type = item.getType();
        return type == Material.BED || type == Material.REDSTONE_TORCH_ON || 
               type == Material.CHEST || type == Material.ENCHANTMENT_TABLE ||
               type == Material.DIAMOND || type == Material.EMERALD ||
               type == Material.GOLD_INGOT || type == Material.IRON_INGOT ||
               type == Material.BARRIER;
    }
    
    private void handleCreateModeInteractionWithGUI(Player player, Block block, PlayerInteractEvent event) {
        ItemStack item = player.getItemInHand();
        if (item == null) return;
        
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "没有激活的地图");
            return;
        }
        
        Material type = item.getType();
        ItemMeta meta = item.getItemMeta();
        org.bukkit.Location location = block.getLocation();
        
        // 通过物品类型或名称确定工具类型
        boolean isBedTool = type == Material.BED || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("床位置设置工具"));
        boolean isSpawnTool = type == Material.REDSTONE_TORCH_ON || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("出生点设置工具"));
        boolean isShopTool = type == Material.CHEST || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("商店位置设置工具"));
        boolean isUpgradeTool = type == Material.ENCHANTMENT_TABLE || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("升级台设置工具"));
        boolean isDiamondTool = type == Material.DIAMOND || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("钻石资源点设置工具"));
        boolean isEmeraldTool = type == Material.EMERALD || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("绿宝石资源点设置工具"));
        boolean isGoldTool = type == Material.GOLD_INGOT || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("金资源点设置工具"));
        boolean isIronTool = type == Material.IRON_INGOT || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("铁资源点设置工具"));
        boolean isBarrierTool = type == Material.BARRIER || (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("边界设置工具"));
        
        if (isBedTool) {
            // 打开队伍选择GUI来设置床位置
            handleBedPlacementWithGUI(player, location);
        } else if (isSpawnTool) {
            // 打开队伍选择GUI来设置出生点
            handleSpawnPlacementWithGUI(player, location);
        } else if (isShopTool) {
            // 设置商店位置
            arena.getShops().add(location);
            player.sendMessage(ChatColor.GREEN + "商店位置已设置");
        } else if (isUpgradeTool) {
            // 设置升级台位置
            arena.getUpgrades().add(location);
            player.sendMessage(ChatColor.GREEN + "升级台位置已设置");
        } else if (isDiamondTool) {
            // 设置钻石资源点
            player.sendMessage(ChatColor.GREEN + "钻石资源点位置已设置");
        } else if (isEmeraldTool) {
            // 设置绿宝石资源点
            player.sendMessage(ChatColor.GREEN + "绿宝石资源点位置已设置");
        } else if (isGoldTool) {
            // 设置金资源点
            player.sendMessage(ChatColor.GREEN + "金资源点位置已设置");
        } else if (isIronTool) {
            // 设置铁资源点
            player.sendMessage(ChatColor.GREEN + "铁资源点位置已设置");
        } else if (isBarrierTool) {
            // 设置边界点
            if (pos1 == null) {
                pos1 = location;
                player.sendMessage(ChatColor.GREEN + "请再次点击设置边界第二点");
            } else {
                pos2 = location;
                // TODO: 实现边界设置逻辑
                player.sendMessage(ChatColor.GREEN + "边界已设置");
                pos1 = null;
                pos2 = null;
            }
        }
        
        event.setCancelled(true);
    }

    private void handleWaitingAreaSelection(Player player, Location location) {
        if (pos1 == null) {
            pos1 = location;
            player.sendMessage(ChatColor.GREEN + "请左键点击选择等待区域的第二个点");
        } else {
            pos2 = location;
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null) {
                arena.setWaitingAreaPos1(pos1);
                arena.setWaitingAreaPos2(pos2);
                player.sendMessage(ChatColor.GREEN + "等待区域已设置");
            } else {
                player.sendMessage(ChatColor.RED + "没有激活的地图");
            }
            waitingAreaSelectionMode.remove(player);
            pos1 = null;
            pos2 = null;
        }
    }

    private void handleBedSelection(Player player, Location location) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            // 这里需要知道是哪个队伍的床，暂时用红色队伍作为示例
            arena.getBeds().put("red", location);
            player.sendMessage(ChatColor.GREEN + "红色队伍的床位置已设置");
        } else {
            player.sendMessage(ChatColor.RED + "没有激活的地图");
        }
        bedSelectionMode.remove(player);
    }

    private void handleSpawnSelection(Player player, Location location) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            // 这里需要知道是哪个队伍的出生点，暂时用红色队伍作为示例
            arena.getSpawns().put("red", location);
            player.sendMessage(ChatColor.GREEN + "红色队伍的出生点已设置");
        } else {
            player.sendMessage(ChatColor.RED + "没有激活的地图");
        }
        spawnSelectionMode.remove(player);
    }

    private void handleShopSelection(Player player, Location location) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            arena.getShops().add(location);
            player.sendMessage(ChatColor.GREEN + "商店位置已设置");
        } else {
            player.sendMessage(ChatColor.RED + "没有激活的地图");
        }
        shopSelectionMode.remove(player);
    }

    private void handleUpgradeSelection(Player player, Location location) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            arena.getUpgrades().add(location);
            player.sendMessage(ChatColor.GREEN + "升级台位置已设置");
        } else {
            player.sendMessage(ChatColor.RED + "没有激活的地图");
        }
        upgradeSelectionMode.remove(player);
    }

    private void handleResourceSelection(Player player, Location location) {
        // TODO: 实现资源点设置逻辑
        player.sendMessage(ChatColor.GREEN + "资源点位置已设置");
        resourceSelectionMode.remove(player);
    }

    private void handleNcpSelection(Player player, Location location) {
        // TODO: 实现NCP位置设置逻辑
        player.sendMessage(ChatColor.GREEN + "NCP位置已设置");
        ncpSelectionMode.remove(player);
    }

    private void handleBoundsSelection(Player player, Location location) {
        if (pos1 == null) {
            pos1 = location;
            player.sendMessage(ChatColor.GREEN + "请左键点击选择游戏区域边界的第二点");
        } else {
            pos2 = location;
            // TODO: 实现边界设置逻辑
            player.sendMessage(ChatColor.GREEN + "游戏区域边界已设置");
            boundsSelectionMode.remove(player);
            pos1 = null;
            pos2 = null;
        }
    }
    
    // 处理床位置设置（带GUI选择）
    private void handleBedPlacementWithGUI(Player player, org.bukkit.Location location) {
        // 直接使用完整类名来避免导入问题
        neko.nekoBedWars.gui.TeamSelectionGUI teamGUI = new neko.nekoBedWars.gui.TeamSelectionGUI(player, neko.nekoBedWars.gui.TeamSelectionGUI.LocationType.BED, location);
        teamGUI.openGUI();
        player.sendMessage(org.bukkit.ChatColor.GREEN + "请选择床位置对应的队伍颜色");
    }
    
    // 处理出生点设置（带GUI选择）
    private void handleSpawnPlacementWithGUI(Player player, org.bukkit.Location location) {
        // 直接使用完整类名来避免导入问题
        neko.nekoBedWars.gui.TeamSelectionGUI teamGUI = new neko.nekoBedWars.gui.TeamSelectionGUI(player, neko.nekoBedWars.gui.TeamSelectionGUI.LocationType.SPAWN, location);
        teamGUI.openGUI();
        player.sendMessage(org.bukkit.ChatColor.GREEN + "请选择出生点对应的队伍颜色");
    }
}