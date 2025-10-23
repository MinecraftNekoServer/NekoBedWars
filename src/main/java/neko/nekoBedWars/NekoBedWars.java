package neko.nekoBedWars;

import neko.nekoBedWars.commands.BWCommand;
import neko.nekoBedWars.database.PlayerData;
import neko.nekoBedWars.listeners.GUIListener;
import neko.nekoBedWars.listeners.PlayerInteractListener;
import neko.nekoBedWars.scoreboard.GameScoreboard;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class NekoBedWars extends JavaPlugin {
    private static NekoBedWars instance;
    private FileConfiguration config;
    private Connection databaseConnection;
    private Logger logger;
    private PlayerData playerData;
    private GUIListener guiListener;
    private boolean configurationMode = true; // 配置模式标记，默认为true
    private GameScoreboard gameScoreboard; // 游戏计分板
    private GameManager gameManager; // 游戏管理器

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = getLogger();
        
        // 保存默认配置文件
        saveDefaultConfig();
        config = getConfig();
        
        // 检查是否已经配置完成
        checkConfigurationStatus();
        
        // 初始化数据库连接
        initializeDatabase();
        
        // 初始化玩家数据管理器
        if (databaseConnection != null) {
            playerData = new PlayerData(databaseConnection);
        }
        
        // 初始化游戏计分板
        gameScoreboard = new GameScoreboard(this);
        
        // 加载地图配置
        ArenaManager.getInstance().loadArenas();
        
        // 初始化游戏管理器
        GameArena activeArena = ArenaManager.getInstance().getActiveArena();
        if (activeArena != null) {
            gameManager = new GameManager(this, activeArena);
        }
        
        // 输出当前地图信息
        if (activeArena != null) {
            logger.info("当前激活地图: " + activeArena.getName());
            logger.info("插件模式: " + (configurationMode ? "配置模式" : "游戏模式"));
        } else {
            logger.info("当前没有激活的地图");
            logger.info("插件模式: " + (configurationMode ? "配置模式" : "游戏模式"));
        }
        
        // 注册指令和事件监听器
        registerCommands();
        registerEvents();
        
        // 启动天气锁定任务
        startWeatherLockTask();
        
        // 启动游戏检查任务
        startGameCheckTask();
        
        logger.info("NekoBedWars 插件已启用!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // 保存所有玩家数据
        if (playerData != null) {
            playerData.saveAll();
        }
        
        // 关闭数据库连接
        if (databaseConnection != null) {
            try {
                databaseConnection.close();
            } catch (SQLException e) {
                logger.severe("关闭数据库连接时出错: " + e.getMessage());
            }
        }
        
        logger.info("NekoBedWars 插件已禁用!");
    }
    
    private void initializeDatabase() {
        String dbType = config.getString("database.type", "SQLite");
        
        try {
            if ("MySQL".equalsIgnoreCase(dbType)) {
                String host = config.getString("database.mysql.host");
                int port = config.getInt("database.mysql.port");
                String database = config.getString("database.mysql.database");
                String username = config.getString("database.mysql.username");
                String password = config.getString("database.mysql.password");
                
                String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
                databaseConnection = DriverManager.getConnection(url, username, password);
            } else {
                // SQLite 作为默认选项
                String path = config.getString("database.path", "db.sql");
                String url = "jdbc:sqlite:" + getDataFolder() + "/" + path;
                databaseConnection = DriverManager.getConnection(url);
            }
            
            logger.info("数据库连接成功: " + dbType);
        } catch (SQLException e) {
            logger.severe("数据库连接失败: " + e.getMessage());
        }
    }
    
    private void checkConfigurationStatus() {
        // 检查配置文件中是否已标记为配置完成
        configurationMode = config.getBoolean("arena.configured", true);
    }
    
    private void registerCommands() {
        // 注册BW指令
        PluginCommand bwCommand = getCommand("bw");
        if (bwCommand != null) {
            bwCommand.setExecutor(new BWCommand(this));
        }
        logger.info("指令注册完成");
    }
    
    private void registerEvents() {
        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        guiListener = new GUIListener(this);
        Bukkit.getPluginManager().registerEvents(guiListener, this);
        logger.info("事件监听器注册完成");
    }
    
    private void startWeatherLockTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // 锁定所有世界的天气为晴朗无雨
                for (World world : Bukkit.getWorlds()) {
                    world.setStorm(false);
                    world.setThundering(false);
                    world.setWeatherDuration(0);
                }
            }
        }.runTaskTimer(this, 0L, 6000L); // 每5分钟检查一次天气
    }
    
    public static NekoBedWars getInstance() {
        return instance;
    }
    
    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
    
    public PlayerData getPlayerData() {
        return playerData;
    }
    
    public GUIListener getGuiListener() {
        return guiListener;
    }
    
    public boolean isConfigurationMode() {
        return configurationMode;
    }
    
    public void setConfigurationMode(boolean configurationMode) {
        this.configurationMode = configurationMode;
    }
    
    public GameScoreboard getGameScoreboard() {
        return gameScoreboard;
    }
    
    public GameManager getGameManager() {
        return gameManager;
    }
    
    /**
     * 启动游戏检查任务
     */
    private void startGameCheckTask() {
        // 每5秒检查一次游戏状态
        getServer().getScheduler().runTaskTimer(this, () -> {
            if (gameManager != null && !configurationMode) {
                gameManager.checkGameStart();
                gameManager.checkGameEnd();
            }
        }, 100L, 100L); // 延迟5秒开始，每5秒执行一次
    }
}
