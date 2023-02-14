package com.aoihosizora.flutter_mdi_plugin

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.jetbrains.lang.dart.ide.completion.DartCompletionExtension
import com.jetbrains.lang.dart.ide.completion.DartServerCompletionContributor
import org.dartlang.analysis.server.protocol.CompletionSuggestion
import javax.swing.Icon

class FlutterIconCompletion : DartCompletionExtension() {
    override fun createLookupElement(project: Project, suggestion: CompletionSuggestion): LookupElementBuilder? {
        val element = suggestion.element ?: return null
        val name = element.name?.trim() ?: return null
        val declaringType = suggestion.declaringType?.trim() ?: return null
        val returnType = element.returnType?.trim() ?: return null

        // for `MdiIcons.xxx`
        if (name.isNotEmpty() && declaringType == "MdiIcons" && returnType == "IconData") {
            return FlutterMdiIcons.getIconByName(name)?.let { icon ->
                createIconLookupElement(project, suggestion, icon)
            }
        }

        return null
    }

    private fun createIconLookupElement(project: Project, suggestion: CompletionSuggestion, icon: Icon): LookupElementBuilder {
        return DartServerCompletionContributor
            .createLookupElement(project, suggestion)
            .withTypeText("", icon, false)
            .withTypeIconRightAligned(true)
    }
}
