package com.aoihosizora.flutter_mdi_plugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.jetbrains.lang.dart.psi.DartArrayAccessExpression
import com.jetbrains.lang.dart.psi.DartCallExpression
import com.jetbrains.lang.dart.psi.DartNewExpression
import com.jetbrains.lang.dart.psi.DartReferenceExpression
import javax.swing.Icon

class FlutterIconAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) {
            return
        }

        val text = element.text?.trim() ?: return
        val filename = element.containingFile?.name?.trim() ?: return

        // 1. for `MdiIcons.xxx`
        if (element is DartReferenceExpression || element is DartArrayAccessExpression) {
            val prefix = "MdiIcons."
            if (text.startsWith(prefix)) {
                val name = text.substring(prefix.length).trim()
                if (name.isNotEmpty()) {
                    FlutterMdiIcons.getIconByName(name)?.let { icon ->
                        attachGutterIcon(element, holder, icon)
                    }
                }
            }
        }

        // 2. for `const _MdiIconData(xxx)` and `_MdiIconData(xxx)`
        if ((element is DartNewExpression || element is DartCallExpression) && filename.contains("material_design_icons_flutter")) {
            val prefix = if (element is DartNewExpression) "const _MdiIconData(" else "_MdiIconData("
            if (text.startsWith(prefix) && text.endsWith(")")) {
                val params = text.substring(prefix.length, text.length - 1).trim()
                if (params.isNotEmpty()) {
                    parseCodepointFromParams(params)?.let { codepoint ->
                        FlutterMdiIcons.getIconByCode(codepoint)?.let { icon ->
                            attachGutterIcon(element, holder, icon)
                        }
                    }
                }
            }
        }
    }

    private fun parseCodepointFromParams(params: String): Int? {
        val param = params.split(',', limit = 2)[0].trim()
        return try {
            if (param.startsWith("0x", ignoreCase = true)) {
                Integer.parseUnsignedInt(param.substring(2), 16)
            } else {
                Integer.parseUnsignedInt(param)
            }
        } catch (ex: Throwable) {
            null
        }
    }

    private fun attachGutterIcon(element: PsiElement, holder: AnnotationHolder, icon: Icon) {
        val renderer = FlutterIconRenderer(text = element.text, icon = icon)
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .gutterIconRenderer(renderer)
            .create()
    }
}
