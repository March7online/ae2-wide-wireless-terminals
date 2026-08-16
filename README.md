# AE2 Wide Wireless Terminals

为 Minecraft 1.20.1 Forge 环境中的 AE2 无线终端增加可切换的 18 列宽屏模式，同时保留原版 9 列布局。

## 功能

- 使用终端左侧工具栏按钮即时切换 9 列和 18 列布局。
- 支持 AE2 无线终端和无线合成终端。
- 支持 AE2WTLib 无线样板编码终端。
- 支持无线通用终端中的合成和样板编码模式。
- 切换时保留搜索内容和当前物品列表。
- 搜索栏固定在终端顶部右侧，不随宽窄模式拉伸。
- 屏幕空间不足时自动使用窄屏布局。
- 宽屏偏好保存在客户端配置中。

无线样板访问终端及无线通用终端中的样板访问模式不在支持范围内。

## 客户端专用

本模组只修改客户端界面，不添加物品、方块、配方、菜单或网络协议。服务器不需要安装本模组，客户端可以连接未安装本模组的服务器。

## 目标版本

- Minecraft 1.20.1
- Forge 47.4.16
- Applied Energistics 2 15.4.10
- AE2 Wireless Terminal Library 15.3.3
- Java 17

其他版本组合未经验证。

## 安装

1. 安装 Forge、AE2 和 AE2WTLib。
2. 下载发行版中的 `ae2wideterminal-1.0.0-forge-1.20.1.jar`。
3. 将 JAR 放入客户端实例的 `mods` 目录。
4. 启动游戏并打开受支持的无线终端，使用左侧工具栏按钮切换布局。

客户端配置文件为 `config/ae2wideterminal-client.toml`。

## 从源码构建

将 `AE2WTLIB_DIR` 指向包含 `ae2wtlib-15.3.3-forge.jar` 的模组目录：

```powershell
$env:AE2WTLIB_DIR = 'C:\path\to\minecraft\mods'
.\gradlew.bat clean test build
```

也可以把 AE2WTLib JAR 放入仓库根目录下的 `local-mods` 文件夹。构建产物位于 `build/libs`。

运行开发客户端时，若 `AE2WTLIB_DIR` 中存在 Architectury、Cloth Config、Curios、GuideME、JEI 和 IMBlocker 的目标版本，构建脚本会自动将它们作为本地开发运行依赖加载。

## 许可证

本项目采用 [MIT License](LICENSE)。
