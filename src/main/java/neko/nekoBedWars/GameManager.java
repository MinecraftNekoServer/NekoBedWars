package neko.nekoBedWars;

import neko.nekoBedWars.scoreboard.GameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {
    private NekoBedWars plugin;
    private GameArena arena;
    private int countdown;
    private BukkitTask countdownTask;
    private boolean gameStarted;

    public GameManager(NekoBedWars plugin, GameArena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.countdown = 30; // 默认30秒倒计时
        this.gameStarted = false;
    }

    /**
     * 检查是否可以开始游戏
     */
    public void checkGameStart() {
        // 检查是否有足够的玩家开始游戏
        int playerCount = arena.getPlayers().size();
        int minPlayers = arena.getTeams().size() * 2; // 最少需要每个队伍2人
        
        if (playerCount >= minPlayers && arena.getState() == GameArena.GameState.WAITING) {
            // 开始倒计时
            startCountdown();
        }
    }

    /**
     * 开始倒计时
     */
    private void startCountdown() {
        arena.setState(GameArena.GameState.STARTING);
        
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        
        countdown = plugin.getConfig().getInt("game.countdown", 30);
        
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                // 更新计分板
                updateScoreboard();
                
                // 向所有玩家发送倒计时消息
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (arena.getPlayers().contains(player.getUniqueId())) {
                        player.sendMessage(ChatColor.YELLOW + "游戏将在 " + countdown + " 秒后开始!");
                    }
                }
                
                // 检查倒计时是否结束
                if (countdown <= 0) {
                    startGame();
                    this.cancel();
                    return;
                }
                
                countdown--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 每秒执行一次
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        arena.setState(GameArena.GameState.INGAME);
        gameStarted = true;
        
        // 向所有玩家发送游戏开始消息
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (arena.getPlayers().contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.GREEN + "游戏开始!");
                
                // 为玩家分配队伍和传送位置
                assignPlayerTeamAndTeleport(player);
            }
        }
        
        // 更新计分板
        updateScoreboard();
    }

    /**
     * 为玩家分配队伍并传送到出生点
     */
    private void assignPlayerTeamAndTeleport(Player player) {
        // 为玩家分配队伍
        if (arena.assignTeam(player)) {
            String team = arena.getPlayerTeams().get(player.getUniqueId());
            
            // 传送玩家到对应队伍的出生点
            if (arena.getSpawns().containsKey(team)) {
                player.teleport(arena.getSpawns().get(team));
            }
        }
    }

    /**
     * 更新计分板
     */
    private void updateScoreboard() {
        GameScoreboard scoreboard = plugin.getGameScoreboard();
        scoreboard.updateScoreboard(arena);
    }

    /**
     * 检查游戏是否结束
     */
    public void checkGameEnd() {
        if (arena.getState() == GameArena.GameState.INGAME && arena.isGameOver()) {
            endGame();
        }
    }

    /**
     * 结束游戏
     */
    private void endGame() {
        arena.setState(GameArena.GameState.ENDING);
        
        // 向所有玩家发送游戏结束消息
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (arena.getPlayers().contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.GOLD + "游戏结束!");
            }
        }
        
        // 启动服务器重启倒计时
        startRestartCountdown();
    }

    /**
     * 启动服务器重启倒计时
     */
    private void startRestartCountdown() {
        int restartTime = plugin.getConfig().getInt("game.restart-time", 10);
        
        new BukkitRunnable() {
            int timeLeft = restartTime;
            
            @Override
            public void run() {
                // 向所有玩家发送倒计时消息
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (arena.getPlayers().contains(player.getUniqueId())) {
                        player.sendMessage(ChatColor.YELLOW + "服务器将在 " + timeLeft + " 秒后重启!");
                    }
                }
                
                if (timeLeft <= 0) {
                    // 重启服务器
                    restartServer();
                    this.cancel();
                }
                
                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 每秒执行一次
    }

    /**
     * 重启服务器
     */
    private void restartServer() {
        // 传送玩家到大厅服务器
        plugin.getServer().broadcastMessage(ChatColor.GOLD + "服务器正在重启，请稍后重新连接!");
        
        // 这里应该实现传送玩家到大厅服务器的逻辑
        // 由于这是一个示例，我们只是简单地停止服务器
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // 重启服务器的逻辑
            arena.setState(GameArena.GameState.RESTARTING);
            Bukkit.shutdown();
        }, 60L); // 3秒后重启
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}