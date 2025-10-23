package neko.nekoBedWars;

import neko.nekoBedWars.scoreboard.GameScoreboard;
import neko.nekoBedWars.scoreboard.WaitingScoreboard;
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
    private BukkitTask gameTask;
    private boolean gameStarted;

    public GameManager(NekoBedWars plugin, GameArena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.countdown = 30; // 默认30秒倒计时
        this.gameStarted = false;
    }

    /**
     * 初始化游戏管理器
     */
    public void initialize() {
        // 启动游戏状态检查任务
        startGameTask();
        
        // 启动等待状态消息发送任务
        startWaitingMessageTask();
    }

    /**
     * 启动游戏状态检查任务
     */
    private void startGameTask() {
        if (gameTask != null) {
            gameTask.cancel();
        }
        
        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                // 只在游戏模式下检查游戏状态
                if (!plugin.isConfigurationMode()) {
                    checkGameState();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L); // 每秒执行一次
    }

    /**
     * 启动等待状态消息发送任务
     */
    private void startWaitingMessageTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // 只在游戏模式下且处于等待状态时发送消息
                if (!plugin.isConfigurationMode() && arena.getState() == GameArena.GameState.WAITING) {
                    sendWaitingStatusMessages();
                }
            }
        }.runTaskTimer(plugin, 100L, 1200L); // 延迟5秒开始，每60秒执行一次
    }

    /**
     * 检查游戏状态并执行相应逻辑
     */
    public void checkGameState() {
        if (arena == null) return;
        
        switch (arena.getState()) {
            case WAITING:
                handleWaitingState();
                break;
            case STARTING:
                // 倒计时已经在startCountdown方法中处理
                break;
            case INGAME:
                handleIngameState();
                break;
            case ENDING:
                handleEndingState();
                break;
            case RESTARTING:
                handleRestartingState();
                break;
        }
    }

    /**
     * 处理等待状态
     */
    private void handleWaitingState() {
        // 检查是否有足够的玩家开始游戏
        int playerCount = arena.getPlayers().size();
        int minPlayers = arena.getTeams().size() * 1; // 最少需要每个队伍1人就可以开始
        
        if (playerCount >= minPlayers && arena.getState() == GameArena.GameState.WAITING) {
            // 开始倒计时
            startCountdown();
        }
        
        // 定期更新计分板
        updateScoreboard();
        
        // 定期向所有玩家发送等待状态提示
        sendWaitingStatusMessages();
    }

    /**
     * 向所有等待中的玩家发送状态消息
     */
    private void sendWaitingStatusMessages() {
        int playerCount = arena.getPlayers().size();
        int minPlayers = arena.getTeams().size() * 1;
        int playersNeeded = Math.max(0, minPlayers - playerCount);
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (arena.getPlayers().contains(player.getUniqueId())) {
                // 不再强制传送玩家到等待区域出生点，允许玩家自由移动
                // 只有在必要时才发送状态更新消息，避免刷屏
            }
        }
    }

    /**
     * 处理游戏进行状态
     */
    private void handleIngameState() {
        // 检查游戏是否结束
        if (arena.isGameOver()) {
            endGame();
            return;
        }
        
        // 定期更新计分板
        updateScoreboard();
        
        // 可以在这里添加其他游戏进行中的逻辑
        // 例如：资源生成、床破坏检测等
    }

    /**
     * 处理游戏结束状态
     */
    private void handleEndingState() {
        // 游戏结束逻辑已经在endGame方法中处理
        // 这里可以添加额外的结束逻辑
    }

    /**
     * 处理服务器重启状态
     */
    private void handleRestartingState() {
        // 服务器重启逻辑已经在restartServer方法中处理
        // 这里可以添加额外的重启前清理工作
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
                // 更新等待区域计分板
                updateWaitingScoreboard();
                
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
                
                // 切换到游戏计分板
                plugin.getGameScoreboard().addPlayer(player);
                plugin.getGameScoreboard().updateScoreboard(arena);
                
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
     * 更新等待区域计分板
     */
    private void updateWaitingScoreboard() {
        WaitingScoreboard scoreboard = plugin.getWaitingScoreboard();
        scoreboard.updateScoreboard(arena);
    }

    /**
     * 结束游戏
     */
    private void endGame() {
        arena.setState(GameArena.GameState.ENDING);
        
        // 确定胜利队伍
        String winnerTeam = determineWinner();
        
        // 向所有玩家发送游戏结束消息
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (arena.getPlayers().contains(player.getUniqueId())) {
                if (winnerTeam != null) {
                    player.sendMessage(ChatColor.GOLD + "游戏结束! " + winnerTeam + " 队获得胜利!");
                } else {
                    player.sendMessage(ChatColor.GOLD + "游戏结束! 没有队伍获得胜利!");
                }
            }
        }
        
        // 更新玩家统计数据
        updatePlayerStats();
        
        // 启动服务器重启倒计时
        startRestartCountdown();
    }

    /**
     * 确定胜利队伍
     */
    private String determineWinner() {
        // 查找还有床且还有玩家的队伍
        for (String team : arena.getTeams()) {
            if (!arena.isBedDestroyed(team) && arena.getTeamPlayersCount().getOrDefault(team, 0) > 0) {
                return team;
            }
        }
        return null;
    }

    /**
     * 更新玩家统计数据
     */
    private void updatePlayerStats() {
        // TODO: 实现玩家统计数据更新逻辑
        // 这里应该更新玩家的胜利/失败记录、击杀数等
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
    
    /**
     * 停止所有任务
     */
    public void stopTasks() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        if (gameTask != null) {
            gameTask.cancel();
        }
    }
}