@echo off
echo 正在编译NekoBedWars项目...
javac -cp "lib/spigot-1.12.2.jar;." -d target/classes ^
src/main/java/neko/nekoBedWars/NekoBedWars.java ^
src/main/java/neko/nekoBedWars/GameArena.java ^
src/main/java/neko/nekoBedWars/ArenaManager.java ^
src/main/java/neko/nekoBedWars/commands/BWCommand.java ^
src/main/java/neko/nekoBedWars/database/PlayerData.java ^
src/main/java/neko/nekoBedWars/database/PlayerStats.java ^
src/main/java/neko/nekoBedWars/effects/ParticleEffect.java ^
src/main/java/neko/nekoBedWars/effects/SoundEffect.java ^
src/main/java/neko/nekoBedWars/gui/GameGUI.java ^
src/main/java/neko/nekoBedWars/listeners/GUIListener.java ^
src/main/java/neko/nekoBedWars/listeners/PlayerInteractListener.java ^
src/main/java/neko/nekoBedWars/scoreboard/GameScoreboard.java ^
src/main/java/neko/nekoBedWars/utils/BungeeCordHelper.java ^
src/main/java/neko/nekoBedWars/utils/ServerRestart.java

if %errorlevel% neq 0 (
    echo 编译失败，请检查错误信息。
    exit /b %errorlevel%
)

echo 正在创建NekoBedWars插件jar文件...
if not exist target\classes\neko\nekoBedWars\commands mkdir target\classes\neko\nekoBedWars\commands
if not exist target\classes\neko\nekoBedWars\database mkdir target\classes\neko\nekoBedWars\database
if not exist target\classes\neko\nekoBedWars\effects mkdir target\classes\neko\nekoBedWars\effects
if not exist target\classes\neko\nekoBedWars\gui mkdir target\classes\neko\nekoBedWars\gui
if not exist target\classes\neko\nekoBedWars\listeners mkdir target\classes\neko\nekoBedWars\listeners
if not exist target\classes\neko\nekoBedWars\scoreboard mkdir target\classes\neko\nekoBedWars\scoreboard
if not exist target\classes\neko\nekoBedWars\utils mkdir target\classes\neko\nekoBedWars\utils

xcopy src\main\resources\* target\classes\ /E /Y /Q
xcopy src\main\java\neko\nekoBedWars\*.class target\classes\neko\nekoBedWars\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\commands\*.class target\classes\neko\nekoBedWars\commands\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\database\*.class target\classes\neko\nekoBedWars\database\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\effects\*.class target\classes\neko\nekoBedWars\effects\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\gui\*.class target\classes\neko\nekoBedWars\gui\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\listeners\*.class target\classes\neko\nekoBedWars\listeners\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\scoreboard\*.class target\classes\neko\nekoBedWars\scoreboard\ /Y /Q 2>nul
xcopy src\main\java\neko\nekoBedWars\utils\*.class target\classes\neko\nekoBedWars\utils\ /Y /Q 2>nul

jar cf target\NekoBedWars.jar -C target\classes .

if %errorlevel% == 0 (
    echo 插件jar文件创建成功: target\NekoBedWars.jar
) else (
    echo 创建插件jar文件失败，请检查错误信息。
)