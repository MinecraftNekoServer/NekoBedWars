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
            case "setwaitingarea":
                return handleSetWaitingAreaCommand(player);
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

    private boolean handleSetWaitingAreaCommand(Player player) {
        waitingAreaSelectionMode.add(player);
        player.sendMessage("§a请左键点击选择等待区域的第一个点");
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

    private boolean handleSaveCommand(Player player) {
        // TODO: 实现保存配置逻辑
        player.sendMessage("§a地图配置已保存");
        return true;
    }
}