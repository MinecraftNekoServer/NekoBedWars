package neko.nekoBedWars.commands;

import neko.nekoBedWars.NekoBedWars;
import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import neko.nekoBedWars.gui.GameGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.HashSet;
import java.util.Set;

public class BWCommand implements CommandExecutor {
    private NekoBedWars plugin;
    private Set<Player> selectionMode1 = new HashSet<>();
    private Set<Player> selectionMode2 = new HashSet<>();
    private Set<Player> waitingAreaSelectionMode = new HashSet<>();
    private Set<Player> bedSelectionMode = new HashSet<>();
    private Set<Player> spawnSelectionMode = new HashSet<>();
    private Set<Player> shopSelectionMode = new HashSet<>();
    private Set<Player> upgradeSelectionMode = new HashSet<>();
    private Set<Player> resourceSelectionMode = new HashSet<>();
    private Set<Player> ncpSelectionMode = new HashSet<>();
    private Set<Player> boundsSelectionMode = new HashSet<>();
    private Set<Player> createModePlayers = new HashSet<>(); // 配置模式玩家
    
    // 用于存储选择的位置
    private Location pos1;
    private Location pos2;

    public BWCommand(NekoBedWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用这个指令");
            return true;
        }

        Player player = (Player) sender;
        
        // 检查权限
        if (args.length > 0 && !args[0].equalsIgnoreCase("join") && 
            !args[0].equalsIgnoreCase("leave") && !args[0].equalsIgnoreCase("stats") &&
            !args[0].equalsIgnoreCase("gui") && !player.hasPermission("nekobedwars.admin")) {
            player.sendMessage("§c你没有权限使用这个指令");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6§l起床战争插件");
            player.sendMessage("§e/bw join <地图名称> §7- 加入指定地图游戏");
            player.sendMessage("§e/bw leave §7- 离开当前游戏");
            player.sendMessage("§e/bw start §7- 强制开始当前游戏");
            player.sendMessage("§e/bw stop §7- 停止当前地图游戏");
            player.sendMessage("§e/bw reload §7- 重新加载配置文件");
            player.sendMessage("§e/bw stats §7- 查看个人游戏数据");
            player.sendMessage("§e/bw gui §7- 打开图形界面快捷操作菜单");
            player.sendMessage("§e/bw setwaitingarea §7- 设置等待区域点（执行两次设置两个点）");
            player.sendMessage("§e/bw setwaitingspawn §7- 设置等待区出生点（使用当前位置）");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join":
                return handleJoinCommand(player, args);
            case "leave":
                return handleLeaveCommand(player);
            case "start":
                return handleStartCommand(player);
            case "stop":
                return handleStopCommand(player);
            case "reload":
                return handleReloadCommand(player);
            case "stats":
                return handleStatsCommand(player);
            case "gui":
                return handleGuiCommand(player);
            case "create":
                return handleCreateCommand(player, args);
            case "setwaitingarea":
                return handleSetWaitingAreaCommand(player);
            case "setwaitingspawn":
                return handleSetWaitingSpawnCommand(player);
            case "setspawn":
                return handleSetSpawnCommand(player, args);
            case "setbed":
                return handleSetBedCommand(player, args);
            case "setshop":
                return handleSetShopCommand(player);
            case "setupgrade":
                return handleSetUpgradeCommand(player);
            case "setresource":
                return handleSetResourceCommand(player, args);
            case "setncp":
                return handleSetNcpCommand(player);
            case "setbounds":
                return handleSetBoundsCommand(player);
            case "setmaxplayers":
                return handleSetMaxPlayersCommand(player, args);
            case "save":
                return handleSaveCommand(player);
            default:
                player.sendMessage("§c未知指令，使用 /bw 查看帮助");
                return true;
        }
    }

    private boolean handleJoinCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /bw join <地图名称>");
            return true;
        }
        
        String arenaName = args[1];
        GameArena arena = ArenaManager.getInstance().getArenas().get(arenaName);
        
        if (arena == null) {
            player.sendMessage("§c地图 " + arenaName + " 不存在");
            return true;
        }
        
        if (arena.addPlayer(player)) {
            player.sendMessage("§a成功加入地图 " + arenaName);
            // TODO: 传送玩家到等待区域
        } else {
            player.sendMessage("§c无法加入地图 " + arenaName + "，可能已满人或游戏已开始");
        }
        
        return true;
    }

    private boolean handleLeaveCommand(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            arena.removePlayer(player);
            player.sendMessage("§a已离开游戏");
            // TODO: 传送玩家到大厅
        } else {
            player.sendMessage("§c你不在游戏中");
        }
        return true;
    }

    private boolean handleStartCommand(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            if (arena.getState() == GameArena.GameState.WAITING || arena.getState() == GameArena.GameState.STARTING) {
                arena.setState(GameArena.GameState.INGAME);
                player.sendMessage("§a游戏已开始");
                // TODO: 实现游戏开始逻辑
            } else {
                player.sendMessage("§c游戏已在进行中或无法开始");
            }
        } else {
            player.sendMessage("§c没有激活的地图");
        }
        return true;
    }

    private boolean handleStopCommand(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            arena.setState(GameArena.GameState.ENDING);
            player.sendMessage("§a游戏已停止");
            // TODO: 实现游戏停止逻辑
        } else {
            player.sendMessage("§c没有激活的地图");
        }
        return true;
    }

    private boolean handleReloadCommand(Player player) {
        plugin.reloadConfig();
        ArenaManager.getInstance().loadArenas();
        player.sendMessage("§a配置文件已重新加载");
        return true;
    }

    private boolean handleStatsCommand(Player player) {
        // TODO: 实现个人数据查询
        player.sendMessage("§a你的游戏数据:");
        player.sendMessage("§e- 击杀数: 0");
        player.sendMessage("§e- 死亡数: 0");
        player.sendMessage("§e- 胜利场次: 0");
        player.sendMessage("§e- 失败场次: 0");
        return true;
    }

    private boolean handleGuiCommand(Player player) {
        // 打开GUI界面
        GameGUI gui = new GameGUI(player);
        gui.openGUI();
        return true;
    }

    private boolean handleCreateCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /bw create <地图名称>");
            return true;
        }
        
        String arenaName = args[1];
        
        // 检查是否已存在同名地图
        if (ArenaManager.getInstance().getArenas().containsKey(arenaName)) {
            player.sendMessage("§c地图 " + arenaName + " 已存在");
            return true;
        }
        
        // 创建新的游戏地图
        GameArena arena = new GameArena(arenaName, player.getWorld());
        ArenaManager.getInstance().getArenas().put(arenaName, arena);
        ArenaManager.getInstance().setActiveArena(arena);
        
        // 启动配置模式
        createModePlayers.add(player);
        
        // 给玩家发放配置工具
        giveConfigurationTools(player);
        
        // 将玩家传送至地图中心
        teleportToCenter(player, arena);
        
        player.sendMessage("§a成功创建地图 " + arenaName + " 并进入配置模式");
        player.sendMessage("§e提示: 使用配置工具点击方块来设置地图元素");
        player.sendMessage("§e使用 /bw save 保存配置");
        
        return true;
    }

    private boolean handleSetWaitingAreaCommand(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            // 使用玩家当前位置作为等待区域的一个点
            Location playerLoc = player.getLocation();
            if (arena.getWaitingAreaPos1() == null) {
                arena.setWaitingAreaPos1(playerLoc);
                player.sendMessage("§a已设置等待区域第一个点");
                player.sendMessage("§e请移动到另一个位置并再次执行此命令设置第二个点");
            } else {
                arena.setWaitingAreaPos2(playerLoc);
                player.sendMessage("§a已设置等待区域第二个点");
                player.sendMessage("§a等待区域已成功设置");
            }
        } else {
            player.sendMessage("§c没有激活的地图");
        }
        return true;
    }

    private boolean handleSetWaitingSpawnCommand(Player player) {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            // 使用玩家当前位置作为等待区出生点
            Location playerLoc = player.getLocation();
            arena.setWaitingSpawnPoint(playerLoc);
            player.sendMessage("§a已设置等待区出生点位置");
        } else {
            player.sendMessage("§c没有激活的地图");
        }
        return true;
    }

    private boolean handleSetSpawnCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /bw setspawn <队伍颜色>");
            return true;
        }
        
        String team = args[1];
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            // 检查队伍是否存在
            if (!arena.getTeams().contains(team)) {
                player.sendMessage("§c队伍 " + team + " 不存在");
                return true;
            }
            
            spawnSelectionMode.add(player);
            player.sendMessage("§a请左键点击设置 " + team + " 队伍的出生点");
        } else {
            player.sendMessage("§c没有激活的地图");
        }
        return true;
    }

    private boolean handleSetBedCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /bw setbed <队伍颜色>");
            return true;
        }
        
        String team = args[1];
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena != null) {
            // 检查队伍是否存在
            if (!arena.getTeams().contains(team)) {
                player.sendMessage("§c队伍 " + team + " 不存在");
                return true;
            }
            
            bedSelectionMode.add(player);
            player.sendMessage("§a请左键点击设置 " + team + " 队伍的床位置");
        } else {
            player.sendMessage("§c没有激活的地图");
        }
        return true;
    }

    private boolean handleSetShopCommand(Player player) {
        shopSelectionMode.add(player);
        player.sendMessage("§a请左键点击设置商店位置");
        return true;
    }

    private boolean handleSetUpgradeCommand(Player player) {
        upgradeSelectionMode.add(player);
        player.sendMessage("§a请左键点击设置升级台位置");
        return true;
    }

    private boolean handleSetResourceCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /bw setresource <资源类型>");
            return true;
        }
        
        String resourceType = args[1];
        resourceSelectionMode.add(player);
        player.sendMessage("§a请左键点击设置 " + resourceType + " 资源点位置");
        return true;
    }

    private boolean handleSetNcpCommand(Player player) {
        ncpSelectionMode.add(player);
        player.sendMessage("§a请左键点击设置NCP位置");
        return true;
    }

    private boolean handleSetBoundsCommand(Player player) {
        boundsSelectionMode.add(player);
        player.sendMessage("§a请左键点击选择游戏区域边界的第一点");
        return true;
    }

    private boolean handleSetMaxPlayersCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /bw setmaxplayers <人数>");
            return true;
        }
        
        try {
            int maxPlayers = Integer.parseInt(args[1]);
            GameArena arena = ArenaManager.getInstance().getActiveArena();
            if (arena != null) {
                arena.setMaxPlayersPerTeam(maxPlayers);
                player.sendMessage("§a已设置每队最大玩家数为 " + maxPlayers);
            } else {
                player.sendMessage("§c没有激活的地图");
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§c人数必须是数字");
        }
        return true;
    }

    
    
    private void giveConfigurationTools(Player player) {
        // 清空玩家物品栏
        player.getInventory().clear();
        
        // 创建带有自定义名称和说明的配置工具
        org.bukkit.inventory.ItemStack bedItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.BED);
        org.bukkit.inventory.ItemStack redstoneTorchItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.REDSTONE_TORCH_ON);
        org.bukkit.inventory.ItemStack chestItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.CHEST);
        org.bukkit.inventory.ItemStack enchantTableItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.ENCHANTMENT_TABLE);
        org.bukkit.inventory.ItemStack diamondItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND);
        org.bukkit.inventory.ItemStack emeraldItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.EMERALD);
        org.bukkit.inventory.ItemStack goldIngotItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_INGOT);
        org.bukkit.inventory.ItemStack ironIngotItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_INGOT);
        org.bukkit.inventory.ItemStack barrierItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.BARRIER);
        
        // 设置物品名称和说明
        setItemDisplayName(bedItem, "§c床位置设置工具", "§7点击方块设置床位置");
        setItemDisplayName(redstoneTorchItem, "§a出生点设置工具", "§7点击方块设置出生点");
        setItemDisplayName(chestItem, "§e商店位置设置工具", "§7点击方块设置商店位置");
        setItemDisplayName(enchantTableItem, "§d升级台设置工具", "§7点击方块设置升级台位置");
        setItemDisplayName(diamondItem, "§b钻石资源点设置工具", "§7点击方块设置钻石资源点");
        setItemDisplayName(emeraldItem, "§a绿宝石资源点设置工具", "§7点击方块设置绿宝石资源点");
        setItemDisplayName(goldIngotItem, "§6金资源点设置工具", "§7点击方块设置金资源点");
        setItemDisplayName(ironIngotItem, "§f铁资源点设置工具", "§7点击方块设置铁资源点");
        setItemDisplayName(barrierItem, "§8边界设置工具", "§7点击两个点设置边界");
        
        // 给玩家发放配置工具
        player.getInventory().setItem(0, bedItem);
        player.getInventory().setItem(1, redstoneTorchItem);
        player.getInventory().setItem(2, chestItem);
        player.getInventory().setItem(3, enchantTableItem);
        player.getInventory().setItem(4, diamondItem);
        player.getInventory().setItem(5, emeraldItem);
        player.getInventory().setItem(6, goldIngotItem);
        player.getInventory().setItem(7, ironIngotItem);
        player.getInventory().setItem(8, barrierItem);
        
        player.sendMessage("§a配置工具已发放，请使用物品栏中的工具进行配置");
    }
    
    private boolean handleSaveCommand(Player player) {
        // 退出配置模式
        createModePlayers.remove(player);
        
        // 保存配置到文件
        saveArenaConfig();
        
        player.sendMessage("§a地图配置已保存");
        return true;
    }
    
    private void saveArenaConfig() {
        GameArena arena = ArenaManager.getInstance().getActiveArena();
        if (arena == null) return;
        
        // 获取配置文件
        org.bukkit.configuration.file.FileConfiguration config = plugin.getConfig();
        
        // 保存地图基本信息
        config.set("arena.name", arena.getName());
        config.set("arena.world", arena.getWorld().getName());
        
        // 保存床位置
        for (java.util.Map.Entry<String, Location> entry : arena.getBeds().entrySet()) {
            String team = entry.getKey();
            Location location = entry.getValue();
            String locationStr = location.getX() + "," + location.getY() + "," + location.getZ();
            config.set("arena.beds." + team, locationStr);
        }
        
        // 保存出生点位置
        for (java.util.Map.Entry<String, Location> entry : arena.getSpawns().entrySet()) {
            String team = entry.getKey();
            Location location = entry.getValue();
            String locationStr = location.getX() + "," + location.getY() + "," + location.getZ();
            config.set("arena.spawns." + team, locationStr);
        }
        
        // 保存商店位置
        java.util.List<String> shopLocations = new java.util.ArrayList<>();
        for (Location location : arena.getShops()) {
            String locationStr = location.getX() + "," + location.getY() + "," + location.getZ();
            shopLocations.add(locationStr);
        }
        config.set("arena.shops", shopLocations);
        
        // 保存升级台位置
        java.util.List<String> upgradeLocations = new java.util.ArrayList<>();
        for (Location location : arena.getUpgrades()) {
            String locationStr = location.getX() + "," + location.getY() + "," + location.getZ();
            upgradeLocations.add(locationStr);
        }
        config.set("arena.upgrades", upgradeLocations);
        
        // 保存等待区域
        if (arena.getWaitingAreaPos1() != null) {
            Location pos1 = arena.getWaitingAreaPos1();
            String pos1Str = pos1.getX() + "," + pos1.getY() + "," + pos1.getZ();
            config.set("arena.waitingarea.pos1", pos1Str);
        }
        if (arena.getWaitingAreaPos2() != null) {
            Location pos2 = arena.getWaitingAreaPos2();
            String pos2Str = pos2.getX() + "," + pos2.getY() + "," + pos2.getZ();
            config.set("arena.waitingarea.pos2", pos2Str);
        }
        
        // 保存等待区出生点
        if (arena.getWaitingSpawnPoint() != null) {
            Location spawnPoint = arena.getWaitingSpawnPoint();
            String spawnPointStr = spawnPoint.getX() + "," + spawnPoint.getY() + "," + spawnPoint.getZ();
            config.set("arena.waitingspawn", spawnPointStr);
        }
        
        // 保存最大玩家数
        config.set("arena.maxplayers", arena.getMaxPlayersPerTeam());
        
        // 保存队伍列表（从床的位置推断）
        java.util.List<String> teams = new java.util.ArrayList<>(arena.getBeds().keySet());
        config.set("arena.teams", teams);
        
        // 保存配置文件
        plugin.saveConfig();
    }
    
    private void setItemDisplayName(org.bukkit.inventory.ItemStack item, String displayName, String lore) {
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            java.util.List<String> loreList = new java.util.ArrayList<>();
            loreList.add(lore);
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
    }
    
    private void teleportToCenter(Player player, GameArena arena) {
        // 计算地图中心位置
        Location center = player.getWorld().getSpawnLocation(); // 简化处理，使用世界出生点
        player.teleport(center);
        
        // 设置玩家视角居中
        player.setCompassTarget(center);
        
        player.sendMessage("§a已传送至地图中心");
    }
}