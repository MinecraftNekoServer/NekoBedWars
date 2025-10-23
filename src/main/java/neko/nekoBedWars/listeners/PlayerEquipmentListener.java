package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.ItemSpawnEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEquipmentListener implements Listener {
    private NekoBedWars plugin;

    public PlayerEquipmentListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                // 移除返回大厅物品
                removeLobbyReturnItem(player);
                
                // 如果游戏正在进行中，给玩家装备护甲和木剑
                if (arena.getState() == GameArena.GameState.INGAME) {
                    String team = arena.getPlayerTeams().get(player.getUniqueId());
                    if (team != null) {
                        // 延迟一段时间后装备护甲和给予木剑，确保玩家已经完全加入游戏
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // 装备队伍护甲
                                equipTeamArmor(player, team);
                                
                                // 给予木剑
                                giveWoodenSword(player);
                            }
                        }.runTaskLater(plugin, 10L); // 延迟10个tick
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                // 移除返回大厅物品
                removeLobbyReturnItem(player);
                
                // 保留木剑在掉落物品中，但确保玩家重生时会获得新的木剑
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                // 移除返回大厅物品
                removeLobbyReturnItem(player);
                
                // 给玩家重新装备护甲和木剑
                String team = arena.getPlayerTeams().get(player.getUniqueId());
                if (team != null) {
                    // 延迟一段时间后装备护甲和给予木剑，确保玩家已经重生
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // 装备队伍护甲
                            equipTeamArmor(player, team);
                            
                            // 给予木剑
                            giveWoodenSword(player);
                        }
                    }.runTaskLater(plugin, 5L); // 延迟5个tick
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            
            // 只在游戏模式下处理
            if (!plugin.isConfigurationMode()) {
                GameArena arena = ArenaManager.getInstance().getActiveArena();
                if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                    // 检查是否尝试移除护甲
                    if (event.getSlot() >= 5 && event.getSlot() <= 8) { // 护甲槽位
                        ItemStack currentItem = event.getCurrentItem();
                        if (currentItem != null && isTeamArmor(currentItem)) {
                            // 取消事件，防止移除护甲
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "你不能移除队伍护甲!");
                        }
                    }
                }
            }
        }
    }

    /**
     * 移除返回大厅物品
     */
    private void removeLobbyReturnItem(Player player) {
        // 移除粘液球（返回大厅物品）
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == Material.SLIME_BALL) {
                if (item.hasItemMeta()) {
                    org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
                    if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GREEN + "返回大厅")) {
                        player.getInventory().setItem(i, null);
                    }
                }
            }
        }
    }

    /**
     * 给予木剑
     */
    private void giveWoodenSword(Player player) {
        // 检查玩家是否已经有木剑
        boolean hasWoodenSword = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.WOOD_SWORD) {
                hasWoodenSword = true;
                break;
            }
        }
        
        // 如果没有木剑，给予一把
        if (!hasWoodenSword) {
            ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
            player.getInventory().setItem(0, sword); // 放在第一个槽位
        }
    }

    /**
     * 为玩家装备与队伍颜色匹配的皮革护甲
     */
    public void equipTeamArmor(Player player, String team) {
        Color armorColor = getTeamColor(team);
        
        // 创建皮革护甲
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        
        // 设置颜色
        setColor(helmet, armorColor);
        setColor(chestplate, armorColor);
        setColor(leggings, armorColor);
        setColor(boots, armorColor);
        
        // 装备护甲
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }
    
    /**
     * 设置皮革护甲的颜色
     */
    private void setColor(ItemStack item, Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(color);
            item.setItemMeta(meta);
        }
    }
    
    /**
     * 检查物品是否为队伍护甲
     */
    private boolean isTeamArmor(ItemStack item) {
        return item.getType() == Material.LEATHER_HELMET ||
               item.getType() == Material.LEATHER_CHESTPLATE ||
               item.getType() == Material.LEATHER_LEGGINGS ||
               item.getType() == Material.LEATHER_BOOTS;
    }
    
    /**
     * 获取队伍颜色
     */
    private Color getTeamColor(String team) {
        switch (team.toLowerCase()) {
            case "red": return Color.RED;
            case "blue": return Color.BLUE;
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "orange": return Color.ORANGE;
            case "purple": return Color.PURPLE;
            case "pink": return Color.FUCHSIA;
            case "white": return Color.WHITE;
            default: return Color.RED;
        }
    }
}