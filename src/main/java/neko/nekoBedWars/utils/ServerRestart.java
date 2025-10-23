package neko.nekoBedWars.utils;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ServerRestart {
    private NekoBedWars plugin;
    private GameArena arena;

    public ServerRestart(NekoBedWars plugin, GameArena arena) {
        this.plugin = plugin;
        this.arena = arena;
    }

    public void restartServer() {
        // 保存所有玩家数据
        plugin.getPlayerData().saveAll();
        
        // 发送重启消息
        Bukkit.broadcastMessage("§6§l游戏结束! 服务器将在10秒后重启...");
        
        // 延迟10秒后重启
        new BukkitRunnable() {
            @Override
            public void run() {
                // 重置地图
                resetWorld();
                
                // 重启服务器
                restart();
            }
        }.runTaskLater(plugin, 200); // 200 ticks = 10 seconds
    }

    private void resetWorld() {
        World world = arena.getWorld();
        if (world != null) {
            String worldName = world.getName();
            
            // 卸载世界
            Bukkit.unloadWorld(world, false);
            
            // 删除世界文件夹
            File worldFolder = new File(worldName);
            if (worldFolder.exists()) {
                deleteDirectory(worldFolder);
            }
            
            // 复制备份世界
            File backupFolder = new File("backup_" + worldName);
            if (backupFolder.exists()) {
                copyDirectory(backupFolder, worldFolder);
            }
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    private void copyDirectory(File source, File target) {
        try {
            Files.walk(source.toPath())
                .forEach(path -> {
                    try {
                        Path targetPath = target.toPath().resolve(source.toPath().relativize(path));
                        Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restart() {
        // 执行重启命令
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }
}