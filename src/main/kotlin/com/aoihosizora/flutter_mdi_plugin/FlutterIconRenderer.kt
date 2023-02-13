package com.aoihosizora.flutter_mdi_plugin

import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.DumbAware
import javax.swing.Icon

class FlutterIconRenderer(private val text: String, private val icon: Icon) : GutterIconRenderer(), DumbAware {
    override fun getTooltipText() = text

    override fun getIcon() = icon

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is FlutterIconRenderer) {
            return false
        }
        return text == other.text // String.equals
    }

    override fun hashCode() = text.hashCode()
}
