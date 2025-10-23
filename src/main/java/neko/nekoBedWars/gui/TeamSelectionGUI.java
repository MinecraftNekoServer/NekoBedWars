package neko.nekoBedWars.gui;

import neko.nekoBedWars.GameArena;
import neko.nekoBedWars.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TeamSelectionGUI {
    private Inventory inventory;
    private Player player;
    private LocationType locationType;
    private org.bukkit.Location location;
    
    public enum LocationType {
        BED,
        SPAWN
    }
    
    public TeamSelectionGUI(Player player, LocationType locationType, org.bukkit.Location location) {
        this.player = player;
        this.locationType = locationType;
        this.location = location;
        createGUI();
    }
    
    private void createGUI() {
        // 创建一个3行的GUI
        inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&6&l选择队伍颜色"));
        
        // 填充GUI
        fillGUI();
    }
    
    private void fillGUI() {
        // 创建八个队伍颜色的羊毛方块
        createTeamWoolItem(0, Material.WOOL, (short) 14, "红色", "red");
        createTeamWoolItem(1, Material.WOOL, (short) 11, "蓝色", "blue");
        createTeamWoolItem(2, Material.WOOL, (short) 13, "绿色", "green");
        createTeamWoolItem(3, Material.WOOL, (short) 4, "黄色", "yellow");
        createTeamWoolItem(4, Material.WOOL, (short) 1, "橙色", "orange");
        createTeamWoolItem(5, Material.WOOL, (short) 2, "紫色", "purple");
        createTeamWoolItem(6, Material.WOOL, (short) 6, "粉色", "pink");
        createTeamWoolItem(7, Material.WOOL, (short) 0, "白色", "white");
        
        // 填充背景
        ItemStack background = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta backgroundMeta = background.getItemMeta();
        backgroundMeta.setDisplayName(" ");
        background.setItemMeta(backgroundMeta);
        
        for (int i = 8; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, background);
            }
        }
    }
    
    private void createTeamWoolItem(int slot, Material material, short color, String displayName, String teamName) {
        ItemStack woolItem = new ItemStack(material, 1, color);
        ItemMeta woolMeta = woolItem.getItemMeta();
        woolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&" + getColorCode(teamName) + "&l" + displayName + "队伍"));
        woolMeta.setLore(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&7点击选择&" + getColorCode(teamName) + teamName + "&7队伍")
        ));
        woolItem.setItemMeta(woolMeta);
        inventory.setItem(slot, woolItem);
    }
    
    private String getColorCode(String teamName) {
        switch (teamName.toLowerCase()) {
            case "red": return "c";
            case "blue": return "9";
            case "green": return "a";
            case "yellow": return "e";
            case "orange": return "6";
            case "purple": return "5";
            case "pink": return "d";
            case "white": return "f";
            default: return "f";
        }
    }
    
    public void openGUI() {
        player.openInventory(inventory);
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public LocationType getLocationType() {
        return locationType;
    }
    
    public org.bukkit.Location getLocation() {
        return location;
    }
    
    public Player getPlayer() {
        return player;
    }
}