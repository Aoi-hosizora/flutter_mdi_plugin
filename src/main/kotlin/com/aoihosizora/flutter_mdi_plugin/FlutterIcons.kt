package com.aoihosizora.flutter_mdi_plugin

import com.google.common.base.CaseFormat
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
        // Example of properties file:
        // f01c9.codepoint=ab_testing
        // ab_testing=/icons/mdi_icons/ab_testing.png
        if (byName != null) {
            return prop.getProperty(byName)
        }
        if (byCode != null) {
            return prop.getProperty("$byCode.codepoint")?.let { prop.getProperty(it) }
        }
        return null
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

object FlutterMdiIcons : FlutterIcons<FlutterMdiIcons>("icons/mdi_icons.properties", FlutterMdiIcons::class.java) {
    override fun getIconByName(name: String): Icon? =
        super.getIconByName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)) // TODO
}
