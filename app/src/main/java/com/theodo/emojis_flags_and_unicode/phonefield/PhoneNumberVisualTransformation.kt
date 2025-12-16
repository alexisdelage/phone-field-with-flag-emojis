package com.theodo.emojis_flags_and_unicode.phonefield

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.i18n.phonenumbers.PhoneNumberUtil

class PhoneNumberVisualTransformation(
    country: Country,
) : VisualTransformation {
    private val phoneUtil = PhoneNumberUtil.getInstance()
    private val countryIsoCode = country.countryCode
    private val internationalPrefix = country.phonePrefix
    private val formatter = phoneUtil.getAsYouTypeFormatter(countryIsoCode)

    private data class DigitExtraction(
        val digits: String,
        val positions: List<Int>,
    )

    override fun filter(text: AnnotatedString): TransformedText {
        formatter.clear()

        val rawText = text.text
        val digitExtraction = extractDigitsAndPositions(rawText)

        val formattedText = formatDigits(
            digits = digitExtraction.digits,
        )

        val offsetMapping = buildOffsetMapping(
            rawLength = rawText.length,
            formatted = formattedText,
            digitPositions = digitExtraction.positions,
        )

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }

    private fun extractDigitsAndPositions(input: String): DigitExtraction {
        val digits = StringBuilder()
        val positions = mutableListOf<Int>()
        input.forEachIndexed { index, char ->
            digits.append(char)
            positions.add(index)
        }
        return DigitExtraction(digits.toString(), positions)
    }

    private fun formatDigits(
        digits: String,
    ): String {
        val fullNumber = internationalPrefix + digits

        val formatted = fullNumber
            .map { formatter.inputDigit(it) }
            .last()

        val formattedWithoutPrefix = formatted
            .removePrefix(internationalPrefix)
            .trimStart()

        return formattedWithoutPrefix
    }

    private fun buildOffsetMapping(
        rawLength: Int,
        formatted: String,
        digitPositions: List<Int>,
    ): OffsetMapping {
        val originalToTransformed = IntArray(rawLength + 1) { formatted.length }
        val transformedToOriginal = IntArray(formatted.length + 1) { rawLength }

        var digitIndex = 0
        var rawIndex = 0

        formatted.forEachIndexed { formattedIndex, char ->
            if (char.isDigit() && digitIndex < digitPositions.size) {
                val originalOffset = digitPositions[digitIndex]
                while (rawIndex <= originalOffset) {
                    originalToTransformed[rawIndex] = formattedIndex
                    rawIndex++
                }
                transformedToOriginal[formattedIndex] = originalOffset
                digitIndex++
            } else {
                transformedToOriginal[formattedIndex] = rawIndex
            }
        }

        originalToTransformed[rawLength] = formatted.length
        transformedToOriginal[formatted.length] = rawLength

        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return originalToTransformed.getOrElse(offset.coerceAtMost(originalToTransformed.lastIndex)) {
                    formatted.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return transformedToOriginal.getOrElse(
                    offset.coerceAtMost(transformedToOriginal.lastIndex),
                ) { rawLength }
            }
        }
    }
}
