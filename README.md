# Flutter MdiIcons Previewer JetBrains Plugin

+ A JetBrains IDE Plugin for previewing `material_design_icons_flutter` icons (MdiIcons) in code line gutter and code completion suggestion list.
+ Develop environment: `IntelliJ IDEA 2022.2.3`, `JDK 11 / Kotlin 1.7.10`, `Flutter 2.10.5 channel stable / Dart 2.16.2`

### Build

```bash
cd flutter_mdi_plugin

# Prepare icon resource
cd tool
flutter run -d windows # wait for saving properties file and icon images
q # exit the generator directly 

# Build plugin
cd ..
gradle -q buildPlugin
```

### Reference

+ [marius-h/flutter_enhancement_suite](https://github.com/marius-h/flutter_enhancement_suite)
+ [Custom Language Support Tutorial - Annotator](https://plugins.jetbrains.com/docs/intellij/annotator.html)
+ [Custom Language Support Tutorial - Completion Contributor](https://plugins.jetbrains.com/docs/intellij/completion-contributor.html)
