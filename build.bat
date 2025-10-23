@echo off
echo 正在编译NekoBedWars项目...
javac -cp "lib/spigot-1.12.2.jar;." -d target/classes ^
src/main/java/neko/nekoBedWars/NekoBedWars.java ^
src/main/java/neko/nekoBedWars/GameArena.java ^
src/main/java/neko/nekoBedWars/ArenaManager.java ^
src/main/java/neko/nekoBedWars/GameManager.java ^
src/main/java/neko/nekoBedWars/commands/BWCommand.java ^
src/main/java/neko/nekoBedWars/database/PlayerData.java ^
src/main/java/neko/nekoBedWars/database/PlayerStats.java ^
src/main/java/neko/nekoBedWars/effects/ParticleEffect.java ^
src/main/java/neko/nekoBedWars/effects/SoundEffect.java ^
src/main/java/neko/nekoBedWars/gui/GameGUI.java ^
src/main/java/neko/nekoBedWars/gui/TeamSelectionGUI.java ^
src/main/java/neko/nekoBedWars/listeners/GUIListener.java ^
src/main/java/neko/nekoBedWars/listeners/HungerListener.java ^
src/main/java/neko/nekoBedWars/listeners/LobbyReturnListener.java ^
src/main/java/neko/nekoBedWars/listeners/PVPListener.java ^
src/main/java/neko/nekoBedWars/listeners/PlayerInteractListener.java ^
src/main/java/neko/nekoBedWars/listeners/PlayerJoinListener.java ^
src/main/java/neko/nekoBedWars/listeners/WaitingAreaListener.java ^
src/main/java/neko/nekoBedWars/scoreboard/GameScoreboard.java ^
src/main/java/neko/nekoBedWars/scoreboard/WaitingScoreboard.java ^
src/main/java/neko/nekoBedWars/utils/BungeeCordHelper.java ^
src/main/java/neko/nekoBedWars/utils/ServerRestart.java

if %errorlevel% == 0 (
    echo 编译成功!
) else (
    echo 编译失败，请检查错误信息。
)