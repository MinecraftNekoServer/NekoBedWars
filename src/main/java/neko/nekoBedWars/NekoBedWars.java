package neko.nekoBedWars;

import neko.nekoBedWars.commands.BWCommand;
import neko.nekoBedWars.database.PlayerData;
import neko.nekoBedWars.listeners.GUIListener;
import neko.nekoBedWars.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = getLogger();
        
        // 保存默认配置文件
        saveDefaultConfig();
        config = getConfig();
        
        // 初始化数据库连接
        initializeDatabase();
        
        // 初始化玩家数据管理器
        if (databaseConnection != null) {
            playerData = new PlayerData(databaseConnection);
        }
        
        // 注册指令和事件监听器
        registerCommands();
        registerEvents();
        
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
        Bukkit.getPluginManager().registerEvents(new GUIListener(this), this);
        logger.info("事件监听器注册完成");
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
}
