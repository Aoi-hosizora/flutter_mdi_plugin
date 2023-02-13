import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:generator/find_and_save.dart';
import 'package:material_design_icons_flutter/icon_map.dart';

// import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
//
// const _1 = MdiIcons.abTesting;
// const _2 = Icons.import_contacts;

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
  const propFilePath = '../src/main/resources/icons/mdi_icons.properties';
  const iconsDirPath = '../src/main/resources/icons/mdi_icons';

  // prepare directory
  final directory = Directory(iconsDirPath);
  if (await directory.exists()) {
    await directory.delete(recursive: true);
  }
  await directory.create(recursive: true);

  // save properties file
  await savePropertiesFile(icons, propFilePath);

  // save icon images
  await saveAllIcons(icons, iconsDirPath, alsoSaveLarge: false);
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
  const IconPage({
    Key? key,
  }) : super(key: key);

  @override
  State<IconPage> createState() => _IconPageState();
}

class _IconPageState extends State<IconPage> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance?.addPostFrameCallback((_) => generate());
  }

  Widget _buildIcon(IconData data, double size, Color color, Key key) {
    return RepaintBoundary(
      child: Icon(data, size: size, color: color, key: key),
    );
  }

  @override
  Widget build(BuildContext context) {
    const color = Color(0xFF777777); // icon color

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
                for (var icon in icons) //
                  _buildIcon(icon.data, 16.0, color, icon.smallKey),
              ],
            ),
            // Stack(
            //   children: [
            //     for (var icon in icons) //
            //       _buildIcon(icon.data, 32.0, color, icon.largeKey),
            //   ],
            // ),
          ],
        ),
      ),
    );
  }
}
