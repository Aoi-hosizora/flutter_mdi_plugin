package com.aoihosizora.flutter_mdi_plugin

import com.intellij.openapi.util.IconLoader
import java.util.Properties
import javax.swing.Icon

open class FlutterIcons<T>(private val iconClass: Class<T>, propFilePath: String, private val iconDirPath: String) {
    private val prop: Properties = Properties()

    init {
        try {
            prop.load(this::class.java.classLoader.getResourceAsStream(propFilePath))
        } catch (ex: Throwable) {
            // ignore
        }
    }

    open fun getIconFilePath(prop: Properties, byName: String? = null, byCode: Int? = null): String? {
        // # MdiIcons.abTesting (0xf01c9)
        // name.abTesting=abTesting.png
        // codepoint.983497=abTesting.png
        val filename: String? =
            if (byName != null) prop.getProperty("name.$byName")
            else if (byCode != null) prop.getProperty("codepoint.$byCode")
            else null
        val dirname = iconDirPath.trim('/')
        return filename?.let { "/$dirname/$it" }
    }

    open fun getIconByName(name: String): Icon? {
        return try {
            getIconFilePath(prop, byName = name)?.let { iconPath ->
                IconLoader.findIcon(iconPath, iconClass)
            }
        } catch (ex: Throwable) {
            null
        }
    }

    open fun getIconByCode(code: Int): Icon? {
        return try {
            getIconFilePath(prop, byCode = code)?.let { iconPath ->
                IconLoader.findIcon(iconPath, iconClass)
            }
        } catch (ex: Throwable) {
            null
        }
    }
}

class FlutterMdiIcons : FlutterIcons<FlutterMdiIcons>(
    FlutterMdiIcons::class.java,
    propFilePath = "icons/mdi_icons.properties",
    iconDirPath = "icons/mdi_icons/"
) {
    companion object {
        fun getIconByName(name: String) = FlutterMdiIcons().getIconByName(name)
        fun getIconByCode(code: Int) = FlutterMdiIcons().getIconByCode(code)
    }
}
