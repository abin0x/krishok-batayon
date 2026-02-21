# Smart Krishi Management System

A JavaFX desktop application for agricultural planning, advisory workflows, and farm operation support.

## Features
- User authentication (login/register)
- Local JSON-based user storage
- Reusable global layout (topbar + sidebar)
- Dynamic center-content navigation (without reloading sidebar)
- Advisory modules:
  - শস্য ক্যালেন্ডার (Planning)
  - সার ক্যালকুলেটর (Fertilizer)
  - সেচ ক্যালকুলেটর (Irrigation)
  - ফসল চক্র (Crop Rotation)
- Bangla-first UI text support

## Tech Stack
- Java 24
- JavaFX
- Maven
- Jackson (JSON read/write)

## Run Locally
1. Set `JAVA_HOME` to your JDK path.
2. Run:

```powershell
./mvnw.cmd -q javafx:run
```

## Build

```powershell
./mvnw.cmd -q -DskipTests compile
```

## Project Structure (important paths)
- `src/main/java/com/example/demo1/app/controller` - main layout controller
- `src/main/java/com/example/demo1/app/ui` - feature controllers
- `src/main/java/com/example/demo1/app/util` - navigation and utility helpers
- `src/main/resources/com/example/demo1/app/fxml` - all FXML views
- `src/main/resources/com/example/demo1/app/css` - stylesheets

## Notes
- Navigation is managed through `NavigationHelper` and `MainLayoutController`.
- Main layout keeps sidebar persistent and swaps only center content.
- If you see JavaFX API version warnings, align FXML/runtime JavaFX versions.

## Maintainers
- abin0x
- showmik121
