package neko.nekoBedWars;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManager {
    private static ArenaManager instance;
    private Map<String, GameArena> arenas;
    private GameArena activeArena;

    private ArenaManager() {
        arenas = new HashMap<>();
    }

    public static ArenaManager getInstance() {
        if (instance == null) {
            instance = new ArenaManager();
        }
        return instance;
    }

    public void loadArenas() {
        // 从配置文件加载地图
        FileConfiguration config = NekoBedWars.getInstance().getConfig();
        
        // 由于是一服一图，只加载一个地图
        String arenaName = config.getString("arena.name");
        String worldName = config.getString("arena.world");
        
        if (arenaName != null && worldName != null) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                GameArena arena = new GameArena(arenaName, world);
                
                // 加载床的位置
                if (config.contains("arena.beds")) {
                    for (String team : config.getConfigurationSection("arena.beds").getKeys(false)) {
                        String locationStr = config.getString("arena.beds." + team);
                        Location location = parseLocation(locationStr, world);
                        if (location != null) {
                            arena.getBeds().put(team, location);
                        }
                    }
                }
                
                // 加载出生点位置
                if (config.contains("arena.spawns")) {
                    for (String team : config.getConfigurationSection("arena.spawns").getKeys(false)) {
                        String locationStr = config.getString("arena.spawns." + team);
                        Location location = parseLocation(locationStr, world);
                        if (location != null) {
                            arena.getSpawns().put(team, location);
                        }
                    }
                }
                
                // 加载商店位置
                if (config.contains("arena.shops")) {
                    for (String locationStr : config.getStringList("arena.shops")) {
                        Location location = parseLocation(locationStr, world);
                        if (location != null) {
                            arena.getShops().add(location);
                        }
                    }
                }
                
                // 加载升级台位置
                if (config.contains("arena.upgrades")) {
                    for (String locationStr : config.getStringList("arena.upgrades")) {
                        Location location = parseLocation(locationStr, world);
                        if (location != null) {
                            arena.getUpgrades().add(location);
                        }
                    }
                }
                
                // 加载等待区域

                String pos1Str = config.getString("arena.waitingarea.pos1");

                String pos2Str = config.getString("arena.waitingarea.pos2");

                if (pos1Str != null && !pos1Str.isEmpty()) {

                    Location pos1 = parseLocation(pos1Str, world);

                    if (pos1 != null) {

                        arena.setWaitingAreaPos1(pos1);

                    }

                }

                if (pos2Str != null && !pos2Str.isEmpty()) {

                    Location pos2 = parseLocation(pos2Str, world);

                    if (pos2 != null) {

                        arena.setWaitingAreaPos2(pos2);

                    }

                }

                

                // 加载等待区出生点

                String waitingSpawnStr = config.getString("arena.waitingspawn");

                if (waitingSpawnStr != null && !waitingSpawnStr.isEmpty()) {

                    Location waitingSpawn = parseLocation(waitingSpawnStr, world);

                    if (waitingSpawn != null) {

                        arena.setWaitingSpawnPoint(waitingSpawn);

                    }

                }

                

                // 加载资源点

                if (config.contains("arena.resources")) {

                    for (String resourceType : config.getConfigurationSection("arena.resources").getKeys(false)) {

                        List<String> locationStrings = config.getStringList("arena.resources." + resourceType);

                        for (String locationStr : locationStrings) {

                            Location location = parseLocation(locationStr, world);

                            if (location != null) {

                                arena.getResourcePoints(resourceType).add(location);

                            }

                        }

                    }

                }

                

                // 加载最大玩家数

                int maxPlayers = config.getInt("arena.maxplayers", 4);

                arena.setMaxPlayersPerTeam(maxPlayers);
                
                if (config.contains("arena.teams")) {
                    arena.getTeams().addAll(config.getStringList("arena.teams"));
                }
                
                // 加载床状态
                if (config.contains("arena.bed-destroyed")) {
                    for (String team : config.getConfigurationSection("arena.bed-destroyed").getKeys(false)) {
                        boolean destroyed = config.getBoolean("arena.bed-destroyed." + team);
                        arena.getBedDestroyed().put(team, destroyed);
                    }
                } else {
                    // 如果没有床状态配置，默认所有床都未被破坏
                    for (String team : arena.getTeams()) {
                        arena.getBedDestroyed().put(team, false);
                    }
                }
                
                arenas.put(arenaName, arena);
                activeArena = arena;

                

                // 标记插件配置完成

                // 不再在这里强制设置配置模式，而是根据配置文件中的设置来决定
                // NekoBedWars.getInstance().setConfigurationMode(false);

                

                // 输出日志

                NekoBedWars.getInstance().getLogger().info("成功加载地图: " + arenaName);

            }

        }

    }

    private Location parseLocation(String locationStr, World world) {
        if (locationStr == null || locationStr.isEmpty()) {
            return null;
        }
        
        String[] parts = locationStr.split(",");
        if (parts.length != 3) {
            return null;
        }
        
        try {
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);
            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public GameArena getActiveArena() {
        return activeArena;
    }

    public Map<String, GameArena> getArenas() {
        return arenas;
    }
    
    public void setActiveArena(GameArena arena) {
        this.activeArena = arena;
    }
}