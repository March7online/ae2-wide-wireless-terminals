# AE2 无线终端宽屏模式实现计划

> **面向 AI 代理的工作者：** 必需子技能：使用 superpowers:subagent-driven-development（推荐）或 superpowers:executing-plans 逐任务实现此计划。步骤使用复选框（`- [ ]`）语法来跟踪进度。

**目标：** 构建一个 Minecraft 1.20.1 Forge 客户端模组，为指定 AE2/AE2WTLib 无线终端增加全局可切换的 18 列宽屏模式，同时允许连接未安装本模组的服务器。

**架构：** 客户端 Mixin 定向拦截 AE2 的 `InitScreens.register`，只替换普通无线终端、无线合成终端和无线样板编码终端的屏幕工厂。专用屏幕子类复用原屏幕全部行为，仅增加左侧切换按钮；工厂根据客户端配置与 GUI 可用宽度选择原版样式或宽屏样式。

**技术栈：** Java 17、Forge 47.4.16、ForgeGradle 6、Sponge Mixin 0.8、AE2 15.4.10、AE2WTLib 15.3.3、JUnit 5、Python Pillow（仅用于确定性生成宽屏 PNG）。

---

## 文件结构

### 构建与元数据

- `settings.gradle`：Gradle 插件仓库和项目名。
- `build.gradle`：ForgeGradle、Mixin、AE2 与本地 AE2WTLib 依赖、运行配置、JUnit 5 和资源展开。
- `gradle.properties`：固定 Minecraft、Forge、AE2、AE2WTLib 与模组版本。
- `gradlew`、`gradlew.bat`、`gradle/wrapper/*`：Forge MDK 47.4.16 的 Gradle Wrapper。
- `src/main/resources/META-INF/mods.toml`：客户端依赖与版本范围。
- `src/main/resources/pack.mcmeta`：资源包元数据。
- `src/main/resources/ae2wideterminal.mixins.json`：仅客户端加载的 Mixin 配置。

### Java 主代码

- `src/main/java/com/ae2wideterminal/AE2WideTerminalMod.java`：服务端安全的根模组类、客户端配置注册和 DisplayTest。
- `src/main/java/com/ae2wideterminal/config/WideTerminalClientConfig.java`：Forge CLIENT 配置定义和读写入口。
- `src/main/java/com/ae2wideterminal/client/WideModePolicy.java`：纯函数形式的宽度决策。
- `src/main/java/com/ae2wideterminal/client/TerminalKind.java`：目标终端、原版样式、宽屏样式和宽度元数据。
- `src/main/java/com/ae2wideterminal/client/WideTerminalStyleSelector.java`：结合配置与当前 GUI 尺寸选择样式。
- `src/main/java/com/ae2wideterminal/client/WideTerminalScreenRegistrar.java`：识别目标 `MenuType` 并注册专用屏幕工厂。
- `src/main/java/com/ae2wideterminal/mixin/client/InitScreensMixin.java`：在 `InitScreens.register` 入口调用 registrar，命中时取消原注册。
- `src/main/java/com/ae2wideterminal/client/screen/WideScreenFactory.java`：重建当前屏幕的函数接口。
- `src/main/java/com/ae2wideterminal/client/screen/WideTerminalScreenSupport.java`：添加按钮、保存状态、切换配置与替换屏幕。
- `src/main/java/com/ae2wideterminal/client/screen/WideWirelessTerminalScreen.java`：普通无线终端屏幕。
- `src/main/java/com/ae2wideterminal/client/screen/WideAe2CraftingTerminalScreen.java`：AE2 原生无线合成屏幕兼容兜底。
- `src/main/java/com/ae2wideterminal/client/screen/WideWtlibCraftingTerminalScreen.java`：AE2WTLib 独立/通用合成屏幕。
- `src/main/java/com/ae2wideterminal/client/screen/WideWtlibPatternEncodingScreen.java`：AE2WTLib 独立/通用样板编码屏幕。
- `src/main/java/com/ae2wideterminal/client/widget/WideModeButton.java`：AE2 左侧工具栏图标按钮和提示文本。

### 资源

- `src/main/resources/assets/ae2/screens/ae2wideterminal/wireless_terminal_wide.json`：普通无线终端宽屏样式。
- `src/main/resources/assets/ae2/screens/ae2wideterminal/ae2_crafting_terminal_wide.json`：AE2 原生无线合成宽屏样式。
- `src/main/resources/assets/ae2/screens/ae2wideterminal/wtlib_crafting_terminal_wide.json`：AE2WTLib 合成宽屏样式。
- `src/main/resources/assets/ae2/screens/ae2wideterminal/wtlib_pattern_encoding_terminal_wide.json`：AE2WTLib 样板编码宽屏样式。
- `src/main/resources/assets/ae2/textures/guis/ae2wideterminal/*.png`：由生成脚本生成的宽屏背景。
- `src/main/resources/assets/ae2wideterminal/textures/gui/wide_mode.png`：展开/收起按钮图标。
- `src/main/resources/assets/ae2wideterminal/lang/zh_cn.json`：简体中文。
- `src/main/resources/assets/ae2wideterminal/lang/en_us.json`：英文。
- `tools/generate_wide_textures.py`：从 AE2/AE2WTLib 精确版本 JAR 中生成宽屏纹理。

### 测试与文档

- `src/test/java/com/ae2wideterminal/MetadataContractTest.java`：客户端专用元数据契约。
- `src/test/java/com/ae2wideterminal/client/WideModePolicyTest.java`：宽度决策边界。
- `src/test/java/com/ae2wideterminal/client/TerminalKindTest.java`：目标范围与样式映射。
- `src/test/java/com/ae2wideterminal/resources/WideStyleResourceTest.java`：JSON、18 列和 PNG 尺寸契约。
- `README.md`：安装、依赖、范围、客户端专用说明。
- `LICENSE`：MIT 许可证。

---

### 任务 1：建立可构建的客户端 Forge 工程

**文件：**
- 创建：`settings.gradle`
- 创建：`build.gradle`
- 创建：`gradle.properties`
- 创建：`gradlew`
- 创建：`gradlew.bat`
- 创建：`gradle/wrapper/gradle-wrapper.jar`
- 创建：`gradle/wrapper/gradle-wrapper.properties`
- 创建：`src/test/java/com/ae2wideterminal/MetadataContractTest.java`
- 创建：`src/main/resources/META-INF/mods.toml`
- 创建：`src/main/resources/pack.mcmeta`
- 创建：`src/main/resources/ae2wideterminal.mixins.json`
- 创建：`src/main/java/com/ae2wideterminal/AE2WideTerminalMod.java`

- [ ] **步骤 1：从官方 MDK 获取 Gradle Wrapper 与基础构建文件**

下载并解压：

```powershell
Invoke-WebRequest `
  -Uri 'https://maven.minecraftforge.net/net/minecraftforge/forge/1.20.1-47.4.16/forge-1.20.1-47.4.16-mdk.zip' `
  -OutFile 'work/forge-1.20.1-47.4.16-mdk.zip'
Expand-Archive -LiteralPath 'work/forge-1.20.1-47.4.16-mdk.zip' -DestinationPath 'work/forge-mdk' -Force
```

复制 Wrapper 后，将构建参数固定为：

```properties
minecraft_version=1.20.1
forge_version=47.4.16
ae2_version=15.4.10
ae2wtlib_version=15.3.3-forge
mod_id=ae2wideterminal
mod_version=1.0.0
org.gradle.jvmargs=-Xmx3G
```

- [ ] **步骤 2：编写失败的元数据契约测试**

```java
@Test
void metadataDeclaresClientOnlyDependenciesAndMixin() throws IOException {
    String modsToml = Files.readString(Path.of("src/main/resources/META-INF/mods.toml"));
    assertTrue(modsToml.contains("modId=\"ae2\""));
    assertTrue(modsToml.contains("versionRange=\"[15.4.10,15.5.0)\""));
    assertTrue(modsToml.contains("modId=\"ae2wtlib\""));
    assertTrue(modsToml.contains("versionRange=\"[15.3.3,15.4.0)\""));
    assertEquals(2, modsToml.split("side=\"CLIENT\"", -1).length - 1);

    String mixins = Files.readString(Path.of("src/main/resources/ae2wideterminal.mixins.json"));
    assertTrue(mixins.contains("\"client\""));
    assertFalse(mixins.contains("\"mixins\""));
}
```

- [ ] **步骤 3：运行测试并确认失败**

运行：

```powershell
./gradlew.bat test --tests com.ae2wideterminal.MetadataContractTest
```

预期：FAIL，因为元数据文件或预期客户端依赖尚未完整创建。

- [ ] **步骤 4：创建最小客户端专用元数据与根模组类**

`mods.toml` 必须包含：

```toml
modLoader="javafml"
loaderVersion="[47,)"
license="MIT"

[[mods]]
modId="ae2wideterminal"
version="${file.jarVersion}"
displayName="AE2 Wide Wireless Terminals"
description='''Adds a client-side 18-column mode to selected AE2 wireless terminals.'''

[[dependencies.ae2wideterminal]]
modId="ae2"
mandatory=true
versionRange="[15.4.10,15.5.0)"
ordering="AFTER"
side="CLIENT"

[[dependencies.ae2wideterminal]]
modId="ae2wtlib"
mandatory=true
versionRange="[15.3.3,15.4.0)"
ordering="AFTER"
side="CLIENT"
```

根类在脚手架阶段只注册显示测试，避免引用尚未创建的客户端配置：

```java
@Mod(AE2WideTerminalMod.MOD_ID)
public final class AE2WideTerminalMod {
    public static final String MOD_ID = "ae2wideterminal";

    public AE2WideTerminalMod() {
        ModLoadingContext.get().registerDisplayTest(IExtensionPoint.DisplayTest.IGNORE_ALL_VERSION);
    }
}
```

构建脚本从 `AE2WTLIB_JAR` 环境变量读取精确 JAR；未设置时使用用户实例路径：

```groovy
def ae2wtlibJar = file(System.getenv('AE2WTLIB_JAR') ?:
        'C:/Users/19654/Desktop/.minecraft/versions/GTL/mods/ae2wtlib-15.3.3-forge.jar')
if (!ae2wtlibJar.exists()) {
    throw new GradleException("Set AE2WTLIB_JAR to ae2wtlib-15.3.3-forge.jar")
}

dependencies {
    minecraft "net.minecraftforge:forge:1.20.1-47.4.16"
    implementation fg.deobf("appeng:appliedenergistics2-forge:15.4.10")
    implementation fg.deobf(files(ae2wtlibJar))
    annotationProcessor "org.spongepowered:mixin:0.8.5:processor"
    testImplementation platform("org.junit:junit-bom:5.10.2")
    testImplementation "org.junit.jupiter:junit-jupiter"
}
```

- [ ] **步骤 5：验证测试与基础构建通过**

运行：

```powershell
./gradlew.bat test
./gradlew.bat classes
```

预期：全部 PASS，客户端根类编译成功，未出现服务端专属或客户端类加载错误。

- [ ] **步骤 6：提交脚手架**

```powershell
git add settings.gradle build.gradle gradle.properties gradlew gradlew.bat gradle src/main
git commit -m "build: scaffold Forge client mod"
```

---

### 任务 2：实现并测试全局宽屏决策

**文件：**
- 创建：`src/main/java/com/ae2wideterminal/config/WideTerminalClientConfig.java`
- 创建：`src/main/java/com/ae2wideterminal/client/WideModePolicy.java`
- 修改：`src/main/java/com/ae2wideterminal/AE2WideTerminalMod.java`
- 创建：`src/test/java/com/ae2wideterminal/client/WideModePolicyTest.java`

- [ ] **步骤 1：编写失败的宽度策略测试**

```java
@Test
void wideModeRequiresPreferenceAndEnoughSpace() {
    assertFalse(WideModePolicy.useWide(false, 500, 357, 24, 12));
    assertTrue(WideModePolicy.useWide(true, 405, 357, 24, 12));
    assertFalse(WideModePolicy.useWide(true, 404, 357, 24, 12));
}

@Test
void requiredWidthIncludesToolbarAndBothMargins() {
    assertEquals(405, WideModePolicy.requiredGuiWidth(357, 24, 12));
    assertEquals(410, WideModePolicy.requiredGuiWidth(362, 24, 12));
}
```

- [ ] **步骤 2：运行测试确认失败**

运行：

```powershell
./gradlew.bat test --tests com.ae2wideterminal.client.WideModePolicyTest
```

预期：FAIL，`WideModePolicy` 尚不存在。

- [ ] **步骤 3：实现最小纯函数策略**

```java
public final class WideModePolicy {
    private WideModePolicy() {}

    public static int requiredGuiWidth(int screenWidth, int toolbarWidth, int sideMargin) {
        return screenWidth + toolbarWidth + sideMargin * 2;
    }

    public static boolean useWide(boolean preferred, int guiWidth,
            int screenWidth, int toolbarWidth, int sideMargin) {
        return preferred && guiWidth >= requiredGuiWidth(screenWidth, toolbarWidth, sideMargin);
    }
}
```

Forge CLIENT 配置：

```java
public final class WideTerminalClientConfig {
    private static final ForgeConfigSpec.BooleanValue WIDE_MODE;
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        WIDE_MODE = builder.define("wideMode", false);
        SPEC = builder.build();
    }

    public static boolean isWidePreferred() { return WIDE_MODE.get(); }
    public static void toggleAndSave() {
        WIDE_MODE.set(!WIDE_MODE.get());
        WIDE_MODE.save();
    }
}
```

在根模组构造器中补上 CLIENT 配置注册：

```java
ModLoadingContext.get().registerConfig(
        ModConfig.Type.CLIENT,
        WideTerminalClientConfig.SPEC,
        MOD_ID + "-client.toml");
```

- [ ] **步骤 4：运行测试确认通过**

运行：

```powershell
./gradlew.bat test --tests com.ae2wideterminal.client.WideModePolicyTest
```

预期：PASS。

- [ ] **步骤 5：提交决策层**

```powershell
git add src/main/java/com/ae2wideterminal/AE2WideTerminalMod.java src/main/java/com/ae2wideterminal/config src/main/java/com/ae2wideterminal/client/WideModePolicy.java src/test/java/com/ae2wideterminal/client
git commit -m "feat: add client wide-mode policy"
```

---

### 任务 3：定义目标终端与样式选择

**文件：**
- 创建：`src/main/java/com/ae2wideterminal/client/TerminalKind.java`
- 创建：`src/main/java/com/ae2wideterminal/client/WideTerminalStyleSelector.java`
- 创建：`src/test/java/com/ae2wideterminal/client/TerminalKindTest.java`

- [ ] **步骤 1：编写失败的终端范围测试**

```java
@Test
void onlyApprovedKindsHaveWideStyles() {
    assertEquals(4, TerminalKind.values().length);
    assertEquals(357, TerminalKind.WIRELESS_STORAGE.wideScreenWidth());
    assertEquals(357, TerminalKind.AE2_WIRELESS_CRAFTING.wideScreenWidth());
    assertEquals(362, TerminalKind.WTLIB_CRAFTING.wideScreenWidth());
    assertEquals(357, TerminalKind.WTLIB_PATTERN_ENCODING.wideScreenWidth());
    assertTrue(Arrays.stream(TerminalKind.values())
            .noneMatch(kind -> kind.name().contains("PATTERN_ACCESS")));
}

@Test
void stylePathsAreAbsoluteAndDistinct() {
    for (TerminalKind kind : TerminalKind.values()) {
        assertTrue(kind.narrowStylePath().startsWith("/screens/"));
        assertTrue(kind.wideStylePath().startsWith("/screens/ae2wideterminal/"));
        assertNotEquals(kind.narrowStylePath(), kind.wideStylePath());
    }
}
```

- [ ] **步骤 2：运行测试确认失败**

```powershell
./gradlew.bat test --tests com.ae2wideterminal.client.TerminalKindTest
```

预期：FAIL，`TerminalKind` 尚不存在。

- [ ] **步骤 3：实现终端枚举和样式选择器**

```java
public enum TerminalKind {
    WIRELESS_STORAGE(
            "/screens/terminals/wireless_terminal.json",
            "/screens/ae2wideterminal/wireless_terminal_wide.json", 357),
    AE2_WIRELESS_CRAFTING(
            "/screens/terminals/crafting_terminal.json",
            "/screens/ae2wideterminal/ae2_crafting_terminal_wide.json", 357),
    WTLIB_CRAFTING(
            "/screens/wtlib/wireless_crafting_terminal.json",
            "/screens/ae2wideterminal/wtlib_crafting_terminal_wide.json", 362),
    WTLIB_PATTERN_ENCODING(
            "/screens/wtlib/wireless_pattern_encoding_terminal.json",
            "/screens/ae2wideterminal/wtlib_pattern_encoding_terminal_wide.json", 357);
}
```

选择器使用 `Minecraft.getInstance().getWindow().getGuiScaledWidth()`，常量为左工具栏 24px、安全边距 12px，并捕获宽屏样式加载异常回退到窄屏：

```java
public static boolean canUseWide(TerminalKind kind) {
    return WideModePolicy.useWide(
            true,
            Minecraft.getInstance().getWindow().getGuiScaledWidth(),
            kind.wideScreenWidth(), 24, 12);
}

public static boolean isWideActive(TerminalKind kind) {
    return WideTerminalClientConfig.isWidePreferred() && canUseWide(kind);
}

public static ScreenStyle select(TerminalKind kind) {
    boolean wide = isWideActive(kind);
    String path = wide ? kind.wideStylePath() : kind.narrowStylePath();
    try {
        return StyleManager.loadStyleDoc(path);
    } catch (RuntimeException error) {
        LOGGER.error("Failed to load terminal style {} for {}", path, kind, error);
        return StyleManager.loadStyleDoc(kind.narrowStylePath());
    }
}
```

- [ ] **步骤 4：运行测试和编译检查**

```powershell
./gradlew.bat test
./gradlew.bat compileJava
```

预期：PASS；枚举和样式选择器编译成功。

- [ ] **步骤 5：提交终端映射层**

```powershell
git add src/main/java/com/ae2wideterminal/client/TerminalKind.java src/main/java/com/ae2wideterminal/client/WideTerminalStyleSelector.java src/test/java/com/ae2wideterminal/client/TerminalKindTest.java
git commit -m "feat: map supported wireless terminal styles"
```

---

### 任务 4：替换目标屏幕并添加左侧切换按钮

**文件：**
- 创建：`src/main/java/com/ae2wideterminal/client/WideTerminalScreenRegistrar.java`
- 创建：`src/main/java/com/ae2wideterminal/mixin/client/InitScreensMixin.java`
- 修改：`src/main/resources/ae2wideterminal.mixins.json`
- 创建：`src/main/java/com/ae2wideterminal/client/screen/WideScreenFactory.java`
- 创建：`src/main/java/com/ae2wideterminal/client/screen/WideTerminalScreenSupport.java`
- 创建：`src/main/java/com/ae2wideterminal/client/screen/WideWirelessTerminalScreen.java`
- 创建：`src/main/java/com/ae2wideterminal/client/screen/WideAe2CraftingTerminalScreen.java`
- 创建：`src/main/java/com/ae2wideterminal/client/screen/WideWtlibCraftingTerminalScreen.java`
- 创建：`src/main/java/com/ae2wideterminal/client/screen/WideWtlibPatternEncodingScreen.java`
- 创建：`src/main/java/com/ae2wideterminal/client/widget/WideModeButton.java`
- 创建：`src/main/resources/assets/ae2wideterminal/textures/gui/wide_mode.png`
- 创建：`src/main/resources/assets/ae2wideterminal/lang/zh_cn.json`
- 创建：`src/main/resources/assets/ae2wideterminal/lang/en_us.json`

- [ ] **步骤 1：定义重建接口与共享切换流程**

```java
@FunctionalInterface
public interface WideScreenFactory {
    Screen create(ScreenStyle style);
}
```

```java
public static void toggle(MEStorageScreen<?> current,
        TerminalKind kind, WideScreenFactory factory) {
    current.storeState();
    WideTerminalClientConfig.toggleAndSave();
    ScreenStyle style = WideTerminalStyleSelector.select(kind);
    Minecraft.getInstance().setScreen(factory.create(style));
}
```

每个屏幕子类在构造器末尾执行：

```java
addToLeftToolbar(new WideModeButton(
        () -> WideTerminalScreenSupport.toggle(
                this,
                TerminalKind.WTLIB_CRAFTING,
                nextStyle -> new WideWtlibCraftingTerminalScreen(
                        menu, playerInventory, title, nextStyle)),
        TerminalKind.WTLIB_CRAFTING));
```

- [ ] **步骤 2：实现注册拦截和四个屏幕工厂**

Mixin 使用被擦除后的静态方法签名，命中时由 registrar 注册并取消原调用：

```java
@Mixin(value = InitScreens.class, remap = false)
abstract class InitScreensMixin {
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void replaceTargetScreen(MenuType<?> type,
            InitScreens.StyledScreenFactory<?, ?> originalFactory,
            String originalStylePath,
            CallbackInfo ci) {
        if (WideTerminalScreenRegistrar.tryRegister(type)) {
            ci.cancel();
        }
    }
}
```

`tryRegister` 只比较下列四个类型，并使用 `MenuScreens.register`：

```java
public static boolean tryRegister(MenuType<?> type) {
    if (type == MEStorageMenu.WIRELESS_TYPE) {
        register(MEStorageMenu.WIRELESS_TYPE, TerminalKind.WIRELESS_STORAGE,
                WideWirelessTerminalScreen::new);
        return true;
    }
    if (type == WirelessCraftingTermMenu.TYPE) {
        register(WirelessCraftingTermMenu.TYPE, TerminalKind.AE2_WIRELESS_CRAFTING,
                WideAe2CraftingTerminalScreen::new);
        return true;
    }
    if (type == WCTMenu.TYPE) {
        register(WCTMenu.TYPE, TerminalKind.WTLIB_CRAFTING,
                WideWtlibCraftingTerminalScreen::new);
        return true;
    }
    if (type == WETMenu.TYPE) {
        register(WETMenu.TYPE, TerminalKind.WTLIB_PATTERN_ENCODING,
                WideWtlibPatternEncodingScreen::new);
        return true;
    }
    return false;
}

private static <M extends AEBaseMenu, S extends Screen & MenuAccess<M>> void register(
        MenuType<M> type,
        TerminalKind kind,
        InitScreens.StyledScreenFactory<M, S> factory) {
    MenuScreens.register(type, (menu, inventory, title) -> factory.create(
            menu, inventory, title, WideTerminalStyleSelector.select(kind)));
}
```

`WATMenu.TYPE` 不引用、不匹配、不替换。Mixin 类只列在配置文件的 `client` 数组中。

- [ ] **步骤 3：实现 AE2 风格按钮**

`WideModeButton` 继承 Vanilla `Button` 并实现 AE2 `ITooltip`：

```java
public final class WideModeButton extends Button implements ITooltip {
    private static final ResourceLocation ICONS =
            new ResourceLocation(AE2WideTerminalMod.MOD_ID, "textures/gui/wide_mode.png");

    public WideModeButton(Runnable toggle, TerminalKind kind) {
        super(0, 0, 16, 16, tooltip(kind), button -> toggle.run(), DEFAULT_NARRATION);
    }

    private static Component tooltip(TerminalKind kind) {
        if (WideTerminalClientConfig.isWidePreferred()
                && !WideTerminalStyleSelector.canUseWide(kind)) {
            return Component.translatable("gui.ae2wideterminal.insufficient_width");
        }
        return Component.translatable(WideTerminalClientConfig.isWidePreferred()
                ? "gui.ae2wideterminal.switch_to_narrow"
                : "gui.ae2wideterminal.switch_to_wide");
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(getX(), getY()).blit(graphics);
        int textureY = WideTerminalClientConfig.isWidePreferred() ? 16 : 0;
        graphics.blit(ICONS, getX(), getY(), 0, textureY, 16, 16, 16, 32);
    }

    @Override
    public List<Component> getTooltipMessage() {
        return List.of(getMessage());
    }

    @Override
    public Rect2i getTooltipArea() {
        return new Rect2i(getX(), getY(), 16, 16);
    }

    @Override
    public boolean isTooltipAreaVisible() {
        return visible;
    }
}
```

本地化键固定为：

```json
{
  "gui.ae2wideterminal.switch_to_wide": "切换到宽屏模式",
  "gui.ae2wideterminal.switch_to_narrow": "切换到窄屏模式",
  "gui.ae2wideterminal.insufficient_width": "窗口空间不足，当前临时使用窄屏"
}
```

- [ ] **步骤 4：完成四个薄屏幕子类并连接 registrar**

每个子类保持原泛型和构造器签名，仅调用 `super(...)`、添加按钮和捕获重建所需参数。不得覆盖搜索、鼠标点击、合成、样板编码或 AE2WTLib 通用终端逻辑。

- [ ] **步骤 5：编译验证所有原模组 API 与 Mixin 签名**

```powershell
./gradlew.bat compileJava
./gradlew.bat test
```

预期：PASS；四个屏幕类均能实例化，按钮满足 `addToLeftToolbar` 的 `Button` 类型约束，Mixin annotation processor 生成 refmap。

- [ ] **步骤 6：提交屏幕交互**

```powershell
git add src/main/java/com/ae2wideterminal/client/WideTerminalScreenRegistrar.java src/main/java/com/ae2wideterminal/mixin src/main/java/com/ae2wideterminal/client/screen src/main/java/com/ae2wideterminal/client/widget src/main/resources/ae2wideterminal.mixins.json src/main/resources/assets/ae2wideterminal
git commit -m "feat: replace wireless terminal screens"
```

---

### 任务 5：生成并验证 18 列样式和纹理

**文件：**
- 创建：`tools/generate_wide_textures.py`
- 创建：`src/main/resources/assets/ae2/screens/ae2wideterminal/wireless_terminal_wide.json`
- 创建：`src/main/resources/assets/ae2/screens/ae2wideterminal/ae2_crafting_terminal_wide.json`
- 创建：`src/main/resources/assets/ae2/screens/ae2wideterminal/wtlib_crafting_terminal_wide.json`
- 创建：`src/main/resources/assets/ae2/screens/ae2wideterminal/wtlib_pattern_encoding_terminal_wide.json`
- 创建：`src/main/resources/assets/ae2/textures/guis/ae2wideterminal/terminal_wide.png`
- 创建：`src/main/resources/assets/ae2/textures/guis/ae2wideterminal/crafting_wide.png`
- 创建：`src/main/resources/assets/ae2/textures/guis/ae2wideterminal/pattern_wide.png`
- 创建：`src/main/resources/assets/ae2/textures/guis/ae2wideterminal/wtlib_extras_wide.png`
- 创建：`src/test/java/com/ae2wideterminal/resources/WideStyleResourceTest.java`

- [ ] **步骤 1：编写失败的资源契约测试**

```java
@ParameterizedTest
@CsvSource({
    "wireless_terminal_wide.json,terminal_wide.png,357",
    "ae2_crafting_terminal_wide.json,crafting_wide.png,357",
    "wtlib_crafting_terminal_wide.json,wtlib_extras_wide.png,362",
    "wtlib_pattern_encoding_terminal_wide.json,pattern_wide.png,357"
})
void styleUsesEighteenColumnsAndExpectedTextureWidth(
        String styleName, String textureName, int expectedWidth) throws IOException {
    Path style = Path.of("src/main/resources/assets/ae2/screens/ae2wideterminal", styleName);
    JsonObject json = JsonParser.parseString(Files.readString(style)).getAsJsonObject();
    assertEquals(18, json.getAsJsonObject("terminalStyle").get("slotsPerRow").getAsInt());

    Path texture = Path.of(
            "src/main/resources/assets/ae2/textures/guis/ae2wideterminal", textureName);
    assertEquals(expectedWidth, ImageIO.read(texture.toFile()).getWidth());
}
```

- [ ] **步骤 2：运行测试确认失败**

```powershell
./gradlew.bat test --tests com.ae2wideterminal.resources.WideStyleResourceTest
```

预期：FAIL，宽屏 JSON 和 PNG 尚不存在。

- [ ] **步骤 3：实现确定性纹理生成器**

脚本接受 AE2 与 AE2WTLib JAR 路径：

```powershell
python tools/generate_wide_textures.py `
  --ae2-jar 'C:/Users/19654/Desktop/.minecraft/versions/GTL/mods/appliedenergistics2-forge-15.4.10.jar' `
  --wtlib-jar 'C:/Users/19654/Desktop/.minecraft/versions/GTL/mods/ae2wtlib-15.3.3-forge.jar' `
  --output 'src/main/resources/assets/ae2/textures/guis/ae2wideterminal'
```

生成规则必须是像素精确的：

```python
EXTRA_COLUMNS = 9
SLOT_PITCH = 18
EXTRA_WIDTH = EXTRA_COLUMNS * SLOT_PITCH  # 162

def expand_grid_strip(source, left=7, grid_width=162, right=26):
    target = Image.new("RGBA", (source.width + EXTRA_WIDTH, source.height))
    target.paste(source.crop((0, 0, left, source.height)), (0, 0))
    grid = source.crop((left, 0, left + grid_width, source.height))
    target.paste(grid, (left, 0))
    target.paste(grid, (left + grid_width, 0))
    target.paste(source.crop((left + grid_width, 0, source.width, source.height)),
                 (left + grid_width * 2, 0))
    return target
```

底部区域不复制内部外框；将源图内部内容整体右移 81px 后重新放置左右外边框，使玩家背包、合成区和编码区居中。

- [ ] **步骤 4：创建四个宽屏 JSON**

普通无线终端样式的关键覆盖：

```json
{
  "includes": ["../terminals/wireless_terminal.json"],
  "terminalStyle": {
    "slotsPerRow": 18,
    "header": {"texture": "guis/ae2wideterminal/terminal_wide.png", "srcRect": [0, 0, 357, 17]},
    "firstRow": {"texture": "guis/ae2wideterminal/terminal_wide.png", "srcRect": [0, 17, 357, 18]},
    "row": {"texture": "guis/ae2wideterminal/terminal_wide.png", "srcRect": [0, 35, 357, 18]},
    "lastRow": {"texture": "guis/ae2wideterminal/terminal_wide.png", "srcRect": [0, 53, 357, 18]},
    "bottom": {"texture": "guis/ae2wideterminal/terminal_wide.png", "srcRect": [0, 71, 357, 97]}
  },
  "slots": {
    "PLAYER_INVENTORY": {"left": 89, "bottom": 83},
    "HOTBAR": {"left": 89, "bottom": 25}
  },
  "widgets": {
    "scrollbar": {"left": 337, "top": 18},
    "search": {"left": 80, "top": 4, "width": 251, "height": 12}
  }
}
```

其他三个 JSON 采用相同的 `+162px` 网络区扩展和 `+81px` 底部居中偏移。`right` 锚定的 AE2WTLib 升级面板与奇点面板保留 `right` 属性，不转换为固定 `left`。

- [ ] **步骤 5：运行资源测试与 Gradle 资源处理**

```powershell
./gradlew.bat test --tests com.ae2wideterminal.resources.WideStyleResourceTest
./gradlew.bat processResources
```

预期：PASS；四个 JSON 均为 18 列，PNG 宽度和 `srcRect` 一致。

- [ ] **步骤 6：提交宽屏资源**

```powershell
git add tools src/main/resources/assets/ae2 src/test/java/com/ae2wideterminal/resources
git commit -m "feat: add 18-column terminal styles"
```

---

### 任务 6：运行客户端集成验证并修正布局

**文件：**
- 修改：`src/main/resources/assets/ae2/screens/ae2wideterminal/*.json`
- 修改：`src/main/resources/assets/ae2/textures/guis/ae2wideterminal/*.png`
- 修改：`src/main/java/com/ae2wideterminal/client/screen/*.java`（仅在运行验证暴露问题时）
- 创建：`work/verification/*.png`

- [ ] **步骤 1：启动开发客户端**

```powershell
./gradlew.bat runClient
```

开发运行目录必须同时包含 AE2WTLib 所需运行依赖。若 ForgeGradle 未从 AE2WTLib JAR 的声明中带入 Architectury、Cloth Config 和 Curios，则在 `build.gradle` 添加与用户实例一致的 `runtimeOnly fg.deobf(files(...))` 或 Maven 依赖。

- [ ] **步骤 2：验证目标与排除范围**

依次打开：

1. 普通无线终端。
2. 独立无线合成终端。
3. 无线通用终端的合成模式。
4. 独立无线样板编码终端。
5. 无线通用终端的样板编码模式。
6. 无线样板访问终端。

前五项必须出现左侧切换按钮并支持 9/18 列；第六项必须保持原界面且没有新按钮。

- [ ] **步骤 3：验证交互回归**

在两种宽度下逐项验证：搜索、排序、滚动、物品取放、自动合成、3x3 合成、样板编码、磁铁设置、垃圾按钮和通用终端模式切换。确认切换屏幕时当前菜单没有关闭，服务端没有收到本模组数据包。

- [ ] **步骤 4：验证 GUI 缩放与小窗口回退**

测试 GUI 缩放 `Auto`、`2`、`3`、`4`；把窗口缩小到宽屏阈值以下，确认显示 9 列但配置仍为 `wideMode=true`。扩大窗口并重新打开终端，确认恢复 18 列。

- [ ] **步骤 5：保存截图并检查像素布局**

至少保存以下截图：

- `work/verification/wireless-narrow.png`
- `work/verification/wireless-wide.png`
- `work/verification/wtlib-crafting-wide.png`
- `work/verification/wtlib-pattern-wide.png`
- `work/verification/pattern-access-unchanged.png`
- `work/verification/small-window-fallback.png`

检查槽位、按钮、文字、JEI 面板和玩家背包没有重叠；发现偏移时只调整对应 JSON 或纹理生成参数。

- [ ] **步骤 6：提交集成修正**

```powershell
git add src/main
git commit -m "fix: align wide terminal layouts"
```

若没有任何修正，则跳过空提交。

---

### 任务 7：验证客户端专用连接、打包和交付

**文件：**
- 创建：`README.md`
- 创建：`LICENSE`
- 修改：`build.gradle`
- 创建：`outputs/ae2wideterminal-1.0.0-forge-1.20.1.jar`
- 创建：`outputs/verification-summary.md`

- [ ] **步骤 1：编写 README 与许可证**

README 必须明确：

- 仅客户端安装。
- 目标版本 Forge 47.4.16、AE2 15.4.10、AE2WTLib 15.3.3。
- 支持普通无线终端、无线合成、无线样板编码及通用终端对应模式。
- 不支持无线样板访问终端。
- 左侧按钮切换，低宽度自动回退，配置文件为 `ae2wideterminal-client.toml`。

- [ ] **步骤 2：运行完整自动验证**

```powershell
./gradlew.bat clean test build
```

预期：`BUILD SUCCESSFUL`，所有 JUnit 测试通过并生成 reobfuscated JAR。

- [ ] **步骤 3：检查最终 JAR 内容**

```powershell
jar tf build/libs/ae2wideterminal-1.0.0.jar
```

必须包含：

- `META-INF/mods.toml`
- `ae2wideterminal.mixins.json`
- 四个宽屏 JSON
- 四个宽屏背景 PNG
- 按钮图标与中英文本地化

不得包含：

- `ae2wtlib-15.3.3-forge.jar`
- 用户实例配置、日志、存档或绝对路径文件
- 自定义网络通道类

- [ ] **步骤 4：验证服务端无须安装**

使用未放入本模组 JAR 的 Forge 服务端启动目标整合包服务端，客户端保留本模组连接。确认服务器列表不显示版本不兼容，连接握手成功，进入世界后三类目标终端正常工作。

- [ ] **步骤 5：复制交付物并写验证摘要**

```powershell
Copy-Item -LiteralPath 'build/libs/ae2wideterminal-1.0.0.jar' `
  -Destination 'outputs/ae2wideterminal-1.0.0-forge-1.20.1.jar' -Force
```

`outputs/verification-summary.md` 记录：自动测试命令与结果、游戏内验证项、服务端连接结果、已知版本边界和截图文件名。

- [ ] **步骤 6：提交最终源码**

```powershell
git add README.md LICENSE build.gradle src tools docs/superpowers/plans/2026-08-15-ae2-wireless-wide-terminal.md
git commit -m "docs: finalize AE2 wide terminal mod"
```

不要提交 `outputs/`、`work/`、运行目录或用户游戏实例文件。
