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

## 指令

### 玩家指令

- `/bw join <游戏名>` - 加入指定游戏
- `/bw leave` - 离开当前游戏
- `/bw start` - 开始游戏（需要权限）
- `/bw stats` - 查看个人统计数据
- `/bw list` - 列出所有可用游戏

### 管理员指令

- `/bw admin add <游戏名> [变体]` - 创建新游戏
- `/bw admin edit <游戏名>` - 编辑游戏
- `/bw admin save <游戏名>` - 保存游戏
- `/bw admin remove <游戏名>` - 删除游戏
- `/bw admin info <游戏名>` - 查看游戏信息

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

- `locale` - 语言设置
- `debug` - 调试模式
- `allow-crafting` - 是否允许合成
- `friendlyfire` - 友军伤害
- `spawner-holograms` - 资源生成器全息显示
- `bossbar` - Boss栏设置
- `sidebar` - 记分板设置

### 商店配置

商店文件位于`shop/shop.yml`，可以自定义商品和价格。

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
- `bw.cmd.start` - 开始游戏
- `bw.cmd.stats` - 查看统计数据

### 管理员权限

- `bw.admin` - 管理员权限
- `bw.admin.alljoin` - 强制加入任何游戏