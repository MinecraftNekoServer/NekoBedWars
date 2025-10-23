package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {
    private NekoBedWars plugin;

    public PlayerJoinListener(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            // 设置玩家为生存模式
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            // 设置玩家饥饿值为满
            player.setFoodLevel(20);
            
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null) {
                // 检查玩家是否已经在游戏中
                if (arena.getPlayers().contains(player.getUniqueId())) {
                    // 玩家在游戏中，确保传送到等待区域出生点
                    handlePlayerInGame(player, arena);
                } else {
                    // 玩家不在游戏中，自动加入游戏
                    if (arena.getState() == GameArena.GameState.WAITING || arena.getState() == GameArena.GameState.STARTING) {
                        if (arena.addPlayer(player)) {
                            player.sendMessage(ChatColor.GREEN + "已自动加入起床战争游戏!");
                            
                            // 显示等待区域计分板
                            plugin.getWaitingScoreboard().addPlayer(player);
                            plugin.getWaitingScoreboard().updateScoreboard(arena);
                            
                            // 给玩家发放返回大厅物品
                            giveLobbyReturnItem(player);
                            
                            // 传送玩家到等待区域出生点
                            if (arena.getWaitingSpawnPoint() != null) {
                                player.teleport(arena.getWaitingSpawnPoint());
                                player.sendMessage(ChatColor.GREEN + "已传送到等待区域出生点");
                            }
                            
                            // 发送一次性的等待消息（不频繁发送）
                            sendInitialWaitingMessages(player, arena);
                        } else {
                            player.sendMessage(ChatColor.RED + "无法加入游戏，游戏可能已满人或已开始");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "游戏正在进行中，无法加入");
                    }
                }
            }
        }
    }
    
    /**
     * 处理已在游戏中的玩家
     */
    private void handlePlayerInGame(Player player, GameArena arena) {
        // 设置玩家为生存模式
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        // 设置玩家饥饿值为满
        player.setFoodLevel(20);
        
        // 确保传送到等待区域出生点
        if (arena.getWaitingSpawnPoint() != null) {
            player.teleport(arena.getWaitingSpawnPoint());
            player.sendMessage(ChatColor.GREEN + "欢迎回来! 已传送到等待区域出生点");
        }
        
        // 显示等待区域计分板
        plugin.getWaitingScoreboard().addPlayer(player);
        plugin.getWaitingScoreboard().updateScoreboard(arena);
        
        // 给玩家发放返回大厅物品
        giveLobbyReturnItem(player);
        
        // 发送一次性的等待消息（不频繁发送）
        sendInitialWaitingMessages(player, arena);
    }
    
    /**
     * 发送初始等待消息（避免频繁刷屏）
     */
    private void sendInitialWaitingMessages(Player player, GameArena arena) {
        // 发送欢迎消息
        player.sendMessage(ChatColor.GREEN + "欢迎加入起床战争游戏!");
        player.sendMessage(ChatColor.YELLOW + "你当前在等待区域，请等待其他玩家加入...");
        
        // 根据当前游戏状态发送不同消息
        switch (arena.getState()) {
            case WAITING:
                int playerCount = arena.getPlayers().size();
                int minPlayers = arena.getTeams().size() * 1;
                int playersNeeded = Math.max(0, minPlayers - playerCount);
                
                if (playersNeeded > 0) {
                    player.sendMessage(ChatColor.YELLOW + "还需要 " + ChatColor.AQUA + playersNeeded + ChatColor.YELLOW + " 名玩家才能开始游戏");
                } else {
                    player.sendMessage(ChatColor.GREEN + "已满足开始条件，游戏即将开始!");
                }
                break;
                
            case STARTING:
                player.sendMessage(ChatColor.YELLOW + "游戏即将开始，请做好准备!");
                break;
                
            case INGAME:
                player.sendMessage(ChatColor.RED + "游戏正在进行中，你将作为观战者加入");
                break;
                
            default:
                player.sendMessage(ChatColor.YELLOW + "请等待游戏开始...");
                break;
        }
        
        player.sendMessage(ChatColor.GRAY + "--------------------");
        player.sendMessage(ChatColor.AQUA + "游戏提示:");
        player.sendMessage(ChatColor.WHITE + "- 你可以在等待区域自由移动");
        player.sendMessage(ChatColor.WHITE + "- 准备好迎接战斗!");
        player.sendMessage(ChatColor.GRAY + "--------------------");
    }
    
    /**
     * 给玩家发放返回大厅物品
     */
    private void giveLobbyReturnItem(Player player) {
        // 创建返回大厅物品（粘液球）
        ItemStack lobbyItem = new ItemStack(Material.SLIME_BALL, 1);
        ItemMeta meta = lobbyItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "返回大厅");
            meta.setLore(java.util.Arrays.asList(
                ChatColor.GRAY + "右键点击返回大厅",
                ChatColor.GRAY + "再次点击可取消返回"
            ));
            lobbyItem.setItemMeta(meta);
        }
        
        // 将物品放在玩家物品栏的第9格（索引8）
        player.getInventory().setItem(8, lobbyItem);
    }
}