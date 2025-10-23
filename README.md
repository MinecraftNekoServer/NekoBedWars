# NekoBedWars

这是一个基于 Minecraft 1.12.2 版本开发的起床战争（Bed Wars）插件。

## 功能特点

- 支持多人在线对战
- 数据库存储所有玩家的游戏数据
- 适配 Minecraft 1.12.2 服务端
- 对局结束后自动重启服务器并重置地图
- 对局结束后将玩家送回 Bwlobby 服务器
- 创建计分板显示游戏当前对局数据
- 粒子效果和音效反馈增强游戏体验

## 游戏状态

### 等待中
- 玩家加入游戏后在等待区域等待其他玩家加入
- 达到最少玩家数后开始倒计时
- 游戏开始前清空等待区域

### 游戏进行中
- 玩家可以破坏方块、放置方块
- 玩家可以攻击其他玩家和破坏床
- 玩家可以通过商店购买物品和升级

### 游戏结束
- 当一方的床被破坏且所有玩家死亡后游戏结束
- 对局结束后自动重启服务器并重置地图
- 玩家将被送回 Bwlobby 服务器

## 数据库配置

插件使用数据库来存储玩家的游戏数据，包括但不限于以下信息：
- 玩家等级
- 击杀数
- 死亡数
- 胜利场次
- 失败场次

请确保在配置文件中正确设置数据库连接信息。

## 地图配置

本插件采用一服一图模式，不支持一服多图。

地图配置文件位于 `plugins/NekoBedWars/config.yml`，配置项说明如下：
- `name`: 地图名称
- `world`: 地图所在世界名称
- `beds`: 各队伍床的位置坐标
- `spawns`: 各队伍出生点坐标
- `shops`: 商店位置坐标
- `upgrades`: 升级台位置坐标
- `waitingarea`: 等待区域的两个边界点坐标
- `waitingspawn`: 等待区出生点坐标
- `maxplayers`: 每队最大玩家数（默认4人）
- `teams`: 队伍配置（最多8个队伍）

示例配置：
```yaml
arena:
  name: "DefaultMap"
  world: "bedwars_world"
  beds:
    red: "100,64,100"
    blue: "100,64,200"
  spawns:
    red: "95,65,95"
    blue: "105,65,205"
  shops:
    - "100,64,105"
    - "100,64,195"
  upgrades:
    - "95,64,100"
    - "105,64,200"
  waitingarea:
    pos1: "50,64,50"
    pos2: "150,80,150"
  waitingspawn: "100,65,150"
  maxplayers: 4
  teams:
    - "red"
    - "blue"
```
```

## 变量使用方式

插件支持在配置文件和消息中使用 PlaceholderAPI (PAPI) 变量。所有变量均需通过 PAPI 进行解析。

示例变量：
- `%player_name%`: 玩家名称
- `%player_kill_count%`: 击杀数
- `%player_death_count%`: 死亡数
- `%player_win_count%`: 胜利场次
- `%player_loss_count%`: 失败场次
- `%arena_name%`: 当前地图名称
- `%team_name%`: 队伍名称

## 指令使用

### 基本指令
- `/bw join <地图名称>`: 加入指定地图游戏
- `/bw leave`: 离开当前游戏
- `/bw start`: 强制开始当前游戏（需要权限）
- `/bw stop`: 停止当前地图游戏（需要权限）
- `/bw reload`: 重新加载配置文件（需要权限）
- `/bw stats`: 查看个人游戏数据
- `/bw gui`: 打开图形界面快捷操作菜单
- `/bw create <地图名称>`: 创建新地图并进入配置模式

### 动态配置指令
- `/bw setwaitingarea`: 设置等待区域（执行两次设置两个点，使用玩家当前位置）
- `/bw setwaitingspawn`: 设置等待区出生点（使用玩家当前位置）
- `/bw setspawn <队伍颜色>`: 设置指定队伍的出生点
- `/bw setbed <队伍颜色>`: 设置指定队伍的床位置
- `/bw setshop`: 设置商店位置
- `/bw setupgrade`: 设置升级台位置
- `/bw setresource <资源类型>`: 设置指定类型的资源点位置
- `/bw setncp`: 设置NCP位置
- `/bw setbounds`: 设置游戏区域边界点（需要设置两个点）
- `/bw setmaxplayers <人数>`: 设置每队最大玩家数（默认4人）
- `/bw save`: 保存当前地图配置