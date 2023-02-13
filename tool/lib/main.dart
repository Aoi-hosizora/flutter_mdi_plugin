import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:generator/saver.dart';
import 'package:material_design_icons_flutter/icon_map.dart';

// import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
// 
// const _1 = MdiIcons.abTesting;
// const _2 = MdiIcons.twitter;
// const _3 = MdiIcons.bookOpenBlankVariant;
// const _4 = Icons.import_contacts;

final icons = <IconTuple>[
  for (var kv in iconMap.entries)
    IconTuple(
      name: kv.key,
      data: IconData(
        kv.value,
        fontFamily: 'Material Design Icons',
        fontPackage: 'material_design_icons_flutter',
      ),
    ),
];

Future<void> generate() async {
  const iconsDirPath = '../src/main/resources/icons/mdi_icons';
  const propFilePath = '../src/main/resources/icons/mdi_icons.properties';

  // prepare directory and create gitkeep file
  final directory = Directory(iconsDirPath);
  if (await directory.exists()) {
    await directory.delete(recursive: true);
  }
  await File('$iconsDirPath/.gitkeep').create(recursive: true);

  // save properties file and icon images
  await savePropertiesFile(icons, propFilePath);
  await saveAllIcons(icons, iconsDirPath);
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Icon Resource Generator',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const IconPage(),
    );
  }
}

class IconPage extends StatefulWidget {
  const IconPage({Key? key}) : super(key: key);

  @override
  State<IconPage> createState() => _IconPageState();
}

class _IconPageState extends State<IconPage> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance?.addPostFrameCallback((_) => generate());
  }

  @override
  Widget build(BuildContext context) {
    const color = Color.fromRGBO(128, 128, 128, 1.0); // icon color
    return Scaffold(
      appBar: AppBar(
        title: const Text('Icon Resource Generator'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Stack(
              children: [
                for (var icon in icons)
                  RepaintBoundary(
                    child: Icon(icon.data, size: 16.0, color: color, key: icon.smallKey),
                  ),
              ],
            ),
            Stack(
              children: [
                for (var icon in icons)
                  RepaintBoundary(
                    child: Icon(icon.data, size: 32.0, color: color, key: icon.largeKey),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
