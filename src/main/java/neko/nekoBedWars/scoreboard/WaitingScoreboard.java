package neko.nekoBedWars.scoreboard;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class WaitingScoreboard {
    private Scoreboard scoreboard;
    private Objective objective;
    private NekoBedWars plugin;

    public WaitingScoreboard(NekoBedWars plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        createObjective();
    }

    private void createObjective() {
        objective = scoreboard.registerNewObjective("waiting", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', 
            plugin.getConfig().getString("scoreboard.waiting-title", "&6&l等待大厅")));
    }

    public void updateScoreboard(GameArena arena) {
        // 清空现有的计分板内容
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
        
        // 获取配置的等待区域计分板行
        java.util.List<String> lines = plugin.getConfig().getStringList("scoreboard.waiting-lines");
        
        // 如果没有配置等待区域计分板，则使用默认配置
        if (lines.isEmpty()) {
            lines.add("&f地图: &a{arena}");
            lines.add("&f玩家: &b{players}&7/&b{maxplayers}");
            lines.add("&f状态: &e等待中");
            lines.add("");
            lines.add("&e等待其他玩家加入...");
            lines.add("&fPowered by &dNekoTeam☆");
        }
        
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
        
        // 计算还需要多少玩家
        int playerCount = arena.getPlayers().size();
        int minPlayers = arena.getTeams().size() * 1;
        int playersNeeded = Math.max(0, minPlayers - playerCount);
        line = line.replace("{playersneeded}", String.valueOf(playersNeeded));
        
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