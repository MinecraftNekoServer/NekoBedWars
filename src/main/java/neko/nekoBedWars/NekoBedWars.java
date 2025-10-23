package neko.nekoBedWars;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class NekoBedWars extends JavaPlugin {
    private static NekoBedWars instance;
    private FileConfiguration config;
    private Connection databaseConnection;
    private Logger logger;

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
        
        // 注册指令和事件监听器
        registerCommands();
        registerEvents();
        
        logger.info("NekoBedWars 插件已启用!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
        // TODO: 注册指令
        logger.info("指令注册完成");
    }
    
    private void registerEvents() {
        // TODO: 注册事件监听器
        logger.info("事件监听器注册完成");
    }
    
    public static NekoBedWars getInstance() {
        return instance;
    }
    
    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
