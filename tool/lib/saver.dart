import 'dart:io';
import 'dart:ui' as ui;

import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

class IconTuple {
  IconTuple({required this.name, required this.data})
      : smallKey = UniqueKey(),
        largeKey = UniqueKey();

  final String name;
  final IconData data;
  final Key smallKey;
  final Key largeKey;
}

Future<void> savePropertiesFile(List<IconTuple> icons, String path) async {
  var lines = <String>[];
  for (var tuple in icons) {
    var iconName = tuple.name;
    var codepoint = tuple.data.codePoint;
    lines.add('# MdiIcons.$iconName (0x${codepoint.toRadixString(16)})');
    lines.add('name.$iconName=$iconName.png');
    lines.add('codepoint.$codepoint=$iconName.png\n');
  }

  final content = '''
# Generated file - do not edit.

# suppress inspection "UnusedProperty" for whole file

${lines.join('\n')}''';

  final file = File(path);
  await file.writeAsString(content.trim() + '\n');
  print('wrote $path');
}

Future<void> saveAllIcons(List<IconTuple> icons, String directory) async {
  for (var icon in icons) {
    await _findIconAndSave(icon.smallKey, '$directory/${icon.name}.png', small: true);
    await _findIconAndSave(icon.largeKey, '$directory/${icon.name}@2x.png', small: false); // <<< necessary
  }
}

Future<void> _findIconAndSave(Key key, String path, {bool small = true}) async {
  Future<ui.Image> _captureImage(Element element) async {
    var renderObject = element.renderObject!;
    while (!renderObject.isRepaintBoundary) {
      renderObject = renderObject.parent! as RenderObject;
    }

    final layer = renderObject.layer! as OffsetLayer; // ignore: invalid_use_of_protected_member
    return await layer.toImage(renderObject.paintBounds);
  }

  final element = find.byKey(key).evaluate().first;
  final image = await _captureImage(element);
  final bytes = await image.toByteData(format: ui.ImageByteFormat.png);

  final file = File(path);
  await file.writeAsBytes(bytes?.buffer.asUint8List() ?? []);
  print('wrote $path');
}
