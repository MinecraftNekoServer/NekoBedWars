# NekoBedWars

一个基于ScreamingBedWars的灵活的BedWars小游戏插件，适用于Minecraft: Java版。

## 功能特性

- 多人游戏支持
- 自定义商店系统
- 统计数据追踪
- 多种特殊物品
- 可配置的游戏规则
- 支持BungeeCord网络
- 数据库存储支持

## 安装说明

1. 将插件JAR文件放入服务器的`plugins`文件夹中
2. 启动服务器以生成配置文件
3. 根据需要配置`config.yml`和商店文件
4. 重启服务器以应用更改

## 推荐依赖插件
| 插件名称                          | 用途                | 是否必须          |
| ----------------------------- | ----------------- | ------------- |
| **Vault**                     | 经济系统（比如商店用金币）     | ⚙️ 推荐（很多插件依赖） |
| **PlaceholderAPI**            | 支持计分板 / 前缀显示变量    | ⚙️ 推荐         |
| **TAB / Scoreboard plugins**  | 自定义计分板、显示队伍信息     | 可选            |
| **Citizens**                  | 用 NPC 开商店、传送、展示队伍 | 可选            |
| **Multiverse-Core**           | 管理多世界（如果你想一个服多图）  | 可选            |
| **FastAsyncWorldEdit (FAWE)** | 快速建图 / 重置地图更快     | 可选            |
| **WorldGuard + WorldEdit**    | 保护地图、防止破坏         | 可选            |
| **LuckPerms**                 | 权限系统（管理玩家指令权限）    | ⚙️ 推荐         |

## PAPI变量

插件支持PlaceholderAPI变量，可以在记分板、聊天、标题等地方使用。

### 游戏相关变量
- `%bedwars_game_<游戏名>_name%` - 游戏名称
- `%bedwars_game_<游戏名>_displayname%` - 游戏显示名称
- `%bedwars_game_<游戏名>_players%` - 当前玩家数
- `%bedwars_game_<游戏名>_maxplayers%` - 最大玩家数
- `%bedwars_game_<游戏名>_minplayers%` - 最少玩家数
- `%bedwars_game_<游戏名>_time%` - 游戏剩余时间
- `%bedwars_game_<游戏名>_timeformat%` - 格式化的游戏剩余时间
- `%bedwars_game_<游戏名>_elapsedtime%` - 游戏已进行时间
- `%bedwars_game_<游戏名>_elapsedtimeformat%` - 格式化的游戏已进行时间
- `%bedwars_game_<游戏名>_world%` - 游戏世界名称
- `%bedwars_game_<游戏名>_state%` - 游戏状态
- `%bedwars_game_<游戏名>_running%` - 游戏是否正在进行
- `%bedwars_game_<游戏名>_waiting%` - 游戏是否在等待中
- `%bedwars_game_<游戏名>_available_teams%` - 可用队伍数
- `%bedwars_game_<游戏名>_connected_teams%` - 已连接队伍数
- `%bedwars_game_<游戏名>_teamchests%` - 队伍箱子数

### 队伍相关变量
- `%bedwars_game_<游戏名>_team_<队伍名>_colored%` - 彩色队伍名称
- `%bedwars_game_<游戏名>_team_<队伍名>_color%` - 队伍颜色
- `%bedwars_game_<游戏名>_team_<队伍名>_ingame%` - 队伍是否在游戏中
- `%bedwars_game_<游戏名>_team_<队伍名>_players%` - 队伍玩家数
- `%bedwars_game_<游戏名>_team_<队伍名>_maxplayers%` - 队伍最大玩家数
- `%bedwars_game_<游戏名>_team_<队伍名>_targetvalid%` - 队伍目标是否有效
- `%bedwars_game_<游戏名>_team_<队伍名>_targetvalidsymbol%` - 队伍目标状态符号
- `%bedwars_game_<游戏名>_team_<队伍名>_teamchests%` - 队伍箱子数

### 玩家当前游戏变量
- `%bedwars_current_game%` - 当前游戏名称
- `%bedwars_current_game_displayname%` - 当前游戏显示名称
- `%bedwars_current_game_players%` - 当前游戏玩家数
- `%bedwars_current_game_time%` - 当前游戏剩余时间
- `%bedwars_current_game_timeformat%` - 格式化的当前游戏剩余时间
- `%bedwars_current_game_elapsedtime%` - 当前游戏已进行时间
- `%bedwars_current_game_elapsedtimeformat%` - 格式化的当前游戏已进行时间
- `%bedwars_current_game_maxplayers%` - 当前游戏最大玩家数
- `%bedwars_current_game_minplayers%` - 当前游戏最少玩家数
- `%bedwars_current_game_world%` - 当前游戏世界名称
- `%bedwars_current_game_state%` - 当前游戏状态
- `%bedwars_current_game_running%` - 当前游戏是否正在进行
- `%bedwars_current_game_waiting%` - 当前游戏是否在等待中
- `%bedwars_current_team%` - 当前队伍名称
- `%bedwars_current_team_colored%` - 彩色当前队伍名称
- `%bedwars_current_team_color%` - 当前队伍颜色
- `%bedwars_current_team_players%` - 当前队伍玩家数
- `%bedwars_current_team_maxplayers%` - 当前队伍最大玩家数
- `%bedwars_current_team_targetvalid%` - 当前队伍目标是否有效
- `%bedwars_current_team_targetvalidsymbol%` - 当前队伍目标状态符号

### 统计数据变量
- `%bedwars_stats_deaths%` - 玩家死亡数
- `%bedwars_stats_destroyed_beds%` - 玩家破坏床数
- `%bedwars_stats_kills%` - 玩家击杀数
- `%bedwars_stats_loses%` - 玩家失败数
- `%bedwars_stats_score%` - 玩家分数
- `%bedwars_stats_wins%` - 玩家胜利数
- `%bedwars_stats_games%` - 玩家游戏数
- `%bedwars_stats_kd%` - 玩家KD比率

### 其他玩家统计数据变量
- `%bedwars_otherstats_<玩家名>_deaths%` - 其他玩家死亡数
- `%bedwars_otherstats_<玩家名>_destroyed_beds%` - 其他玩家破坏床数
- `%bedwars_otherstats_<玩家名>_kills%` - 其他玩家击杀数
- `%bedwars_otherstats_<玩家名>_loses%` - 其他玩家失败数
- `%bedwars_otherstats_<玩家名>_score%` - 其他玩家分数
- `%bedwars_otherstats_<玩家名>_wins%` - 其他玩家胜利数
- `%bedwars_otherstats_<玩家名>_games%` - 其他玩家游戏数
- `%bedwars_otherstats_<玩家名>_kd%` - 其他玩家KD比率

### 排行榜变量
- `%bedwars_leaderboard_score_<排名>_name%` - 排行榜指定排名玩家名称
- `%bedwars_leaderboard_score_<排名>_score%` - 排行榜指定排名玩家分数
- `%bedwars_leaderboard_score_<排名>_kills%` - 排行榜指定排名玩家击杀数
- `%bedwars_leaderboard_score_<排名>_deaths%` - 排行榜指定排名玩家死亡数
- `%bedwars_leaderboard_score_<排名>_destroyed_beds%` - 排行榜指定排名玩家破坏床数
- `%bedwars_leaderboard_score_<排名>_wins%` - 排行榜指定排名玩家胜利数
- `%bedwars_leaderboard_score_<排名>_loses%` - 排行榜指定排名玩家失败数
- `%bedwars_leaderboard_score_<排名>_games%` - 排行榜指定排名玩家游戏数
- `%bedwars_leaderboard_score_<排名>_kd%` - 排行榜指定排名玩家KD比率

## 指令

### 玩家指令

- `/bw join [游戏名]` - 加入指定游戏，如果不指定游戏名则自动加入玩家最多的可用游戏
- `/bw leave` - 离开当前游戏
- `/bw autojoin` - 自动加入最佳游戏
- `/bw list` - 列出所有可用游戏
- `/bw stats [玩家名]` - 查看个人统计数据，管理员可以查看其他玩家的数据
- `/bw rejoin` - 重新加入上一场比赛
- `/bw leaderboard` - 显示排行榜
- `/bw gamesinv` - 打开游戏菜单
- `/bw party` - 队伍命令
- `/bw mainlobby` - 传送到主大厅
- `/bw group` - 游戏组命令
- `/bw joingroup` - 加入指定游戏组

### 管理员指令

- `/bw admin add <游戏名> [变体]` - 创建新游戏
- `/bw admin edit <游戏名>` - 编辑游戏
- `/bw admin save <游戏名>` - 保存游戏
- `/bw admin remove <游戏名>` - 删除游戏
- `/bw admin info <游戏名>` - 查看游戏信息
- `/bw admin interactive` - 交互式编辑模式

#### 游戏编辑子命令

- `/bw admin <游戏名> pos1` - 设置游戏区域的第一个角落
- `/bw admin <游戏名> pos2` - 设置游戏区域的第二个角落
- `/bw admin <游戏名> lobby` - 设置大厅出生点
- `/bw admin <游戏名> spec` - 设置观战点
- `/bw admin <游戏名> lobbypos1` - 设置大厅区域的第一个角落
- `/bw admin <游戏名> lobbypos2` - 设置大厅区域的第二个角落
- `/bw admin <游戏名> minplayers <数量>` - 设置最少玩家数
- `/bw admin <游戏名> time <秒数>` - 设置游戏时间
- `/bw admin <游戏名> lobbycountdown <秒数>` - 设置大厅倒计时
- `/bw admin <游戏名> postgamewaiting <秒数>` - 设置游戏结束后等待时间
- `/bw admin <游戏名> displayName <名称>` - 设置游戏显示名称
- `/bw admin <游戏名> fee <费用>` - 设置游戏费用
- `/bw admin <游戏名> arenaweather <天气>` - 设置游戏天气
- `/bw admin <游戏名> prefab <预制配置>` - 应用预制配置

#### 队伍管理子命令

- `/bw admin <游戏名> team add <队伍名> <颜色> <最大玩家数>` - 添加队伍
- `/bw admin <游戏名> team remove <队伍名>` - 删除队伍
- `/bw admin <游戏名> team color <队伍名> <颜色>` - 设置队伍颜色
- `/bw admin <游戏名> team maxplayers <队伍名> <数量>` - 设置队伍最大玩家数
- `/bw admin <游戏名> team spawn <队伍名>` - 设置队伍出生点
- `/bw admin <游戏名> team target <队伍名> looking_at|standing_on` - 设置队伍目标方块
- `/bw admin <游戏名> team bed <队伍名> looking_at|standing_on` - 设置队伍床方块
- `/bw admin <游戏名> team anchor <队伍名> looking_at|standing_on` - 设置队伍重生锚方块
- `/bw admin <游戏名> team door <队伍名> looking_at|standing_on` - 设置队伍门方块
- `/bw admin <游戏名> team cake <队伍名> looking_at|standing_on` - 设置队伍蛋糕方块
- `/bw admin <游戏名> team list-spawns <队伍名>` - 列出队伍所有出生点
- `/bw admin <游戏名> team add-spawn <队伍名>` - 为队伍添加出生点
- `/bw admin <游戏名> team remove-spawn <队伍名>` - 移除队伍指定出生点
- `/bw admin <游戏名> team reset-spawns <队伍名>` - 重置队伍所有出生点
- `/bw admin <游戏名> jointeam <队伍名>` - 加入指定队伍

#### 资源生成器管理子命令

- `/bw admin <游戏名> spawner add <资源类型>` - 添加资源生成器
- `/bw admin <游戏名> spawner remove` - 移除当前位置的资源生成器
- `/bw admin <游戏名> spawner reset` - 重置所有资源生成器
- `/bw admin <游戏名> spawner change-type <新类型>` - 更改资源生成器类型
- `/bw admin <游戏名> spawner custom-name <名称>` - 设置资源生成器自定义名称
- `/bw admin <游戏名> spawner hologram-type <类型>` - 设置资源生成器全息图类型
- `/bw admin <游戏名> spawner linked-team <队伍名>` - 链接资源生成器到队伍
- `/bw admin <游戏名> spawner base-amount <数量>` - 设置资源生成器基础生成数量
- `/bw admin <游戏名> spawner max-spawned-resources <数量>` - 设置资源生成器最大生成资源数
- `/bw admin <游戏名> spawner rotation-mode <模式>` - 设置资源生成器旋转模式

#### 商店管理子命令

- `/bw admin <游戏名> store add` - 添加商店
- `/bw admin <游戏名> store remove` - 移除商店
- `/bw admin <游戏名> store type <实体类型>` - 设置商店实体类型
- `/bw admin <游戏名> store child` - 设置商店为幼体
- `/bw admin <游戏名> store adult` - 设置商店为成体
- `/bw admin <游戏名> store name <名称>` - 设置商店名称
- `/bw admin <游戏名> store team <队伍名>` - 链接商店到队伍
- `/bw admin <游戏名> store file <文件名>` - 设置商店文件

#### 配置管理子命令

- `/bw admin <游戏名> config set <键> <值>` - 设置配置值
- `/bw admin <游戏名> config reset <键>` - 重置配置值
- `/bw admin <游戏名> config list add <键> <值>` - 向列表添加配置值
- `/bw admin <游戏名> config list remove <键> <值>` - 从列表移除配置值
- `/bw admin <游戏名> config list clear <键>` - 清空列表配置
- `/bw admin <游戏名> config get <键>` - 获取配置值

#### 远程游戏管理命令

- `/bw admin remote add <服务器名>` - 添加远程服务器
- `/bw admin remote remove <服务器名>` - 移除远程服务器
- `/bw admin remote set <服务器名> <游戏名>` - 设置远程服务器游戏
- `/bw admin remote list` - 列出所有远程服务器

#### 其他管理命令

- `/bw reload` - 重新加载插件
- `/bw addholo` - 添加全息图
- `/bw removeholo` - 移除全息图
- `/bw alljoin <游戏名>` - 强制所有在线玩家加入指定游戏
- `/bw dump <服务名>` - 转储服务数据
- `/bw migrate` - 迁移数据
- `/bw migrate bedwarsrel` - 从BedWarsRel迁移数据
- `/bw migrate bedwars1058` - 从BedWars1058迁移数据
- `/bw lang <语言代码>` - 更改语言
- `/bw npc add` - 添加NPC
- `/bw cheat give <物品>` - 给予作弊物品
- `/bw cheat kill <玩家>` - 杀死玩家
- `/bw cheat build-pop-up-tower` - 建造弹出塔
- `/bw cheat rebuild-region` - 重建区域

#### 游戏编辑子命令

- `/bw admin <游戏名> pos1` - 设置游戏区域的第一个角落
- `/bw admin <游戏名> pos2` - 设置游戏区域的第二个角落
- `/bw admin <游戏名> lobby` - 设置大厅出生点
- `/bw admin <游戏名> spec` - 设置观战点
- `/bw admin <游戏名> minplayers <数量>` - 设置最少玩家数
- `/bw admin <游戏名> time <秒数>` - 设置游戏时间
- `/bw admin <游戏名> lobbycountdown <秒数>` - 设置大厅倒计时
- `/bw admin <游戏名> postgamewaiting <秒数>` - 设置游戏结束后等待时间
- `/bw admin <游戏名> displayName <名称>` - 设置游戏显示名称
- `/bw admin <游戏名> fee <费用>` - 设置游戏费用

#### 队伍管理子命令

- `/bw admin <游戏名> team add <队伍名> <颜色> <最大玩家数>` - 添加队伍
- `/bw admin <游戏名> team remove <队伍名>` - 删除队伍
- `/bw admin <游戏名> team color <队伍名> <颜色>` - 设置队伍颜色
- `/bw admin <游戏名> team maxplayers <队伍名> <数量>` - 设置队伍最大玩家数
- `/bw admin <游戏名> team spawn <队伍名>` - 设置队伍出生点
- `/bw admin <游戏名> team target <队伍名> looking_at|standing_on` - 设置队伍目标方块
- `/bw admin <游戏名> jointeam <队伍名>` - 加入指定队伍

#### 资源生成器管理子命令

- `/bw admin <游戏名> spawner add <资源类型>` - 添加资源生成器
- `/bw admin <游戏名> spawner remove` - 移除当前位置的资源生成器
- `/bw admin <游戏名> spawner reset` - 重置所有资源生成器

#### 商店管理子命令

- `/bw admin <游戏名> store add` - 添加商店
- `/bw admin <游戏名> store remove` - 移除商店
- `/bw admin <游戏名> store type <实体类型>` - 设置商店实体类型
- `/bw admin <游戏名> store child` - 设置商店为幼体
- `/bw admin <游戏名> store adult` - 设置商店为成体

#### 配置管理子命令

- `/bw admin <游戏名> config set <键> <值>` - 设置配置值
- `/bw admin <游戏名> config reset <键>` - 重置配置值
- `/bw admin <游戏名> config list add <键> <值>` - 向列表添加配置值
- `/bw admin <游戏名> config list remove <键> <值>` - 从列表移除配置值
- `/bw admin <游戏名> config list clear <键>` - 清空列表配置

#### 其他管理命令

- `/bw reload` - 重新加载插件
- `/bw addholo` - 添加全息图
- `/bw removeholo` - 移除全息图
- `/bw alljoin <游戏名>` - 强制所有在线玩家加入指定游戏
- `/bw dump <服务名>` - 转储服务数据
- `/bw migrate` - 迁移数据
- `/bw lang <语言代码>` - 更改语言

## 数据库配置

插件支持MySQL数据库存储统计数据。在`config.yml`中配置数据库连接：

```yaml
database:
  host: "localhost"
  port: 3306
  db: "database"
  user: "root"
  password: "secret"
  table-prefix: "bw_"
  type: "mysql"
```

## 配置文件

### config.yml

主要配置文件，包含游戏规则、商店设置、数据库连接等。重要配置项包括：

#### 基本设置
- `locale` - 语言设置
- `debug` - 调试模式
- `prefix` - 插件消息前缀

#### 游戏规则
- `allow-crafting` - 是否允许合成
- `friendlyfire` - 友军伤害
- `player-drops` - 玩家死亡是否掉落物品
- `keep-inventory-on-death` - 死亡后是否保留物品栏
- `keep-armor-on-death` - 死亡后是否保留装备
- `disable-hunger` - 是否禁用饥饿值
- `allow-block-falling` - 是否允许方块下落

#### 游戏设置
- `join-random-team-after-lobby` - 大厅倒计时结束后是否随机加入队伍
- `join-random-team-on-join` - 加入游戏时是否随机加入队伍
- `add-wool-to-inventory-on-join` - 加入游戏时是否添加羊毛到物品栏
- `prevent-killing-villagers` - 是否阻止杀死村民
- `team-join-item-enabled` - 是否启用队伍选择物品
- `invisible-lobby-on-game-start` - 游戏开始时是否隐藏大厅玩家
- `allow-spectator-join` - 是否允许观战者加入

#### 资源生成器设置
- `spawner-holograms` - 资源生成器全息显示
- `spawner-holograms-countdown` - 资源生成器倒计时显示
- `spawner-disable-merge` - 是否禁用资源生成器合并
- `spawner-holo-height` - 资源生成器全息显示高度
- `use-certain-popular-server-like-holograms-for-spawners` - 是否使用特定流行服务器样式的资源生成器全息显示

#### 界面设置
- `bossbar` - Boss栏设置
  - `lobby` - 大厅Boss栏设置
  - `game` - 游戏中Boss栏设置
- `sidebar` - 记分板设置
  - `game` - 游戏中记分板设置
  - `lobby` - 大厅记分板设置
- `chat` - 聊天设置
- `tab` - Tab列表设置

#### 商店设置
- `shop` - 商店界面设置
- `automatic-coloring-in-shop` - 商店中是否自动染色
- `sell-max-64-per-click-in-shop` - 商店中每次点击最多出售64个物品

#### 特殊物品设置
- `specials` - 特殊物品设置
  - `rescue-platform` - 救援平台设置
  - `protection-wall` - 防护墙设置
  - `tnt-sheep` - TNT羊设置
  - `arrow-blocker` - 箭矢阻挡器设置
  - `warp-powder` - 传送粉设置
  - `bridge-egg` - 造桥蛋设置
  - `popup-tower` - 弹出塔设置
  - `golem` - 铁傀儡设置

#### 数据库设置
- `database` - 数据库连接设置
  - `host` - 数据库主机
  - `port` - 数据库端口
  - `db` - 数据库名称
  - `user` - 数据库用户名
  - `password` - 数据库密码
  - `table-prefix` - 表前缀
  - `type` - 数据库类型

#### 统计数据设置
- `statistics` - 统计数据设置
  - `enabled` - 是否启用统计数据
  - `type` - 统计数据存储类型
  - `scores` - 分数设置

#### 其他设置
- `bungee` - BungeeCord设置
- `sounds` - 声音设置
- `rewards` - 奖励设置
- `permissions` - 权限设置

### 商店配置

商店文件位于`shop/shop.yml`，可以自定义商品和价格。商店配置包含以下主要部分：

- `data` - 商店数据
  - `stack` - 分类物品显示
  - `items` - 商品列表
    - `price` - 价格
    - `stack` - 物品信息
      - `type` - 物品类型
      - `amount` - 数量
      - `enchants` - 附魔
      - `display-name` - 显示名称
      - `lore` - 描述

### 游戏变体配置

游戏变体文件位于`variants/`目录下，可以自定义游戏规则和资源生成器类型。

- `name` - 变体名称
- `config` - 配置设置
- `custom-spawner-types` - 自定义资源生成器类型
- `upgrades` - 升级设置

## 地图配置

### 创建新游戏

1. 使用指令 `/bw admin add <游戏名>` 创建新游戏
2. 使用 `/bw admin edit <游戏名>` 进入编辑模式

### 设置游戏边界

1. 进入游戏世界，站在一个角落
2. 使用 `/bw admin <游戏名> pos1` 设置第一个角落
3. 移动到对角，使用 `/bw admin <游戏名> pos2` 设置第二个角落

### 设置大厅和观战点

1. 使用 `/bw admin <游戏名> lobby` 设置大厅出生点
2. 使用 `/bw admin <游戏名> spec` 设置观战点

### 配置队伍

1. 添加队伍：`/bw admin <游戏名> team add <队伍名> <颜色> <最大玩家数>`
2. 设置队伍床点：站在床附近，使用 `/bw admin <游戏名> team <队伍名> target looking_at`
3. 设置队伍出生点：站在出生点，使用 `/bw admin <游戏名> team <队伍名> spawn`

### 添加资源生成器

1. 站在要添加生成器的位置
2. 使用 `/bw admin <游戏名> spawner add <资源类型>` 添加生成器
3. 可用的资源类型包括：bronze(青铜)、iron(铁)、gold(金)

### 添加商店

1. 站在要添加商店的位置
2. 使用 `/bw admin <游戏名> store add` 添加商店
3. 可选：设置商店实体类型 `/bw admin <游戏名> store type <实体类型>`

### 保存游戏

完成配置后，使用 `/bw admin <游戏名> save` 保存游戏

## 特殊物品

- 传送粉 (Warp Powder)
- 陷阱 (Trap)
- 磁力鞋 (Magnet Shoes)
- 追踪器 (Tracker)
- 救援平台 (Rescue Platform)
- 箭矢阻挡器 (Arrow Blocker)
- 防护墙 (Protection Wall)
- 铁傀儡 (Golem)
- TNT羊 (TNT Sheep)
- 火球 (Throwable Fireball)
- 造桥蛋 (Bridge Egg)

## 权限节点

### 玩家权限

- `bw.cmd.join` - 加入游戏
- `bw.cmd.leave` - 离开游戏
- `bw.cmd.autojoin` - 自动加入游戏
- `bw.cmd.list` - 查看游戏列表
- `bw.cmd.stats` - 查看统计数据
- `bw.cmd.rejoin` - 重新加入游戏
- `bw.cmd.leaderboard` - 查看排行榜
- `bw.cmd.gamesinv` - 打开游戏菜单
- `bw.cmd.party` - 使用队伍命令
- `bw.otherstats` - 查看其他玩家统计数据

### 管理员权限

- `bw.admin` - 管理员权限
- `bw.admin.alljoin` - 强制加入任何游戏
- `bw.disable.joinall` - 禁用所有玩家加入游戏