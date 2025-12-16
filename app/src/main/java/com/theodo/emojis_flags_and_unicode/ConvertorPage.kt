package com.theodo.emojis_flags_and_unicode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConvertorPage(modifier: Modifier = Modifier) {
    var textValue by remember { mutableStateOf("") }
    var codePointsValue by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = textValue,
            onValueChange = { newText ->
                textValue = newText
                codePointsValue = textToCodePoints(newText)
            },
            label = { Text("Text (with emojis)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            textStyle = TextStyle(fontSize = 40.sp),
        )

        OutlinedTextField(
            value = codePointsValue,
            onValueChange = { newCodePoints ->
                codePointsValue = newCodePoints
                textValue = codePointsToText(newCodePoints)
            },
            label = { Text("Unicode Code Points") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            textStyle = TextStyle(fontSize = 25.sp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Ascii,
            ),
        )
    }
}

fun textToCodePoints(text: String): String {
    return text
        .codePoints()
        .toArray()
        .joinToString(",") {
            it.toString(radix = 16).uppercase()
        }
}

fun codePointsToText(codePoints: String): String {
    return codePoints
        .split(",")
        .mapNotNull { it.trim().toIntOrNull(16) }
        .joinToString("") {
            try {
                Character.toString(it)
            } catch (_: Exception) {
                "[X]"
            }
        }
}
