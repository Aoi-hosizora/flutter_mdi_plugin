import 'dart:io';
import 'dart:typed_data';
import 'dart:ui' as ui;

import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

class IconTuple {
  final IconData data;
  final String name;
  final Key smallKey = UniqueKey();
  final Key largeKey = UniqueKey();

  IconTuple(this.data, this.name);
}

Future<void> saveAllIcons(List<IconTuple> icons, String directory) async {
  for (var icon in icons) {
    await findAndSave(
      icon.smallKey,
      '$directory/${icon.name}.png',
      small: true,
    );
    await findAndSave(
      icon.largeKey,
      '$directory/${icon.name}@2x.png',
      small: false,
    );
  }
}

Future<void> findAndSave(Key key, String path, {bool small = true}) async {
  Finder finder = find.byKey(key);

  final Iterable<Element> elements = finder.evaluate();
  Element element = elements.first;

  Future<ui.Image> imageFuture = _captureImage(element);

  final ui.Image image = await imageFuture;
  final ByteData? bytes = await image.toByteData(format: ui.ImageByteFormat.png);

  await File(path).writeAsBytes(bytes?.buffer.asUint8List() ?? []);

  print('wrote $path');
}

Future<ui.Image> _captureImage(Element element) {
  RenderObject renderObject = element.renderObject!;
  while (!renderObject.isRepaintBoundary) {
    renderObject = renderObject.parent! as RenderObject;
    // assert(renderObject != null);
  }

  // assert(!renderObject.debugNeedsPaint);
  final OffsetLayer layer = renderObject.layer! as OffsetLayer;
  return layer.toImage(renderObject.paintBounds);
}
