package com.aoihosizora.flutter_mdi_plugin

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.jetbrains.lang.dart.ide.completion.DartCompletionExtension
import com.jetbrains.lang.dart.ide.completion.DartServerCompletionContributor
import org.apache.commons.lang.StringUtils
import org.dartlang.analysis.server.protocol.CompletionSuggestion
import javax.swing.Icon

class FlutterIconCompletionContributor : DartCompletionExtension() {
    override fun createLookupElement(project: Project, suggestion: CompletionSuggestion): LookupElementBuilder? {
        val icon = findIcon(suggestion) ?: return null

        return DartServerCompletionContributor
            .createLookupElement(project, suggestion)
            .withTypeText("", icon, false)
            .withTypeIconRightAligned(true)
    }

    private fun findIcon(suggestion: CompletionSuggestion): Icon? {
        val element = suggestion.element
        if (element != null) {
            val returnType = element.returnType
            if (!StringUtils.isEmpty(returnType)) {
                element.name?.let { name ->
                    return getIconForDeclaringType(suggestion, name)
                }
            }
        }

        return null
    }

    private fun getIconForDeclaringType(
        suggestion: CompletionSuggestion,
        name: String
    ) = when (suggestion.declaringType) {
        // "FontAwesome" -> {
        //     FontAwesomeIcons.getIcon(name)
        // }
        //
        // "Ionicons" -> {
        //     IonIcons.getIcon(name)
        // }
        //
        // "MaterialCommunityIcons" -> {
        //     MaterialCommunityIcons.getIcon(name)
        // }

        "MdiIcons" -> {
            MdiIcons.getIcon(name)
        }

        else -> null
    }
}
