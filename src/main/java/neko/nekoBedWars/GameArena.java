package neko.nekoBedWars;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameArena {
    private String name;
    private World world;
    private Map<String, Location> beds;
    private Map<String, Location> spawns;
    private List<Location> shops;
    private List<Location> upgrades;
    private Location waitingAreaPos1;
    private Location waitingAreaPos2;
    private Location waitingSpawnPoint; // 等待区出生点
    private int maxPlayersPerTeam;
    private List<String> teams;
    private GameState state;
    private Map<UUID, String> playerTeams;
    private Map<String, Integer> teamPlayersCount;
    private List<UUID> players;

    public GameArena(String name, World world) {
        this.name = name;
        this.world = world;
        this.beds = new HashMap<>();
        this.spawns = new HashMap<>();
        this.shops = new ArrayList<>();
        this.upgrades = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.state = GameState.WAITING;
        this.playerTeams = new HashMap<>();
        this.teamPlayersCount = new HashMap<>();
        this.players = new ArrayList<>();
        this.maxPlayersPerTeam = 4; // 默认每队4人
        this.waitingSpawnPoint = null; // 初始化等待区出生点
    }

    public enum GameState {
        WAITING,
        STARTING,
        INGAME,
        ENDING,
        RESTARTING
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Map<String, Location> getBeds() {
        return beds;
    }

    public Map<String, Location> getSpawns() {
        return spawns;
    }

    public List<Location> getShops() {
        return shops;
    }

    public List<Location> getUpgrades() {
        return upgrades;
    }

    public Location getWaitingAreaPos1() {
        return waitingAreaPos1;
    }

    public void setWaitingAreaPos1(Location waitingAreaPos1) {
        this.waitingAreaPos1 = waitingAreaPos1;
    }

    public Location getWaitingAreaPos2() {
        return waitingAreaPos2;
    }

    public void setWaitingAreaPos2(Location waitingAreaPos2) {
        this.waitingAreaPos2 = waitingAreaPos2;
    }

    public Location getWaitingSpawnPoint() {
        return waitingSpawnPoint;
    }

    public void setWaitingSpawnPoint(Location waitingSpawnPoint) {
        this.waitingSpawnPoint = waitingSpawnPoint;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public List<String> getTeams() {
        return teams;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Map<UUID, String> getPlayerTeams() {
        return playerTeams;
    }

    public Map<String, Integer> getTeamPlayersCount() {
        return teamPlayersCount;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    // 添加玩家到对局
    public boolean addPlayer(Player player) {
        if (state != GameState.WAITING && state != GameState.STARTING) {
            return false;
        }

        if (players.size() >= teams.size() * maxPlayersPerTeam) {
            return false;
        }

        players.add(player.getUniqueId());
        return true;
    }

    // 从对局中移除玩家
    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        String team = playerTeams.remove(player.getUniqueId());
        if (team != null) {
            teamPlayersCount.put(team, teamPlayersCount.get(team) - 1);
        }
    }

    // 为玩家分配队伍
    public boolean assignTeam(Player player) {
        for (String team : teams) {
            int count = teamPlayersCount.getOrDefault(team, 0);
            if (count < maxPlayersPerTeam) {
                playerTeams.put(player.getUniqueId(), team);
                teamPlayersCount.put(team, count + 1);
                return true;
            }
        }
        return false;
    }

    // 检查床是否被破坏
    public boolean isBedDestroyed(String team) {
        // TODO: 实现床状态检查逻辑
        return false;
    }

    // 破坏床
    public void destroyBed(String team) {
        // TODO: 实现床破坏逻辑
    }

    // 检查游戏是否结束
    public boolean isGameOver() {
        int activeTeams = 0;
        for (String team : teams) {
            if (!isBedDestroyed(team) && teamPlayersCount.getOrDefault(team, 0) > 0) {
                activeTeams++;
            }
        }
        return activeTeams <= 1;
    }
}