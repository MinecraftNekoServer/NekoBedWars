package neko.nekoBedWars.listeners;



import neko.nekoBedWars.NekoBedWars;

import neko.nekoBedWars.GameArena;

import neko.nekoBedWars.ArenaManager;

import org.bukkit.event.EventHandler;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.Material;

import org.bukkit.ChatColor;

import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;

import java.util.HashMap;

import java.util.Map;

import java.util.UUID;

public class LobbyReturnListener implements Listener {
    private NekoBedWars plugin;
    private Map<UUID, BukkitTask> returnTasks;
    private Map<UUID, Long> lastClickTime;

    public LobbyReturnListener(NekoBedWars plugin) {
        this.plugin = plugin;
        this.returnTasks = new HashMap<>();
        this.lastClickTime = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // 只在游戏模式下处理
        if (!plugin.isConfigurationMode()) {
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null && arena.getPlayers().contains(player.getUniqueId())) {
                ItemStack item = event.getItem();
                if (item != null && item.getType() == Material.SLIME_BALL) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && 
                        meta.getDisplayName().equals(ChatColor.GREEN + "返回大厅")) {
                        
                        event.setCancelled(true);
                        
                        // 检查是否已经有一个返回任务在运行
                        UUID playerId = player.getUniqueId();
                        long currentTime = System.currentTimeMillis();
                        
                        if (returnTasks.containsKey(playerId)) {
                            // 取消之前的返回任务
                            returnTasks.get(playerId).cancel();
                            returnTasks.remove(playerId);
                            
                            player.sendMessage(ChatColor.YELLOW + "返回大厅已取消!");
                        } else {
                            // 检查是否在冷却时间内（1秒内重复点击）
                            if (lastClickTime.containsKey(playerId) && 
                                (currentTime - lastClickTime.get(playerId)) < 1000) {
                                player.sendMessage(ChatColor.RED + "请不要频繁点击!");
                                return;
                            }
                            
                            lastClickTime.put(playerId, currentTime);
                            
                            // 开始3秒倒计时
                            player.sendMessage(ChatColor.GREEN + "3秒后将返回大厅，再次点击取消...");
                            
                            BukkitTask task = new BukkitRunnable() {
                                int countdown = 3;
                                
                                @Override
                                public void run() {
                                    if (countdown > 0) {
                                        player.sendMessage(ChatColor.YELLOW + "" + countdown + "...");
                                        countdown--;
                                    } else {
                                        // 传送玩家到大厅
                                        sendToLobby(player);
                                        returnTasks.remove(playerId);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 20L); // 每秒执行一次
                            
                            returnTasks.put(playerId, task);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // 玩家离开时取消返回任务
        if (returnTasks.containsKey(playerId)) {
            returnTasks.get(playerId).cancel();
            returnTasks.remove(playerId);
        }
        
        lastClickTime.remove(playerId);
    }

    /**

     * 传送玩家到大厅

     */

    private void sendToLobby(Player player) {

        player.sendMessage(ChatColor.GREEN + "正在传送回大厅...");

        

        // 从游戏中移除玩家

        GameArena arena = ArenaManager.getInstance().getActiveArena();

        if (arena != null) {

            arena.removePlayer(player);

        }

        

        // 使用插件通道发送玩家到大厅服务器

        // 这种方法适用于BungeeCord和Velocity

        try {

            ByteArrayOutputStream b = new ByteArrayOutputStream();

            DataOutputStream out = new DataOutputStream(b);

            

            out.writeUTF("Connect");

            out.writeUTF("Bwlobby");

            

            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

            plugin.getLogger().info("已发送玩家 " + player.getName() + " 到大厅服务器");

        } catch (Exception e) {

            plugin.getLogger().severe("通过插件通道发送玩家时出错: " + e.getMessage());

            e.printStackTrace();

            

            // 如果插件通道失败，回退到命令方式

            fallbackToCommandMethod(player);

        }

    }

    

    /**

     * 回退到命令方式传送玩家

     */

    private void fallbackToCommandMethod(Player player) {

        plugin.getLogger().warning("[NekoBedWars] 插件通道方法失败，回退到命令方式");

        

        // 尝试不同的命令格式，确保兼容性

        boolean success = false;

        

        // 首先尝试标准的命令格式（玩家直接执行）

        try {

            player.performCommand("server Bwlobby");

            success = true;

            plugin.getLogger().info("[NekoBedWars] 尝试使用玩家命令连接到大厅服务器");

        } catch (Exception e) {

            plugin.getLogger().warning("[NekoBedWars] 玩家命令连接失败: " + e.getMessage());

        }

        

        // 如果上面的命令失败，尝试使用控制台命令

        if (!success) {

            try {

                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "server " + player.getName() + " Bwlobby");

                success = true;

                plugin.getLogger().info("[NekoBedWars] 尝试使用控制台命令连接到大厅服务器");

            } catch (Exception e) {

                plugin.getLogger().warning("[NekoBedWars] 控制台命令连接失败: " + e.getMessage());

            }

        }

        

        // 如果所有方法都失败了，发送错误消息

        if (!success) {

            player.sendMessage(ChatColor.RED + "无法连接到大厅服务器，请联系管理员！");

            plugin.getLogger().severe("[NekoBedWars] 无法连接到大厅服务器，所有方法都已尝试");

        }

    }
}