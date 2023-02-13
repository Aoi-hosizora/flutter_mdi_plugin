package com.aoihosizora.flutter_mdi_plugin

import com.intellij.openapi.util.IconLoader
import java.util.Properties
import javax.swing.Icon

open class FlutterIcons<T>(propFile: String, private val iconClass: Class<T>) {
    private val prop: Properties = Properties()

    init {
        try {
            prop.load(this::class.java.classLoader.getResourceAsStream(propFile))
        } catch (ex: Throwable) {
            // ignore
        }
    }

    open fun getIconPath(prop: Properties, byName: String? = null, byCode: Int? = null): String? {
        // # MdiIcons.abTesting (0xf01c9)
        // name.abTesting=abTesting.png
        // codepoint.983497=abTesting.png
        val filename: String? =
            if (byName != null) prop.getProperty("name.$byName")
            else if (byCode != null) prop.getProperty("codepoint.$byCode")
            else null
        return filename?.let { "/icons/mdi_icons/$it" }
    }

    open fun getIconByName(name: String): Icon? {
        return getIconPath(prop, byName = name)?.let { iconPath ->
            IconLoader.findIcon(iconPath, iconClass)
        }
    }

    open fun getIconByCode(code: Int): Icon? {
        return getIconPath(prop, byCode = code)?.let { iconPath ->
            IconLoader.findIcon(iconPath, iconClass)
        }
    }
}

object FlutterMdiIcons :
    FlutterIcons<FlutterMdiIcons>("icons/mdi_icons.properties", FlutterMdiIcons::class.java)
