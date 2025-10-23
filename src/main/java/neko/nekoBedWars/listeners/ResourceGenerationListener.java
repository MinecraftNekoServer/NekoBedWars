package neko.nekoBedWars.listeners;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.ItemSpawnEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ResourceGenerationListener implements Listener {
    private NekoBedWars plugin;
    private Map<String, Long> lastGenerationTimes;

    public ResourceGenerationListener(NekoBedWars plugin) {
        this.plugin = plugin;
        this.lastGenerationTimes = new HashMap<>();
        startResourceGeneration();
    }

    /**
     * 启动资源生成任务
     */
    private void startResourceGeneration() {
        // 每20tick（1秒）检查一次资源生成
        new BukkitRunnable() {
            @Override
            public void run() {
                generateResources();
            }
        }.runTaskTimer(plugin, 20L, 20L); // 延迟1秒开始，每1秒执行一次
    }

    /**
     * 生成资源
     */
    private void generateResources() {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena == null || !arena.getState().equals(GameArena.GameState.INGAME)) {
            return;
        }

        // 生成钻石资源（每5秒生成一次）
        generateResourceForType(arena, "diamond", Material.DIAMOND, 5);
        
        // 生成绿宝石资源（每10秒生成一次）
        generateResourceForType(arena, "emerald", Material.EMERALD, 10);
        
        // 生成金资源（每3秒生成一次）
        generateResourceForType(arena, "gold", Material.GOLD_INGOT, 3);
        
        // 生成铁资源（每2秒生成一次）
        generateResourceForType(arena, "iron", Material.IRON_INGOT, 2);
    }

    /**
     * 为特定类型的资源生成物品
     */
    private void generateResourceForType(GameArena arena, String resourceType, Material material, int intervalSeconds) {
        String taskKey = resourceType + "_generation";
        long currentTime = System.currentTimeMillis() / 1000;
        
        // 检查是否应该生成该类型资源
        if (!lastGenerationTimes.containsKey(taskKey)) {
            lastGenerationTimes.put(taskKey, currentTime);
        }
        
        long lastGenerationTime = lastGenerationTimes.get(taskKey);
        if (currentTime - lastGenerationTime >= intervalSeconds) {
            // 更新生成时间
            lastGenerationTimes.put(taskKey, currentTime);
            
            // 生成资源
            List<Location> resourcePoints = arena.getResourcePoints(resourceType);
            for (Location location : resourcePoints) {
                if (location != null && location.getWorld() != null) {
                    // 在资源点生成物品
                    ItemStack itemStack = new ItemStack(material, 1);
                    Item item = location.getWorld().dropItemNaturally(location, itemStack);
                    
                    // 给物品一些随机的运动
                    item.setVelocity(new Vector(
                        (Math.random() - 0.5) * 0.2,
                        Math.random() * 0.2,
                        (Math.random() - 0.5) * 0.2
                    ));
                }
            }
        }
    }
}