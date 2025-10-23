package neko.nekoBedWars.database;

import java.util.UUID;

public class PlayerStats {
    private UUID uuid;
    private String name;
    private int kills;
    private int deaths;
    private int wins;
    private int losses;
    private int bedsDestroyed;
    private int gamesPlayed;

    public PlayerStats(UUID uuid, String name, int kills, int deaths, int wins, int losses, int bedsDestroyed, int gamesPlayed) {
        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.wins = wins;
        this.losses = losses;
        this.bedsDestroyed = bedsDestroyed;
        this.gamesPlayed = gamesPlayed;
    }

    // Getters and setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getBedsDestroyed() {
        return bedsDestroyed;
    }

    public void setBedsDestroyed(int bedsDestroyed) {
        this.bedsDestroyed = bedsDestroyed;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    // 便捷方法
    public double getKillDeathRatio() {
        if (deaths == 0) {
            return kills;
        }
        return (double) kills / deaths;
    }

    public double getWinLossRatio() {
        if (losses == 0) {
            return wins;
        }
        return (double) wins / losses;
    }
}