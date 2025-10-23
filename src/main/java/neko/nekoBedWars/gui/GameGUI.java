package neko.nekoBedWars.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import java.util.Arrays;

public class GameGUI {
    private Inventory inventory;
    private Player player;

    public GameGUI(Player player) {
        this.player = player;
        createGUI();
    }

    private void createGUI() {
        // 创建一个3行的GUI
        inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&6&l起床战争菜单"));

        // 填充GUI
        fillGUI();
    }

    private void fillGUI() {
        // 创建加入游戏按钮
        ItemStack joinItem = new ItemStack(Material.BED);
        ItemMeta joinMeta = joinItem.getItemMeta();
        joinMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&l加入游戏"));
        joinMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&7点击加入当前地图游戏"),
            ChatColor.translateAlternateColorCodes('&', "&7地图名称: &a" + getCurrentArenaName())
        ));
        joinItem.setItemMeta(joinMeta);
        inventory.setItem(11, joinItem);

        // 创建离开游戏按钮
        ItemStack leaveItem = new ItemStack(Material.BARRIER);
        ItemMeta leaveMeta = leaveItem.getItemMeta();
        leaveMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l离开游戏"));
        leaveMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&7点击离开当前游戏")
        ));
        leaveItem.setItemMeta(leaveMeta);
        inventory.setItem(13, leaveItem);

        // 创建个人数据按钮
        ItemStack statsItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta statsMeta = statsItem.getItemMeta();
        statsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l个人数据"));
        statsMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&7点击查看个人游戏数据")
        ));
        statsItem.setItemMeta(statsMeta);
        inventory.setItem(15, statsItem);

        // 填充背景
        ItemStack background = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta backgroundMeta = background.getItemMeta();
        backgroundMeta.setDisplayName(" ");
        background.setItemMeta(backgroundMeta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, background);
            }
        }
    }

    private String getCurrentArenaName() {
        // TODO: 获取当前地图名称
        return "DefaultMap";
    }

    public void openGUI() {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}