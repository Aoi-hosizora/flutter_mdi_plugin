package com.aoihosizora.flutter_mdi_plugin

import com.google.common.base.CaseFormat
import com.intellij.openapi.util.IconLoader
import java.util.Properties
import javax.swing.Icon

open class FlutterIcons<T>(fileName: String, private val iconClass: Class<T>) {
    private val icons: Properties = Properties()

    init {
        icons.load(this::class.java.classLoader.getResourceAsStream("flutter/${fileName}_icons.properties"))
    }

    fun getIconByCode(code: String): Icon? {
        val iconName = icons.getProperty("$code.codepoint")
        return getIcon(iconName)
    }

    open fun getIcon(name: String?): Icon? {
        name?.let {
            icons.getProperty(it)?.let { path ->
                return IconLoader.findIcon(path, iconClass)
            }
        }
        return null
    }
}

fun String.toSnakeCase(): String = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this)

object MdiIcons : FlutterIcons<MdiIcons>("mdi", MdiIcons::class.java) {
    override fun getIcon(name: String?): Icon? {
        return super.getIcon(name?.toSnakeCase())
    }
}
