package neko.nekoBedWars.utils;

import neko.nekoBedWars.NekoBedWars;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BungeeCordHelper implements PluginMessageListener {
    private NekoBedWars plugin;

    public BungeeCordHelper(NekoBedWars plugin) {
        this.plugin = plugin;
        // 注册BungeeCord通道
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
    }

    public void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public void sendPlayerToBwlobby(Player player) {
        sendPlayerToServer(player, "Bwlobby");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        // 处理从BungeeCord接收到的消息
        // 这里可以添加处理逻辑，如果需要的话
    }
}