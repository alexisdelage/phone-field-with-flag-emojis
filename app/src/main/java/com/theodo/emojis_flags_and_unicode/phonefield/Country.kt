package com.theodo.emojis_flags_and_unicode.phonefield

import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

data class Country(
    val countryCode: String,
) {
    val phonePrefix by lazy { loadPhonePrefix() }

    val flagEmoji by lazy { loadFlagEmoji() }

    // Display name is not lazy because Locale can change
    val displayName: String
        get() = Locale.of("", countryCode).displayCountry

    private fun loadPhonePrefix(): String {
        // using libphonenumber to get the phone prefix
        val phoneUtil = PhoneNumberUtil.getInstance()
        val phonePrefixCode = phoneUtil.getCountryCodeForRegion(countryCode)
        return "+$phonePrefixCode"
    }

    /**
     * Retrieve the flag emoji for the country based on its country code.
     *
     * Based on https://stackoverflow.com/a/35849652
     */
    private fun loadFlagEmoji(): String {
        if (this.countryCode.length != 2) return ""

        val uppercaseCountryCode = countryCode.uppercase()
        val emojiFirstPartChars = getCharsForEmoji(uppercaseCountryCode[0])
        val emojiSecondPartChars = getCharsForEmoji(uppercaseCountryCode[1])

        return buildString {
            append(emojiFirstPartChars)
            append(emojiSecondPartChars)
        }
    }

    private fun getCharsForEmoji(countryCodeChar: Char): CharArray {
        val unicodeOffsetForFlagEmojis = 0x1F1E6
        val deltaCodePoint = countryCodeChar.code - 'A'.code
        val emojiCharCodePoint = deltaCodePoint + unicodeOffsetForFlagEmojis

        return Character.toChars(emojiCharCodePoint)
    }

    companion object {
        // Specific countries used in the code
        val France = Country("FR")
        val Spain = Country("ES")

        fun fromCountryCode(countryCode: String?): Country? {
            if (countryCode != null && countryCode.length == 2) {
                return Country(countryCode.uppercase())
            }
            return null
        }
    }
}
