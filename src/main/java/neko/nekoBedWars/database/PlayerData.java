package neko.nekoBedWars.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private Connection connection;
    private Map<UUID, PlayerStats> cache;

    public PlayerData(Connection connection) {
        this.connection = connection;
        this.cache = new HashMap<>();
        createTable();
    }

    private void createTable() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player_stats (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(16), " +
                "kills INT DEFAULT 0, " +
                "deaths INT DEFAULT 0, " +
                "wins INT DEFAULT 0, " +
                "losses INT DEFAULT 0, " +
                "beds_destroyed INT DEFAULT 0, " +
                "games_played INT DEFAULT 0" +
                ")"
            );
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerStats getPlayerStats(UUID uuid) {
        // 先从缓存中获取
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM player_stats WHERE uuid = ?"
            );
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            PlayerStats stats;
            if (result.next()) {
                stats = new PlayerStats(
                    UUID.fromString(result.getString("uuid")),
                    result.getString("name"),
                    result.getInt("kills"),
                    result.getInt("deaths"),
                    result.getInt("wins"),
                    result.getInt("losses"),
                    result.getInt("beds_destroyed"),
                    result.getInt("games_played")
                );
            } else {
                // 如果数据库中没有记录，创建新的玩家数据
                stats = new PlayerStats(uuid, "Unknown", 0, 0, 0, 0, 0, 0);
                createPlayerStats(stats);
            }

            // 添加到缓存
            cache.put(uuid, stats);
            statement.close();
            return stats;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createPlayerStats(PlayerStats stats) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player_stats (uuid, name, kills, deaths, wins, losses, beds_destroyed, games_played) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, stats.getUuid().toString());
            statement.setString(2, stats.getName());
            statement.setInt(3, stats.getKills());
            statement.setInt(4, stats.getDeaths());
            statement.setInt(5, stats.getWins());
            statement.setInt(6, stats.getLosses());
            statement.setInt(7, stats.getBedsDestroyed());
            statement.setInt(8, stats.getGamesPlayed());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerStats(PlayerStats stats) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE player_stats SET name = ?, kills = ?, deaths = ?, wins = ?, losses = ?, beds_destroyed = ?, games_played = ? " +
                "WHERE uuid = ?"
            );
            statement.setString(1, stats.getName());
            statement.setInt(2, stats.getKills());
            statement.setInt(3, stats.getDeaths());
            statement.setInt(4, stats.getWins());
            statement.setInt(5, stats.getLosses());
            statement.setInt(6, stats.getBedsDestroyed());
            statement.setInt(7, stats.getGamesPlayed());
            statement.setString(8, stats.getUuid().toString());
            statement.execute();
            statement.close();
            
            // 更新缓存
            cache.put(stats.getUuid(), stats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for (PlayerStats stats : cache.values()) {
            updatePlayerStats(stats);
        }
    }
}