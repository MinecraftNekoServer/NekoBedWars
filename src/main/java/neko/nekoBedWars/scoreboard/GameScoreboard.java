package neko.nekoBedWars.scoreboard;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import java.util.HashMap;
import java.util.Map;

public class GameScoreboard {
    private Scoreboard scoreboard;
    private Objective objective;
    private Map<String, org.bukkit.scoreboard.Team> teams;
    private NekoBedWars plugin;

    public GameScoreboard(NekoBedWars plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.teams = new HashMap<>();
        createObjective();
    }

    private void createObjective() {
        objective = scoreboard.registerNewObjective("bedwars", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', 
            plugin.getConfig().getString("scoreboard.title", "&6&l起床战争")));
    }

    public void updateScoreboard(GameArena arena) {
        // 清空现有的计分板内容
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
        
        // 获取配置的计分板行
        java.util.List<String> lines = plugin.getConfig().getStringList("scoreboard.lines");
        
        int score = lines.size();
        for (String line : lines) {
            // 替换变量
            line = replaceVariables(line, arena);
            
            // 添加到计分板
            objective.getScore(ChatColor.translateAlternateColorCodes('&', line)).setScore(score);
            score--;
        }
    }

    private String replaceVariables(String line, GameArena arena) {
        // 替换变量
        line = line.replace("{arena}", arena.getName());
        line = line.replace("{players}", String.valueOf(arena.getPlayers().size()));
        line = line.replace("{maxplayers}", String.valueOf(arena.getTeams().size() * arena.getMaxPlayersPerTeam()));
        line = line.replace("{teams}", String.valueOf(arena.getTeams().size()));
        line = line.replace("{beds}", String.valueOf(arena.getBeds().size()));
        
        // 添加游戏状态变量
        String gameState = "未知";
        switch (arena.getState()) {
            case WAITING:
                gameState = "等待中";
                break;
            case STARTING:
                gameState = "即将开始";
                break;
            case INGAME:
                gameState = "进行中";
                break;
            case ENDING:
                gameState = "结束中";
                break;
            case RESTARTING:
                gameState = "重启中";
                break;
        }
        line = line.replace("{gamestate}", gameState);
        
        // 添加床剩余数量（未被破坏的床）
        int bedsRemaining = 0;
        for (String team : arena.getTeams()) {
            if (!arena.isBedDestroyed(team)) {
                bedsRemaining++;
            }
        }
        line = line.replace("{beds_remaining}", String.valueOf(bedsRemaining));
        
        // TODO: 实现击杀数统计
        line = line.replace("{kills}", "0");
        
        return line;
    }

    public void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void removePlayer(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}