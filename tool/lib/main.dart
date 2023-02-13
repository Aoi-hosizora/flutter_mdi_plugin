import 'dart:io';

import 'package:basic_utils/basic_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:generator/find_and_save.dart';
import 'package:material_design_icons_flutter/icon_map.dart';

final icons = <IconTuple>[
  for (var kv in iconMap.entries.toList().sublist(0, 10))
    IconTuple(
      IconData(kv.value, fontFamily: 'Material Design Icons', fontPackage: 'material_design_icons_flutter'),
      StringUtils.camelCaseToLowerUnderscore(kv.key),
    ),
];

Future<void> main() async {
  runApp(const MyApp());
  await pumpEventQueue();

  const dirPath = '../src/main/resources/icons/mdi_icons_test';
  await Directory(dirPath).create();
  await saveAllIcons(icons, dirPath);
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Generator',
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
  Widget build(BuildContext context) {
    const Color color = Color(0xFF777777);

    return Scaffold(
      appBar: AppBar(
        title: const Text('IconPage'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // small icon stack
            Stack(
              children: [
                for (var icon in icons)
                  RepaintBoundary(
                    child: Icon(
                      icon.data,
                      size: 16.0,
                      color: color,
                      key: icon.smallKey,
                    ),
                  )
              ],
            ),
            // large icon stack
            Stack(
              children: [
                for (var icon in icons)
                  RepaintBoundary(
                    child: Icon(
                      icon.data,
                      size: 32.0,
                      color: color,
                      key: icon.largeKey,
                    ),
                  )
              ],
            ),
          ],
        ),
      ),
    );
  }
}
