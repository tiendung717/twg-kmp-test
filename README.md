# Search & Product (Kotlin Multiplatform)

A Compose Multiplatform app against The Warehouse Group product API: a paged, debounced
product search that opens a product detail screen. Android and iOS share everything above
the platform bridges.

---

## Requirements

| | |
|---|---|
| JDK | **21** — the build fails at configuration time on anything lower |
| Gradle | 9.1.0 (wrapper) |
| Kotlin | 2.3.20 · Compose Multiplatform 1.10.2 · AGP 9.0.0 |
| Android | minSdk 24, compile/target 36 |
| iOS | `iosArm64`, `iosSimulatorArm64` (Xcode + macOS to link and run) |

If your default `JAVA_HOME` is not 21, prefix every Gradle call:

```bash
JAVA_HOME=/path/to/jdk-21 ./gradlew <task>
```

---

## Architecture

Split by clean-architecture layer *and* by feature. `:data` owns the data layer, each
`:feature:*` owns its own domain + presentation, and `:app` is composition only — it builds
the navigation graph and loads the DI modules.

```
:androidApp ─▶ :app ─▶ :feature:search ─┐
                   ├─▶ :feature:product ┼─▶ :data ─▶ :base:common ─▶ :base:logging
                   ├─▶ :base:navigation ┘         ▲
                   └─▶ :data ─────────────────────┘
                       (features also use :base:designsystem)
```

Dependencies run strictly one way; nothing in `:data` knows a feature exists.

| Module | Role |
|---|---|
| `:app` | `App()` builds the `NavHost`; `KoinStarter` loads every `@Module`. Network monitor, Coil loader. Produces the `ComposeApp` iOS framework. |
| `:data` | `remote/` API client + DTOs, `local/` DataStore token store, `repository/`, `di/DataModule`. Also `ResultState` + `safeApiCall`. |
| `:feature:search` | domain (`Product`, `SearchMapper`, `SearchPagingSource`, `SearchUseCase`) + presentation. |
| `:feature:product` | domain (`ProductDetail`, `ProductMapper`, `ProductUseCase`) + presentation. |
| `:base:navigation` | `NavScreen` routes and the `Navigation()` NavHost wrapper. |
| `:base:common` | `formatPrice`. |
| `:base:designsystem` | `AppTheme` + shared components. |
| `:base:logging` | `AppLogger` (Kermit). |
