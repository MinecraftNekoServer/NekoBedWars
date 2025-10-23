package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BedBreakListener implements Listener {
    private NekoBedWars plugin;

    public BedBreakListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                // 检查是否破坏的是床
                if (block.getType() == Material.BED_BLOCK) {
                    // 防止床掉落物品（通过取消掉落事件实现）
                    
                    // 确定是哪个队伍的床
                    String team = getBedTeam(block.getLocation(), arena);
                    if (team != null) {
                        // 检查是否是自己队伍的床
                        String playerTeam = arena.getPlayerTeams().get(player.getUniqueId());
                        if (team.equals(playerTeam)) {
                            player.sendMessage(ChatColor.RED + "你不能破坏自己队伍的床!");
                            event.setCancelled(true);
                            return;
                        }
                        
                        // 破坏床
                        arena.destroyBed(team);
                        player.sendMessage(ChatColor.GREEN + "你破坏了 " + team + " 队的床!");
                        
                        // 向所有玩家广播消息
                        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                            if (arena.getPlayers().contains(onlinePlayer.getUniqueId())) {
                                onlinePlayer.sendMessage(ChatColor.GOLD + player.getName() + " 破坏了 " + team + " 队的床!");
                            }
                        }
                        
                        // 手动设置床方块为空气，避免自然掉落
                        block.setType(Material.AIR);
                        
                        // 如果是双床，还需要破坏相邻的床方块
                        destroyAdjacentBedBlock(block);
                    }
                }
            }
        }
    }
    
    /**
     * 破坏相邻的床方块
     */
    private void destroyAdjacentBedBlock(Block block) {
        // 检查四个方向的相邻方块
        Block[] adjacentBlocks = {
            block.getRelative(org.bukkit.block.BlockFace.NORTH),
            block.getRelative(org.bukkit.block.BlockFace.SOUTH),
            block.getRelative(org.bukkit.block.BlockFace.EAST),
            block.getRelative(org.bukkit.block.BlockFace.WEST)
        };
        
        for (Block adjacent : adjacentBlocks) {
            if (adjacent.getType() == Material.BED_BLOCK) {
                adjacent.setType(Material.AIR);
                break; // 只需要破坏一个相邻的床方块
            }
        }
    }
    
    /**
     * 确定床属于哪个队伍
     */
    private String getBedTeam(org.bukkit.Location bedLocation, GameArena arena) {
        for (Map.Entry<String, org.bukkit.Location> entry : arena.getBeds().entrySet()) {
            String team = entry.getKey();
            org.bukkit.Location location = entry.getValue();
            
            // 检查位置是否匹配（考虑到床可能由两个方块组成）
            if (location.getWorld().equals(bedLocation.getWorld())) {
                double distance = location.distance(bedLocation);
                if (distance <= 1.5) { // 考虑到床的两个方块
                    return team;
                }
            }
        }
        return null;
    }
}